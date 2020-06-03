package com.icebartech.phoneparts.system.param;

import com.icebartech.core.enums.ChooseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/9/4.
 * @desc
 */
@Data
public class SysUseConfigUpdateParam {

    @NotNull
    @ApiModelProperty(value = "id",example = "id")
    private Long id;

    @ApiModelProperty(value = "速度",example = "速度")
    private Integer speed;

    @ApiModelProperty(value = "压力",example = "压力")
    private Integer pressure;

    @ApiModelProperty(value = "名称",example = "名称")
    private String name;

    @ApiModelProperty(value = "使用状态",example = "使用状态")
    private ChooseType state;

}
