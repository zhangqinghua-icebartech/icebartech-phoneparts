package com.icebartech.phoneparts.agent.param;

import com.icebartech.core.enums.ChooseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Created by liuao on 2019/9/5.
 * @desc
 */
@Data
public class AgentInsertParam {

    @NotBlank
    @ApiModelProperty("公司名称")
    private String companyName;

    @NotBlank
    @ApiModelProperty("公司联系人")
    private String userName;

    @NotBlank
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty(value = "新增切割次数",hidden = true)
    private Integer useNum = 0;

    @NotBlank
    @ApiModelProperty("账号")
    private String loginName;

    @NotBlank
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty(value = "是否下架",hidden = true)
    private ChooseType enable = ChooseType.n;

    @NotBlank
    @ApiModelProperty("分类名称")
    private String className;

    @ApiModelProperty(value = "排序",example = "排序")
    private Integer sort = 0;


    @ApiModelProperty(value = "代理商上级id",example = "代理商上级id")
    private Long parentId;


}
