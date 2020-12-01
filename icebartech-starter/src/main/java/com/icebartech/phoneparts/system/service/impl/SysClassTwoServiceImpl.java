package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.phoneparts.system.dto.SysClassOneDto;
import com.icebartech.phoneparts.system.dto.SysClassThreeDTO;
import com.icebartech.phoneparts.system.dto.SysClassTwoDto;
import com.icebartech.phoneparts.system.po.SysClassTwo;
import com.icebartech.phoneparts.system.repository.SysClassOneRepository;
import com.icebartech.phoneparts.system.repository.SysClassTwoRepository;
import com.icebartech.phoneparts.system.service.SysClassOneService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-06-18T11:11:08.280
 * @Description 二级分类表
 */

@Service
public class SysClassTwoServiceImpl extends AbstractService<SysClassTwoDto, SysClassTwo, SysClassTwoRepository> implements SysClassTwoService {


    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;
    @Autowired
    private SysClassOneService sysClassOneService;
    @Autowired
    private SysClassOneRepository sysClassOneRepository;

    @Override
    protected void warpDTO(Long id, SysClassTwoDto sysClassTwo) {
//        sysClassTwo.setIcon(aliyunOSSComponent.generateDownloadUrl(sysClassTwo.getIcon()));
//        SysClassOneDto classOne = sysClassOneService.findOne(sysClassTwo.getClassOneId());
//        sysClassTwo.setClassOneName(classOne.getChinaName());
    }

    @Override
    protected void warpDTO(List<Long> ids, List<SysClassTwoDto> ds) {
        // 1. 设置图片
        ds.forEach(d -> d.setIcon(aliyunOSSComponent.generateDownloadUrl(d.getIcon())));

        // 2. 设置一级分类
        List<SysClassOneDto> classOnes = BeanMapper.map(sysClassOneRepository.findName(ds.stream()
                                                                                             .map(SysClassTwoDto::getClassOneId)
                                                                                             .collect(Collectors.toList())),
                                                            SysClassOneDto.class);
        ds.forEach(d -> classOnes.stream().filter(t -> t.getId().equals(d.getClassOneId())).findAny().ifPresent(t -> d.setClassOneName(t.getChinaName())));

    }

    @Override
    public Boolean changeSort(Long id, Integer sort) {
        return super.update(eq(SysClassTwo::getId, id), eq(SysClassTwo::getSort, sort));
    }

    @Override
    public SysClassTwo findByClassOneId(Long id) {
        return super.findOneOrNull(eq(SysClassTwoDto::getClassOneId, id));
    }

    @Override
    public Boolean verifyPwd(Long id, String password) {
        SysClassTwo sysClassTwo = findOne(id);
        if (sysClassTwo.getIsLock() == 0) {
            return true;
        }
        return sysClassTwo.getPassword().equals(password);
    }

    @Override
    public String findName(Long classTwoId) {
        return repository.findName(classTwoId);
    }

    @Override
    public List<SysClassTwoDto> find_name_by_one(Long classOneId) {
        if (classOneId == null) {
            return new ArrayList<>();
        }
        return BeanMapper.map(repository.find_name_by_one(classOneId), SysClassTwoDto.class);
    }
}