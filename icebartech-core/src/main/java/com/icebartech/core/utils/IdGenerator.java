package com.icebartech.core.utils;

import com.icebartech.core.components.RedisComponent;
import com.icebartech.core.constants.IcebartechConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 各种id
 *
 * @author wenhsh
 */
@Component
public class IdGenerator {

    @Autowired
    private RedisComponent redisComponent;

    private final static String CODE_KEY = "Code";

    private final static String CITY_CODE = "CityCode";

    public final static String ALIYUN_OSS_FILE_GLOBAL_ID_KEY = "AliYunOssFileGlobalId";

    /**
     * 获取一个文件key
     *
     * @return
     */
    public String getFileKey(String suffix) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYMMddHHmmss"))
                + "-" + redisComponent.incr(ALIYUN_OSS_FILE_GLOBAL_ID_KEY, "GlobalFile").toString()
                + "." + suffix;
    }

    /**
     * 获取流水号id
     *
     * @return
     */
    public String getSeqId() {
        String rtnStr = "";
        long orderId = redisComponent.incr(IcebartechConstants.ORDER_ID_KEY, "seq_id");
        if (100000000 > orderId) {
            orderId = redisComponent.incr(IcebartechConstants.ORDER_ID_KEY, "seq_id", 100000000L);
        }
        //生成4位数字随机数
        String randomNum = RandomStringUtils.randomNumeric(4);
        rtnStr += orderId + randomNum;
        return rtnStr;
    }

    /**
     * 获取订单id
     *
     * @return
     */
    public String getOrderId() {
        String rtnStr = "";
        //String date = DateTimeUtility.dateofnowonlynumber().substring(0, 4);
        long orderId = redisComponent.incr(IcebartechConstants.ORDER_ID_KEY, "order_id");
        if (1499937180 > orderId) {
            orderId = redisComponent.incr(IcebartechConstants.ORDER_ID_KEY, "order_id", 1499937180L);
        }
        //生成4位数字随机数
        String randomNum = RandomStringUtils.randomNumeric(4);
        rtnStr += orderId + randomNum;
        return rtnStr;
    }

    /**
     * 获取推荐码
     *
     * @return
     */
    public String getCode() {
        String rtnStr = "";
        long code = redisComponent.incr(CODE_KEY, "code_id");
        if (code < 1000) {
            code = redisComponent.incr(CODE_KEY, "code_id", 1000L);
        }
        rtnStr = String.valueOf(code);
        return rtnStr;
    }

    /**
     * 获取城市code
     *
     * @return
     */
    public Long getCityCode() {
        long code = redisComponent.incr(CITY_CODE, "city_code");
        if (code < 100) {
            code = redisComponent.incr(CITY_CODE, "city_code", 100L);
        }
        return code;
    }

    /**
     * 生成商品编号
     *
     * @return
     */
    public String getProductCode() {
        String rtnStr = "";
        long code = redisComponent.incr(CODE_KEY, "product_code");
        if (code < 10000000) {
            code = redisComponent.incr(CODE_KEY, "product_code", 10000000L);
        }
        rtnStr = "P" + String.valueOf(code);
        return rtnStr;
    }
}
