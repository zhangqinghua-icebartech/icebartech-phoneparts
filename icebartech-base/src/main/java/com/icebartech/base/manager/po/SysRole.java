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
@Table(appliesTo = "sys_role", comment = "角色表")
public class SysRole extends BasePo {

    @ApiModelProperty(value = "角色名称", example = "超级管理员")
    @Column(columnDefinition = "VARCHAR(64) NOT NULl COMMENT '角色名称'")
    private String roleName;

    @ApiModelProperty(value = "角色描述", example = "这个角色是做。。。用的")
    @Column(columnDefinition = "VARCHAR(512) NOT NULl COMMENT '角色描述'")
    private String roleDesc;
}
