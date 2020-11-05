package com.icebartech.phoneparts.product.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.product.dto.UseRecordDTO;
import com.icebartech.phoneparts.product.param.UseRecordPageParam;
import com.icebartech.phoneparts.product.param.UseRecordProductPageParam;
import com.icebartech.phoneparts.product.param.UseRecordUserPageParam;
import com.icebartech.phoneparts.product.service.UseRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Api(tags = "使用记录模块接口")
@RestController
@RequestMapping(value = "/useRecord", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UseRecordController extends BaseController {

    @Autowired
    UseRecordService service;


    @ApiOperation("获取用户切割统计")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_user_record")
    public RespPage<Map> findUserRecord(@Valid @RequestBody UseRecordUserPageParam param) {
        return getPageRtnDate(service.findUserRecord(param));
    }

    @ApiOperation("获取用户切割总数")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_user_record_count")
    public RespDate<Map<String,Object>> findUserRecordCount(@Valid @RequestBody UseRecordUserPageParam param) {
        return getRtnDate(service.findUserRecordCount(param));
    }

    @ApiOperation("获取产品切割统计")
    @RequireLogin({UserEnum.admin, UserEnum.app,UserEnum.agent})
    @PostMapping("/find_product_record")
    public RespPage<Map> findProductRecord(@Valid @RequestBody UseRecordProductPageParam param) {
        if(UserThreadLocal.getUserType() == UserEnum.app) param.setUserId(UserThreadLocal.getUserId());
        return getPageRtnDate(service.findProductRecord(param));
    }

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin, UserEnum.app,UserEnum.agent})
    @PostMapping("/find_page")
    public RespPage<UseRecordDTO> findPage(@Valid @RequestBody UseRecordPageParam param) {
        if(UserThreadLocal.getUserType() == UserEnum.app) param.setUserId(UserThreadLocal.getUserId());
        return getPageRtnDate(service.findPage(param));
    }

}
