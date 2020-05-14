package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.enums.AppConfigTypeEnum;
import com.icebartech.phoneparts.system.dto.SysAppConfigDTO;
import com.icebartech.phoneparts.system.param.*;
import com.icebartech.phoneparts.system.service.SysAppConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Api(tags = "App配置信息的模块")
@RestController
@RequestMapping(value = "/sysAppConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysAppConfigController extends BaseController {

    @Autowired
    SysAppConfigService service;

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/find_page")
    public RespPage<SysAppConfigDTO> findPage(@Valid @RequestBody SysAppConfigPageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysAppConfigInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/find_detail")
    public RespDate<SysAppConfigDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("获取隐私协议")
    @GetMapping("/find_protocol")
    public RespDate<String> findProtocol() {
        return getRtnDate(service.findProtocol());
    }

    @ApiOperation("获取升级配置")
    @PostMapping("/find_config")
    public RespDate<SysAppConfigDTO> findConfig(@RequestParam AppConfigTypeEnum type) {
        return getRtnDate(service.findOneOrNull(eq(SysAppConfigDTO::getType,type)));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysAppConfigUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(service.delete(id));
    }
}
