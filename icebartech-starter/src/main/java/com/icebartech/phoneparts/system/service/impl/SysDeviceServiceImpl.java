package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.system.dto.SysDeviceDTO;
import com.icebartech.phoneparts.system.po.SysDevice;
import com.icebartech.phoneparts.system.repository.SysDeviceRepository;
import com.icebartech.phoneparts.system.service.SysDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.in;

/**
 * @author Created by liuao on 2020/9/27.
 * @desc
 */
@Service
public class SysDeviceServiceImpl extends AbstractService
                                                  <SysDeviceDTO, SysDevice, SysDeviceRepository> implements SysDeviceService {

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;
    @Autowired
    private AgentService agentService;


    @Override
    protected void warpDTO(List<Long> ids, List<SysDeviceDTO> ds) {
        ds.forEach(d -> {
            d.setFile(aliyunOSSComponent.generateDownloadUrl(d.getFile()));
            d.setDetailIcon(aliyunOSSComponent.generateDownloadUrl(d.getDetailIcon()));
        });

        List<AgentDTO> agents = agentService.findList(in(Agent::getId,
                                                         ds.stream()
                                                           .map(SysDeviceDTO::getAgentId)
                                                           .collect(Collectors.toList())));
        ds.forEach(d -> d.setAgentName(agents.stream().filter(a -> a.getId().equals(d.getAgentId()))
                                             .map(AgentDTO::getClassName)
                                             .findAny().orElse(null)));
    }
}
