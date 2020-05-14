package com.icebartech.base.manager.api;

import com.icebartech.base.manager.dto.Role;
import com.icebartech.base.manager.param.RoleAdminFindPageParam;
import com.icebartech.base.manager.param.RoleAdminInsertParam;
import com.icebartech.base.manager.param.RoleAdminUpdateParam;
import com.icebartech.base.manager.service.RoleService;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "后台 角色接口")
@RestController
@RequestMapping(value = "/admin/role", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminRoleController extends BaseController {

    @Autowired
    private RoleService service;

    @ApiOperation("获取分页")
    @PostMapping("/find_page")
    public RespPage<Role> findPage(@Valid @RequestBody RoleAdminFindPageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取列表")
    @PostMapping("/find_list")
    public RespDate<List<Role>> findList() {
        return getRtnDate(service.findList());
    }

    @ApiOperation("获取详情")
    @PostMapping("/find_detail")
    public RespDate<Role> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("新增")
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody RoleAdminInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody RoleAdminUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(service.delete(id));
    }
}
