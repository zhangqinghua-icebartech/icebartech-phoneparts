package com.icebartech.phoneparts.redeem.service.impl;

import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.redeem.dto.RedeemRecordDTO;
import com.icebartech.phoneparts.redeem.po.RedeemRecord;
import com.icebartech.phoneparts.redeem.repository.RedeemRecordRepository;
import com.icebartech.phoneparts.redeem.service.RedeemRecordService;
import org.springframework.stereotype.Service;

/**
 * @author pc
 * @Date 2019-08-28T15:38:51.191
 * @Description 兑换记录
 */

@Service
public class RedeemRecordServiceImpl extends AbstractService
<RedeemRecordDTO, RedeemRecord, RedeemRecordRepository> implements RedeemRecordService {

}