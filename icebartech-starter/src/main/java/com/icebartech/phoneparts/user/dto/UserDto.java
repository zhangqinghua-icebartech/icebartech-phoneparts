package com.icebartech.phoneparts.user.dto;

import com.icebartech.phoneparts.user.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author pc
 * @Date 2019-06-18T11:03:37.885
 * @Description 用户表
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户表")
public class UserDto extends User{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "过期时间",example = "过期时间")
    private Date pastTime;

    @ApiModelProperty(value = "来源",example = "来源")
    private String origin;

    @ApiModelProperty(value = "代理商名称",example = "代理商名称")
    private String agentName;

    @ApiModelProperty(value = "一级分类名称",example = "一级分类名称")
    private String agentClassName;

}
