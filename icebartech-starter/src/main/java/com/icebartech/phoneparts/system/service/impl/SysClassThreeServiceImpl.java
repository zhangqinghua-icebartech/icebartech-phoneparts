package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.system.dto.SysClassOneDto;
import com.icebartech.phoneparts.system.dto.SysClassThreeDTO;
import com.icebartech.phoneparts.system.dto.SysClassTwoDto;
import com.icebartech.phoneparts.system.po.SysClassThree;
import com.icebartech.phoneparts.system.repository.SysClassThreeRepository;
import com.icebartech.phoneparts.system.service.SysClassOneService;
import com.icebartech.phoneparts.system.service.SysClassThreeService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author Created by liuao on 2020/6/8 0008$.
 * @desc
 */
@Service
public class SysClassThreeServiceImpl extends AbstractService
        <SysClassThreeDTO, SysClassThree, SysClassThreeRepository> implements SysClassThreeService {

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;

    @Autowired
    private SysClassOneService sysClassOneService;

    @Autowired
    private SysClassTwoService sysClassTwoService;

    @Autowired
    private SysClassThreeRepository repository;

    @Override
    protected void warpDTO(Long id, SysClassThreeDTO classThree) {
        classThree.setIcon(aliyunOSSComponent.generateDownloadUrl(classThree.getIcon()));
        SysClassOneDto classOne = sysClassOneService.findOne(classThree.getClassOneId());
        classThree.setClassOneName(classOne.getChinaName());
        SysClassTwoDto classTwo = sysClassTwoService.findOne(classThree.getClassTwoId());
        classThree.setClassTwoName(classTwo.getChinaName());
    }


    @Override
    public Boolean changeSort(Long id, Integer sort) {
        return super.update(eq(SysClassThree::getId,id),eq(SysClassThree::getSort,sort));
    }

    @Override
    public String findName(Long classThreeId) {
        return repository.findName(classThreeId);
    }
}
