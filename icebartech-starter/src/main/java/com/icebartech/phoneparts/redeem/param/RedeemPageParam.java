package com.icebartech.phoneparts.redeem.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RedeemPageParam extends PageParam {

    @ApiModelProperty(value = "兑换码标题",example = "兑换码标题")
    private String titleLike;

    @ApiModelProperty(value = "充值号码",example = "充值号码")
    private String code;

    @ApiModelProperty(value = "邮箱",example = "邮箱")
    private String email;

    @ApiModelProperty(value = "ids",example = "ids",hidden = true)
    private List<Long> idIn;
}
