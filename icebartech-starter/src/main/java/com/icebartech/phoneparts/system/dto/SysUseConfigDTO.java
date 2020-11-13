package com.icebartech.phoneparts.system.dto;

import com.icebartech.phoneparts.system.po.SysUseConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-09-04T14:32:53.763
 * @Description 常用设置
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "常用设置")
public class SysUseConfigDTO extends SysUseConfig{

    private static final long serialVersionUID = 1L;

}
