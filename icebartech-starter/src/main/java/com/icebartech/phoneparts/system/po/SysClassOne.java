package com.icebartech.phoneparts.system.po;

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

/**
 * @author Created by liuao on 2019/6/17.
 * @desc
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_class_one", comment = "一级菜单表")
public class SysClassOne extends BasePo {

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '分类中文名称'")
    private String chinaName;

    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '分类英文名称'")
    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称")
    private String englishName;

    @ApiModelProperty(value = "图标",example = "图标")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '图标'")
    private String icon;

    @ApiModelProperty(value = "英文图标",example = "英文图标")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '英文图标'")
    private String englishIcon;

    @ApiModelProperty(value = "一级分类id代理商id",example = "一级分类id代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;

    @ApiModelProperty(value = "排序",example = "排序")
    @Column(columnDefinition = "int(64) NOT NULL DEFAULT '0' COMMENT '排序'")
    private Integer sort;

    @ApiModelProperty(value = "启用禁用 0启用1禁用",example = "启用禁用 0启用1禁用")
    @Column(columnDefinition = "int(1) NOT NULL DEFAULT '0' COMMENT '启用禁用 0启用1禁用'")
    private Integer enable;


}
