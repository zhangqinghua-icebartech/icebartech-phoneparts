package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.enums.SerialEnum;
import com.icebartech.phoneparts.system.dto.SysSerialClassDTO;
import com.icebartech.phoneparts.system.dto.SysSerialDto;
import com.icebartech.phoneparts.system.param.SysSerialCreateParam;
import com.icebartech.phoneparts.system.param.SysSerialInsertParam;
import com.icebartech.phoneparts.system.po.SysSerial;
import com.icebartech.phoneparts.system.repository.SysSerialRepository;
import com.icebartech.phoneparts.system.service.SysSerialClassService;
import com.icebartech.phoneparts.system.service.SysSerialService;
import com.icebartech.phoneparts.user.po.User;
import com.icebartech.phoneparts.user.service.UserService;
import com.icebartech.phoneparts.util.ProduceCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.icebartech.core.vo.QueryParam.eq;
import static com.icebartech.core.vo.QueryParam.in;

/**
 * @author pc
 * @Date 2019-06-18T11:09:42.138
 * @Description 序列号表
 */

@Service
public class SysSerialServiceImpl extends AbstractService<SysSerialDto, SysSerial, SysSerialRepository> implements SysSerialService {

    @Autowired
    AgentService agentService;
    @Autowired
    SysSerialClassService sysSerialClassService;
    @Autowired
    UserService userService;
    @Autowired
    SysSerialService serialService;

    @Autowired
    SysSerialRepository repository;

    @Override
    protected void warpDTO(Long id, SysSerialDto serial) {

        if (serial.getEndTime() != null && new Date().compareTo(serial.getEndTime()) > 0)
            serial.setStatus(SerialEnum.NO_STATUS.getKey());

        LocalUser localUser = UserThreadLocal.getUserInfo(true);

        //后台代理商
        if (localUser != null && localUser.getLevel() == 0) {
            //二级代理商名称
            AgentDTO agent = agentService.findOneOrNull(serial.getAgentId());
            if (agent != null) {
                serial.setAgentClassName(agent.getClassName());
            }
            SysSerialClassDTO sysSerialClass = sysSerialClassService.findOneOrNull(serial.getSerialClassId());
            if (sysSerialClass != null) {
                serial.setSerialClassName(sysSerialClass.getChinaName());
            }
        }

        if (localUser != null && (localUser.getLevel() == 1 || localUser.getLevel() == 2)) {
            //二级代理商名称
            AgentDTO agent = agentService.findOneOrNull(serial.getSecondAgentId());
            if (agent != null) {
                serial.setAgentClassName(agent.getClassName());
            }
            SysSerialClassDTO sysSerialClass = sysSerialClassService.findOneOrNull(serial.getSecondSerialClassId());
            if (sysSerialClass != null) {
                serial.setSerialClassName(sysSerialClass.getChinaName());
            }
            SysSerialClassDTO sysSerialClass2 = sysSerialClassService.findOneOrNull(serial.getSerialClassId());
            if (sysSerialClass2 != null) {
                serial.setBatchName(sysSerialClass2.getChinaName());
            }
        }
    }

    @Override
    protected void preDelete(Long id) {
        userService.delete(eq(User::getSerialId, id));
    }

    @Override
    protected void preInsert(SysSerialDto serial) {
    }


    @Override
    @Transactional
    public Boolean excelInput(List<SysSerialInsertParam> sysSerialInsertParams) {
        sysSerialInsertParams.forEach(serial -> {
            if (findBySerialNum(serial.getSerialNum()) == null) {
                super.insert(serial);
            }
        });
        return true;
    }

    @Override
    public List<SysSerialDto> excelExports(String randomStr) {
        // List<SysSerialDto> ds = super.findList(eq(SysSerialDto::getRandomStr, randomStr));
        //
        // // 算后台代理商
        //
        // List<AgentDTO> agents = agentService.findList(in(Agent::getId, ds.stream().map(SysSerialDto::)));
        // //二级代理商名称
        //
        //
        //
        // AgentDTO agent = agentService.findOneOrNull(serial.getSecondAgentId());
        // if (agent != null) {
        //     serial.setAgentClassName(agent.getClassName());
        // }
        // SysSerialClassDTO sysSerialClass = sysSerialClassService.findOneOrNull(serial.getSecondSerialClassId());
        // if (sysSerialClass != null) {
        //     serial.setSerialClassName(sysSerialClass.getChinaName());
        // }
        // SysSerialClassDTO sysSerialClass2 = sysSerialClassService.findOneOrNull(serial.getSerialClassId());
        // if (sysSerialClass2 != null) {
        //     serial.setBatchName(sysSerialClass2.getChinaName());
        // }
        return BeanMapper.map( repository.manager_excelExports(randomStr), SysSerialDto.class);
    }

    @Override
    public SysSerialDto findBySerialNum(String serialNum) {
        return super.findOneOrNull(eq(SysSerialDto::getSerialNum, serialNum));
    }

    @Override
    public Boolean useNumAdd(SysSerialDto serialDTO) {
        Integer useNum = serialDTO.getUseNum();
        useNum++;
        serialDTO.setUseNum(useNum);
        return super.update(serialDTO);
    }

    @Override
    public Boolean isValid(String serialNum) {
        SysSerialDto sysSerial = findBySerialNum(serialNum);
        if (sysSerial == null) return false;
        //判断过期
        if (sysSerial.getStatus() == SerialEnum.NO_STATUS.getKey())
            return false;

        if (sysSerial.getEndTime() != null && new Date().compareTo(sysSerial.getEndTime()) > 0)
            throw new ServiceException(CommonResultCodeEnum.NO_SERIAL, "已过期");

        //判断上限
        return sysSerial.getUseNum() < SerialEnum.MAX_USENUM.getKey();
    }

    @Override
    public String create(SysSerialCreateParam param) {
        String randomStr = ProduceCodeUtil.findSerialNum("random");
        for (int i = 0; i < param.getNum(); i++) {
            Agent agent = agentService.findOne(param.getAgentId());
            String title = agent.getClassName();
            //序列号
            String serialNum = ProduceCodeUtil.findSerialNum(title);
            super.insert(new SysSerialInsertParam(serialNum, param.getAgentId(), param.getSerialClassId(), randomStr));
        }
        return randomStr;
    }

    @Override
    @Transactional
    public Boolean init(Long id) {
        repository.init(id);
        return userService.delete(eq(User::getSerialId, id));
    }

    @Override
    public SysSerialDto findBySerialClassId(Long id) {
        return super.findOneOrNull(eq(SysSerialDto::getSerialClassId, id));
    }

    @Override
    public Integer unUseNum(Long userId) {
        return repository.unUseNum(userId);
    }

    @Override
    @Transactional
    public Boolean allocation(Long secondAgentId, List<Long> serialIds, Long serialClassId) {
        // 1. 原来是已经分配的无法再次分配。
        //        serialIds.forEach(serialId->{
        //            SysSerial sysSerial = serialService.findOne(serialId);
        //            if(sysSerial.getStatus()!=0){
        //                throw new ServiceException(CommonResultCodeEnum.INVALID_NULL, "已使用的序列号无法分配！");
        //            }
        //            super.update(eq(SysSerialDto::getId,serialId),
        //                    eq(SysSerialDto::getSecondSerialClassId,serialClassId),
        //                    eq(SysSerialDto::getSecondAgentId,secondAgentId));
        //        });

        // 1. 现在是，已经分配的，可以转到其他人头上。
        serialIds.forEach(serialId -> {
            super.update(eq(SysSerialDto::getId, serialId),
                         eq(SysSerialDto::getSecondSerialClassId, serialClassId),
                         eq(SysSerialDto::getSecondAgentId, secondAgentId));

            //重新分配用户
            userService.allocation(serialId, secondAgentId, serialClassId);
        });
        return true;
    }

}