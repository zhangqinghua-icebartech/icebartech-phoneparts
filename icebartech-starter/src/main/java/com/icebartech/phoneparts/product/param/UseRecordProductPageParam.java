package com.icebartech.phoneparts.product.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Created by liuao on 2019/10/22.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UseRecordProductPageParam extends PageParam {

    @ApiModelProperty
    private Long userId;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "次级代理商id",example = "次级代理商id")
    private Long secondAgentId;

    @ApiModelProperty(value = "是否倒序")
    private Boolean useCountDESC = true;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime strTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

}
