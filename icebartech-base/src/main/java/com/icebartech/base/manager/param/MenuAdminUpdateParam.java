package com.icebartech.base.manager.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MenuAdminUpdateParam {

    @NotNull
    private Long id;

    @ApiModelProperty(value = "图标Key", example = "icon.png")
    private String iconKey;

    @ApiModelProperty(value = "权限url", example = "/banner/manager")
    private String menuUrl;

    @ApiModelProperty(value = "权限名称", example = "系统设置")
    private String menuName;
}
