package com.icebartech.base.manager.dto;

import com.icebartech.base.manager.po.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Role extends SysRole {

    @ApiModelProperty(value = "拥有的一级权限集合名称，用英文逗号分割")
    private String topMenuName;

    @ApiModelProperty(value = "权限Id列表")
    private List<Long> menuIds;
}
