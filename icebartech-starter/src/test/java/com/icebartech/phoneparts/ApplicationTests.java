package com.icebartech.phoneparts;

import com.icebartech.core.IcebartechApplication;
import com.icebartech.phoneparts.manager.AdminManagerTests;
import com.icebartech.phoneparts.manager.AdminMenuTests;
import com.icebartech.phoneparts.manager.AdminRoleTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

// @RunWith(SpringRunner.class)
// @ActiveProfiles("dev")
// @SpringBootTest(classes = IcebartechApplication.class)
public class ApplicationTests {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        // 构造MockMvc
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test() throws Exception {

        // 管理员设置
        AdminMenuTests.run(mockMvc);
        AdminRoleTests.run(mockMvc);
        AdminManagerTests.run(mockMvc, "ojg6RZPHP01UGW3O/bK76/OLxybiywnw+WMP0SVi+p2xLZtyqimQy+9Xa8Sif0gz7Z7kA2k4NV0=");
    }
}

