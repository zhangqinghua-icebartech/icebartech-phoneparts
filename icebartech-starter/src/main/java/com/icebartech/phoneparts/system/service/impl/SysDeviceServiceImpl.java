package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.system.dto.SysClassOneDto;
import com.icebartech.phoneparts.system.dto.SysDeviceDTO;
import com.icebartech.phoneparts.system.po.SysDevice;
import com.icebartech.phoneparts.system.repository.SysDeviceRepository;
import com.icebartech.phoneparts.system.service.SysDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    protected void warpDTO(Long id, SysDeviceDTO sysDeviceDTO) {
        sysDeviceDTO.setFile(aliyunOSSComponent.generateDownloadUrl(sysDeviceDTO.getFile()));
        sysDeviceDTO.setDetailIcon(aliyunOSSComponent.generateDownloadUrl(sysDeviceDTO.getDetailIcon()));
        Agent agent = agentService.findOne(sysDeviceDTO.getAgentId());
        sysDeviceDTO.setAgentName(agent.getCompanyName());
    }


}
