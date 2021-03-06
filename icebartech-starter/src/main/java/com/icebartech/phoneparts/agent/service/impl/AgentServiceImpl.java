package com.icebartech.phoneparts.agent.service.impl;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.EncryptUtil;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.repository.AgentRepository;
import com.icebartech.phoneparts.agent.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-09-05T16:06:46.810
 * @Description 代理商
 */

@Service
public class AgentServiceImpl extends AbstractService
<AgentDTO, Agent, AgentRepository> implements AgentService {

    @Autowired
    EncryptUtil encryptUtil;

    @Override
    protected void preInsert(AgentDTO d) {
        // 加密密码
        if(d.getPassword() != null) d.setPassword(encryptUtil.Base64Encode(d.getPassword()));
        Agent agent = super.findOneOrNull(eq(AgentDTO::getLoginName,d.getLoginName()));
        if(agent !=null){
            throw new ServiceException(CommonResultCodeEnum.NUM_ERROR, "此账号已使用，请换一个");
        }

    }

    @Override
    protected void preUpdate(AgentDTO d) {
        // 加密密码
        if(d.getPassword() != null) d.setPassword(encryptUtil.Base64Encode(d.getPassword()));
    }

    @Override
    protected void warpDTO(AgentDTO d) {
        // 解密密码
        if(d.getPassword()!=null) d.setPassword(encryptUtil.Base64Decode(d.getPassword()));
    }


    @Override
    public Boolean changeEnable(Long id, ChooseType enable) {
        return super.update(eq(AgentDTO::getId,id),eq(AgentDTO::getEnable,enable));
    }

    @Override
    public Boolean changeSort(Long id, Integer sort) {
        AgentDTO agent = super.findOne(id);
        return super.update(eq(AgentDTO::getId,agent.getId()),eq(AgentDTO::getSort,sort));
    }

    @Override
    @Transactional
    public Boolean addUseCount(Long agentId, Integer num) {
        AgentDTO agent = super.findOne(agentId);
        Integer  useCount = agent.getUseCount() + num;
        Integer mayUseCount = agent.getMayUseCount() + num;
        if(agent.getParentId() != 0){
            AgentDTO agent2 = super.findOne(eq(AgentDTO::getId,agent.getParentId()));
            if(num>agent2.getMayUseCount()){
                throw new ServiceException(CommonResultCodeEnum.NUM_ERROR, "使用次数不足");
            }
            reduceUseCount(agent2.getId(),num);
        }
        return super.update(eq(AgentDTO::getId,agent.getId()),
                eq(AgentDTO::getUseCount,useCount),
                eq(AgentDTO::getMayUseCount,mayUseCount));
    }


    @Override
    public Boolean reduceUseCount(Long agentId, Integer num) {
        Agent agent = super.findOne(agentId);
        int mayUseCount;
        int reNum = num;
        if(num > agent.getMayUseCount()){
            reNum = agent.getMayUseCount();
            mayUseCount = 0;
        }
        else{
            mayUseCount = agent.getMayUseCount() - num;
        }

        //返还次数
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if(localUser.getLevel() == 1 || localUser.getLevel() == 2){
            Agent agent2 = super.findOne(localUser.getUserId());
            reNum += agent2.getMayUseCount();
            super.update(eq(AgentDTO::getId,agent2.getId()),
                    eq(AgentDTO::getMayUseCount,reNum));
        }

        return super.update(eq(AgentDTO::getId,agent.getId()),eq(AgentDTO::getMayUseCount,mayUseCount));
    }


}