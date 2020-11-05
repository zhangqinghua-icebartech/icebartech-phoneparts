package com.icebartech.phoneparts.system.param;

import com.esotericsoftware.kryo.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by liuao on 2019/9/10.
 * @desc
 */
@Data
public class SysSerialCreateParam {

    @NotNull
    @ApiModelProperty("序列号个数")
    private Integer num;

    @NotNull
    @ApiModelProperty(value = "代理商id")
    private Long agentId;

    @NotNull
    @ApiModelProperty(value = "二级分类id")
    private Long serialClassId;

}
