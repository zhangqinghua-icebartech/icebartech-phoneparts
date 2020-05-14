package com.icebartech.phoneparts.manager;

import com.icebartech.phoneparts.util.JSON;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminRoleTests {

    public final static String list = "/admin/role/list";
    public final static String page = "/admin/role/page";
    public final static String insert = "/admin/role/insert";
    public final static String update = "/admin/role/update";
    public final static String delete = "/admin/role/delete";
    public final static String detail = "/admin/role/detail";

    private static MockMvc mockMvc;

    @Test
    public static void run(MockMvc mockMvc) throws Exception {
        AdminRoleTests.mockMvc = mockMvc;
        AdminRoleTests.insert();
        AdminRoleTests.update();
        AdminRoleTests.delete();
        AdminRoleTests.list();
        AdminRoleTests.page();
    }

    private static void insert() throws Exception {
        JSON param = new JSON();
        param.put("roleName", "超级管理员");
        param.put("roleDesc", "拥有所有权限的角色，老板专用");
        param.put("menuIds", "[2,4,6,7,9,11,12,14]");
        mockMvc.perform(post(insert).content(param.toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));

        mockMvc.perform(post(detail).param("id", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200))
               .andExpect(jsonPath("data.bussData.roleMenuDesc").exists());

    }

    private static void update() throws Exception {
        JSON param = new JSON();
        param.put("id", "1");
        param.put("roleName", "测试专员（001）");
        param.put("menuIds", "[2,4,6,7]");
        mockMvc.perform(post(update).content(param.toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
    }

    private static void delete() throws Exception {
        mockMvc.perform(post(delete).param("id", "3"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
    }

    private static void list() throws Exception {
        mockMvc.perform(post(list))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
    }

    private static void page() throws Exception {
        mockMvc.perform(post(page).content(new JSON().toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
    }
}
