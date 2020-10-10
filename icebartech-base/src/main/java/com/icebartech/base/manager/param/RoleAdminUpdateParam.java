package com.icebartech.base.manager.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RoleAdminUpdateParam {

    @NotNull
    private Long id;

    @ApiModelProperty(value = "角色名称", example = "超级管理员")
    private String roleName;

    @ApiModelProperty(value = "角色描述", example = "这个角色是做。。。用的")
    private String roleDesc;

    @ApiModelProperty(value = "权限Id列表")
    private List<Long> menuIds;
}
