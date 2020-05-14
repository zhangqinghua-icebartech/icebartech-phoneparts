package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.system.dto.SysAppConfigDTO;
import com.icebartech.phoneparts.system.po.SysAppConfig;
import com.icebartech.phoneparts.system.repository.SysAppConfigRepository;
import com.icebartech.phoneparts.system.service.SysAppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.icebartech.core.vo.QueryParam.eq;


/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Service
public class SysAppConfigServiceImpl extends AbstractService
        <SysAppConfigDTO, SysAppConfig, SysAppConfigRepository> implements SysAppConfigService {

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;

    @Override
    protected void preInsert(SysAppConfigDTO d) {
        SysAppConfigDTO sysAppConfig = findOneOrNull(eq(SysAppConfigDTO::getType,d.getType()));
        if(sysAppConfig != null){
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "同类型只能添加一条数据");
        }

    }


    @Override
    public String findProtocol() {
        return aliyunOSSComponent.generateDownloadUrl("icebartech-enweis-test/191031141052-9633.html");
    }
}
