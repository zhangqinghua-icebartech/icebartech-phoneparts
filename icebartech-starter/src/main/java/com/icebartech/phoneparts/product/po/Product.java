package com.icebartech.phoneparts.product.po;

import com.icebartech.core.po.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
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
@Table(appliesTo = "product", comment = "单品表")
public class Product extends BasePo {

    @Index(name = "classOneId")
    @ApiModelProperty(value = "一级分类id",example = "一级分类id")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '一级分类id'")
    private Long classOneId;

    @Index(name = "classTwoId")
    @ApiModelProperty(value = "二级分类id",example = "二级分类id")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '二级分类id'")
    private Long classTwoId;

    @ApiModelProperty(value = "三级分类id",example = "三级分类id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT 0 COMMENT '三级分类id'")
    private Long classThreeId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '分类中文名称'")
    private String chinaName;

    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '分类英文名称'")
    @ApiModelProperty(value = "分类英文名称",example = "分类英文名称")
    private String englishName;

    @ApiModelProperty(value = "展示图",example = "展示图")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '展示图'")
    private String coverIcon;

    @ApiModelProperty(value = "详情图",example = "详情图")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '详情图'")
    private String detailIcon;

    @ApiModelProperty(value = "文件",example = "文件")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '文件'")
    private String file;

    @ApiModelProperty(value = "文件名称",example = "文件名称")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '文件名称'")
    private String fileName;

    @ApiModelProperty(value = "排序",example = "2")
    @Column(columnDefinition = "int(64) NOT NULL DEFAULT '0' COMMENT '排序 '")
    private Integer sort;

}
