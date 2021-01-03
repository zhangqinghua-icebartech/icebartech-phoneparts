package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.system.dto.SysConfigDTO;
import com.icebartech.phoneparts.system.po.SysConfig;
import com.icebartech.phoneparts.system.repository.SysConfigRepository;
import com.icebartech.phoneparts.system.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.in;

@Service
public class SysConfigServiceImpl extends AbstractService<SysConfigDTO, SysConfig, SysConfigRepository> implements SysConfigService {

    @Autowired
    private AgentService agentService;
    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;

    @Override
    protected void warpDTO(Long id, SysConfigDTO d) {

    }

    @Override
    protected void warpDTO(List<Long> ids, List<SysConfigDTO> ds) {
        ds.forEach(d -> d.setFileUrl(aliyunOSSComponent.generateDownloadUrl(d.getFileKey())));

        List<AgentDTO> agents = agentService.findList(in(Agent::getId,
                                                         ds.stream()
                                                           .map(SysConfigDTO::getAgentId)
                                                           .collect(Collectors.toList())));
        ds.forEach(d -> d.setAgentName(agents.stream().filter(a -> a.getId().equals(d.getAgentId()))
                                             .map(AgentDTO::getClassName)
                                             .findAny().orElse(null)));
    }
}

