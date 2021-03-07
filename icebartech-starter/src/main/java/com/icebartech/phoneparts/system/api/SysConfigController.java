package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.system.dto.SysConfigDTO;
import com.icebartech.phoneparts.system.param.SysConfigInsertParam;
import com.icebartech.phoneparts.system.param.SysConfigPageParam;
import com.icebartech.phoneparts.system.param.SysConfigUpdateParam;
import com.icebartech.phoneparts.system.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.icebartech.core.vo.QueryParam.eq;
import static com.icebartech.core.vo.QueryParam.ge;

@Api(tags = "系统升级配置接口")
@RestController
@RequestMapping(value = "/sysConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysConfigController extends BaseController {

    @Autowired
    SysConfigService service;


    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/find_page")
    public RespPage<SysConfigDTO> findPage(@Valid @RequestBody SysConfigPageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/find_list")
    public RespDate<List<SysConfigDTO>> findList() {
        return getRtnDate(service.findList());
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<SysConfigDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("获取配置")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/find_config")
    public RespDate<SysConfigDTO> findConfig(@RequestParam Integer type) {
        return getRtnDate(service.findDetail(eq(SysConfigDTO::getType, type)));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysConfigInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysConfigUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/deletes")
    public RespDate<Boolean> update(@RequestParam List<Long> ids) {
        for (Long id : ids) {
            service.delete(id);
        }
        return getRtnDate(true);
    }
}
