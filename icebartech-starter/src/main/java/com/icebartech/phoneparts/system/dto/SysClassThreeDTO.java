package com.icebartech.phoneparts.system.dto;

import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.system.po.SysClassThree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Created by liuao on 2020/6/8 0008$.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "二级分类表")
public class SysClassThreeDTO extends SysClassThree {

    @ApiModelProperty(value = "一级分类中文名称",example = "一级分类中文名称")
    private String classOneName;

    @ApiModelProperty(value = "二级分类中文名称",example = "二级分类中文名称")
    private String classTwoName;

    @ApiModelProperty(value = "用户可见")
    private AgentDTO agent;
}
