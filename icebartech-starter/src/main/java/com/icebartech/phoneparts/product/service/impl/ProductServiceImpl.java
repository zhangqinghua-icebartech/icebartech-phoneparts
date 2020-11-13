package com.icebartech.phoneparts.product.service.impl;


import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.product.dto.ProductDto;
import com.icebartech.phoneparts.product.repository.ProductRepository;
import com.icebartech.phoneparts.product.service.ProductService;
import com.icebartech.phoneparts.product.po.Product;
import com.icebartech.phoneparts.system.service.SysClassOneService;
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

    private final SysClassOneService sysClassOneService;
    private final SysClassTwoService sysClassTwoService;
    private final AliyunOSSComponent aliyunOSSComponent;

    @Autowired
    public ProductServiceImpl(SysClassOneService sysClassOneService,
                              SysClassTwoService sysClassTwoService,
                              AliyunOSSComponent aliyunOSSComponent) {
        this.sysClassOneService = sysClassOneService;
        this.sysClassTwoService = sysClassTwoService;
        this.aliyunOSSComponent = aliyunOSSComponent;
    }

    @Override
    protected void warpDTO(Long id, ProductDto product) {
        product.setOneClassName(sysClassOneService.findDetail(product.getClassOneId()).getChinaName());
        product.setTwoClassName(sysClassTwoService.findDetail(product.getClassTwoId()).getChinaName());
        product.setCoverIcon(aliyunOSSComponent.generateDownloadUrl(product.getCoverIcon()));
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

}