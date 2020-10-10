package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/9/6.
 * @desc
 */
@Data
public class SysSerialClassUpdateParam {

    @NotNull
    @ApiModelProperty(value = "id",example = "id")
    private Long id;

    @ApiModelProperty(value = "公司id",example = "公司id")
    private Long agentId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    private String chinaName;

}
