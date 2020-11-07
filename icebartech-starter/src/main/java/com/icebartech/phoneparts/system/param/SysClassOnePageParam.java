package com.icebartech.phoneparts.system.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysClassOnePageParam extends PageParam {

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称（选填）")
    private String chinaNameLike;

    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称（选填）")
    private String englishNameLike;

    @ApiModelProperty(value = "递减排序", hidden = true)
    private Boolean sortDESC = true;

    @ApiModelProperty(value = "递增排序", hidden = true)
    private Boolean idASC = true;

    @ApiModelProperty(value = "代理商ids", hidden = true)
    private List<Long> agentIdIn;
}
