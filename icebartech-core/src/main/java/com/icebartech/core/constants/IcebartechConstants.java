package com.icebartech.core.constants;

/**
 * 常量类
 *
 * @author wenhsh
 */
public class IcebartechConstants {

    /**
     * 结果码: 成功
     */
    public static final int RESULT_OK = 200;
    /**
     * 结果码: 异常
     */
    public static final int RESULT_ERROR = -11;
    /**
     * 结果码: 异常
     */
    public static final int RESULT_NOT_LOGIN = 401;
    /**
     * 结果码: 异常
     */
    public static final int RESULT_NOT_AUTHORITY = 403;
    /**
     * 结果码码：服务异常
     **/
    public static final int SERVICE_ERROR = 500;

    public static final String RESULT_STATUS_SUCCESS = "success";

    public static final String RESULT_STATUS_FAILED = "fail";

    public final static String ACCESS_TOKEN_GROUP = "accessToken";//微信公众号accessToken缓存group值

    public final static String JSAPI_TICKET_GROUP = "jsapiTicket";//微信公众号jsapi_ticket缓存group值

    public final static String WEIXIN_USER_AGENT = "MicroMessenger";//微信请求过来的头信息

    public final static String VERIFY_CODE_GROUP_KEY = "verifyCode";

    public final static String VERIFY_CODE_COUNT_GROUP_KEY = "verifyCodeCount";

    public final static String USER_SESSION_GROUP_KEY = "UserSession";

    public final static String SESSIONID_SET_GROUP_KEY = "SessionIdSet";

    public final static String SYS_ROLE = "sysRole";

    public final static String SYS_ROLE_MENU = "sysRoleMenu";

    public final static String SYS_ROLE_PERMISSION = "sysRolePermission";

    public final static String SYS_PERMISSION_IDS = "sysPermissionIds";

    public final static String ORDER_ID_KEY = "OrderId";

    public final static String SYSTEM_DISABLE = "system_disable";

    public final static String SYSTEM_VERSION = "version";

}
