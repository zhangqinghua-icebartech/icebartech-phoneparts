package com.icebartech.base.manager.po;

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
@Table(appliesTo = "sys_manager", comment = "管理员表")
public class SysManager extends BasePo {

    @ApiModelProperty(value = "角色Id")
    @Column(columnDefinition = "BIGINT(20) NOT NULL COMMENT '角色Id'")
    private Long roleId;

    @ApiModelProperty(value = "用户名")
    @Column(columnDefinition = "varchar(32) NOT NULL COMMENT '用户姓名'")
    private String userName;

    @ApiModelProperty(value = "账号名")
    @Index(name = "loginName")
    @Column(columnDefinition = "varchar(32) NOT NULL COMMENT '用户账号'")
    private String loginName;

    @ApiModelProperty(value = "MD5密码", example = "sdkgjskd353025734782")
    @Column(columnDefinition = "VARCHAR(64) NOT NULL COMMENT 'MD5密码'")
    private String password;

    @ApiModelProperty(value = "头像Key", example = "avator.png")
    @Column(columnDefinition = "VARCHAR(64) COMMENT '头像Key'")
    private String avatorKey;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;

}
