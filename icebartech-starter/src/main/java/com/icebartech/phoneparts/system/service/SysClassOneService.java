package com.icebartech.phoneparts.system.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.system.dto.SysClassOneDto;
import com.icebartech.phoneparts.system.po.SysClassOne;

/**
 * @author pc
 * @Date 2019-06-18T11:10:43.524
 * @Description 一级菜单表
 */

public interface SysClassOneService extends BaseService
<SysClassOneDto, SysClassOne> {

    Boolean changeSort(Long id, Integer sort);
}
