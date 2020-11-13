package com.icebartech.phoneparts.product.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductPageParam extends PageParam {

    @ApiModelProperty(value = "一级分类id",example = "一级分类id")
    private Long classOneId;

    @ApiModelProperty(value = "二级分类id",example = "二级分类id")
    private Long classTwoId;

    @ApiModelProperty(value = "中文名称",example = "中文名称")
    private String chinaNameLike;

    @ApiModelProperty(value = "英文名称",example = "英文名称")
    private String englishNameLike;

    @ApiModelProperty(value = "递减排序", hidden = true)
    private Boolean sortDESC = true;

    @ApiModelProperty(value = "递增排序", hidden = true)
    private Boolean idASC = true;

}
