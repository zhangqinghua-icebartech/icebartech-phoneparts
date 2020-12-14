package com.icebartech.phoneparts.user.dto;

import com.icebartech.excel.annotation.ExcelField;
import com.icebartech.phoneparts.user.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户表")
public class UserDto extends User {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "过期时间", example = "过期时间")
    private Date pastTime;

    @ApiModelProperty(value = "来源", example = "来源")
    private String origin;

    @ApiModelProperty(value = "代理商名称", example = "代理商名称")
    private String agentName;

    @ApiModelProperty(value = "一级分类名称", example = "一级分类名称")
    private String agentClassName;

    @ApiModelProperty(value = "批次", example = "二级分类称")
    private String serialClassName;

    @ApiModelProperty(value = "二级分类称", example = "二级分类称")
    private String secondSerialClassName;

    @ExcelField(value = "注册时间")
    @ApiModelProperty(value = "注册时间")
    private String registerTime;
}
