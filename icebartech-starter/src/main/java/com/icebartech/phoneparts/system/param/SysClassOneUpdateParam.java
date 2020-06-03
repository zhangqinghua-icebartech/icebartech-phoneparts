package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
public class SysClassOneUpdateParam {

    @NotNull
    private Long id;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    private String chinaName;

    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称")
    private String englishName;

    @ApiModelProperty(value = "图标",example = "图标")
    private String icon;

}
