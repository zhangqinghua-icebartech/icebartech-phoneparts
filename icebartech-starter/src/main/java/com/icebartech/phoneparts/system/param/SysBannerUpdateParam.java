package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by liuao on 2020/9/27.
 * @desc
 */
@Data
public class SysBannerUpdateParam {

    @ApiModelProperty(value = "id",example = "id")
    private Long id;

    @ApiModelProperty(value = "详情图",example = "详情图")
    private String detailIcon;


}
