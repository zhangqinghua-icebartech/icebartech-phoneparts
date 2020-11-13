package com.icebartech.phoneparts.redeem.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.redeem.dto.RedeemDTO;
import com.icebartech.phoneparts.redeem.param.RedeemInsertParam;
import com.icebartech.phoneparts.redeem.po.Redeem;

/**
 * @author pc
 * @Date 2019-08-28T15:35:58.161
 * @Description 兑换码管理
 */

public interface RedeemService extends BaseService
<RedeemDTO, Redeem> {

    Boolean insertAll(RedeemInsertParam param);

    Boolean deleteAll(Long id);
}
