package com.icebartech.phoneparts.company.service;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.company.dto.CompanyInfoDto;
import com.icebartech.phoneparts.company.po.CompanyInfo;

public interface CompanyInfoService extends BaseService<CompanyInfoDto, CompanyInfo> {

    Boolean changeEnable(Long id, ChooseType enable);

    CompanyInfoDto findInCompany(Long userId);
}
