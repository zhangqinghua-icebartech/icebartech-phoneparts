package com.icebartech.phoneparts;

import com.icebartech.core.IcebartechApplication;
import com.icebartech.phoneparts.manager.AdminManagerTests;
import com.icebartech.phoneparts.manager.AdminMenuTests;
import com.icebartech.phoneparts.manager.AdminRoleTests;
import com.icebartech.phoneparts.user.repository.UserRepository;
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

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = IcebartechApplication.class)
public class ApplicationTests {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        // 构造MockMvc
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test() throws Exception {
        userRepository.save(null);
        // 管理员设置
        AdminMenuTests.run(mockMvc);
        AdminRoleTests.run(mockMvc);
        AdminManagerTests.run(mockMvc, "ojg6RZPHP01UGW3O/bK76/OLxybiywnw+WMP0SVi+p2xLZtyqimQy+9Xa8Sif0gz7Z7kA2k4NV0=");
    }

    /**
     * 代理商
     * <p>
     * 依赖：
     * 1. parentId
     * <p>
     * 影响：
     * 1. SysManager
     * 1. CompanyInfo
     * 1. CoverInfo
     * 1. Redeem
     * <p>
     * 1. SysClassOne
     * 1. SysClassTwo
     * 1. SysClassThree
     * <p>
     * 1. SysSerialClass
     * 1. SysSerial
     * <p>
     * 1. User
     * 1. UseRecord
     * 1. AddUseRecord
     */
    public void test_agent() {

    }

    /**
     * 一级菜单表
     * 1. Agent
     * <p>
     * 1. SysClassTwo
     * 2. SysClassThree
     * 3. Product
     */
    public void test_sys_class_one() {

    }

    /**
     * 二级菜单表
     * 1. Agent
     * 2. SysClassOne
     * <p>
     * 1. SysClassThree
     * 2. Product
     */
    public void test_sys_class_two() {

    }

    /**
     * 三级分类表
     * 1. Agent
     * 2. SysClassOne
     * 3. SysClassTwo
     * <p>
     * 1. Product
     */
    public void test_sys_class_three() {

    }

    /**
     * 单品表
     * 依赖：
     * 1. SysClassOne
     * 1. SysClassTwo
     * 1. SysClassThree
     * <p>
     * 影响：
     * 1. UseRecord
     */
    public void test_product() {

    }


    /**
     * 序列号类别
     * <p>
     * 依赖：
     * 1. Agent.agentId
     * 1. Agent.secondAgentId
     * 1. SysSerialClass.parentId
     * <p>
     * 影响：
     * 1. SysSerial.serialClassId
     * 1. SysSerial.secondSerialClassId
     * 1. User.serialClassId
     */
    public void test_sys_serial_class() {

    }

    /**
     * 序列号表
     * <p>
     * 依赖：
     * 1. Agent.agentId
     * 1. Agent.secondAgentId
     * 1. SysSerialClass.serialClassId
     * 1. SysSerialClass.secondSerialClassId
     * <p>
     * 影响：
     * 1. User.serialId
     */
    public void test_sys_serial() {

    }

    /**
     * 用户表
     * <p>
     * 依赖：
     * 1. Agent.agentId
     * 1. Agent.secondAgentId
     * 1. SysSerial.serialId
     * 1. SysSerialClass.secondSerialClassId
     * <p>
     * 影响：
     * 1. UseRecord
     * 1. AddUseRecord
     * 1. RedeemCode
     * 1. RedeemRecord
     * 1. SysUseConfig
     */
    public void test_user() {

    }

    /**
     * 使用记录表
     * <p>
     * 依赖：
     * 1. User.userId
     * 1. Agent.agentId
     * 1. Agent.secondAgentId
     * 1. Product.productId
     * <p>
     * 影响：
     */
    public void test_use_record() {

    }

    /**
     * 添加记录
     * <p>
     * 依赖：
     * 1. User.userId
     * 1. Agent.agentId
     * <p>
     * 影响：
     */
    public void test_add_use_record() {

    }

    /**
     * 兑换码
     * <p>
     * 依赖：
     * 1. User.userId
     * 1. Redeem.redeemId
     * <p>
     * 影响：
     * 1. RedeemRecord.RedeemCodeId
     */
    public void test_redeem_code() {

    }

    /**
     * 兑换记录
     * <p>
     * 依赖：
     * 1. User.userId
     * 1. RedeemCode.RedeemCodeId
     * <p>
     * 影响：
     */
    public void test_redeem_code_record() {

    }

    /**
     * 常用设置
     * <p>
     * 依赖：
     * 1. User.userId
     * <p>
     * 影响：
     */
    public void test_sys_use_config() {

    }
}

