package com.icebartech.phoneparts.system.po;

import com.icebartech.core.enums.ChooseType;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_device", comment = "设备")
public class SysDevice extends BasePo {

    @ApiModelProperty(value = "设备类型 0-手机版 1-竖屏版 2-横屏版", example = "0")
    @Column(columnDefinition = "int(1) NOT NULL DEFAULT '0' COMMENT '设备类型 0-手机版 1-竖屏版 2-横屏版'")
    private Integer type;

    @ApiModelProperty(value = "设备名称", example = "设备名称")
    @Column(columnDefinition = "varchar(20) NOT NULL COMMENT '名称'")
    private String name;

    @ApiModelProperty(value = "代理商id", example = "代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;

    @ApiModelProperty(value = "详情图", example = "详情图")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '详情图'")
    private String detailIcon;

    @ApiModelProperty(value = "文件", example = "文件")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '文件'")
    private String file;

    @ApiModelProperty(value = "文件名称", example = "文件名称")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '文件名称'")
    private String fileName;

    @ApiModelProperty(value = "使用状态", example = "使用状态")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(1) NOT NULL DEFAULT 'n' COMMENT '使用状态'")
    private ChooseType state;
}
