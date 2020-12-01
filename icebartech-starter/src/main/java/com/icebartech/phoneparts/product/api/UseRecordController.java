package com.icebartech.phoneparts.product.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.utils.BeanMapperNew;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.excel.ExcelUtils;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.product.dto.UseRecordDTO;
import com.icebartech.phoneparts.product.dto.UseRecordExcel;
import com.icebartech.phoneparts.product.param.UseRecordPageParam;
import com.icebartech.phoneparts.product.param.UseRecordProductPageParam;
import com.icebartech.phoneparts.product.param.UseRecordUserPageParam;
import com.icebartech.phoneparts.product.service.UseRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@SuppressWarnings("rawtypes")
@Api(tags = "使用记录模块接口")
@RestController
@RequestMapping(value = "/useRecord", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UseRecordController extends BaseController {

    @Autowired
    private AgentService agentService;
    @Autowired
    private UseRecordService useRecordService;

    @ApiOperation("获取用户切割统计")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/find_user_record")
    public RespPage<Map> findUserRecord(@Valid @RequestBody UseRecordUserPageParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();

        // 一级代理商
        if (localUser.getLevel() == 1) {
            param.setAgentId(localUser.getUserId());

        }
        // 二级代理商
        if (localUser.getLevel() == 2) {
            param.setSecondAgentId(localUser.getUserId());
        }

        return getPageRtnDate(useRecordService.findUserRecord(param));
    }

    @ApiOperation("获取用户切割统计（新版本）")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/find_user_record1")
    public RespPage<Map> findUserRecord1(@Valid @RequestBody UseRecordUserPageParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();

        // 一级代理商
        if (localUser.getLevel() == 1) {
            param.setAgentId(localUser.getUserId());

        }
        // 二级代理商
        if (localUser.getLevel() == 2) {
            param.setSecondAgentId(localUser.getUserId());
        }

        return getPageRtnDate(useRecordService.findUserRecord1(param));
    }

    @ApiOperation("获取用户切割总数")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/find_user_record_count")
    public RespDate<Map<String, Object>> findUserRecordCount(@Valid @RequestBody UseRecordUserPageParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();

        // 一级代理商
        if (localUser.getLevel() == 1) {
            param.setAgentId(localUser.getUserId());

        }
        // 二级代理商
        if (localUser.getLevel() == 2) {
            param.setSecondAgentId(localUser.getUserId());
        }

        return getRtnDate(useRecordService.findUserRecordCount(param));
    }

    @ApiOperation("获取产品切割统计")
    @RequireLogin({UserEnum.admin, UserEnum.app, UserEnum.agent})
    @PostMapping("/find_product_record")
    public RespPage<Map> findProductRecord(@Valid @RequestBody UseRecordProductPageParam param) {
        if (UserThreadLocal.getUserType() == UserEnum.app) param.setUserId(UserThreadLocal.getUserId());
        return getPageRtnDate(useRecordService.findProductRecord(param));
    }

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin, UserEnum.app, UserEnum.agent})
    @PostMapping("/find_page")
    public RespPage<UseRecordDTO> findPage(@Valid @RequestBody UseRecordPageParam param) {
        if (UserThreadLocal.getUserType() == UserEnum.app) param.setUserId(UserThreadLocal.getUserId());
        return getPageRtnDate(useRecordService.findPage(param));
    }


    @ApiOperation("数据导出")
    @GetMapping("/excelOut")
    public void excelOut(HttpServletResponse response,
                         String serialNum,
                         String email,
                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime strTime,
                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                         int query,
                         Long agentId) throws Exception {
        UseRecordUserPageParam param = new UseRecordUserPageParam();
        param.setPageSize(1000000000);
        param.setPageIndex(1);
        param.setSerialNum(serialNum);
        param.setEmail(email);

        // 按照条件搜索
        if (query == 0) {
            param.setStrTime(strTime);
            param.setEndTime(endTime);
        }

        // 查询当天数据
        if (query == 1) {
            param.setStrTime(LocalDate.now().atStartOfDay());
            param.setEndTime(param.getStrTime().plusDays(1));
        }


        // 查询昨天数据
        if (query == 2) {
            param.setStrTime(LocalDate.now().minusDays(1).atStartOfDay());
            param.setEndTime(param.getStrTime().plusDays(1));
        }

        // 查询近10天数据
        if (query == 3) {
            param.setStrTime(LocalDate.now().minusDays(10).atStartOfDay());
            param.setEndTime(param.getStrTime().plusDays(10));
        }

        // 查询近半个月数据
        if (query == 4) {
            param.setStrTime(LocalDate.now().minusDays(15).atStartOfDay());
            param.setEndTime(param.getStrTime().plusDays(15));
        }

        if (agentId != null) {
            Agent agent = agentService.findOne(agentId);
            // 代理商一级
            if (agent.getParentId() == 0) {
                param.setAgentId(agentId);

            }
            // 代理商二级
            else {
                param.setSecondAgentId(agentId);
            }
        }

        System.out.println(param);

        Page<Map> page = useRecordService.findUserRecord1(param);

        ExcelUtils.exports(BeanMapperNew.map(page.getContent(), UseRecordExcel.class),
                           response, "切割统计");
    }
}
