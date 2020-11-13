package com.icebartech.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xiaoxiong
 * @date 2018/11/13 17:46
 */
@Data
@ApiModel(value = "Capital", description = "贷款本息还款")
public class Capital {

    @ApiModelProperty(value = "还款总额")
    private String totalMoney;

    @ApiModelProperty(value = "贷款总额")
    private String principal;

    @ApiModelProperty(value = "还款总利息")
    private String interest;

    @ApiModelProperty(value = "每月还款金额")
    private String preLoan;

    @ApiModelProperty(value = "首月还款金额")
    private String firstMonth;

    @ApiModelProperty(value = "还款期限")
    private String months;

    @ApiModelProperty(value = "每月递减利息")
    private String decreaseMonth;

}
