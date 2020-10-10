package com.icebartech.base.manager.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ManagerAdminFindPageParam extends PageParam {

    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    @ApiModelProperty(value = "用户名", example = "小明")
    private String userNameLike;

    @ApiModelProperty(value = "账号名", example = "skw12345")
    private String loginNameLike;
}
