package com.icebartech.phoneparts.user.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.lock.RedisLock;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.utils.DateTimeUtility;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.repository.AgentRepository;
import com.icebartech.phoneparts.agent.service.AddUseRecordService;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.enums.RedeemStateEnum;
import com.icebartech.phoneparts.enums.SerialEnum;
import com.icebartech.phoneparts.enums.UserEnableEnum;
import com.icebartech.phoneparts.product.service.UseRecordService;
import com.icebartech.phoneparts.redeem.po.Redeem;
import com.icebartech.phoneparts.redeem.po.RedeemCode;
import com.icebartech.phoneparts.redeem.service.RedeemCodeService;
import com.icebartech.phoneparts.redeem.service.RedeemService;
import com.icebartech.phoneparts.system.dto.SysSerialClassDTO;
import com.icebartech.phoneparts.system.dto.SysSerialDto;
import com.icebartech.phoneparts.system.po.SysSerialClass;
import com.icebartech.phoneparts.system.service.SysSerialClassService;
import com.icebartech.phoneparts.system.service.SysSerialService;
import com.icebartech.phoneparts.system.service.SysUseConfigService;
import com.icebartech.phoneparts.user.dto.UserDto;
import com.icebartech.phoneparts.user.param.UserInsertParam;
import com.icebartech.phoneparts.user.param.UserOutParam;
import com.icebartech.phoneparts.user.po.User;
import com.icebartech.phoneparts.user.repository.UserRepository;
import com.icebartech.phoneparts.user.service.UserService;
import com.icebartech.phoneparts.util.CacheComponent;
import com.icebartech.phoneparts.util.ProduceCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.eq;
import static com.icebartech.core.vo.QueryParam.in;

@Slf4j
@Service
public class UserServiceImpl extends AbstractService<UserDto, User, UserRepository> implements UserService {

    @Autowired
    private AgentService agentService;
    @Autowired
    private RedeemService redeemService;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UseRecordService useRecordService;
    @Autowired
    private SysSerialService sysSerialService;
    @Autowired
    private RedeemCodeService redeemCodeService;
    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;

    @Autowired
    private SysUseConfigService sysUseConfigService;
    @Autowired
    private AddUseRecordService addUseRecordService;
    @Autowired
    private SysSerialClassService sysSerialClassService;

    @Override
    protected void postUpdate(Long id) {
        // 更新缓存
        cacheComponent.setUserDetail(id, super.findDetail(id));
    }

    @Override
    protected void postDelete(Long id) {
        // 删除缓存
        cacheComponent.delUserDetail(id);
    }

    @Override
    protected void warpDTO(List<Long> ids, List<UserDto> ds) {
//        // 1. 设置一级分类名称
//        List<AgentDTO> agents = agentService.findList(in(Agent::getId, ds.stream().map(UserDto::getAgentId).collect(Collectors.toList())));
//        ds.forEach(d->agents.stream().filter(a->a.getId().equals(d.getAgentId())).findAny().ifPresent(a-> {
//            d.setOrigin(a.getCompanyName());
//            d.setAgentClassName(a.getClassName());
//        }));
//
//        // 2. 设置二级分类名称
//
//
//        // 3. 头像，过期时间
//        for (UserDto d : ds) {
//            d.setHeadPortrait(aliyunOSSComponent.generateDownloadUrl(d.getHeadPortrait()));
//            d.setPastTime(DateTimeUtility.delayTime(d.getGmtCreated(), 1));
//        }

        // 1. 设置批次
        List<SysSerialClassDTO> sysSerialClasses = sysSerialClassService.findList(in(SysSerialClass::getId, ds.stream().map(UserDto::getSerialClassId).collect(Collectors.toList())));
        ds.forEach(d -> sysSerialClasses.stream().filter(sc -> sc.getId().equals(d.getSerialClassId())).findAny().ifPresent(sc -> d.setSerialClassName(sc.getChinaName())));


        // 2. 设置二级分类名称
        List<SysSerialClassDTO> secondSysSerialClasses = sysSerialClassService.findList(in(SysSerialClass::getId, ds.stream().map(UserDto::getSecondSerialClassId).collect(Collectors.toList())));
        ds.forEach(d -> secondSysSerialClasses.stream().filter(sc -> sc.getId().equals(d.getSecondSerialClassId())).findAny().ifPresent(sc -> d.setSecondSerialClassName(sc.getChinaName())));
    }

    @Override
    protected void warpDTO(Long id, UserDto user) {
        user.setHeadPortrait(aliyunOSSComponent.generateDownloadUrl(user.getHeadPortrait()));
        user.setPastTime(DateTimeUtility.delayTime(user.getGmtCreated(), 1));

        LocalUser localUser = UserThreadLocal.getUserInfo(true);

        if (localUser != null && user.getAgentId() != 0) {
            Agent agent = agentService.findOne(user.getAgentId());
            user.setOrigin(agent.getCompanyName());
            //总后台
            if (localUser.getLevel() == 0) {
                user.setAgentClassName(agent.getClassName());
                //一级代理商

            } else if (localUser.getLevel() == 1 || localUser.getLevel() == 2) {
                Agent agent2 = agentService.findOneOrNull(user.getSecondAgentId());
                if (agent2 != null) {
                    user.setAgentClassName(agent2.getClassName());
                }
            }
        }
    }

    @Override
    @RedisLock(key = "#serialNum")
    @Transactional
    public Long register(String serialNum, UserInsertParam param) {
        log.info("注册{}", param.toString());
        //邮箱重复
        if (findByEmail(param.getEmail()) != null)
            throw new ServiceException(CommonResultCodeEnum.EAIL_REPET, "邮箱已重复");
        //序列号
        SysSerialDto serialDTO = sysSerialService.findBySerialNum(param.getSerialNum());

        if (!sysSerialService.isValid(param.getSerialNum()))
            throw new ServiceException(CommonResultCodeEnum.NO_SERIAL, "序列号不存在或已过期");

        //用户所属代理商
        param.setAgentId(serialDTO.getAgentId());
        param.setSecondAgentId(serialDTO.getSecondAgentId());


        //用户所属代理商批次
        param.setSerialClassId(serialDTO.getSerialClassId());
        //用户所属代理商分类
        param.setSecondSerialClassId(serialDTO.getSecondSerialClassId());


        //增加使用次数绑定邮箱
        Date date = new Date();
        serialDTO.setBindMail(param.getEmail());
        serialDTO.setStartTime(date);
        serialDTO.setIsBindMail(SerialEnum.IS_BIND.getKey());
        serialDTO.setStatus(SerialEnum.IS_STATUS.getKey());
        if (serialDTO.getEndTime() == null)
            serialDTO.setEndTime(DateTimeUtility.delayTime(date, 1));
        if (!sysSerialService.useNumAdd(serialDTO))
            throw new ServiceException(CommonResultCodeEnum.NO_SERIAL, "序列号使用失败");

        //失效使用该序列值用户
        if (!passUser(param.getSerialNum()))
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "注册失败");

        //密码加密
        param.setSerialId(serialDTO.getId());
        param.setPassword(passwordEncoder.encode(param.getPassword()));

        Long id = super.insert(BeanMapper.map(param, UserDto.class));
        //初始化
        sysUseConfigService.init(id);
        return id;
    }


    @Override
    @RedisLock(key = "#serialNum")
    @Transactional
    public UserDto codeLogin(String serialNum) {
        // 1. 用户已存在，校验
        if (super.exists(eq(User::getSerialNum, serialNum))) {
            UserDto user = BeanMapper.map(repository.loginBySerialNum(serialNum), UserDto.class);

            if (user.getEnable() == UserEnableEnum.NO_ENABLE.getKey())
                throw new ServiceException(CommonResultCodeEnum.USER_ERROR, "用户已失效");

            return user;
        }

        // 2. 用户不存在，新增
        String email = ProduceCodeUtil.findRedeemCode() + "@sys.com";
        String pwd = "dev123456";
        UserInsertParam userInsertParam = new UserInsertParam(serialNum, email, pwd);
        Long id = this.register(serialNum, userInsertParam);

        return BeanMapper.map(repository.loginById(id), UserDto.class);
    }


    /**
     * 失效使用该序列值用户
     *
     * @param serialNum 序列值
     * @return Boolean
     */
    private Boolean passUser(String serialNum) {
        //有效用户
        UserDto userDTO = findValidBySerialNum(serialNum);
        if (userDTO == null) return true;
        //userDTO.setEnable(UserEnum.NO_ENABLE.getKey());
        return super.delete(userDTO.getId());
    }

    /**
     * 获取有效序列值用户
     *
     * @param serialNum 序列值
     * @return 用户
     */
    private UserDto findValidBySerialNum(String serialNum) {
        return super.findOneOrNull(eq(UserDto::getSerialNum, serialNum), eq(UserDto::getEnable, UserEnableEnum.Y_ENABLE.getKey()));
    }

    @Override
    public UserDto login(String email, String pwd) {
        log.info("登录，邮箱{}密码{}", email, pwd);

        // 1. 查询用户数据
        UserDto user = findByEmailAndPwd(email);

        // 2. 密码校验
        if (user == null || !passwordEncoder.matches(pwd, user.getPassword())) {
            throw new ServiceException(CommonResultCodeEnum.LOGIN_ERROR, "账号或密码错误");
        }

        // 3. 状态校验
        if (user.getEnable() == UserEnableEnum.NO_ENABLE.getKey())
            throw new ServiceException(CommonResultCodeEnum.USER_ERROR, "用户已失效");

        return user;
    }


    @Override
    public Boolean changeHead(Long id, String headPortrait) {
        return super.update(eq(UserDto::getId, id), eq(UserDto::getHeadPortrait, headPortrait));
    }

    @Override
    @Transactional
    public Boolean changePwd(String email, String pwd) {
        UserDto userDTO = findByEmail(email);

        //邮箱重复
        if (userDTO == null)
            throw new ServiceException(CommonResultCodeEnum.EAIL_NULL, "邮箱不存在");
        //更新密码
        return super.update(eq(User::getId, userDTO.getId()), eq(User::getPassword, passwordEncoder.encode(pwd)));
    }

    @Override
    @Transactional
    public Boolean addUseCount(Long userId, Integer num) {
        User user = super.findOne(userId);
        Integer useCount = user.getUseCount() + num;
        Integer mayUseCount = user.getMayUseCount() + num;

        //消减次数
        LocalUser localUser = UserThreadLocal.getUserInfo();
        //一级代理商
        if (localUser.getLevel() == 1 || localUser.getLevel() == 2) {
            AgentDTO agent2 = agentService.findOne(eq(AgentDTO::getId, localUser.getUserId()));
            if (num > agent2.getMayUseCount()) {
                throw new ServiceException(CommonResultCodeEnum.NUM_ERROR, "使用次数不足");
            }
            agentService.reduceUseCount(agent2.getId(), num);
        }

        //添加记录
        addUseRecordService.add(userId, num);
        return super.update(eq(UserDto::getId, user.getId()),
                            eq(UserDto::getUseCount, useCount),
                            eq(UserDto::getMayUseCount, mayUseCount));
    }

    @Override
    public Boolean addUseCount(String code) {
        Long userId = UserThreadLocal.getUserId();
        User user = findOne(userId);
        RedeemCode redeemCode = redeemCodeService.findOneOrNull(eq(RedeemCode::getCode, code));
        if (redeemCode == null || redeemCode.getState() == RedeemStateEnum.Y)
            throw new ServiceException(CommonResultCodeEnum.NOT_AUTHORITY, "兑换码已使用或不存在");
        Redeem redeem = redeemService.findOneOrNull(redeemCode.getRedeemId());
        if (redeem.getAgentId() != 0 && !user.getAgentId().equals(redeem.getAgentId())) {
            throw new ServiceException(CommonResultCodeEnum.NOT_AUTHORITY, "您无权限使用此兑换码");
        }
        redeemCode.setUserId(userId);
        redeemCode.setEmail(user.getEmail());
        redeemCode.setState(RedeemStateEnum.Y);
        redeemCode.setUseTime(new Date());
        redeemCodeService.update(redeemCode);
        return addUseCount(userId, redeemCode.getUseNum());
    }

    @Override
    @Transactional
    public Boolean reduceUseCount(Long productId, Long userId) {
        // 1. 数据校验
        UserDto user = BeanMapper.map(repository.mayUseCount(userId), UserDto.class);
        if (user.getMayUseCount() <= 0)
            throw new ServiceException(CommonResultCodeEnum.NUM_ERROR, "使用次数不足，请充值！");

        // 2. 添加记录
        useRecordService.add(userId, productId, user.getAgentId(), user.getSecondAgentId());

        // 3. 修改数据
        return super.update(eq(User::getId, user.getId()),
                            eq(User::getMayUseCount, user.getMayUseCount() - 1));
    }

    @Override
    public Boolean reduceUseCount(Long userId, Integer num) {
        User user = super.findOne(userId);

        // 1. 计算授权次数和剩余次数
        int reNum = num > user.getMayUseCount() ? user.getMayUseCount() : num;
        user.setUseCount(user.getUseCount() - reNum);
        user.setUseCount(user.getUseCount() < 0 ? 0 : user.getUseCount());
        user.setMayUseCount(user.getMayUseCount() - reNum);

        // 2. 返还剩余次数给上级
        LocalUser localUser = UserThreadLocal.getUserInfo(true);
        if (localUser != null && (localUser.getLevel() == 1 || localUser.getLevel() == 2)) {
            Agent agent2 = agentService.findOne(localUser.getUserId());
            reNum += agent2.getMayUseCount();
            agentService.update(eq(AgentDTO::getId, agent2.getId()),
                                eq(AgentDTO::getMayUseCount, reNum));
        }

        // 3. 设置授权次数和剩余次数
        return super.update(eq(User::getId, user.getId()),
                            eq(User::getUseCount, user.getUseCount()),
                            eq(User::getMayUseCount, user.getMayUseCount()));
    }


    /**
     * 获取用户
     *
     * @param email 邮箱
     */
    private UserDto findByEmailAndPwd(String email) {
        Map<String, Object> param = super.repository.loginByEmail(email);
        return BeanMapper.map(param, UserDto.class);

        // return super.findOneOrNull(eq(UserDto::getEmail,email));
    }

    /**
     * 邮箱获取用户
     *
     * @param email 邮箱获取用户
     * @return UserDTO
     */
    private UserDto findByEmail(String email) {
        return super.findOneOrNull(eq(UserDto::getEmail, email));
    }

    @Override
    public List<UserDto> export(UserOutParam param) {
        if (param == null) param = new UserOutParam();

        List<UserDto> list = new ArrayList<>();
        for (Map<String, Object> map : repository.export(param)) {
            UserDto user = new UserDto();
            user.setSerialNum(map.get("serial_num") + "");
            user.setEmail(map.get("email") + "");

            if (param.getAgentlevel() == 0) {
                user.setAgentClassName(map.get("class_name") + "");
            }else {
                user.setAgentClassName(map.get("second_agent_class_name") == null ? "" : String.valueOf(map.get("second_agent_class_name")));
            }

            user.setSecondSerialClassName(map.get("china_name") == null ? "" : map.get("china_name") + "");
            user.setUseCount((int) map.get("use_count"));
            user.setMayUseCount((int) map.get("may_use_count"));

            user.setRegisterTime(((Timestamp) map.get("gmt_created")).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            list.add(user);
        }

        return list;
    }

    @Override
    public void allocation(Long serialId, Long secondAgentId, Long serialClassId) {
        repository.allocation(serialId, secondAgentId, serialClassId);
    }

    @Override
    public List<AgentDTO> find_user_first_agent_list() {
        return BeanMapper.map(repository.find_user_first_agent_list(), AgentDTO.class);
    }

    @Override
    public List<AgentDTO> find_user_second_agent_list(Long agentId) {
        return BeanMapper.map(repository.find_user_second_agent_list(agentId), AgentDTO.class);
    }

    @Override
    public List<SysSerialClassDTO> find_second_serial_class_list(Long agentId, Long secondAgentId) {
        return BeanMapper.map(repository.find_second_serial_class_list(agentId, secondAgentId), SysSerialClassDTO.class);
    }
}