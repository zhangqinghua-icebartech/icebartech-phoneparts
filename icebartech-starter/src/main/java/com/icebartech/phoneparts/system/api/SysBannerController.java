package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.system.dto.SysBannerDTO;
import com.icebartech.phoneparts.system.param.SysBannerInsertParam;
import com.icebartech.phoneparts.system.param.SysBannerPageParam;
import com.icebartech.phoneparts.system.param.SysBannerUpdateParam;
import com.icebartech.phoneparts.system.service.SysBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Created by liuao on 2020/9/27.
 * @desc
 */
@Api(tags = "轮播图")
@RestController
@RequestMapping(value = "/sysBanner", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysBannerController extends BaseController {

    @Autowired
    SysBannerService service;

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_page")
    public RespPage<SysBannerDTO> findPage(@Valid @RequestBody SysBannerPageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<SysBannerDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }


    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysBannerInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysBannerUpdateParam param) {
        return getRtnDate(service.update(param));
    }


}
