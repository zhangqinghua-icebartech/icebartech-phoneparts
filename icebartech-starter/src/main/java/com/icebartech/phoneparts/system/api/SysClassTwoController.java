package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.product.dto.ProductDto;
import com.icebartech.phoneparts.product.service.ProductService;
import com.icebartech.phoneparts.system.dto.SysClassTwoDto;
import com.icebartech.phoneparts.system.param.*;
import com.icebartech.phoneparts.system.po.SysClassThree;
import com.icebartech.phoneparts.system.service.SysClassThreeService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Api(tags = "二级分类接口")
@RestController
@RequestMapping(value = "/sysClassTwo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysClassTwoController extends BaseController {

    @Autowired
    private SysClassTwoService service;

    @Autowired
    private ProductService productService;

    @Autowired
    private SysClassThreeService sysClassThreeService;

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_page")
    public RespPage<SysClassTwoDto> findPage(@Valid @RequestBody SysClassTwoPageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_list")
    public RespDate<List<SysClassTwoDto>> findList() {
        return getRtnDate(service.findList(new SysClassTwoListParam()));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<SysClassTwoDto> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysClassTwoInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysClassTwoUpdateParam param) {
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
        ProductDto product = productService.findByClassTwoId(id);
        if(product!=null)
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "请先删除其菜单下的单品");

        SysClassThree classThree = sysClassThreeService.findOneOrNull(eq(SysClassThree::getClassTwoId,id));
        if(classThree!=null)
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "请先删除其菜单下的三级分类");
        return getRtnDate(service.delete(id));
    }

    @ApiOperation("核实密码")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/verify_pwd")
    public RespDate<Boolean> verifyPwd(@RequestParam Long id,
                                        @RequestParam String password) {
        return getRtnDate(service.verifyPwd(id,password));
    }
}
