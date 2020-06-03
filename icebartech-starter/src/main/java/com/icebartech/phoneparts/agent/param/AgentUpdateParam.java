package com.icebartech.phoneparts.agent.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author Created by liuao on 2019/9/5.
 * @desc
 */
@Data
public class AgentUpdateParam {


    @NotNull
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("公司联系人")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("账号")
    private String loginName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("分类名称")
    private String className;

    @ApiModelProperty(value = "排序",example = "排序")
    private Integer sort;

    @ApiModelProperty(value = "代理商上级id",example = "代理商上级id")
    private Long parentId;

}
