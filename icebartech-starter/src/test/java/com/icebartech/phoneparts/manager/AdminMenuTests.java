package com.icebartech.phoneparts.manager;

import com.icebartech.phoneparts.util.JSON;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminMenuTests {

    public final static String list = "/admin/menu/list";
    public final static String insert = "/admin/menu/insert";

    private static MockMvc mockMvc;

    public static void run(MockMvc mockMvc) throws Exception {
        AdminMenuTests.mockMvc = mockMvc;

        AdminMenuTests.insert();
    }

    private static void insert() throws Exception {
        List<JSON> params = new ArrayList<>();

        JSON param = new JSON();
        param.put("parentId", "0");
        param.put("menuUrl", "sysSerial");
        param.put("menuName", "设备管理");
        param.put("menuSort", "1");
        params.add(param);

        param = new JSON();
        param.put("parentId", "1");
        param.put("menuUrl", "sysSerial");
        param.put("menuName", "设备管理");
        param.put("menuSort", "2");
        params.add(param);

        param = new JSON();
        param.put("parentId", "0");
        param.put("menuUrl", "user");
        param.put("menuName", "用户管理");
        param.put("menuSort", "3");
        params.add(param);

        param = new JSON();
        param.put("parentId", "3");
        param.put("menuUrl", "user");
        param.put("menuName", "用户管理");
        param.put("menuSort", "4");
        params.add(param);

        param = new JSON();
        param.put("parentId", "0");
        param.put("menuUrl", "sysClass");
        param.put("menuName", "分类管理");
        param.put("menuSort", "5");
        params.add(param);

        param = new JSON();
        param.put("parentId", "5");
        param.put("menuUrl", "sysClassOne");
        param.put("menuName", "一级分类管理");
        param.put("menuSort", "6");
        params.add(param);

        param = new JSON();
        param.put("parentId", "5");
        param.put("menuUrl", "sysClassTwo");
        param.put("menuName", "二级分类管理");
        param.put("menuSort", "7");
        params.add(param);

        param = new JSON();
        param.put("parentId", "0");
        param.put("menuUrl", "product");
        param.put("menuName", "产品管理");
        param.put("menuSort", "8");
        params.add(param);

        param = new JSON();
        param.put("parentId", "8");
        param.put("menuUrl", "product");
        param.put("menuName", "产品管理");
        param.put("menuSort", "9");
        params.add(param);

        param = new JSON();
        param.put("parentId", "0");
        param.put("menuUrl", "manager");
        param.put("menuName", "管理员设置");
        param.put("menuSort", "10");
        params.add(param);

        param = new JSON();
        param.put("parentId", "10");
        param.put("menuUrl", "role");
        param.put("menuName", "角色管理");
        param.put("menuSort", "11");
        params.add(param);

        param = new JSON();
        param.put("parentId", "10");
        param.put("menuUrl", "manager");
        param.put("menuName", "账号管理");
        param.put("menuSort", "12");
        params.add(param);

        param = new JSON();
        param.put("parentId", "0");
        param.put("menuUrl", "company");
        param.put("menuName", "联系我们");
        param.put("menuSort", "13");
        params.add(param);

        param = new JSON();
        param.put("parentId", "13");
        param.put("menuUrl", "company");
        param.put("menuName", "联系我们");
        param.put("menuSort", "14");
        params.add(param);

        for (JSON insertparam : params) {
            mockMvc.perform(post(insert).content(insertparam.toString()).contentType(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("code").value(200));
        }
    }
}
