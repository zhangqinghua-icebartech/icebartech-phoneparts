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
 * @author Created by liuao on 2020/6/8 0008$.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_class_three", comment = "三级分类表")
public class SysClassThree extends BasePo {

    @ApiModelProperty(value = "所属一级分类",example = "所属一级分类")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '所属一级分类'")
    private Long classOneId;

    @ApiModelProperty(value = "二级分类id",example = "二级分类id")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '二级分类id'")
    private Long classTwoId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '分类中文名称'")
    private String chinaName;

    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '分类英文名称'")
    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称")
    private String englishName;

    @ApiModelProperty(value = "图标",example = "图标")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '图标'")
    private String icon;

    @ApiModelProperty(value = "排序",example = "排序")
    @Column(columnDefinition = "int(64) NOT NULL DEFAULT '0' COMMENT '排序'")
    private Integer sort;

    @ApiModelProperty(value = "是否启用 0启动1禁用",example = "是否启用 0启动1禁用")
    @Column(columnDefinition = "int(1) NOT NULL DEFAULT '0' COMMENT '是否启用 0启动1禁用'")
    private Integer enable;

}
