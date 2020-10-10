package com.icebartech.phoneparts.agent.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.agent.dto.AddUseRecordDTO;
import com.icebartech.phoneparts.agent.param.AddUseRecordPageParam;
import com.icebartech.phoneparts.agent.service.AddUseRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Created by liuao on 2019/9/5.
 * @desc
 */

@Api(tags = "代理商调整次数模块接口1")
@RestController
@RequestMapping(value = "/addUseRecord", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AddUseRecordController extends BaseController {


    @Autowired
    AddUseRecordService service;


    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_page")
    public RespPage<AddUseRecordDTO> findPage(@Valid @RequestBody AddUseRecordPageParam param) {
        return getPageRtnDate(service.findPage(param));
    }


    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_list")
    public RespDate<List<AddUseRecordDTO>> findList() {
        return getRtnDate(service.findList());
    }


    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_detail")
    public RespDate<AddUseRecordDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }


}
