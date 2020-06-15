package com.icebartech.phoneparts.system.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.system.dto.SysClassTwoDto;
import com.icebartech.phoneparts.system.po.SysClassTwo;

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
}
