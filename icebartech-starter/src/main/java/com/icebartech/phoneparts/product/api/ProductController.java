package com.icebartech.phoneparts.product.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.product.dto.ProductDto;
import com.icebartech.phoneparts.product.param.ProductInsertParam;
import com.icebartech.phoneparts.product.param.ProductPageParam;
import com.icebartech.phoneparts.product.param.ProductUpdateParam;
import com.icebartech.phoneparts.product.service.ProductService;
import com.icebartech.phoneparts.system.po.SysClassOne;
import com.icebartech.phoneparts.system.po.SysClassTwo;
import com.icebartech.phoneparts.system.service.SysClassOneService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Api(tags = "单品模块接口")
@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SysClassOneService sysClassOneService;
    @Autowired
    private SysClassTwoService sysClassTwoService;

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/find_page")
    public RespPage<ProductDto> findPage(@Valid @RequestBody ProductPageParam param) {
        Page<ProductDto> page = productService.findPage(param);
        // todo 迎合前端临时这样子搞，202012月份后面要切换回来。
        page.getContent().forEach(d -> d.setCoverIcon(d.getDetailIcon()));
        return getPageRtnDate(page);
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/find_list")
    public RespDate<List<ProductDto>> findList() {
        return getRtnDate(productService.findList(new ProductPageParam()));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<ProductDto> findDetail(@RequestParam Long id) {
        return getRtnDate(productService.findDetail(id));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody ProductInsertParam param) {
        SysClassOne sysClassOne = sysClassOneService.findOneOrNull(param.getClassOneId());
        if (sysClassOne == null)
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_AVAILABLE, "一级分类不存在");

        SysClassTwo sysClassTwo = sysClassTwoService.findOneOrNull(param.getClassTwoId());
        if (sysClassTwo == null)
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_AVAILABLE, "二级分类不存在");

        if (!sysClassTwo.getClassOneId().equals(sysClassOne.getId()))
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_AVAILABLE, "二级分类不在一级分类下");

        return getRtnDate(productService.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody ProductUpdateParam param) {
        return getRtnDate(productService.update(param));
    }

    @ApiOperation("修改排序")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/changeSort")
    public RespDate<Boolean> changeSort(@RequestParam("id") Long id,
                                        @RequestParam("sort") Integer sort) {
        return getRtnDate(productService.changeSort(id, sort));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(productService.delete(id));
    }
}
