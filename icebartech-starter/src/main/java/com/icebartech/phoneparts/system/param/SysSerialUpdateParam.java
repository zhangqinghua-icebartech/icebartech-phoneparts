package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
public class SysSerialUpdateParam {

    @NotNull
    private Long id;

    @ApiModelProperty(value = "序列号",example = "序列号")
    private String serialNum;

    @ApiModelProperty(value = "使用次数",example = "使用次数")
    private Integer useNum;

    @ApiModelProperty(value = "绑定邮箱",example = "绑定邮箱")
    private String bindMail;

    @ApiModelProperty(value = "启用时间",example = "启用时间")
    private Date startTime;

    @ApiModelProperty(value = "是否绑定邮箱 0没有1有'",example = "是否绑定邮箱 0没有1有'")
    private Integer isBindMail;

    @ApiModelProperty(value = "状态 0未使用 1使用中 2已过期",example = "状态 0未使用 1使用中 2已过期")
    private Integer status;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "二级分类id",example = "二级分类id")
    private Long serialClassId;

}
