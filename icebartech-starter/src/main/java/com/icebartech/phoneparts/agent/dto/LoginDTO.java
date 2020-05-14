package com.icebartech.phoneparts.agent.dto;

import com.icebartech.core.constants.UserEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by liuao on 2019/9/17.
 * @desc
 */
@Data
public class LoginDTO {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户名称")
    private String name;

    @ApiModelProperty("用户类型")
    private UserEnum userEnum;

    @ApiModelProperty("代理商等级 0 总后台 1 代理商一级 2 代理商二级")
    private Integer level;

    @ApiModelProperty("当代理商2级时，返回代理商一级的id")
    private Long parentId;
}
