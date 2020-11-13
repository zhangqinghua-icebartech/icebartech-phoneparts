package com.icebartech.phoneparts.user.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Created by liuao on 2019/5/17.
 * @desc
 */
@Data
@ToString(callSuper = true)
public class LoginDto {

    @ApiModelProperty(value = "id",example = "id")
    private Long id;

    @ApiModelProperty(value = "登录id",example = "登录id")
    private String sessionId;

    @ApiModelProperty(value = "头像",example = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "过期时间",example = "过期时间")
    private LocalDateTime pastTime;

}
