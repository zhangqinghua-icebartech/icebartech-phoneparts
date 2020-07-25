package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.system.dto.SysClassOneDto;
import com.icebartech.phoneparts.system.po.SysClassOne;
import com.icebartech.phoneparts.system.repository.SysClassOneRepository;
import com.icebartech.phoneparts.system.service.SysClassOneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-06-18T11:10:43.524
 * @Description 一级菜单表
 */

@Service
public class SysClassOneServiceImpl extends AbstractService
<SysClassOneDto, SysClassOne, SysClassOneRepository> implements SysClassOneService {

    private  AliyunOSSComponent aliyunOSSComponent;

    @Autowired
    public SysClassOneServiceImpl(AliyunOSSComponent aliyunOSSComponent) {
        this.aliyunOSSComponent = aliyunOSSComponent;
    }

    @Override
    protected void warpDTO(Long id, SysClassOneDto sysClassOne) {
        sysClassOne.setIcon(aliyunOSSComponent.generateDownloadUrl(sysClassOne.getIcon()));
        sysClassOne.setEnglishIcon(aliyunOSSComponent.generateDownloadUrl(sysClassOne.getEnglishIcon()));
    }


    @Override
    public Boolean changeSort(Long id, Integer sort) {
        return super.update(eq(SysClassOneDto::getId,id),eq(SysClassOneDto::getSort,sort));
    }
}