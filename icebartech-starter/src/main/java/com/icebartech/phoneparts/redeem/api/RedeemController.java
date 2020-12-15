package com.icebartech.phoneparts.redeem.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.vo.QueryParam;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.excel.ExcelUtils;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.redeem.dto.RedeemDTO;
import com.icebartech.phoneparts.redeem.excel.RedeemImports;
import com.icebartech.phoneparts.redeem.param.RedeemCustomInsertParam;
import com.icebartech.phoneparts.redeem.param.RedeemInsertParam;
import com.icebartech.phoneparts.redeem.param.RedeemPageParam;
import com.icebartech.phoneparts.redeem.po.RedeemCode;
import com.icebartech.phoneparts.redeem.service.RedeemCodeService;
import com.icebartech.phoneparts.redeem.service.RedeemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.eq;

@Api(tags = "兑换码管理")
@RestController
@RequestMapping(value = "/redeem", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RedeemController extends BaseController {

    @Autowired
    private AgentService agentService;
    @Autowired
    private RedeemService redeemService;
    @Autowired
    private RedeemCodeService redeemCodeService;


    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/insert")
    public RespDate<Boolean> insert(@Valid @RequestBody RedeemInsertParam param) {
        return getRtnDate(redeemService.insertAll(param));
    }

    @ApiOperation("自定义新增")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/custom_insert")
    public RespDate<Boolean> custom_insert(@Valid @RequestBody RedeemCustomInsertParam param) {
        return getRtnDate(redeemService.insertCustom(param));
    }

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/find_page")
    public RespPage<RedeemDTO> findPage(@Valid @RequestBody RedeemPageParam param) {
        boolean is = false;
        List<Long> list = new ArrayList<>();
        if (param.getEmail() != null && !param.getEmail().equals("")) {
            is = true;
            list = redeemCodeService.findRedeemIdList(param.getEmail());
            // 防止数据为空
            list.add(-11111L);
        }
        if (param.getCode() != null && !param.getCode().equals("")) {
            is = true;
            RedeemCode redeemCode = redeemCodeService.findOneOrNull(eq(RedeemCode::getCode, param.getCode()));
            if (redeemCode != null) {
                list.add(redeemCode.getRedeemId());
            }
        }
        if (is) {
            param.setIdIn(list);
        }
        return getPageRtnDate(redeemService.findPage(param));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(redeemService.deleteAll(id));
    }

    @ApiOperation("兑换码导入")
    @PostMapping("/imposrts")
    public RespDate<Boolean> imposrts(@RequestParam("file") MultipartFile file) throws IOException {
        List<RedeemImports> imports = ExcelUtils.imports(file.getInputStream(), RedeemImports.class);

        // 1. 获取代理商Id
        List<AgentDTO> agents = agentService.findList(QueryParam.in(AgentDTO::getClassName, imports.stream().map(RedeemImports::getClassName).collect(Collectors.toList())));

        // 2. 对数据按照标题和代理商进行分组
        Map<String, List<RedeemImports>> group = imports.stream().collect(Collectors.groupingBy(RedeemImports::group));

        // 3. 分别新增数据
        for (String key : group.keySet()) {
            String title = key.split(":")[0];
            String className = key.split(":")[1];
            Long agentId = agents.stream().filter(a -> a.getClassName().equals(className)).map(AgentDTO::getId).findAny().orElse(null);
            List<RedeemImports> groupImports = group.get(key);
            redeemService.insertCustom(title, agentId, BeanMapper.map(groupImports, RedeemCode.class));
        }
        return getRtnDate(true);
    }

    @ApiOperation("兑换码导入模版")
    @GetMapping("/imposrts_template")
    public void imposrtsTemplate() {
        ExcelUtils.exports(RedeemImports.demo(), super.getResponse(), "兑换码导入模版");
    }
}
