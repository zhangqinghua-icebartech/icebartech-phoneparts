package com.icebartech.phoneparts.system.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.system.dto.SysAppConfigDTO;
import com.icebartech.phoneparts.system.po.SysAppConfig;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
public interface SysAppConfigService extends BaseService
        <SysAppConfigDTO, SysAppConfig> {
    String findProtocol();
}
