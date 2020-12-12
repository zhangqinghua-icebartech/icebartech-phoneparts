package com.icebartech.phoneparts.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
@ToString
public class UserInsertParam {

    @ApiModelProperty(value = "序列id",hidden = true)
    private Long serialId;

    @ApiModelProperty(value = "序列号",example = "序列号")
    @NotBlank
    private String serialNum;

    @ApiModelProperty(value = "验证码",example = "验证码")
    private String code;

    @ApiModelProperty(value = "邮箱",example = "邮箱")
    @NotBlank
    private String email;

    @ApiModelProperty(value = "密码",example = "密码")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "代理商id",hidden = true)
    private Long agentId;

    @ApiModelProperty(value = "二级分类id",hidden = true)
    private Long serialClassId;

    @ApiModelProperty(value = "一级代理商的二级分类id", example = "一级代理商的二级分类id")
    private Long secondSerialClassId;

    @ApiModelProperty(value = "次级代理商id",example = "次级代理商id")
    private Long secondAgentId;

    public UserInsertParam(){

    }

    public UserInsertParam(String serialNum,String email,String password){

        this.serialNum = serialNum;
        this.email = email;
        this.password = password;

    }

}
