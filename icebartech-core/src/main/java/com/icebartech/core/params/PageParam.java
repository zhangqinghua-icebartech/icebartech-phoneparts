package com.icebartech.core.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PageParam {

    @ApiModelProperty(value = "第几页，默认第一页", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageIndex = 1;

    @ApiModelProperty(value = "每页大小，默认10", example = "10")
    @Min(value = 1, message = "每页大小不能小于1")
    private Integer pageSize = 10;
}
