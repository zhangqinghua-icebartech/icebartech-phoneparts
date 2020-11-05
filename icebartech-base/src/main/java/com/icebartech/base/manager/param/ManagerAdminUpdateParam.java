package com.icebartech.base.manager.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ManagerAdminUpdateParam {

    @NotNull
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    @ApiModelProperty(value = "账号名")
    private String loginName;

    @ApiModelProperty(value = "密码", example = "sdkgjskd353025734782")
    private String password;

    @ApiModelProperty(value = "管理员头像Key", example = "avator.png")
    private String avatorKey;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;
}
