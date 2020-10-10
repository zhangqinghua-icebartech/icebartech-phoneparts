package com.icebartech.core.constants;


/**
 * 微信公众平台常量类
 *
 * @author Administrator
 */
public class WeiXinConstant {

    public static final String WEIPAI_DOMAIN_URL = "https://hf.icebartech.com";

    public static final String TRADE_TYPE_JSAPI = "JSAPI";//微信支付jsapi支付方式

    public static final String PAY_CALLBACK = WEIPAI_DOMAIN_URL + "/weixin/pay/weixinPayNotify";

    //微信公众号网页授权回调地址
    public static final String OAUTH2_WEB_CALLBACK = "http://dingfanhe.icebartech.com/weixin/weiXinLogin";

    //获取access_token请求地址
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%1$s&secret=%2$s";

    public static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%1$s&type=jsapi";

    //上传媒体到微信的请求地址
    public static final String UPLOAD_FILE_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload";

    //创建微信服务号自定义菜单的请求地址
    public static final String MENU_CREATE_URL = " https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%1$s";

    //获取微信服务号菜单json结构的请求地址
    public static final String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/is?access_token=%1$s";

    //删除微信服务号菜单json结构的请求地址
    public static final String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%1$s";

    //code 换取 session_key
    public static final String JSCODE_2_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=%1$s&secret=%2$s&js_code=%3$s&grant_type=authorization_code";

    //微信授权页面的请求地址
    public static final String OAUTH2_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%1$s&redirect_uri=%2$s&response_type=code&scope=snsapi_userinfo&state=%3$s#wechat_redirect";

    //微信授权获取access_token的请求地址
    public static final String OAUTH2_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%1$s&secret=%2$s&code=%3$s&grant_type=authorization_code";

    //摘取微信用户基本信息的请求地址
    public static final String USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%1$s&openid=%2$s";

    //获取微信用户基本信息的请求地址（服务端调用时用）
    public static final String USER_INFO_CGI = "https://api.weixin.qq.com/cgi-bin/msg/info?access_token=%1$s&openid=%2$s&lang=zh_CN";

    //调用客户接口向微信用户发消息的请求地址
    public static final String CUSTOMER_SEND = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%1$s";

    //微信支付统一支付接口，调用成功后返回prepay_id, jsapi调用时要用到该参数
    public static final String WEIXIN_PAY_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //微信支付申请退款接口
    public static final String WEIXIN_PAY_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    //微信支付关闭订单
    public static final String WEIXIN_PAY_CLOSE_ORDER = "https://api.mch.weixin.qq.com/pay/closeorder";

    //微信支付订单查询接口，主要用于支付成功后回调时用于订单的二次确认
    public static final String WEIXIN_PAY_ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";

    //获取临时媒体文件接口地址
    public static final String MEDIA_GET_URL = "https://api.weixin.qq.com/cgi-bin/media/is?access_token=%1$s&media_id=%2$s";

    //获取页面小程序码
    public static final String GET_WX_ACODE = "https://api.weixin.qq.com/wxa/getwxacode?access_token=%1$s";

    //小程序发送模板消息接口地址
    public static final String MESSAGE_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=%1$s";

    //微信公众号发送模板消息接口地址
    public static final String MP_MESSAGE_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%1$s";

    //获取关注用户列表
    public static final String USER_GET = "https://api.weixin.qq.com/cgi-bin/msg/is?access_token=%1$s&next_openid=%2$s";

    //批量获取关注用户列表
    public static final String BATCH_USER_GET = "https://api.weixin.qq.com/cgi-bin/msg/info/batchget?access_token=%1$s";

    //微信支付企业传到到用户微信余额
    public static final String WEIXIN_TRANSFER_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    //获取RSA公钥API
    public static final String WEIXIN_GET_PUBLICkEY = "https://fraud.mch.weixin.qq.com/risk/getpublickey";

    //微信企业转账url
    public static final String WEIXIN_PAY_BANK_URL = "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank";

    //默认编码
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String UTF8_CHARSET = "UTF-8";

    public static final String FILE_UPLOAD = "fileUpload/";

    public static final String CDATA_FORMAT = "<![CDATA[%1$s]]>";

    public static final String ACCESS_TOKEN_PARAM = "access_token";

    public static final String JSAPI_TICKET_PARAM = "ticket";

    public static final String EXPIRES_IN_PARAM = "expires_in";

    public static final String ERROR_CODE_PARAM = "errcode";

    public static final String ERROR_MGS_PARAM = "errmsg";

    public static final String IMAGE_TYPE = "image";//图片类型值

    public static final String VOICE_TYPE = "voice";//声音类型值

    public static final String VIDEO_TYPE = "video";//视频类型值

    public static final String THUMB_TYPE = "thumb";//缩略图类型值

    public static final String MEDIA_PARAM = "media";//media参数值

    public static final String TEXT_TYPE = "text";//文件类型值

    public static final String MUSIC_TYPE = "music";//音乐类型值

    public static final String NEWS_TYPE = "news";//图文类型值

    public static final String EVENT_TYPE = "event";//事件推送类型值

    public static final String LOCATION_TYPE = "location";//地理位置消息类型值

    public static final String LINK_TYPE = "link";//链接消息类型值

    public static final String MEDIA_ID_PARAM = "media_id";//media_id参数值

    public static final String TYPE_PARAM = "type";//类型值

    public static final String MSG_TYPE_PARAM = "MsgType";//xml里面的msgType节点名称

    public static final String TO_USER_NAME_PARAM = "ToUserName";//xml里面的ToUserName节点名称

    public static final String FROM_USER_NAME_PARAM = "FromUserName";//xml里面的FromUserName节点名称

    public static final String CREATE_TIME_PARAM = "CreateTime";//xml里面CreateTime节点名称

    public static final String EVENT_PARAM = "Event";//xml里面Event节点名称

    public static final String EVENT_KEY_PARAM = "EventKey";//xml里面的EventKey节点名称

    public static final String SUBSCRIBE_EVENT = "subscribe";//订阅事件名称

    public static final String UN_SUBSCRIBE_EVENT = "unsubscribe";//取消订阅事件名称

    public static final String CLICK_EVENT = "CLICK";//点击自定义菜单事件

    public static final String VIEW_EVENT = "VIEW";//点击页面跳转自定义菜单事件

    public static final String LOCATION_EVENT = "LOCATION";//上报地理位置事件

    public static final String SUCCESS_CODE = "0";//成功返回码

    public static final String HELP_MODE_EVENT_KEY = "";//客服模式的事件kwy

    public static final String GOOD_CONTENT = "您的消息我们已收到，感谢您的支持~，我们将尽快回复您的消息！";

    public static final String ERROR_CONTENT = "获取数据发生错误，请稍后重试";

    public static final String SUBSCRIBE_WELCOME = "感谢您的关注！";

    //事件key定义
    public static final String WEIXIN_V001_ORDER_CENTER = "V001_ORDER_CENTER";

    public static final String WEIXIN_V001_MY_ACCOUNT = "V001_MY_ACCOUNT";

    public static final String WEIXIN_V002_RECOMMEND_FOOD = "V002_RECOMMEND_FOOD";

    public static final String WEIXIN_V002_RESTAURANT = "V002_RESTAURANT";

    public static final String WEIXIN_V002_EATING_PEOPLE = "V002_EATING_PEOPLE";

    public static final String WEIXIN_V002_VOICE_FOOD_ORDER = "V002_VOICE_FOOD_ORDER";

    public static final String WEIXIN_V002_SNACK_SHOP = "V002_SNACK_SHOP";

    public static final String WEIXIN_V003_OPINION = "V003_OPINION";

    public static final String CONDITION_BEGIN = "begin";

    public static final String CONDITION_CONTAINS = "contains";

    public static final String CONDITION_LAST = "last";

    public static final String CONDITION_EQUAL = "equal";

    public static final String MEDIA_ID_XML_NODE_VALUE = "MediaId";
    public static final String FORMAT_XML_NODE_VALUE = "Format";
    public static final String RECOGNITION_XML_NODE_VALUE = "Recognition";
    public static final String MSG_ID_XML_NODE_VALUE = "MsgId";

    public static final String PIC_URL_XML_NODE_VALUE = "PicUrl";

    public static final String THUMB_MEDIA_ID_XML_NODE_VALUE = "ThumbMediaId";

    public static final String LOCATION_X_XML_NODE_VALUE = "Location_X";
    public static final String LOCATION_Y_XML_NODE_VALUE = "Location_Y";
    public static final String SCALE_XML_NODE_VALUE = "Scale";
    public static final String LABEL_XML_NODE_VALUE = "Label";

    public static final String LOCATION_LATITUDE = "Latitude";
    public static final String LOCATION_LONGITUDE = "Longitude";
    public static final String LOCATION_PRECISION = "Precision";

    public static final String TITLE_XML_NODE_VALUE = "Title";
    public static final String DESCRIPTION_XML_NODE_VALUE = "Description";
    public static final String URL_XML_NODE_VALUE = "Url";

    public final static int SESSION_EXPIRES = 24 * 30 * 60;//如果没有操作24小时，就判定为session过期

}
