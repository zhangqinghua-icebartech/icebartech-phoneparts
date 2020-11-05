package com.icebartech.base.manager.po;

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

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_menu", comment = "权限表")
public class SysMenu extends BasePo {

    @ApiModelProperty(value = "该权限的父级权限Id（0表示是一级权限）")
    @Column(columnDefinition = "BIGINT(20) DEFAULT 0 COMMENT '该权限的父级权限Id（0表示是一级权限）'")
    private Long parentId;

    @ApiModelProperty(value = "图标Key", example = "icon.png")
    @Column(columnDefinition = "VARCHAR(64) COMMENT '图标Key'")
    private String iconKey;

    @ApiModelProperty(value = "权限url", example = "/banner/manager")
    @Column(columnDefinition = "varchar(128) DEFAULT '' COMMENT '权限url'")
    private String menuUrl;

    @ApiModelProperty(value = "权限名称", example = "系统设置")
    @Column(columnDefinition = "VARCHAR(32) NOT NULL COMMENT '权限名称'")
    private String menuName;

    @ApiModelProperty(value = "排序（从小打大排）")
    @Column(columnDefinition = "INT(3) DEFAULT 0 COMMENT '排序（从小打大排）'")
    private Integer menuSort;
}
