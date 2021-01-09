package com.icebartech.phoneparts;

import com.icebartech.core.IcebartechApplication;
import com.icebartech.phoneparts.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = IcebartechApplication.class)
public class ApplicationTests {

//    private MockMvc mockMvc;
//    @Autowired
//    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        // 构造MockMvc
        // this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test() throws Exception {

        // 管理员设置
        // AdminMenuTests.run(mockMvc);
        // AdminRoleTests.run(mockMvc);
        // AdminManagerTests.run(mockMvc, "ojg6RZPHP01UGW3O/bK76/OLxybiywnw+WMP0SVi+p2xLZtyqimQy+9Xa8Sif0gz7Z7kA2k4NV0=");
    }

    /**
     * 删除重复的账号
     * 1. 找出所有重复的序列号
     * 1. 针对已经重复的账号作删除处理（may_use_count比较小的那些）
     * 2. 使用记录表也一并删掉
     */
    @Test
    public void test1() {
        // 重复的邮箱
        List<String> serialNums = userRepository.repeatSerialNum();

        // 要删除的用户（去掉剩余次数最高的邮箱）
        List<Long> userIds = new ArrayList<>();
        for (String serialNum : serialNums) {
            userIds.addAll(userRepository.deleteUserIds(serialNum).stream().map(BigInteger::longValue).collect(Collectors.toList()));
        }

        if (!CollectionUtils.isEmpty(userIds)) {
            // 删除邮箱
            userRepository.deleteUser(userIds);

            // 删除用户记录
            userRepository.deleteUserRecord(userIds);
        }
    }
}

