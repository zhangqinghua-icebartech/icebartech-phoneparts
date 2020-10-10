package com.icebartech.phoneparts.manager;

import com.icebartech.phoneparts.util.JSON;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminManagerTests {

    public final static String page = "/admin/manager/page";
    public final static String login = "/admin/manager/login";
    public final static String insert = "/admin/manager/insert";
    public final static String update = "/admin/manager/update";
    public final static String delete = "/admin/manager/delete";
    public final static String detail = "/admin/manager/detail";
    public final static String resetPassword = "/admin/manager/reset_passowrd";

    private static String sessionId;
    private static MockMvc mockMvc;

    public static void run(MockMvc mockMvc, String sessionId) throws Exception {
        AdminManagerTests.sessionId = sessionId;
        AdminManagerTests.mockMvc = mockMvc;
        AdminManagerTests.insert();
        AdminManagerTests.update();
        AdminManagerTests.delete();
        AdminManagerTests.page();
        AdminManagerTests.login();
        AdminManagerTests.resetPassword();
    }

    private static void insert() throws Exception {
        JSON param = new JSON();
        param.put("roleId", "1");
        param.put("avatorKey", "avatorKey");
        param.put("nickName", "张三");
        param.put("loginName", "asd001");
        param.put("password", "123456");
        mockMvc.perform(post(insert).content(param.toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));

        mockMvc.perform(post(detail).param("id", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200))
               .andExpect(jsonPath("data.bussData.loginName").value("asd001"));

        param = new JSON();
        param.put("roleId", "2");
        param.put("avatorKey", "avatorKey");
        param.put("nickName", "李四");
        param.put("loginName", "asd002");
        param.put("password", "654321");
        mockMvc.perform(post(insert).content(param.toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
        mockMvc.perform(post(detail).param("id", "2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200))
               .andExpect(jsonPath("data.bussData.roleId").value(2));

        param = new JSON();
        param.put("roleId", "2");
        param.put("avatorKey", "avatorKey");
        param.put("nickName", "王五");
        param.put("loginName", "asd002");
        param.put("password", "654321");
        mockMvc.perform(post(insert).content(param.toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(500));

        param = new JSON();
        param.put("roleId", "2");
        param.put("avatorKey", "avatorKey");
        param.put("nickName", "王五");
        param.put("loginName", "asd003");
        param.put("password", "654321");
        mockMvc.perform(post(insert).content(param.toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
        mockMvc.perform(post(detail).param("id", "3"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
    }

    private static void update() throws Exception {
        JSON param = new JSON();
        param.put("id", "1");
        param.put("nickName", "张三的昵称");
        mockMvc.perform(post(update).content(param.toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
    }

    private static void delete() throws Exception {
        mockMvc.perform(post(delete).param("id", "3"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));

        // 测试删除角色
        mockMvc.perform(post(AdminRoleTests.delete).param("id", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(500));
    }


    private static void page() throws Exception {
        mockMvc.perform(post(page).content(new JSON().toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
    }

    private static void login() throws Exception {
        mockMvc.perform(post(login).param("loginName", "asd001").param("password", "12345"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(500));
        mockMvc.perform(post(login).param("loginName", "asd001").param("password", "123456"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
    }

    private static void resetPassword() throws Exception {
        mockMvc.perform(post(resetPassword).header("sessionId", sessionId).param("oldPassword", "654321").param("newPassword", "123456"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
        mockMvc.perform(post(login).param("loginName", "asd002").param("password", "123456"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("code").value(200));
    }
}
