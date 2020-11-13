package com.icebartech.phoneparts.redeem.dto;

import com.icebartech.phoneparts.redeem.po.RedeemCode;
import com.icebartech.phoneparts.user.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-08-28T15:38:14.870
 * @Description 兑换码
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "兑换码")
public class RedeemCodeDTO extends RedeemCode{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("二维码")
    private String qrCode;

}
