package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2020/6/8 0008$.
 * @desc
 */
@Data
public class SysClassThreeInsertParam {

    @ApiModelProperty(value = "所属一级分类",example = "所属一级分类")
    @NotNull
    private Long classOneId;

    @NotNull
    @ApiModelProperty(value = "二级分类id",example = "二级分类id")
    private Long classTwoId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    private String chinaName;

    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称")
    private String englishName;

    @ApiModelProperty(value = "图标",example = "图标")
    @NotBlank
    private String icon;

}
