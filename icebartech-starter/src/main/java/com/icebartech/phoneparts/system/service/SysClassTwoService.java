package com.icebartech.phoneparts.system.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.system.dto.SysClassTwoDto;
import com.icebartech.phoneparts.system.po.SysClassTwo;

import java.util.List;

/**
 * @author pc
 * @Date 2019-06-18T11:11:08.280
 * @Description 二级分类表
 */

public interface SysClassTwoService extends BaseService
<SysClassTwoDto, SysClassTwo> {

    Boolean changeSort(Long id, Integer sort);

    SysClassTwo findByClassOneId(Long id);

    Boolean verifyPwd(Long id, String password);

    String findName(Long classTwoId);

    /**
     * 获取一级分类下的二级分类名称
     */
    List<SysClassTwoDto> find_name_by_one(Long classOneId);
}
