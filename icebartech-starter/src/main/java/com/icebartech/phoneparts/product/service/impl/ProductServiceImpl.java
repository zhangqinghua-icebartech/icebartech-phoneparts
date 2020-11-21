package com.icebartech.phoneparts.product.service.impl;


import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.phoneparts.product.dto.ProductDto;
import com.icebartech.phoneparts.product.repository.ProductRepository;
import com.icebartech.phoneparts.product.service.ProductService;
import com.icebartech.phoneparts.product.po.Product;
import com.icebartech.phoneparts.system.po.SysClassOne;
import com.icebartech.phoneparts.system.po.SysClassTwo;
import com.icebartech.phoneparts.system.service.SysClassOneService;
import com.icebartech.phoneparts.system.service.SysClassThreeService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-06-18T11:11:51.866
 * @Description 单品表
 */

@Service
public class ProductServiceImpl extends AbstractService
        <ProductDto, Product, ProductRepository> implements ProductService {

    @Autowired
    private SysClassOneService sysClassOneService;

    @Autowired
    private SysClassTwoService sysClassTwoService;

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;

    @Autowired
    private SysClassThreeService sysClassThreeService;


//    @Override
//    protected void warpDTO(Long id, ProductDto product) {
//        product.setOneClassName(sysClassOneService.findDetail(product.getClassOneId()).getChinaName());
//        product.setTwoClassName(sysClassTwoService.findName(product.getClassTwoId()));
//        product.setThreeClassName(sysClassThreeService.findName(product.getClassThreeId()));
//        product.setCoverIcon(aliyunOSSComponent.generateDownloadUrl(product.getDetailIcon()));
//        product.setFile(aliyunOSSComponent.generateDownloadUrl(product.getFile()));
//        product.setDetailIcon(aliyunOSSComponent.generateDownloadUrl(product.getDetailIcon()));
//    }

    @Override
    protected void warpDTO(List<Long> ids, List<ProductDto> ds) {
        // 1. 一级分类名称
        List<SysClassOne> sysClassOnes = BeanMapper.mapList(repository.oneClassNames(ds.stream()
                                                                                       .map(ProductDto::getClassOneId)
                                                                                       .collect(Collectors.toList())),
                                                            SysClassOne.class);
        ds.forEach(d -> sysClassOnes.stream()
                                    .filter(s -> s.getId().equals(d.getClassOneId()))
                                    .findAny()
                                    .ifPresent(s -> d.setOneClassName(s.getChinaName())));

        // 2. 二级分类名称
        List<SysClassTwo> sysClassTwos = BeanMapper.mapList(repository.twoClassNames(ds.stream()
                                                                                       .map(ProductDto::getClassTwoId)
                                                                                       .collect(Collectors.toList())),
                                                            SysClassTwo.class);
        ds.forEach(d -> sysClassTwos.stream()
                                    .filter(s -> s.getId().equals(d.getClassTwoId()))
                                    .findAny()
                                    .ifPresent(s -> d.setTwoClassName(s.getChinaName())));

        // 3. 封面图、文件、明细图
        for (ProductDto d : ds) {
            d.setCoverIcon(aliyunOSSComponent.generateDownloadUrl(d.getCoverIcon()));
            d.setFile(aliyunOSSComponent.generateDownloadUrl(d.getFile()));
            d.setDetailIcon(aliyunOSSComponent.generateDownloadUrl(d.getDetailIcon()));
        }
    }

    @Override
    public Boolean changeSort(Long id, Integer sort) {
        return super.update(eq(ProductDto::getId,id),eq(ProductDto::getSort,sort));
    }

    @Override
    public ProductDto findByClassTwoId(Long id) {
        return super.findOneOrNull(eq(ProductDto::getClassTwoId,id));
    }

    @Override
    public ProductDto findByClassThreeId(Long id) {
        return super.findOneOrNull(eq(ProductDto::getClassThreeId,id));
    }

}