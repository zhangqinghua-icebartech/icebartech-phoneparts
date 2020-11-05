package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.system.dto.SysDeviceDTO;
import com.icebartech.phoneparts.system.dto.SysSerialDto;
import com.icebartech.phoneparts.system.param.SysDeviceInsertParam;
import com.icebartech.phoneparts.system.param.SysDevicePageParam;
import com.icebartech.phoneparts.system.param.SysDeviceUpdateParam;
import com.icebartech.phoneparts.system.service.SysDeviceService;
import com.icebartech.phoneparts.user.po.User;
import com.icebartech.phoneparts.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.icebartech.core.vo.QueryParam.in;


/**
 * @author Created by liuao on 2020/9/27.
 * @desc
 */
@Api(tags = "设备管理模块")
@RestController
@RequestMapping(value = "/sysDevice", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysDeviceController extends BaseController {

    @Autowired
    SysDeviceService service;
    @Autowired
    UserService userService;

    @ApiOperation("获取分页")
    @PostMapping("/find_page")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    public RespPage<SysDeviceDTO> findPage(@Valid @RequestBody SysDevicePageParam param) {

        if(param.getUserId()!=null){
            User user = userService.findOne(param.getUserId());
            param.setAgentId(user.getAgentId());
        }
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<SysDeviceDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }


    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysDeviceInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysDeviceUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("批量删除")
    @PostMapping("/deletes")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    public RespDate<Boolean> deletes(@RequestParam List<Long> ids) {
        return getRtnDate(service.delete(in(SysSerialDto::getId, ids)));
    }

}
