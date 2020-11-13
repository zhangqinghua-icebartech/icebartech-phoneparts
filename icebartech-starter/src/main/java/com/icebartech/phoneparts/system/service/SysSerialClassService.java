package com.icebartech.phoneparts.system.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.system.dto.SysSerialClassDTO;
import com.icebartech.phoneparts.system.po.SysSerialClass;

/**
 * @author pc
 * @Date 2019-09-06T16:15:06.735
 * @Description 序列号类别
 */

public interface SysSerialClassService extends BaseService
<SysSerialClassDTO, SysSerialClass> {

    Boolean changeSort(Long id, Integer sort);
}
