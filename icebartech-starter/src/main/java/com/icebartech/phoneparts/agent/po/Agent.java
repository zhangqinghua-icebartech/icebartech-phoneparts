package com.icebartech.phoneparts.agent.po;

import com.icebartech.core.enums.ChooseType;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Created by liuao on 2019/8/27.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "agent", comment = "代理商")
public class Agent extends BasePo {

    @ApiModelProperty("公司名称")
    @Column(columnDefinition = "VARCHAR(30) NOT NULL COMMENT '公司名称'")
    private String companyName;

    @ApiModelProperty("公司联系人")
    @Column(columnDefinition = "VARCHAR(30) NOT NULL COMMENT '公司联系人'")
    private String userName;

    @ApiModelProperty("手机号")
    @Column(columnDefinition = "VARCHAR(20) NOT NULL COMMENT '手机号'")
    private String phone;

    @ApiModelProperty("新增切割次数")
    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '新增切割次数'")
    private Integer useNum;

    @ApiModelProperty("账号")
    @Column(columnDefinition = "VARCHAR(20) NOT NULL COMMENT '账号'")
    private String loginName;

    @ApiModelProperty("密码")
    @Column(columnDefinition = "VARCHAR(64) NOT NULL COMMENT '密码'")
    private String password;

    @ApiModelProperty(value = "是否下架",example = "是否下架")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(1) NOT NULL DEFAULT 'n' COMMENT '是否下架'")
    private ChooseType enable;

    @ApiModelProperty(value = "排序",example = "排序")
    @Column(columnDefinition = "int(64) NOT NULL DEFAULT '0' COMMENT '排序'")
    private Integer sort;

    @ApiModelProperty("分类名称")
    @Column(columnDefinition = "VARCHAR(20) NOT NULL COMMENT '分类名称'")
    private String className;


    @ApiModelProperty(value = "代理商上级id",example = "代理商上级id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商上级id'")
    private Long parentId;

    @ApiModelProperty(value = "总切割次数",example = "总切割次数")
    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '总切割次数'")
    private Integer useCount;

    @ApiModelProperty(value = "剩余切割次数",example = "剩余切割次数")
    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '剩余切割次数'")
    private Integer mayUseCount;


}
