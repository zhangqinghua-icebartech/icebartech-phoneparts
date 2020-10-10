package com.icebartech.core.utils;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoxiong
 * @date 2019/1/23 16:44
 */
public class WaihuiUtil {
    private static final String appcode = "52b227ab8e544f6bbaadd0bfcb1c7785";

    private static final String host = "https://ali-waihui.showapi.com";

    private static final String path = "/waihui-transform";

    private static final String method = "GET";

    /**
     * @param fromCode 源货币类型
     * @param money    转换的金额，单位元
     * @param toCode   目标货币类型
     * @return
     */
    public static BigDecimal transform(String fromCode, String money, String toCode) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("fromCode", fromCode);
        querys.put("money", money);
        querys.put("toCode", toCode);
        try {
            String response = HttpUtils.doGet(host, path, method, headers, querys);
            JSONObject object = JSONObject.parseObject(response);
            JSONObject showapi_res_body = object.getJSONObject("showapi_res_body");
            Integer showapi_res_code = object.getInteger("showapi_res_code");
            if (showapi_res_code.equals(0)) {
                BigDecimal retMoney = showapi_res_body.getBigDecimal("money");
                return retMoney;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
