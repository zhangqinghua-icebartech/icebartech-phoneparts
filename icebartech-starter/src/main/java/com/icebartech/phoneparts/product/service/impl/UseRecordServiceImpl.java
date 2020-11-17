package com.icebartech.phoneparts.product.service.impl;

import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.vo.QueryParam;
import com.icebartech.phoneparts.product.dto.ProductDto;
import com.icebartech.phoneparts.product.dto.UseRecordDTO;
import com.icebartech.phoneparts.product.param.UseRecordInsertParam;
import com.icebartech.phoneparts.product.param.UseRecordProductPageParam;
import com.icebartech.phoneparts.product.param.UseRecordUserPageParam;
import com.icebartech.phoneparts.product.po.Product;
import com.icebartech.phoneparts.product.po.UseRecord;
import com.icebartech.phoneparts.product.repository.UseRecordRepository;
import com.icebartech.phoneparts.product.service.ProductService;
import com.icebartech.phoneparts.product.service.UseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UseRecordServiceImpl extends AbstractService<UseRecordDTO, UseRecord, UseRecordRepository> implements UseRecordService {

    @Autowired
    private ProductService productService;

    @Override
    protected void warpDTO(List<Long> ids, List<UseRecordDTO> ds) {
        List<ProductDto> products = productService.findList(QueryParam.in(Product::getId, ds.stream().map(UseRecordDTO::getProductId).collect(Collectors.toList())));
        ds.forEach(d->d.setProduct(products.stream().filter(p->p.getId().equals(d.getProductId())).findAny().orElse(null)));
    }

    @Override
    @Async
    public void add(Long userId, Long productId, Long agentId, Long secondAgentId) {
        super.insert(new UseRecordInsertParam(userId, productId, agentId, secondAgentId));
    }

    @Override
    public Page<Map> findUserRecord(UseRecordUserPageParam param) {

        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize());
        if (param.getUseCountDESC()) {
            return repository.findUserRecordDESC(param, pageable);
        }
        return repository.findUserRecordASC(param, pageable);
    }

    @Override
    public Page<Map> findProductRecord(UseRecordProductPageParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize());
        if (param.getUseCountDESC()) {
            return repository.findProductRecordDESC(param, pageable);
        }
        return repository.findProductRecordASC(param, pageable);
    }
}
