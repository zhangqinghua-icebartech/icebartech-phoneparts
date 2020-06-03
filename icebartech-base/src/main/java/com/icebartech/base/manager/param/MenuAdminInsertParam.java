package com.icebartech.base.manager.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MenuAdminInsertParam {

    @ApiModelProperty(value = "该权限的父级权限Id（0表示是一级权限）")
    private Long parentId = 0L;

    @ApiModelProperty(value = "图标Key", example = "icon.png")
    private String iconKey;

    @NotBlank
    @ApiModelProperty(value = "权限url", example = "/banner/manager")
    private String menuUrl;

    @NotBlank
    @ApiModelProperty(value = "权限名称", example = "系统设置")
    private String menuName;
}
