package com.icebartech.phoneparts.company.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.company.dto.CompanyInfoDto;
import com.icebartech.phoneparts.company.param.CompanyInsertParam;
import com.icebartech.phoneparts.company.param.CompanyPageParam;
import com.icebartech.phoneparts.company.param.CompanyUpdateParam;
import com.icebartech.phoneparts.company.service.CompanyInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Api(tags = "关于我们模块的接口")
@RestController
@RequestMapping(value = "/company", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CompanyInfoController extends BaseController {

    private CompanyInfoService service;

    @Autowired
    public CompanyInfoController(CompanyInfoService service) {
        this.service = service;
    }

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_page")
    public RespPage<CompanyInfoDto> findPage(@Valid @RequestBody CompanyPageParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if(localUser.getLevel() == 0){
            param.setSecondAgentId(0L);
            //一级代理商
        }else if(localUser.getLevel() == 1){
            param.setAgentId(localUser.getUserId());
            param.setSecondAgentIdNotEq(0L);
        //二级代理商
        }else if(localUser.getLevel() == 2){
            param.setSecondAgentId(localUser.getUserId());
        }

        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取使用中配置")
    @RequireLogin({UserEnum.app})
    @PostMapping("/find_in_company")
    public RespDate<CompanyInfoDto> findInCompany() {
        return getRtnDate(service.findInCompany());
    }

//    @ApiOperation("获取列表")
//    @PostMapping("/find_list")git
//    public RespDate<List<CompanyInfoDto>> findList() {
//        return getRtnDate(service.findList());
//    }


    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_detail")
    public RespDate<CompanyInfoDto> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("上架下架")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/changeShow")
    public RespDate<Boolean> changeEnable(@RequestParam("id") Long id,
                                          @RequestParam("enable") ChooseType enable) {
        return getRtnDate(service.changeEnable(id,enable));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody CompanyInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody CompanyUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(service.delete(id));
    }
}
