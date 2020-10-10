package com.icebartech.core.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapDealUtil {

    public static Map<String, Object> requestMap(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (request.getParameter("page") != null) {
            paramMap.put("page", Integer.valueOf(request.getParameter("page")));
        }
        if (request.getParameter("rows") != null) {
            paramMap.put("rows", Integer.valueOf(request.getParameter("rows")));
        }
        if (request.getParameter("sort") != null) {
            paramMap.put("sort", request.getParameter("sort"));
        }
        if (request.getParameter("order") != null) {
            paramMap.put("order", request.getParameter("order"));
        }
        return paramMap;
    }

    public static Map<String, String> requestParameterMapToHashMap(Map requestParams) {
        Map<String, String> params = new HashMap<String, String>();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;

    }

}
