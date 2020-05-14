package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.system.dto.SysClassOneDto;
import com.icebartech.phoneparts.system.param.SysClassOneInsertParam;
import com.icebartech.phoneparts.system.param.SysClassOneListParam;
import com.icebartech.phoneparts.system.param.SysClassOnePageParam;
import com.icebartech.phoneparts.system.param.SysClassOneUpdateParam;
import com.icebartech.phoneparts.system.po.SysClassTwo;
import com.icebartech.phoneparts.system.service.SysClassOneService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Api(tags = "一级分类接口")
@RestController
@RequestMapping(value = "/sysClassOne", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysClassOneController extends BaseController {

    private SysClassOneService service;
    private SysClassTwoService sysClassTwoService;


    @Autowired
    public SysClassOneController(SysClassOneService service,
                                 SysClassTwoService sysClassTwoService) {
        this.service = service;
        this.sysClassTwoService = sysClassTwoService;
    }

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_page")
    public RespPage<SysClassOneDto> findPage(@Valid @RequestBody SysClassOnePageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_list")
    public RespDate<List<SysClassOneDto>> findList() {
        return getRtnDate(service.findList(new SysClassOneListParam()));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<SysClassOneDto> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysClassOneInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysClassOneUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("修改排序")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/changeSort")
    public RespDate<Boolean> changeSort(@RequestParam("id") Long id,
                                        @RequestParam("sort") Integer sort) {
        return getRtnDate(service.changeSort(id,sort));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        SysClassTwo sysClassTwo= sysClassTwoService.findByClassOneId(id);
        if(sysClassTwo!=null)
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "请先删除其菜单下的二级菜单");
        return getRtnDate(service.delete(id));
    }
}
