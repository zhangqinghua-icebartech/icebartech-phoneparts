package com.icebartech.phoneparts.company.service;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.company.dto.CompanyInfoDto;
import com.icebartech.phoneparts.company.po.CompanyInfo;

/**
 * @author pc
 * @Date 2019-06-18T11:12:57.763
 * @Description 公司简介
 */

public interface CompanyInfoService extends BaseService
<CompanyInfoDto, CompanyInfo> {

    Boolean changeEnable(Long id, ChooseType enable);

    CompanyInfoDto findInCompany();
}
