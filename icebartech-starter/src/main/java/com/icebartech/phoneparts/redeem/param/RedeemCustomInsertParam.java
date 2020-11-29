package com.icebartech.phoneparts.redeem.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RedeemCustomInsertParam {

    @NotBlank
    @ApiModelProperty(value = "兑换码标题", example = "兑换码标题")
    private String title;

    @NotBlank
    @ApiModelProperty(value = "兑换码", example = "兑换码")
    private String code;

    @NotNull
    @ApiModelProperty(value = "兑换码次数", example = "兑换码次数")
    private Integer useNum;

    @NotNull
    @ApiModelProperty(value = "代理商id", example = "代理商id")
    private Long agentId;
}
