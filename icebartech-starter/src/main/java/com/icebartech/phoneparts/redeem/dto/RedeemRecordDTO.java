package com.icebartech.phoneparts.redeem.dto;

import com.icebartech.phoneparts.redeem.po.RedeemRecord;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-08-28T15:38:51.191
 * @Description 兑换记录
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "兑换记录")
public class RedeemRecordDTO extends RedeemRecord{

    private static final long serialVersionUID = 1L;

}
