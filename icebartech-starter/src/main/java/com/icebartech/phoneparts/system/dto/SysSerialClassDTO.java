package com.icebartech.phoneparts.system.dto;

import com.icebartech.phoneparts.system.po.SysSerialClass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-09-06T16:15:06.735
 * @Description 序列号类别
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysSerialClassDTO extends SysSerialClass{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("一级分类名称")
    private String agentClassName;

}
