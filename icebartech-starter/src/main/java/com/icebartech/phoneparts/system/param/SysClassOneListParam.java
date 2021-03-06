package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Created by liuao on 2019/7/12.
 * @desc
 */
@Data
public class SysClassOneListParam {

    @ApiModelProperty(value = "递减排序", hidden = true)
    private Boolean sortDESC = true;

    @ApiModelProperty(value = "递增排序", hidden = true)
    private Boolean idASC = true;

    @ApiModelProperty(value = "代理商ids", hidden = true)
    private List<Long> agentIdIn;

}
