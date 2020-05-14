package com.icebartech.base.manager.dto;

import com.icebartech.base.manager.po.SysManager;
import com.icebartech.core.utils.AliyunOSSUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Manager extends SysManager {

    @ApiModelProperty(value = "头像Link", example = "avator.png")
    private String avatorLink;

    @ApiModelProperty(value = "管理员可见的权限列表")
    private List<Menu> menus;

    @ApiModelProperty(value = "角色名称", example = "超级管理员")
    private String roleName;

    @Override
    public void setAvatorKey(String avatorKey) {
        super.setAvatorKey(avatorKey);
        this.avatorLink = AliyunOSSUtil.generateurl(avatorKey);
    }
}
