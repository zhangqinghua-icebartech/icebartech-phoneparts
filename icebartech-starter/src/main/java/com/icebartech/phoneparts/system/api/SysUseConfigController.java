package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.system.dto.SysUseConfigDTO;
import com.icebartech.phoneparts.system.param.*;
import com.icebartech.phoneparts.system.service.SysUseConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author Created by liuao on 2019/9/4.
 * @desc
 */
@Api(tags = "使用设置模块接口")
@RestController
@RequestMapping(value = "/sysUseConfig", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysUseConfigController extends BaseController {

    @Autowired
    SysUseConfigService service;


    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_page")
    public RespPage<SysUseConfigDTO> findPage(@Valid @RequestBody SysUseConfigPageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_list")
    public RespDate<List<SysUseConfigDTO>> findList() {
        return getRtnDate(service.findList(eq(SysUseConfigDTO::getUserId, UserThreadLocal.getUserId())));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<SysUseConfigDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysUseConfigInsertParam param) {
        param.setUserId(UserThreadLocal.getUserId());
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysUseConfigUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("修改使用状态")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/change_state")
    public RespDate<Boolean> changeState(@RequestParam("id") Long id,
                                    @RequestParam("state") ChooseType state) {
        return getRtnDate(service.changeState(id,state));
    }

    @ApiOperation("获取使用中配置")
    @RequireLogin({UserEnum.app})
    @PostMapping("/find_in_use")
    public RespDate<SysUseConfigDTO> findInUse() {
        return getRtnDate(service.findInUse());
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(service.delete(id));
    }

}
