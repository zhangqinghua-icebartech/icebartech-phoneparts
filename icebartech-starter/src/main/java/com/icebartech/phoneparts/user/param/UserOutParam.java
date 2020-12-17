package com.icebartech.phoneparts.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Created by liuao on 2019/6/27.
 * @desc
 */
@Data
@ToString(callSuper = true)
public class UserOutParam {

    @ApiModelProperty(value = "序列号", example = "序列号（选填）")
    private String serialNumLike;

    @ApiModelProperty(value = "邮箱", example = "邮箱（选填）")
    private String emailLike;

    @ApiModelProperty(value = "代理商id", example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "次级代理商id", example = "次级代理商id")
    private Long secondAgentId;

    @ApiModelProperty(value = "二级分类", example = "次级代理商id")
    private Long secondSerialClassId;

    @ApiModelProperty(value = "判断查询是什么类型的 0 管理员 1 一级代理 2 二级代理 ")
    private Integer agentlevel;
}
