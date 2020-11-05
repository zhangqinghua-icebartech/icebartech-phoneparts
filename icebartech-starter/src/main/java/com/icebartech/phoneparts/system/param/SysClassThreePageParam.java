package com.icebartech.phoneparts.system.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author Created by liuao on 2020/6/8 0008$.
 * @desc
 */
@Data
public class SysClassThreePageParam extends PageParam {

    @ApiModelProperty(value = "所属一级分类",example = "所属一级分类（选填）")
    private Long classOneId;

    @ApiModelProperty(value = "二级分类id",example = "二级分类id")
    private Long classTwoId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称（选填）")
    private String chinaNameLike;

    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称（选填）")
    private String englishNameLike;

    @ApiModelProperty(value = "递减排序", hidden = true)
    private Boolean sortDESC = true;

    @ApiModelProperty(value = "递增排序", hidden = true)
    private Boolean idASC = true;

}
