package com.icebartech.phoneparts.system.param;

import com.icebartech.core.params.PageParam;
import com.icebartech.phoneparts.enums.AppConfigTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysAppConfigPageParam extends PageParam {


    @ApiModelProperty(value = "配置类型")
    private AppConfigTypeEnum type;

}
