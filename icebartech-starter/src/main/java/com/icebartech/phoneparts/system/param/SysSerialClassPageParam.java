package com.icebartech.phoneparts.system.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Created by liuao on 2019/9/6.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysSerialClassPageParam extends PageParam {

    @ApiModelProperty(value = "公司id",example = "公司id")
    private Long agentId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    private String chinaNameLike;

    @ApiModelProperty(value = "递减排序", hidden = true)
    private Boolean sortDESC = true;

    @ApiModelProperty(value = "代理商上级id",example = "代理商上级id")
    private Long parentId;

    @ApiModelProperty(value = "递增排序", hidden = true)
    private Boolean idASC = true;

}
