package com.icebartech.phoneparts.system.param;

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
public class SysClassTwoPageParam extends PageParam {

    @ApiModelProperty(value = "所属一级分类",example = "所属一级分类（选填）")
    private Long classOneId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称（选填）")
    private String chinaNameLike;

    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称（选填）")
    private String englishNameLike;

    @ApiModelProperty(value = "是否使用密码 1使用 0不使用",example = "是否使用密码 1使用 0不使用")
    private Integer isLock;

    @ApiModelProperty(value = "递减排序", hidden = true)
    private Boolean sortDESC = true;

    @ApiModelProperty(value = "递增排序", hidden = true)
    private Boolean idASC = true;
}
