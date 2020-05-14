package com.icebartech.phoneparts.product.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.product.dto.UseRecordDTO;
import com.icebartech.phoneparts.product.param.UseRecordProductPageParam;
import com.icebartech.phoneparts.product.param.UseRecordUserPageParam;
import com.icebartech.phoneparts.product.po.UseRecord;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
public interface UseRecordService extends BaseService
        <UseRecordDTO, UseRecord> {

    /**
     * 添加记录
     * @param userId 用户
     * @param productId 产品
     */
    void add(Long userId, Long productId,Long agentId,Long secondAgentId);

    Page<Map> findUserRecord(UseRecordUserPageParam param);

    Page<Map> findProductRecord(UseRecordProductPageParam param);
}
