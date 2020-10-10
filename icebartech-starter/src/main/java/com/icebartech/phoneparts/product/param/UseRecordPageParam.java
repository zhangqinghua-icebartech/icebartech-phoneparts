package com.icebartech.phoneparts.product.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UseRecordPageParam extends PageParam {

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "次级代理商id",example = "次级代理商id")
    private Long secondAgentId;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime gmtCreatedLT;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime gmtCreatedGT;

    @ApiModelProperty(value = "用户id",example = "用户id")
    private Long userId;

    @ApiModelProperty(value = "产品id",example = "产品id")
    private Long productId;


}
