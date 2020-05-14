package com.icebartech.base.manager.dto;

import com.icebartech.base.manager.po.SysMenu;
import com.icebartech.core.utils.AliyunOSSUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Menu extends SysMenu {

    @ApiModelProperty(value = "图标链接", example = "icon.png")
    private String iconLink;

    @ApiModelProperty(value = "子权限列表")
    private List<Menu> subMenus;

    public Menu() {
    }

    public Menu(Long id, String menuName) {
        super.setId(id);
        super.setMenuName(menuName);
    }

    @Override
    public void setIconKey(String iconKey) {
        super.setIconKey(iconKey);
        this.iconLink = AliyunOSSUtil.generateurl(iconKey);
    }
}
