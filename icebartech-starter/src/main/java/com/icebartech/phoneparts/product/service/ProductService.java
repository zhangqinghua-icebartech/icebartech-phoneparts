package com.icebartech.phoneparts.product.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.product.dto.ProductDto;
import com.icebartech.phoneparts.product.po.Product;

/**
 * @author pc
 * @Date 2019-06-18T11:11:51.866
 * @Description 单品表
 */

public interface ProductService extends BaseService
<ProductDto, Product> {

    Boolean changeSort(Long id, Integer sort);

    ProductDto findByClassTwoId(Long id);

    ProductDto findByClassThreeId(Long id);
}
