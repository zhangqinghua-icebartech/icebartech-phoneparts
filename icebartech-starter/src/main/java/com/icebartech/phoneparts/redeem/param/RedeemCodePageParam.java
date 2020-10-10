package com.icebartech.phoneparts.redeem.param;

import com.icebartech.core.params.PageParam;
import com.icebartech.phoneparts.enums.RedeemStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RedeemCodePageParam extends PageParam {


    @ApiModelProperty(value = "兑换码",example = "兑换码")
    private String codeLike;

    @ApiModelProperty(value = "兑换状态",example = "兑换状态")
    private RedeemStateEnum state;

    @ApiModelProperty(value = "兑换管理id",example = "兑换管理id")
    private Long redeemId;

    @ApiModelProperty(value = "邮箱",example = "邮箱")
    private String emailLike;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime gmtCreatedLT;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime gmtCreatedGT;
}
