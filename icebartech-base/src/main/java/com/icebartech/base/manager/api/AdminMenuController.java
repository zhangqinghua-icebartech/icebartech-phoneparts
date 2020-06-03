package com.icebartech.base.manager.api;

import com.icebartech.base.manager.dto.Menu;
import com.icebartech.base.manager.param.MenuAdminInsertParam;
import com.icebartech.base.manager.param.MenuAdminUpdateParam;
import com.icebartech.base.manager.service.MenuService;
import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.vo.RespDate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags = "后台 权限接口")
@RestController
@RequestMapping(value = "/admin/menu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminMenuController extends BaseController {

    @Autowired
    private MenuService service;

    @ApiOperation("获取一级权限详情列表")
    @PostMapping("/find_top_detail_list")
    public RespDate<List<Menu>> findTopDetailList() {
        return getRtnDate(service.findTopDetailList(0L));
    }

    @ApiOperation("获取用户可见的权限列表")
    @GetMapping(value = "/find_manager_menus")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    public RespDate<List<Menu>> findManagerMenus() {
        return getRtnDate(service.findManagerMenus(UserThreadLocal.getUserId()));
    }

    @ApiOperation("新增")
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody MenuAdminInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody MenuAdminUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(service.delete(id));
    }

    @ApiOperation("上移")
    @PostMapping("/shift_up")
    public RespDate<Boolean> shiftUp(@RequestParam Long id) {
        return getRtnDate(service.shiftUp(id));
    }

    @ApiOperation("下移")
    @PostMapping("/shift_down")
    public RespDate<Boolean> shiftDown(@RequestParam Long id) {
        return getRtnDate(service.shiftDown(id));
    }
}
