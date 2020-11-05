package com.icebartech.base.manager.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ManagerAdminInsertParam {

    @NotNull
    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    @NotBlank
    @ApiModelProperty(value = "用户名")
    private String userName;

    @NotBlank
    @ApiModelProperty(value = "账号名")
    private String loginName;

    @NotBlank
    @ApiModelProperty(value = "密码", example = "sdkgjskd353025734782")
    private String password;

    @ApiModelProperty(value = "管理员头像Key", example = "avator.png")
    private String avatorKey;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

}
