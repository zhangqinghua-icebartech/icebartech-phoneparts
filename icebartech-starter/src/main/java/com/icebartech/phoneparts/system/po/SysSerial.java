package com.icebartech.phoneparts.system.po;

import com.github.annotation.ExcelField;
import com.icebartech.core.po.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * @author Created by liuao on 2019/6/17.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_serial", comment = "序列号表")
public class SysSerial extends BasePo {

    @ExcelField(title = "序列号", order = 1)
    @ApiModelProperty(value = "序列号",example = "序列号")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '序列号'")
    private String serialNum;

    @ApiModelProperty(value = "使用次数",example = "使用次数")
    @Column(columnDefinition = "int(10) NOT NULL DEFAULT '0' COMMENT '使用次数'")
    private Integer useNum;

    @ApiModelProperty(value = "绑定邮箱",example = "绑定邮箱")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '绑定邮箱'")
    private String bindMail;

    @ApiModelProperty(value = "启用时间",example = "启用时间")
    @Column(columnDefinition = "timestamp NULL DEFAULT NULL COMMENT '启用时间'")
    private Date startTime;

    @ApiModelProperty(value = "过期时间",example = "过期时间")
    @Column(columnDefinition = "timestamp NULL DEFAULT NULL COMMENT '启用时间'")
    private Date endTime;

    @ApiModelProperty(value = "是否绑定邮箱 0没有1有'",example = "是否绑定邮箱 0没有1有'")
    @Column(columnDefinition = "int(2) NOT NULL DEFAULT '0' COMMENT '是否绑定邮箱 0没有1有'")
    private Integer isBindMail;

    @ApiModelProperty(value = "随机字符串",example = "随机字符串")
    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '随机字符串'")
    private String randomStr;

    @ApiModelProperty(value = "一级分类id代理商id",example = "一级分类id代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;

    @ApiModelProperty(value = "二级分类id次级代理商id",example = "二级分类id次级代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '次级代理商id'")
    private Long secondAgentId;

    @ApiModelProperty(value = "总后台的二级分类id",example = "总后台的二级分类id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '二级分类id'")
    private Long serialClassId;

    @ApiModelProperty(value = "一级代理商的二级分类id",example = "一级代理商的二级分类id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '二级分类id'")
    private Long secondSerialClassId;

    @ApiModelProperty(value = "状态 0未使用 1使用中 2已过期",example = "状态 0未使用 1使用中 2已过期")
    @Column(columnDefinition = "int(2) NOT NULL DEFAULT '0' COMMENT '状态 0未使用 1使用中 2已过期'")
    private Integer status;


}
