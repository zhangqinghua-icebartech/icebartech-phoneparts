package com.icebartech.phoneparts.product.service.impl;

import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.product.dto.UseRecordDTO;
import com.icebartech.phoneparts.product.param.UseRecordInsertParam;
import com.icebartech.phoneparts.product.param.UseRecordProductPageParam;
import com.icebartech.phoneparts.product.param.UseRecordUserPageParam;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Service
public class UseRecordServiceImpl extends AbstractService
        <UseRecordDTO, UseRecord, UseRecordRepository> implements UseRecordService {

    @Autowired
    private ProductService productService;
    @Autowired
    private UseRecordRepository repository;

    @Override
    protected void warpDTO(Long id, UseRecordDTO useRecord) {
        useRecord.setProduct(productService.findOneOrNull(useRecord.getProductId()));
    }

    @Override
    @Async
    public void add(Long userId, Long productId,Long agentId,Long secondAgentId) {
        super.insert(new UseRecordInsertParam(userId,productId,agentId,secondAgentId));
    }

    @Override
    public Page<Map> findUserRecord(UseRecordUserPageParam param) {

        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize());
        if(param.getUseCountDESC()){
            return repository.findUserRecordDESC(param,pageable);
        }
        return repository.findUserRecordASC(param,pageable);
    }

    @Override
    public Page<Map> findProductRecord(UseRecordProductPageParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize());
        if(param.getUseCountDESC()){
            return repository.findProductRecordDESC(param,pageable);
        }
        return repository.findProductRecordASC(param,pageable);
    }

    @Override
    public Map<String, Object> findUserRecordCount(UseRecordUserPageParam param) {
        Integer countRecord = repository.findUserCountRecord(param);
        Map<String,Object> map = new HashMap<>();
        map.put("countRecord",countRecord);
        return map;
    }

}
