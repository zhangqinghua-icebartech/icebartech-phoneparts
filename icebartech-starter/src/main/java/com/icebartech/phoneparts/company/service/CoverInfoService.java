package com.icebartech.phoneparts.company.service;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.company.dto.CoverInfoDTO;
import com.icebartech.phoneparts.company.po.CoverInfo;

/**
 * @author pc
 * @Date 2019-09-10T15:13:02.532
 * @Description 启动页
 */

public interface CoverInfoService extends BaseService
<CoverInfoDTO, CoverInfo> {
    Boolean changeEnable(Long id, ChooseType enable);

    CoverInfoDTO findInCover();
}
