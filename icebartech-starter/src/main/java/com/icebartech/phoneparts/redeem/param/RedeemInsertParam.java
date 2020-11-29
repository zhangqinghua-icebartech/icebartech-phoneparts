package com.icebartech.phoneparts.redeem.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
@Data
public class RedeemInsertParam {

    @NotBlank
    @ApiModelProperty(value = "兑换码标题", example = "兑换码标题")
    private String title;

    @NotNull
    @ApiModelProperty(value = "兑换码数量", example = "兑换码数量")
    private Integer redeemNum;

    @NotNull
    @ApiModelProperty(value = "兑换码次数", example = "兑换码次数")
    private Integer useNum;

    @NotNull
    @ApiModelProperty(value = "代理商id", example = "代理商id")
    private Long agentId;

}
