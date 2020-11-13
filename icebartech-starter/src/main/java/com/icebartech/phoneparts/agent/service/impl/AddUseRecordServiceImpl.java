package com.icebartech.phoneparts.agent.service.impl;

import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.agent.dto.AddUseRecordDTO;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.param.AddUseRecordInsertParam;
import com.icebartech.phoneparts.agent.po.AddUseRecord;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.repository.AddUseRecordRepository;
import com.icebartech.phoneparts.agent.service.AddUseRecordService;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-09-05T17:01:59.673
 * @Description 添加记录
 */

@Service
public class AddUseRecordServiceImpl extends AbstractService
<AddUseRecordDTO, AddUseRecord, AddUseRecordRepository> implements AddUseRecordService {

    @Autowired
    AgentService agentService;
    @Autowired
    UserService userService;

    @Override
    public void add(Long userId, Integer useNum) {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        //渠道商操作
        if(localUser.getUserEnum() == UserEnum.agent){
            //添加记录
            super.insert(new AddUseRecordInsertParam(localUser.getUserId(),useNum,userService.findOne(userId)));
            //记录总数
            Agent agent = agentService.findOne(localUser.getUserId());
            Integer newUseNum = agent.getUseNum() + useNum;
            agentService.update(eq(AgentDTO::getId,agent.getId()),eq(AgentDTO::getUseNum,newUseNum));
        }
    }
}