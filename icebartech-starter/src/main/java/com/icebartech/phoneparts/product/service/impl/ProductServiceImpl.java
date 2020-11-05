package com.icebartech.phoneparts.product.service.impl;


import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.product.dto.ProductDto;
import com.icebartech.phoneparts.product.repository.ProductRepository;
import com.icebartech.phoneparts.product.service.ProductService;
import com.icebartech.phoneparts.product.po.Product;
import com.icebartech.phoneparts.system.service.SysClassOneService;
import com.icebartech.phoneparts.system.service.SysClassThreeService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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


    @Override
    protected void warpDTO(Long id, ProductDto product) {
        product.setOneClassName(sysClassOneService.findDetail(product.getClassOneId()).getChinaName());
        product.setTwoClassName(sysClassTwoService.findName(product.getClassTwoId()));
        product.setThreeClassName(sysClassThreeService.findName(product.getClassThreeId()));
        product.setCoverIcon(aliyunOSSComponent.generateDownloadUrl(product.getDetailIcon()));
        product.setFile(aliyunOSSComponent.generateDownloadUrl(product.getFile()));
        product.setDetailIcon(aliyunOSSComponent.generateDownloadUrl(product.getDetailIcon()));
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