package com.icebartech.phoneparts.system.dto;

import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.system.po.SysClassOne;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-06-18T11:10:43.524
 * @Description 一级菜单表
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "一级菜单表")
public class SysClassOneDto extends SysClassOne{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "代理商")
    private AgentDTO agent;
}
