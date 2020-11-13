package com.icebartech.core.enums;

/**
 * 平台常用的枚举值
 *
 * @author Administrator
 */
public class BaseEnums {

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        DO_NOT_PAY(OrderStatusParent.PAY, "无需支付"),
        WAIT_PAY(OrderStatusParent.WAIT_PAY, "未支付"),
        PAY(OrderStatusParent.PAY, "已支付"),
        CANCEL_USER(OrderStatusParent.CANCEL, "用户主动取消"),
        CANCEL_ADMIN(OrderStatusParent.CANCEL, "后台取消"),
        CANCEL_TIMEOUT(OrderStatusParent.CANCEL, "超时取消");
        public OrderStatusParent parent;
        public String text;

        OrderStatus(OrderStatusParent parent, String text) {
            this.parent = parent;
            this.text = text;
        }
    }

    public enum OrderStatusParent {
        WAIT_PAY("未支付"),
        PAY("已支付"),
        CANCEL("已取消");
        public String text;

        OrderStatusParent(String text) {
            this.text = text;
        }
    }

    public enum OrderRefundStatus {
        NO_REFUND("无需退款"),
        WAIT_REFUND("待退款"),
        REFUND_SUCCESS("已退款"),
        AUTO_REFUND_FAIL("自动退款失败"),
        WAIT_MANUAL_REFUND("待手动退款"),
        MANUAL_REFUND("手动退款成功");
        public String text;

        OrderRefundStatus(String text) {
            this.text = text;
        }
    }


    public enum OperateType {
        IN("收入"),
        OUT("支出");
        public String text;

        OperateType(String text) {
            this.text = text;
        }
    }


}