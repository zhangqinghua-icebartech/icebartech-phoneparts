package com.icebartech.phoneparts.product.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
public class ProductInsertParam {

    @ApiModelProperty(value = "一级分类id",example = "一级分类id")
    @NotNull
    private Long classOneId;

    @ApiModelProperty(value = "二级分类id",example = "二级分类id")
    @NotNull
    private Long classTwoId;

    @NotNull
    @ApiModelProperty(value = "三级分类id",example = "三级分类id")
    private Long classThreeId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    @NotBlank
    private String chinaName;

    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称")
    @NotBlank
    private String englishName;

    @ApiModelProperty(value = "展示图",example = "展示图")
    @NotBlank
    private String coverIcon;

    @ApiModelProperty(value = "详情图",example = "详情图")
    @NotBlank
    private String detailIcon;

    @NotBlank
    @ApiModelProperty(value = "文件名称",example = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件",example = "文件")
    @NotBlank
    private String file;

}
