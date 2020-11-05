package com.icebartech.phoneparts.agent.service;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;

/**
 * @author pc
 * @Date 2019-09-05T16:06:46.810
 * @Description 代理商
 */

public interface AgentService extends BaseService
<AgentDTO, Agent> {

    Boolean changeEnable(Long id, ChooseType enable);

    Boolean changeSort(Long id, Integer sort);

    /**
     * 添加次数
     * @param agentId 代理商id
     * @param num 数量
     * @return
     */
    Boolean addUseCount(Long agentId, Integer num);

    /**
     *
     * @param agentId 代理商id
     * @param num 减少次数
     * @return
     */
    Boolean reduceUseCount(Long agentId,Integer num);

}
