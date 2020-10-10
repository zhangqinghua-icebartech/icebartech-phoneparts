package com.icebartech.phoneparts.system.po;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.po.BasePo;
import com.icebartech.phoneparts.enums.AppConfigTypeEnum;
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

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_app_config", comment = "app配置信息表")
public class SysAppConfig extends BasePo {

    @ApiModelProperty(value = "apk地址",example = "apk地址")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT 'apk地址'")
    private String apkAddress;

    @ApiModelProperty(value = "版本号",example = "版本号")
    @Column(columnDefinition = "varchar(64) DEFAULT NULL COMMENT '版本号'")
    private String versionNum;

    @ApiModelProperty(value = "版本名称",example = "版本名称")
    @Column(columnDefinition = "varchar(64) DEFAULT NULL COMMENT '版本名称'")
    private String versionName;

    @ApiModelProperty(value = "更新信息",example = "更新信息")
    @Column(columnDefinition = "text COMMENT '更新信息'")
    private String upgradeInfo;

    @ApiModelProperty(value = "是否强制更新")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(1) NOT NULL DEFAULT 'n' COMMENT '是否强制更新'")
    private ChooseType forcedUpdate ;

    @ApiModelProperty(value = "配置类型")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '配置类型'")
    private AppConfigTypeEnum type;


}
