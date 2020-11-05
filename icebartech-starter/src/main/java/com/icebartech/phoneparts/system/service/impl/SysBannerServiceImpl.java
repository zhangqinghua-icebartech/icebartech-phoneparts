package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.system.dto.SysBannerDTO;
import com.icebartech.phoneparts.system.dto.SysDeviceDTO;
import com.icebartech.phoneparts.system.po.SysBanner;
import com.icebartech.phoneparts.system.repository.SysBannerRepository;
import com.icebartech.phoneparts.system.service.SysBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Created by liuao on 2020/9/27.
 * @desc
 */
@Service
public class SysBannerServiceImpl extends AbstractService
        <SysBannerDTO, SysBanner, SysBannerRepository> implements SysBannerService {

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;

    @Override
    protected void warpDTO(Long id, SysBannerDTO sysBannerDTO) {
        sysBannerDTO.setDetailIcon(aliyunOSSComponent.generateDownloadUrl(sysBannerDTO.getDetailIcon()));
    }


}
