package com.icebartech.phoneparts.system.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.system.dto.SysClassThreeDTO;
import com.icebartech.phoneparts.system.po.SysClassThree;

/**
 * @author Created by liuao on 2020/6/8 0008$.
 * @desc
 */

public interface SysClassThreeService extends BaseService
        <SysClassThreeDTO, SysClassThree> {
    Boolean changeSort(Long id, Integer sort);

    String findName(Long classThreeId);
}
