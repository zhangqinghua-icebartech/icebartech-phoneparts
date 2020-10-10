package com.icebartech.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxiong
 * @date 2018/10/16 19:56
 */
@ApiModel(value = "AttachmentInfo", description = "附件对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentInfo {

    @ApiModelProperty(value = "文件fileKey", example = "fileKey")
    private String fileKey;

    @ApiModelProperty(value = "文件地址", example = "文件地址")
    private String fileUrl;
}
