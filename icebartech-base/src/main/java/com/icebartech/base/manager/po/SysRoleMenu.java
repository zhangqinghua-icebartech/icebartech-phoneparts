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
@Table(appliesTo = "sys_role_menu", comment = "角色权限表")
public class SysRoleMenu extends BasePo {

    @ApiModelProperty(value = "角色Id")
    @Column(columnDefinition = "BIGINT(20) NOT NULL COMMENT '角色Id'")
    private Long roleId;

    @ApiModelProperty(value = "权限Id")
    @Column(columnDefinition = "BIGINT(20) NOT NULL COMMENT '权限Id'")
    private Long menuId;
}
