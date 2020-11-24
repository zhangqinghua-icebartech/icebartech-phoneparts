package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.phoneparts.system.dto.SysClassOneDto;
import com.icebartech.phoneparts.system.dto.SysClassThreeDTO;
import com.icebartech.phoneparts.system.dto.SysClassTwoDto;
import com.icebartech.phoneparts.system.po.SysClassThree;
import com.icebartech.phoneparts.system.repository.SysClassOneRepository;
import com.icebartech.phoneparts.system.repository.SysClassThreeRepository;
import com.icebartech.phoneparts.system.repository.SysClassTwoRepository;
import com.icebartech.phoneparts.system.service.SysClassOneService;
import com.icebartech.phoneparts.system.service.SysClassThreeService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author Created by liuao on 2020/6/8 0008$.
 * @desc
 */
@Service
public class SysClassThreeServiceImpl extends AbstractService<SysClassThreeDTO, SysClassThree, SysClassThreeRepository> implements SysClassThreeService {

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;
    @Autowired
    private SysClassOneService sysClassOneService;
    @Autowired
    private SysClassTwoService sysClassTwoService;
    @Autowired
    private SysClassOneRepository sysClassOneRepository;
    @Autowired
    private SysClassTwoRepository sysClassTwoRepository;


    @Override
    protected void warpDTO(Long id, SysClassThreeDTO classThree) {
//        classThree.setIcon(aliyunOSSComponent.generateDownloadUrl(classThree.getIcon()));
//        SysClassOneDto classOne = sysClassOneService.findOne(classThree.getClassOneId());
//        classThree.setClassOneName(classOne.getChinaName());
//        SysClassTwoDto classTwo = sysClassTwoService.findOne(classThree.getClassTwoId());
//        classThree.setClassTwoName(classTwo.getChinaName());
    }

    @Override
    protected void warpDTO(List<Long> ids, List<SysClassThreeDTO> ds) {
        // 1. 设置图片
        ds.forEach(d -> d.setIcon(aliyunOSSComponent.generateDownloadUrl(d.getIcon())));

        // 2. 设置一级分类
        List<SysClassOneDto> classOnes = BeanMapper.mapList(sysClassOneRepository.findName(ds.stream()
                                                                                             .map(SysClassThreeDTO::getClassOneId)
                                                                                             .collect(Collectors.toList())),
                                                            SysClassOneDto.class);
        ds.forEach(d -> classOnes.stream()
                                 .filter(t -> t.getId().equals(d.getClassOneId()))
                                 .findAny()
                                 .ifPresent(t -> d.setClassOneName(t.getChinaName())));


        // 3. 设置二级分类
        List<SysClassTwoDto> classTwos = BeanMapper.mapList(sysClassTwoRepository.findName(ds.stream()
                                                                                             .map(SysClassThreeDTO::getClassTwoId)
                                                                                             .collect(Collectors.toList())),
                                                            SysClassTwoDto.class);
        ds.forEach(d -> classTwos.stream()
                                 .filter(t -> t.getId().equals(d.getClassTwoId()))
                                 .findAny()
                                 .ifPresent(t -> d.setClassTwoName(t.getChinaName())));
    }

    @Override
    public Boolean changeSort(Long id, Integer sort) {
        return super.update(eq(SysClassThree::getId, id), eq(SysClassThree::getSort, sort));
    }

    @Override
    public String findName(Long classThreeId) {
        return repository.findName(classThreeId);
    }

    @Override
    public List<SysClassThreeDTO> find_name_by_two(Long classTwoId) {
        if (classTwoId == null) {
            return new ArrayList<>();
        }
        return BeanMapper.mapList(repository.find_name_by_two(classTwoId), SysClassThreeDTO.class);
    }
}
