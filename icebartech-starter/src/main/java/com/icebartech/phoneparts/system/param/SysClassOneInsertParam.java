package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
public class SysClassOneInsertParam {

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    private String chinaName;

    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称")
    private String englishName;

    @ApiModelProperty(value = "一级分类id代理商id",example = "一级分类id代理商id")
    private Long agentId;

    @ApiModelProperty(value = "图标",example = "图标")
    @NotBlank
    private String icon;

    @NotBlank
    @ApiModelProperty(value = "英文图标",example = "英文图标")
    private String englishIcon;

}
