package com.icebartech.base.common.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.components.RedisComponent;
import com.icebartech.core.components.TencentCOSComponent;
import com.icebartech.core.constants.IcebartechConstants;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.vo.RespDate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Api(tags = "系统模块接口")
@RestController
@RequireLogin(userEnum = {UserEnum.agent,UserEnum.admin, UserEnum.app, UserEnum.weixin, UserEnum.h5_master, UserEnum.h5_slaver})
@RequestMapping(value = {"/base/admin", "/base/app", "/base/mini", "/base/h5"})
public class BaseSysController extends BaseController {

    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;
    @Autowired
    private TencentCOSComponent tencentCOSComponent;

    @ApiOperation("获取阿里云OSS文件上传链接")
    @GetMapping(value = "/getOSSUploadUrl/{suffix}")
    public RespDate<Map<String, String>> getOSSUploadUrl(@ApiParam("文件后缀") @PathVariable(value = "suffix") String suffix, @ApiParam("文件类型") @RequestParam(value = "contentType") String contentType) {
        return getRtnDate(aliyunOSSComponent.generateUploadUrl(suffix, contentType));
    }

    @ApiOperation("获取阿里云OSS文件上传表单，小程序用")
    @GetMapping(value = "/getOSSUploadFormData/{suffix}")
    public RespDate<Map<String, Object>> getOSSUploadFormData(@ApiParam("文件后缀") @PathVariable(value = "suffix") String suffix) {
        return getRtnDate(aliyunOSSComponent.generateUploadFormData(suffix));
    }

    @ApiOperation("生成阿里云OSS文件下载链接,30分钟有效")
    @GetMapping(value = "/getOSSDownloadUrl")
    public RespDate<String> getOSSDownloadUrl(@RequestParam(value = "fileKey") String fileKey) {
        return getRtnDate(aliyunOSSComponent.generateDownloadUrl(fileKey));
    }

    @ApiOperation("获取腾讯云COS文件上传链接")
    @GetMapping(value = "/getCOSUploadUrl/{suffix}")
    public RespDate<Map<String, String>> getCOSUploadUrl(@ApiParam("文件后缀") @PathVariable(value = "suffix") String suffix) {
        return getRtnDate(tencentCOSComponent.generateUploadUrl(suffix));
    }

    @ApiOperation("生成腾讯云COS文件下载链接,30分钟有效")
    @GetMapping(value = "/getCOSDownloadUrl")
    public RespDate<String> getCOSDownloadUrl(@RequestParam(value = "fileKey") String fileKey) {
        return getRtnDate(tencentCOSComponent.generateDownloadUrl(fileKey));
    }

    @ApiIgnore
    @RequireLogin(userEnum = UserEnum.no_login)
    @PostMapping(value = "/setDisable/{disable}")
    public RespDate<String> setDisable(@ApiParam("是否禁用") @PathVariable(value = "disable") String disable) {
        redisComponent.set(IcebartechConstants.SYSTEM_DISABLE, IcebartechConstants.SYSTEM_DISABLE, disable);
        return getRtnDate(disable);
    }
}
