package com.icebartech.phoneparts.product.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
public class ProductUpdateParam {

    @NotNull
    private Long id;

    @ApiModelProperty(value = "一级分类id", example = "一级分类id")
    private Long classOneId;

    @ApiModelProperty(value = "二级分类id", example = "二级分类id")
    private Long classTwoId;

    @ApiModelProperty(value = "三级分类id", example = "三级分类id")
    private Long classThreeId;

    @ApiModelProperty(value = "分类中文名称", example = "分类中文名称")
    private String chinaName;

    @ApiModelProperty(value = "分类英文名称", example = "分类英文名称")
    private String englishName;

    @ApiModelProperty(value = "展示图", example = "展示图")
    private String coverIcon;

    @ApiModelProperty(value = "详情图", example = "详情图")
    private String detailIcon;

    @ApiModelProperty(value = "文件", example = "文件")
    private String file;

    @ApiModelProperty(value = "文件名称", example = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "排序", example = "2")
    private Integer sort;
}
