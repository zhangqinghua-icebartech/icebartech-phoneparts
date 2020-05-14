package com.icebartech.phoneparts.system.param;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.phoneparts.enums.AppConfigTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Data
public class SysAppConfigUpdateParam {

    @ApiModelProperty(value = "id",example = "id")
    private Long id;

    @ApiModelProperty(value = "apk地址",example = "apk地址")
    private String apkAddress;

    @ApiModelProperty(value = "版本号",example = "版本号")
    private String versionNum;

    @ApiModelProperty(value = "版本名称",example = "版本名称")
    private String versionName;

    @ApiModelProperty(value = "更新信息",example = "更新信息")
    private String upgradeInfo;

    @ApiModelProperty(value = "是否强制更新")
    private ChooseType forcedUpdate ;

}
