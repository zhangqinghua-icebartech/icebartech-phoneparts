package com.icebartech.phoneparts.redeem.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RedeemPageParam extends PageParam {

    @ApiModelProperty(value = "兑换码标题",example = "兑换码标题")
    private String titleLike;


}
