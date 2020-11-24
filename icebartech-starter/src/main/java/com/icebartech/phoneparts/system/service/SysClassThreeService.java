package com.icebartech.phoneparts.system.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.system.dto.SysClassThreeDTO;
import com.icebartech.phoneparts.system.po.SysClassThree;

import java.util.List;

/**
 * @author Created by liuao on 2020/6/8 0008$.
 * @desc
 */

public interface SysClassThreeService extends BaseService
                                                      <SysClassThreeDTO, SysClassThree> {
    Boolean changeSort(Long id, Integer sort);

    String findName(Long classThreeId);


    /**
     * 获取二级分类下的三级分类名称
     */
    List<SysClassThreeDTO> find_name_by_two(Long classTwoId);
}
