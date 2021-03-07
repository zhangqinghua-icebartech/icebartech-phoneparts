package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysConfigInsertParam {

    @NotNull
    @ApiModelProperty(value = "代理商id", example = "代理商id")
    private Long agentId;

    @NotNull
    @ApiModelProperty(value = "类型 0 cut 1 devia 2 usams 3 byoyond cell 4 green mnky 5. 竖屏 6.横屏", example = "0")
    private Integer type;

    @NotBlank
    @ApiModelProperty(value = "版本号", example = "版本号")
    private String version;

    @NotBlank
    @ApiModelProperty(value = "安装包链接", example = "安装包链接")
    private String fileKey;

    @NotBlank
    @ApiModelProperty(value = "安装包名称", example = "安装包名称")
    private String fileName;
}
