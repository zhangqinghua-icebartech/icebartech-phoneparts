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

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_config", comment = "系统更新配置")
public class SysConfig extends BasePo {

    @ApiModelProperty(value = "代理商id", example = "代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;

    @ApiModelProperty(value = "类型 0 cut 1 devia 2 usams 3 byoyond cell 4 green mnky 5. 竖屏 6.横屏", example = "0")
    @Column(columnDefinition = "int(1) NOT NULL DEFAULT '0' COMMENT '类型 0 cut 1 devia 2 usams 3 byoyond cell 4 green mnky 5. 竖屏 6.横屏'")
    private Integer type;

    @ApiModelProperty(value = "版本号", example = "版本号")
    @Column(columnDefinition = "varchar(128) NOT NULL COMMENT '版本号'")
    private String version;

    @ApiModelProperty(value = "安装包链接", example = "安装包链接")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '安装包链接'")
    private String fileKey;

    @ApiModelProperty(value = "安装包名称", example = "安装包名称")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '安装包名称'")
    private String fileName;
}
