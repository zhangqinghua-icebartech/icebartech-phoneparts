package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
public class SysClassTwoUpdateParam {

    @NotNull
    private Long id;

    @ApiModelProperty(value = "所属一级分类",example = "所属一级分类")
    private Long classOneId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    private String chinaName;

    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称")
    private String englishName;

    @ApiModelProperty(value = "图标",example = "图标")
    private String icon;

    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty(value = "是否使用密码 1使用 2不使用",example = "是否使用密码 1使用 2不使用")
    private Integer isLock;
}
