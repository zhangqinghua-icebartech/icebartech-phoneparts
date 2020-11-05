package com.icebartech.phoneparts.product.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UseRecordInsertParam {

    @ApiModelProperty(value = "用户id",example = "用户id")
    private Long userId;

    @ApiModelProperty(value = "产品id",example = "产品id")
    private Long productId;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "次级代理商id",example = "次级代理商id")
    private Long secondAgentId;

}
