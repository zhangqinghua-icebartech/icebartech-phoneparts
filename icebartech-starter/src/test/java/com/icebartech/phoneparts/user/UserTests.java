package com.icebartech.phoneparts.user;

import com.icebartech.phoneparts.util.JSON;
import com.icebartech.phoneparts.util.MockMvcUtil;

/**
 * @author Created by liuao on 2019/6/24.
 * @desc
 */
public class UserTests {
    private final static String insert = "/app/menu/insert";
    private final static String update = "/app/menu/update";
    private final static String delete = "/app/menu/delete";

    private final static String findList = "/app/menu/find_list";
    private final static String findDetail = "/app/menu/find_detail";

    private final static String login = "/app/user/login";
    private final static String register = "/app/user/register";

    public static void run() throws Exception {

    }

    private void register() throws Exception {
        JSON param = new JSON();
        param.put("serialNum","xafsdf");
        param.put("code","1234");
        param.put("email","aliu95@qq.com");
        param.put("pwd","123456");
        //MockMvcUtil.appSession(new JSON(ret).getStr("data/bussData/sessionId"));
    }

}
