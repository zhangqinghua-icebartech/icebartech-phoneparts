package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.system.dto.SysConfigDTO;
import com.icebartech.phoneparts.system.po.SysConfig;
import com.icebartech.phoneparts.system.repository.SysConfigRepository;
import com.icebartech.phoneparts.system.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Created by liuao on 2020/4/2.
 * @desc
 */
@Service
public class SysConfigServiceImpl extends AbstractService
        <SysConfigDTO, SysConfig, SysConfigRepository> implements SysConfigService {

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;


    @Override
    protected void warpDTO(Long id, SysConfigDTO d) {
        d.setFileUrl(aliyunOSSComponent.generateDownloadUrl(d.getFile()));
    }

}

