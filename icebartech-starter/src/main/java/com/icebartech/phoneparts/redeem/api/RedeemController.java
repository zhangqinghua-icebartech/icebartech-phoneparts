package com.icebartech.phoneparts.redeem.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.redeem.dto.RedeemDTO;
import com.icebartech.phoneparts.redeem.param.RedeemInsertParam;
import com.icebartech.phoneparts.redeem.param.RedeemPageParam;
import com.icebartech.phoneparts.redeem.service.RedeemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
@Api(tags = "兑换码管理")
@RestController
@RequestMapping(value = "/redeem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RedeemController extends BaseController {

    @Autowired
    RedeemService service;


    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/insert")
    public RespDate<Boolean> insert(@Valid @RequestBody RedeemInsertParam param) {
        return getRtnDate(service.insertAll(param));
    }

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/find_page")
    public RespPage<RedeemDTO> findPage(@Valid @RequestBody RedeemPageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(service.deleteAll(id));
    }

}
