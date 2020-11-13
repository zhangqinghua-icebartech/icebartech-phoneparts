package com.icebartech.phoneparts.util;

import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Data
public class MockMvcUtil {

    private static MockMvc mockMvc;

    private static String appSessionId;
    private static String adminSessionId;

    private static Boolean useAdminSession = true;

    private ResultActions resultActions;
    private MockHttpServletRequestBuilder requestBuilder;

    public static void mockMvc(MockMvc mockMvc) {
        MockMvcUtil.mockMvc = mockMvc;
    }

    public static void appSession(String appSessionId) {
        MockMvcUtil.appSessionId = appSessionId;
    }

    public static void adminSession(String adminSessionId) {
        MockMvcUtil.adminSessionId = adminSessionId;
    }

    public static void useAppSession() {
        MockMvcUtil.useAdminSession = false;
    }

    public static void useAdminSession() {
        MockMvcUtil.useAdminSession = true;
    }

    public static MockMvcUtil post(String url) {
        if (MockMvcUtil.mockMvc == null) {
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "未设置mockMvc");
        }

        MockMvcUtil instance = new MockMvcUtil();

        String sessionId = useAdminSession ? adminSessionId : appSessionId;

        if (StringUtils.isBlank(sessionId)) {
            instance.setRequestBuilder(MockMvcRequestBuilders.post(url));
        } else {
            instance.setRequestBuilder(MockMvcRequestBuilders.post(url).header("sessionId", sessionId));
        }

        return instance;
    }

    public MockMvcUtil param(String name, Object value) {
        if (StringUtils.isNotBlank(name)) {
            requestBuilder.param(name, String.valueOf(value));
        }
        return this;
    }

    public MockMvcUtil content(String json) {
        if (StringUtils.isNotBlank(json)) {
            requestBuilder.content(json).contentType(MediaType.APPLICATION_JSON);
        }
        return this;
    }

    public MockMvcUtil content(JSON json) {
        if (json != null) {
            requestBuilder.content(json.toString()).contentType(MediaType.APPLICATION_JSON);
        }
        return this;
    }

    public MockMvcUtil andExists(String path) throws Exception {
        if (resultActions == null) success();

        resultActions.andExpect(jsonPath(path).exists());
        return this;
    }

    public MockMvcUtil andExpect(String path, String value) throws Exception {
        if (resultActions == null) success();

        resultActions.andExpect(jsonPath(path).value(value));
        resultActions.andReturn();
        return this;
    }

    public String andReturn() throws Exception {
        if (resultActions == null) success();
        return resultActions.andReturn().getResponse().getContentAsString();
    }

    private void perform() throws Exception {
        resultActions = mockMvc.perform(requestBuilder).andDo(print());
    }

    public void success() throws Exception {
        perform();
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("code").value(200));
    }

    public void error() throws Exception {
        perform();
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("code").value(500));
    }
}
