package com.icebartech.phoneparts.user.po;

import com.icebartech.core.po.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "user", comment = "用户表")
public class User extends BasePo {

    @Index(name = "serialId")
    @ApiModelProperty(value = "序列id", example = "序列id")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '序列id'")
    private Long serialId;

    @ApiModelProperty(value = "序列号", example = "序列号")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '序列号'")
    private String serialNum;

    @ApiModelProperty(value = "头像", example = "头像")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '头像'")
    private String headPortrait;

    @ApiModelProperty(value = "邮箱", example = "邮箱")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '邮箱'")
    private String email;

    @ApiModelProperty(value = "密码", example = "密码")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '密码'")
    private String password;

    @ApiModelProperty(value = "是否有效，0有效 1无效", example = "是否有效，0有效 1无效")
    @Column(columnDefinition = "int(1) NOT NULL DEFAULT '0' COMMENT '是否有效，0有效 1无效'")
    private Integer enable;

    @ApiModelProperty(value = "总切割次数", example = "总切割次数")
    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '总切割次数'")
    private Integer useCount;

    @ApiModelProperty(value = "剩余切割次数", example = "剩余切割次数")
    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '剩余切割次数'")
    private Integer mayUseCount;

    @Index(name = "agentId")
    @ApiModelProperty(value = "代理商id", example = "代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;

    @Index(name = "secondAgentId")
    @ApiModelProperty(value = "次级代理商id", example = "次级代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '次级代理商id'")
    private Long secondAgentId;

    @Index(name = "serialClassId")
    @ApiModelProperty(value = "二级分类id", example = "二级分类id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '二级分类id'")
    private Long serialClassId;

    @Index(name = "secondSerialClassId")
    @ApiModelProperty(value = "一级代理商的二级分类id", example = "一级代理商的二级分类id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '二级分类id'")
    private Long secondSerialClassId;
}
