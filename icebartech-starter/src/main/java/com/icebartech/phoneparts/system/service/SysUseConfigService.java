package com.icebartech.phoneparts.system.service;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.system.dto.SysUseConfigDTO;
import com.icebartech.phoneparts.system.po.SysUseConfig;

/**
 * @author pc
 * @Date 2019-09-04T14:32:53.763
 * @Description 常用设置
 */

public interface SysUseConfigService extends BaseService
<SysUseConfigDTO, SysUseConfig> {

    Boolean changeState(Long id, ChooseType state);

    SysUseConfigDTO findInUse();

    void init(Long userId);
}
