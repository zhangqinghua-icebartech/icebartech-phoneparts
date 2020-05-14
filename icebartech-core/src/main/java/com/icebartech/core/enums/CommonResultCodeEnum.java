package com.icebartech.core.enums;

public enum CommonResultCodeEnum implements ResultEnum<Integer> {
    /**
     * 执行成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 服务不可用
     */
    SERVICE_CURRENTLY_UNAVAILABLE(201, "服务不可用"),
    /**
     * 未登录或token失效
     */
    NOT_LOGIN(401, "未登录或token失效"),
    /**
     * 不支持的请求方式
     */
    MISSING_SUPPORTED_METHODS(405, "不支持的请求方式"),
    /**
     * 缺少必选参数
     */
    MISSING_REQUIRED_ARGUMENTS(4001, "缺少必选参数"),
    /**
     * 非法的参数
     */
    INVALID_ARGUMENTS(4002, "非法的参数"),
    /**
     * 没有操作权限
     */
    USER_NOT_BINDING(4031, "用户未绑定"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(5000, "未知错误"),
    /**
     * 未找到对应数据
     */
    DATA_NOT_FOUND(5001, "未找到对应数据"),
    /**
     * 数据保存失败
     */
    DATA_SAVE_FAILED(5002, "数据保存失败"),
    /**
     * 数据已存在
     */
    DATA_ALREADY_EXISTED(5003, "数据已存在"),
    /**
     * 数据有误
     */
    DATA_IS_WRONG(5004, "数据有误"),
    /**
     * 数据不可用
     */
    DATA_NOT_AVAILABLE(5005, "数据不可用"),
    /**
     * 数据不可用
     */
    CONFIGURATION_ERROR(5006, "配置错误"),
    /**
     * 无效操作
     */
    INVALID_OPERATION(5007, "无效操作"),
    /**
     * 调用次数超限
     */
    CALL_LIMITED(5010, "调用次数超限"),
    /**
     * 调用频率超限
     */
    CALL_EXCEEDS_LIMITED_FREQUENCY(5011, "调用频率超限"),
    /**
     * 系统内部接口调用异常
     */
    INTERFACE_INNER_INVOKE_ERROR(5020, "系统内部接口调用异常"),
    /**
     * 系统外部接口调用异常
     */
    INTERFACE_OUTER_INVOKE_ERROR(5021, "系统外部接口调用异常"),


    //一期
    CODE_ERROR(10007,"验证码有误"),
    EAIL_REPET(10001,"邮箱重复"),
    EAIL_NULL(10002,"邮箱为空"),
    NO_SERIAL(10003,"序列号重复或已过期"),
    LOGIN_ERROR(10004,"账户或密码错误"),
    USER_ERROR(10005,"用户失效"),
    SOLE_DATA(10006,"只能添加一条数据"),
    NUM_REPET(30001,"序列号已存在"),
    //二期
    REDEEM_CODE_NULL(20002,"兑换码不存在"),
    NUM_ERROR(10006,"使用次数不足"),
    REDEEM_CODE_ERROR(20001,"邮箱为空"),
    //四期
    NOT_AUTHORITY(403, "没有操作权限"),
    INVALID_NULL(40002, "无效操作"),
    UN_AGENT_1(40001,"代理商一级才能获取");








    private Integer code;
    private String desc;

    CommonResultCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    /**
     * 根据code值换取中文提示信息
     *
     * @param code 错误码
     * @return
     */
    public static String getDescByCode(String code) {
        return CommonResultCodeEnum.valueOf(code).getDesc();
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

}
