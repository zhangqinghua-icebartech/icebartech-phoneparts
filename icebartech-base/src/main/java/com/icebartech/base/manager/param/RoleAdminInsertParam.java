package com.icebartech.base.manager.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class RoleAdminInsertParam {

    @NotBlank
    @ApiModelProperty(value = "角色名称", example = "超级管理员")
    private String roleName;

    @NotBlank
    @ApiModelProperty(value = "角色描述", example = "这个角色是做。。。用的")
    private String roleDesc;

    @NotEmpty
    @ApiModelProperty(value = "权限Id列表")
    private List<Long> menuIds;
}
