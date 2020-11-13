package com.icebartech.phoneparts.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
public class UserUpdateParam {

    private Long id;

    @ApiModelProperty(value = "序列id",example = "序列id")
    private Long serialId;

    @ApiModelProperty(value = "序列号",example = "序列号")
    private String serialNum;

    @ApiModelProperty(value = "头像",example = "头像")
    private String headPortrait;

    @ApiModelProperty(value = "邮箱",example = "邮箱")
    private String email;

    @ApiModelProperty(value = "是否有效，0有效 1无效",example = "是否有效，0有效 1无效")
    private String enable;

}
