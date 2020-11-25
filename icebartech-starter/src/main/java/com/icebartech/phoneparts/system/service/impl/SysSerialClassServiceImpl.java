package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.repository.AgentRepository;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.system.dto.SysSerialClassDTO;
import com.icebartech.phoneparts.system.po.SysSerialClass;
import com.icebartech.phoneparts.system.repository.SysSerialClassRepository;
import com.icebartech.phoneparts.system.service.SysSerialClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-09-06T16:15:06.735
 * @Description 序列号类别
 */

@Service
public class SysSerialClassServiceImpl extends AbstractService
                                                       <SysSerialClassDTO, SysSerialClass, SysSerialClassRepository> implements SysSerialClassService {


    @Autowired
    private AgentService agentService;
    @Autowired
    private AgentRepository agentRepository;

    @Override
    protected void warpDTO(Long id, SysSerialClassDTO sysSerialClass) {
        // sysSerialClass.setAgentClassName(agentService.findOne(sysSerialClass.getAgentId()).getClassName());
    }

    @Override
    protected void warpDTO(List<Long> ids, List<SysSerialClassDTO> ds) {
        List<AgentDTO> agents = BeanMapper.mapList(agentRepository.findClassNames(ds.stream()
                                                                                    .map(SysSerialClassDTO::getAgentId)
                                                                                    .collect(Collectors.toList())),
                                                   AgentDTO.class);

        ds.forEach(d -> agents.stream()
                              .filter(a -> a.getId().equals(d.getAgentId()))
                              .findAny()
                              .ifPresent(a -> d.setAgentClassName(a.getClassName())));
    }

    @Override
    protected void preInsert(SysSerialClassDTO serial) {
        Agent agent = agentService.findOne(serial.getAgentId());
        serial.setParentId(agent.getParentId());
    }

    @Override
    public Boolean changeSort(Long id, Integer sort) {
        return super.update(eq(SysSerialClassDTO::getId, id), eq(SysSerialClassDTO::getSort, sort));
    }

}