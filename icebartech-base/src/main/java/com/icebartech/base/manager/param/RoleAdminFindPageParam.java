package com.icebartech.base.manager.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoleAdminFindPageParam extends PageParam {

    @ApiModelProperty(value = "角色名称", example = "超级管理员")
    private String roleNameLike;
}

