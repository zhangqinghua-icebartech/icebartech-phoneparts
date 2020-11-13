package com.icebartech.phoneparts.user.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.utils.DateTimeUtility;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
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
import com.icebartech.phoneparts.system.dto.SysSerialDto;
import com.icebartech.phoneparts.system.service.SysSerialService;
import com.icebartech.phoneparts.system.service.SysUseConfigService;
import com.icebartech.phoneparts.user.dto.UserDto;
import com.icebartech.phoneparts.user.param.UserInsertParam;
import com.icebartech.phoneparts.user.po.LoginDto;
import com.icebartech.phoneparts.user.repository.UserRepository;
import com.icebartech.phoneparts.user.service.UserService;
import com.icebartech.phoneparts.user.po.User;
import com.icebartech.phoneparts.util.ProduceCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Map;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-06-18T11:03:37.885
 * @Description 用户表
 */

@Service
@Slf4j
public class UserServiceImpl extends AbstractService
<UserDto, User, UserRepository> implements UserService {

    private SysSerialService sysSerialService;
    private AliyunOSSComponent aliyunOSSComponent;
    private PasswordEncoder passwordEncoder;
    private RedeemCodeService redeemCodeService;
    private AddUseRecordService addUseRecordService;
    private AgentService agentService;
    private SysUseConfigService sysUseConfigService;
    private UseRecordService useRecordService;
    private RedeemService redeemService;
    private UserRepository repository;

    @Autowired
    @Lazy
    public UserServiceImpl(SysSerialService sysSerialService,
                           PasswordEncoder passwordEncoder,
                           AliyunOSSComponent aliyunOSSComponent,
                           RedeemCodeService redeemCodeService,
                           AddUseRecordService addUseRecordService,
                           AgentService agentService, SysUseConfigService sysUseConfigService,
                           UseRecordService useRecordService,
                           RedeemService redeemService, UserRepository repository) {
        this.sysSerialService = sysSerialService;
        this.passwordEncoder = passwordEncoder;
        this.aliyunOSSComponent = aliyunOSSComponent;
        this.redeemCodeService = redeemCodeService;
        this.addUseRecordService = addUseRecordService;
        this.agentService = agentService;
        this.sysUseConfigService = sysUseConfigService;
        this.useRecordService = useRecordService;
        this.redeemService = redeemService;
        this.repository = repository;
    }

    @Override
    protected void warpDTO(Long id, UserDto user) {
        user.setHeadPortrait(aliyunOSSComponent.generateDownloadUrl(user.getHeadPortrait()));
        user.setPastTime(DateTimeUtility.delayTime(user.getGmtCreated(),1));

        LocalUser localUser = UserThreadLocal.getUserInfo();

        if(user.getAgentId()!=0){
            Agent agent = agentService.findOne(user.getAgentId());
            user.setOrigin(agent.getCompanyName());
            //总后台
            if(localUser.getLevel() == 0){
                user.setAgentClassName(agent.getClassName());
            //一级代理商
            }else if(localUser.getLevel() == 1 || localUser.getLevel() == 2){
                Agent agent2 = agentService.findOneOrNull(user.getSecondAgentId());
                if(agent2!=null){
                    user.setAgentClassName(agent2.getClassName());
                }
            }
        }
    }



    @Override
    @Transactional
    public Long register(UserInsertParam param) {
        log.info("注册{}",param.toString());
        //邮箱重复
        if(findByEmail(param.getEmail())!=null)
            throw new ServiceException(CommonResultCodeEnum.EAIL_REPET, "邮箱已重复");
        //序列号
        SysSerialDto serialDTO = sysSerialService.findBySerialNum(param.getSerialNum());

        if(!sysSerialService.isValid(param.getSerialNum()))
            throw new ServiceException(CommonResultCodeEnum.NO_SERIAL, "序列号不存在或已过期");

        //用户所属代理商
        param.setAgentId(serialDTO.getAgentId());
        param.setSecondAgentId(serialDTO.getSecondAgentId());


        //用户所属代理商分类
        param.setSerialClassId(serialDTO.getSerialClassId());


        //增加使用次数绑定邮箱
        Date date = new Date();
        serialDTO.setBindMail(param.getEmail());
        serialDTO.setStartTime(date);
        serialDTO.setIsBindMail(SerialEnum.IS_BIND.getKey());
        serialDTO.setStatus(SerialEnum.IS_STATUS.getKey());
        if(serialDTO.getEndTime() == null)
            serialDTO.setEndTime(DateTimeUtility.delayTime(date,1));
        if(!sysSerialService.useNumAdd(serialDTO))
            throw new ServiceException(CommonResultCodeEnum.NO_SERIAL, "序列号使用失败");

        //失效使用该序列值用户
        if(!passUser(param.getSerialNum()))
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
    @Transactional
    public UserDto codeLogin(String serialNum) {
        UserDto user = super.findOneOrNull(eq(User::getSerialNum,serialNum));
        if(user!=null) return checkLogin(user);
        String email = ProduceCodeUtil.findRedeemCode() + "@sys.com";
        String pwd = "dev123456";
        UserInsertParam userInsertParam = new UserInsertParam(serialNum,email,pwd);
        Long id = this.register(userInsertParam);
        UserDto user2 = super.findOne(id);
        user2.setEnable(0);
        return checkLogin(user2);
    }

    @Override
    public void allocation(Long serialId, Long secondAgentId, Long secondSerialClassId) {
        repository.allocation(serialId,secondAgentId,secondSerialClassId);
    }


    /**
     * 失效使用该序列值用户
     * @param serialNum 序列值
     * @return Boolean
     */
    public Boolean passUser(String serialNum) {
        //有效用户
        UserDto userDTO = findValidBySerialNum(serialNum);
        if(userDTO==null) return true;
        //userDTO.setEnable(UserEnum.NO_ENABLE.getKey());
        return super.delete(userDTO.getId());
    }

    /**
     * 获取有效序列值用户
     * @param serialNum 序列值
     * @return 用户
     */
    public UserDto findValidBySerialNum(String serialNum){
        return super.findOneOrNull(eq(UserDto::getSerialNum,serialNum),eq(UserDto::getEnable, UserEnableEnum.Y_ENABLE.getKey()));
    }

    @Override
    public UserDto login(String email, String pwd) {
        log.info("登录，邮箱{}密码{}",email,pwd);
        UserDto userDTO = findByEmailAndPwd(email);
        if (userDTO == null || !passwordEncoder.matches(pwd, userDTO.getPassword())) {
            throw new ServiceException(CommonResultCodeEnum.LOGIN_ERROR, "账号或密码错误");
        }
        return checkLogin(userDTO);
    }

    public UserDto checkLogin(UserDto user) {

        if (user.getEnable() == UserEnableEnum.NO_ENABLE.getKey())
            throw new ServiceException(CommonResultCodeEnum.USER_ERROR, "用户已失效");

        // SysSerialDto sysSerial = sysSerialService.findOne(user.getSerialId());

        // user.setPastTime(sysSerialService.findEndTime(user.getSerialId()));
        return user;
    }


    @Override
    public Boolean changeHead(Long id,String headPortrait) {
        return super.update(eq(UserDto::getId,id),eq(UserDto::getHeadPortrait,headPortrait));
    }

    @Override
    @Transactional
    public Boolean changePwd(String email,String pwd) {
        UserDto userDTO = findByEmail(email);

        //邮箱重复
        if(userDTO == null)
            throw new ServiceException(CommonResultCodeEnum.EAIL_NULL, "邮箱不存在");
        //更新密码
        return super.update(eq(User::getId,userDTO.getId()),eq(User::getPassword,passwordEncoder.encode(pwd)));
    }

    @Override
    @Transactional
    public Boolean addUseCount(Long userId, Integer num) {
        User user = super.findOne(userId);
        Integer  useCount = user.getUseCount() + num;
        Integer mayUseCount = user.getMayUseCount() + num;

        //消减次数
        LocalUser localUser = UserThreadLocal.getUserInfo();
        //一级代理商
        if(localUser.getLevel() == 1 || localUser.getLevel() == 2){
            AgentDTO agent2 = agentService.findOne(eq(AgentDTO::getId,localUser.getUserId()));
            if(num>agent2.getMayUseCount()){
                throw new ServiceException(CommonResultCodeEnum.NUM_ERROR, "使用次数不足");
            }
            agentService.reduceUseCount(agent2.getId(),num);
        }

        //添加记录
        addUseRecordService.add(userId,num);
        return super.update(eq(UserDto::getId,user.getId()),
                eq(UserDto::getUseCount,useCount),
                eq(UserDto::getMayUseCount,mayUseCount));
    }

    @Override
    public Boolean addUseCount(String code) {
        Long userId = UserThreadLocal.getUserId();
        User user = findOne(userId);
        RedeemCode redeemCode = redeemCodeService.findOneOrNull(eq(RedeemCode::getCode,code));
        if(redeemCode==null||redeemCode.getState()== RedeemStateEnum.Y)
            throw new ServiceException(CommonResultCodeEnum.NOT_AUTHORITY, "兑换码已使用或不存在");
        Redeem redeem = redeemService.findOneOrNull(redeemCode.getRedeemId());
        if(redeem.getAgentId() !=0 && !user.getAgentId().equals(redeem.getAgentId())){
            throw new ServiceException(CommonResultCodeEnum.NOT_AUTHORITY, "您无权限使用此兑换码");
        }
        redeemCode.setUserId(userId);
        redeemCode.setEmail(user.getEmail());
        redeemCode.setState(RedeemStateEnum.Y);
        redeemCodeService.update(redeemCode);
        return addUseCount(userId,redeemCode.getUseNum());
    }

    @Override
    @Transactional
    public Boolean reduceUseCount(Long productId,Long userId) {
        User user = super.findOne(userId);
        if(user.getMayUseCount() <= 0)
            throw new ServiceException(CommonResultCodeEnum.NUM_ERROR, "使用次数不足，请充值！");
        //添加记录
        useRecordService.add(userId,productId,user.getAgentId(),user.getSecondAgentId());
        return super.update(eq(User::getId,user.getId()),eq(User::getMayUseCount,user.getMayUseCount() - 1));
    }

    @Override
    public Boolean reduceUseCount(Long userId, Integer num) {
        User user = super.findOne(userId);

        int reNum = num;

        int mayUseCount;
        if(num > user.getMayUseCount()) {
            reNum = user.getMayUseCount();
            mayUseCount = 0;
        }
        else{

            mayUseCount = user.getMayUseCount() - num;
        }

        //返还次数
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if(localUser.getLevel() == 1 || localUser.getLevel() == 2){
            Agent agent2 = agentService.findOne(localUser.getUserId());
            reNum += agent2.getMayUseCount();
            agentService.update(eq(AgentDTO::getId,agent2.getId()),
                    eq(AgentDTO::getMayUseCount,reNum));
        }

        return super.update(eq(User::getId,user.getId()),eq(User::getMayUseCount,mayUseCount));
    }


    /**
     * 获取用户
     * @param email 邮箱
     * @return
     */
    public UserDto findByEmailAndPwd(String email) {
        Map<String, Object> param = super.repository.loginByEmail(email);
        return BeanMapper.map(param, UserDto.class);

       // return super.findOneOrNull(eq(UserDto::getEmail,email));
    }

    /**
     * 邮箱获取用户
     * @param email 邮箱获取用户
     * @return UserDTO
     */
    public UserDto findByEmail(String email){
        return super.findOneOrNull(eq(UserDto::getEmail,email));
    }
}