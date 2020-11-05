package com.icebartech.phoneparts.agent.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


/**
 * @author Created by liuao on 2019/9/5.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddUseRecordPageParam extends PageParam {

    @ApiModelProperty(value = "用户id",example = "用户id")
    private Long userId;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "序列号",example = "序列号")
    private String serialNumLike;

    @ApiModelProperty(value = "绑定邮箱",example = "绑定邮箱")
    private String bindMailLike;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime gmtCreatedLT;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime gmtCreatedGT;

}
