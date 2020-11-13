package com.icebartech.phoneparts.redeem.api;

import com.github.ExcelUtils;
import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.redeem.dto.RedeemCodeDTO;
import com.icebartech.phoneparts.redeem.param.RedeemCodeOutParam;
import com.icebartech.phoneparts.redeem.param.RedeemCodePageParam;
import com.icebartech.phoneparts.redeem.service.RedeemCodeService;
import com.icebartech.phoneparts.user.dto.UserDto;
import com.icebartech.phoneparts.user.param.UserOutParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
@Api(tags = "兑换码详情")
@RestController
@RequestMapping(value = "/redeemCode", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RedeemCodeController extends BaseController {

    @Autowired
    RedeemCodeService service;

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/find_page")
    public RespPage<RedeemCodeDTO> findPage(@Valid @RequestBody RedeemCodePageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("数据导出")
    @GetMapping("/excelOut")
    public void excelOut(HttpServletResponse response,
                         RedeemCodeOutParam param) throws Exception {
        ExcelUtils.getInstance().
                exportObjects2Excel(service.findList(param), RedeemCodeDTO.class, true, null, true,response,"兑换码导出");
    }


    @ApiOperation("根据兑换码获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_detail_code")
    public RespDate<RedeemCodeDTO> findDetail(@ApiParam("兑换码") @RequestParam("code") String code) {
        RedeemCodeDTO redeemCode = service.findOneOrNull(eq(RedeemCodeDTO::getCode,code));
        if(redeemCode==null){
            throw new ServiceException(CommonResultCodeEnum.REDEEM_CODE_NULL, "兑换码不存在");
        }
        return getRtnDate(redeemCode);
    }

    @ApiOperation("根据id获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<RedeemCodeDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }


}
