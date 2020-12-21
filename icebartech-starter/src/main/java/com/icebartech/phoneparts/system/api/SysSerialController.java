package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.excel.ExcelUtils;
import com.icebartech.phoneparts.system.dto.SysSerialDto;
import com.icebartech.phoneparts.system.param.*;
import com.icebartech.phoneparts.system.po.SysSerial;
import com.icebartech.phoneparts.system.service.SysSerialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.icebartech.core.vo.QueryParam.eq;
import static com.icebartech.core.vo.QueryParam.in;

@Api(tags = "序列号模块接口")
@RestController
@Slf4j
@RequestMapping(value = "/sysSerial", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysSerialController extends BaseController {

    @Autowired
    private SysSerialService service;


    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/find_page")
    public RespPage<SysSerialDto> findPage(@Valid @RequestBody SysSerialPageParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();

        //一级代理商
        if (localUser.getLevel() == 1) {
            //二级代理商id 前端传成了一级代理商变回来
            param.setSecondAgentId(param.getAgentId());
            //二级代理商分类id 前端传成了一级代理商分类变回来
            param.setSecondSerialClassId(param.getSerialClassId());
            //置空一级代理商分类  //批次字段
            param.setSerialClassId(param.getBatchId());
            //自己代理商的数据
            param.setAgentId(localUser.getUserId());

            //二级代理商
        } else if (localUser.getLevel() == 2) {
            //自己代理商的数据
            param.setSecondAgentId(localUser.getUserId());
            //二级代理商分类id 前端传成了一级代理商分类变回来
            param.setSecondSerialClassId(param.getSerialClassId());
            //前端不用传，传了置空
            param.setAgentId(null);
            //前端不用传，传了置空
            param.setSerialClassId(null);
        }

        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("检测序列号是否正确")
    @PostMapping("/isValid")
    @RequireLogin(UserEnum.no_login)
    public RespDate<Boolean> isValid(@Valid @RequestParam("serialNum") String serialNum) {
        return getRtnDate(service.isValid(serialNum));
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/find_list")
    public RespDate<List<SysSerialDto>> findList() {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        List<SysSerialDto> sysSerialDtos;
        //一级代理商
        if (localUser.getLevel() == 1) {
            sysSerialDtos = service.findList(eq(SysSerialDto::getAgentId, localUser.getUserId()));
            //二级代理商
        } else if (localUser.getLevel() == 2) {
            sysSerialDtos = service.findList(eq(SysSerialDto::getSecondAgentId, localUser.getUserId()));
            //平台
        } else {
            sysSerialDtos = service.findList();
        }
        return getRtnDate(sysSerialDtos);
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin, UserEnum.app, UserEnum.agent})
    @PostMapping("/find_detail")
    public RespDate<SysSerialDto> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("分配")
    @RequireLogin({UserEnum.agent})
    @PostMapping("/allocation")
    public RespDate<Boolean> allocation(@RequestParam Long secondAgentId,
                                        @RequestParam Long serialClassId,
                                        @RequestBody List<Long> serialIds) {
        return getRtnDate(service.allocation(secondAgentId, serialIds, serialClassId));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysSerialInsertParam param) {
        SysSerial sysSerial = service.findBySerialNum(param.getSerialNum());
        if (sysSerial != null) throw new ServiceException(CommonResultCodeEnum.NUM_REPET, "序列号已存在");
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("获取未分配数量")
    @RequireLogin({UserEnum.agent})
    @PostMapping("/unUseNum")
    public RespDate<Integer> unUseNum() {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if (localUser.getLevel() != 1) {
            return getRtnDate(0);
        }
        return getRtnDate(service.unUseNum(localUser.getUserId()));
    }

    @ApiOperation("生成序列号")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/create")
    public RespDate<String> create(@Valid @RequestBody SysSerialCreateParam param) {
        return getRtnDate(service.create(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysSerialUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("重置")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/init")
    public RespDate<Boolean> init(@RequestParam Long id) {
        return getRtnDate(service.init(id));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(service.delete(id));
    }

    @ApiOperation("批量删除")
    @PostMapping("/deletes")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    public RespDate<Boolean> deletes(@RequestParam List<Long> ids) {
        return getRtnDate(service.delete(in(SysSerialDto::getId, ids)));
    }

    @ApiOperation("批量导入")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/excelInput")
    public RespDate<Boolean> excelInput(@RequestParam("file") MultipartFile file) throws Exception {
        List<SysSerialInsertParam> sysSerialInsertParams = ExcelUtils.imports(file.getInputStream(), SysSerialInsertParam.class);
        return getRtnDate(service.excelInput(sysSerialInsertParams));
    }

    @ApiOperation("下载序列号")
    @GetMapping("/excelOut")
    @RequireLogin(UserEnum.no_login)
    public void createExcelOut(HttpServletResponse response, @RequestParam String randomStr) {
        List<SysSerialDto> sysSerialDtos = service.excelExports(randomStr);
        ExcelUtils.exports(BeanMapper.map(sysSerialDtos, SysSerialExports.class), response, "生成序列号列表");
    }

    @ApiOperation("设备写入")
    @PostMapping("/write")
    public RespDate<Boolean> write(@RequestParam Long id) {
        SysSerial sysSerial = new SysSerial();
        sysSerial.setId(id);
        sysSerial.setWriteStatus(1);
        return getRtnDate(service.update(sysSerial));
    }
}
