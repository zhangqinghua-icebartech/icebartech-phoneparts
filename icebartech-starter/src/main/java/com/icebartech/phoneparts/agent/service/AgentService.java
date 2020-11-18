package com.icebartech.phoneparts.agent.service;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;

import java.util.List;

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
     *
     * @param agentId 代理商id
     * @param num     数量
     */
    Boolean addUseCount(Long agentId, Integer num);

    /**
     * @param agentId 代理商id
     * @param num     减少次数
     */
    Boolean reduceUseCount(Long agentId, Integer num);

    /**
     * 获取此parentId下面的代理商Id
     */
    List<Long> findAgentIdsByParentId(Long parentId);

}
