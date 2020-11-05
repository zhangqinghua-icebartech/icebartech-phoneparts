package com.icebartech.phoneparts.redeem.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
@Data
public class RedeemInsertParam {

    @ApiModelProperty(value = "兑换码标题",example = "兑换码标题")
    private String title;

    @ApiModelProperty(value = "兑换码数量",example = "兑换码数量")
    private Integer redeemNum;

    @ApiModelProperty(value = "兑换码次数",example = "兑换码次数")
    private Integer useNum;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;
    
}
