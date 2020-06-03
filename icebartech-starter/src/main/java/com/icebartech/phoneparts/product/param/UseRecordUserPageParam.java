package com.icebartech.phoneparts.product.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Created by liuao on 2019/10/21.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UseRecordUserPageParam extends PageParam {

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "次级代理商id",example = "次级代理商id")
    private Long secondAgentId;

    @ApiModelProperty(value = "是否倒序")
    private Boolean useCountDESC = true;

    @ApiModelProperty(value = "序列号",example = "序列号")
    private String serialNum = "";

    @ApiModelProperty(value = "邮箱",example = "邮箱")
    private String email = "";

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime strTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

}
