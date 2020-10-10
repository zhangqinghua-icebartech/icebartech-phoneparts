package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.system.dto.SysSerialClassDTO;
import com.icebartech.phoneparts.system.dto.SysSerialDto;
import com.icebartech.phoneparts.system.param.*;
import com.icebartech.phoneparts.system.service.SysSerialClassService;
import com.icebartech.phoneparts.system.service.SysSerialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author Created by liuao on 2019/9/6.
 * @desc
 */
@Api(tags = "序列号二级分类接口")
@RestController
@RequestMapping(value = "/sysSerialClass", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysSerialClassController extends BaseController {


    @Autowired
    SysSerialClassService service;
    @Autowired
    SysSerialService sysSerialService;


    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.app,UserEnum.agent})
    @PostMapping("/find_page")
    public RespPage<SysSerialClassDTO> findPage(@Valid @RequestBody SysSerialClassPageParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if(localUser.getLevel() == 0){
            param.setParentId(0L);
        }else if(localUser.getLevel() == 1){
            if(param.getAgentId()==null){
                param.setParentId(localUser.getUserId());
            }else {
                param.setParentId(0L);
            }
        }
        if(localUser.getLevel() == 2){
            param.setParentId(localUser.getParentId());
        }
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin,UserEnum.app,UserEnum.agent})
    @PostMapping("/find_list")
    public RespDate<List<SysSerialClassDTO>> findList() {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        Long parentId = UserThreadLocal.getUserId();
        if(localUser.getLevel() == 0){
            parentId = 0L;
        }
        if(localUser.getLevel() == 2){
            parentId = localUser.getParentId();
        }
        return getRtnDate(service.findList(eq(SysSerialClassDTO::getParentId,parentId)));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app,UserEnum.agent})
    @PostMapping("/find_detail")
    public RespDate<SysSerialClassDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysSerialClassInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysSerialClassUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("修改排序")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/changeSort")
    public RespDate<Boolean> changeSort(@RequestParam("id") Long id,
                                        @RequestParam("sort") Integer sort) {
        return getRtnDate(service.changeSort(id,sort));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        SysSerialDto sysSerial = sysSerialService.findBySerialClassId(id);
        if(sysSerial!=null)
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "请先删除其菜单下序列号");
        return getRtnDate(service.delete(id));
    }


}
