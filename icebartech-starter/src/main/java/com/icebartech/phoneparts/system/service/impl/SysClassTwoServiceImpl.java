package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.system.dto.SysClassOneDto;
import com.icebartech.phoneparts.system.dto.SysClassTwoDto;
import com.icebartech.phoneparts.system.po.SysClassTwo;
import com.icebartech.phoneparts.system.repository.SysClassTwoRepository;
import com.icebartech.phoneparts.system.service.SysClassOneService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-06-18T11:11:08.280
 * @Description 二级分类表
 */

@Service
public class SysClassTwoServiceImpl extends AbstractService
<SysClassTwoDto, SysClassTwo, SysClassTwoRepository> implements SysClassTwoService {


    private  AliyunOSSComponent aliyunOSSComponent;
    private  SysClassOneService sysClassOneService;

    @Autowired
    public SysClassTwoServiceImpl(AliyunOSSComponent aliyunOSSComponent, SysClassOneService sysClassOneService) {
        this.aliyunOSSComponent = aliyunOSSComponent;
        this.sysClassOneService = sysClassOneService;
    }

    @Override
    protected void warpDTO(Long id, SysClassTwoDto sysClassTwo) {
        sysClassTwo.setIcon(aliyunOSSComponent.generateDownloadUrl(sysClassTwo.getIcon()));
        SysClassOneDto classOne = sysClassOneService.findOne(sysClassTwo.getClassOneId());
        sysClassTwo.setClassOneName(classOne.getChinaName());
    }

    @Override
    public Boolean changeSort(Long id, Integer sort) {
        return super.update(eq(SysClassTwo::getId,id),eq(SysClassTwo::getSort,sort));
    }

    @Override
    public SysClassTwo findByClassOneId(Long id) {
        return super.findOneOrNull(eq(SysClassTwoDto::getClassOneId,id));
    }

    @Override
    public Boolean verifyPwd(Long id, String password) {
        SysClassTwo sysClassTwo = findOne(id);
        if(sysClassTwo.getIsLock()==0){
            return true;
        }
        return sysClassTwo.getPassword().equals(password);
    }

    @Override
    public String findName(Long classTwoId) {
        return repository.findName(classTwoId);
    }
}