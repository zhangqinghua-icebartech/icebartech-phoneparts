package com.icebartech.phoneparts.redeem.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.redeem.dto.RedeemDTO;
import com.icebartech.phoneparts.redeem.param.RedeemCustomInsertParam;
import com.icebartech.phoneparts.redeem.param.RedeemInsertParam;
import com.icebartech.phoneparts.redeem.po.Redeem;
import com.icebartech.phoneparts.redeem.po.RedeemCode;

import java.util.List;

/**
 * @author pc
 */

public interface RedeemService extends BaseService<RedeemDTO, Redeem> {

    Boolean insertAll(RedeemInsertParam param);

    Boolean insertCustom(RedeemCustomInsertParam param);

    Boolean insertCustom(String title, Long agentId, List<RedeemCode> params);

    Boolean deleteAll(Long id);


}
