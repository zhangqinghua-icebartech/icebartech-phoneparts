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
 * @author Created by liuao on 2020/4/2.
 * @desc
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_config", comment = "系统更新配置")
public class SysConfig extends BasePo {


    @ApiModelProperty(value = "类型 0 cut 1 devia 2 usams 3 byoyond cell 4 green mnky 5. 竖屏 6.横屏",example = "0")
    @Column(columnDefinition = "int(1) NOT NULL DEFAULT '0' COMMENT '类型 0普通 1横屏 2竖屏'")
    private Integer type;

    @ApiModelProperty(value = "安装包",example = "安装包")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '安装包'")
    private String file;

    @ApiModelProperty(value = "版本号",example = "版本号")
    @Column(columnDefinition = "varchar(128) NOT NULL COMMENT '版本号'")
    private String version;

    @ApiModelProperty(value = "版本编号",example = "0")
    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '版本编号'")
    private Integer versionCode;

    @ApiModelProperty(value = "国内下载链接",example = "国内下载链接")
    @Column(columnDefinition = "varchar(512) NOT NULL COMMENT '国内下载链接'")
    private String innerDownloadUrl;

    @ApiModelProperty(value = "国外下载链接",example = "国外下载链接")
    @Column(columnDefinition = "varchar(512) NOT NULL COMMENT '国外下载链接'")
    private String outerDownloadUrl;


}
