package com.icebartech.phoneparts.system.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSerialPageParam extends PageParam {

    @ApiModelProperty(value = "序列号",example = "序列号（选填）")
    private String serialNumLike;

    @ApiModelProperty(value = "绑定邮箱",example = "绑定邮箱（选填）")
    private String bindMailLike;

    @ApiModelProperty(value = "状态 0未使用 1使用中 2已过期",example = "状态 0未使用 1使用中 2已过期（选填）")
    private Integer status;

    @ApiModelProperty(value = "是否绑定邮箱 0没有 1有 2邮箱已过期'",example = "是否绑定邮箱 0没有 1有 2邮箱已过期'（选填）")
    private Integer isBindMail;

    @ApiModelProperty(value = "一级分类id代理商id",example = "一级分类id代理商id")
    private Long agentId;

    @ApiModelProperty(value = "总后台的二级分类id",example = "总后台的二级分类id")
    private Long serialClassId;

    @ApiModelProperty(value = "二级分类id次级代理商id",example = "二级分类id次级代理商id")
    private Long secondAgentId;

    @ApiModelProperty(value = "批次",example = "批次")
    private Long batchId;

    @ApiModelProperty(value = "二级分类id次级代理商id 不等于",example = "二级分类id次级代理商id 不等于")
    private Long secondAgentIdNotEq;

    @ApiModelProperty(value = "一级代理商的二级分类id",example = "一级代理商的二级分类id")
    private Long secondSerialClassId;

    @ApiModelProperty(value = "来源 0 平台 1 运营商",example = "来源 0 平台 1 运营商")
    private Integer origin;

}
