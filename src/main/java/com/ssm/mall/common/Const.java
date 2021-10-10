package com.ssm.mall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public interface Const {
    String CURRENT_USER = "current_user";
    String TOKEN_PREFIX = "token_";//设定token令牌前缀
    String STANDARD_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //确定验证类型是用户名，还是email
    interface ValidType {
        String USERNAME = "username";
        String EMAIL = "email";
    }

    //确定用户角色是否是管理员，默认为1-USER
    interface Role {
        Integer ADMIN = 0;
        Integer USER = 1;
    }

    interface Product {
        Integer ON_SALE = 1;
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    interface Cart {
        Integer CHECKED = 1;//选中
        Integer UNCHECKED = 0;//未选中
        String QUANTITY_OUT_OF_STOCK = "购买数量超出库存数量";
        String QUANTITY_SUCCESS = "库存充足";
    }

    //订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
    enum OrderStatus {
        ORDER_CANCLE(0, "已取消"),
        ORDER_NO_PAY(10, "未付款"),
        ORDER_ALREADY_PAY(20, "已付款"),
        ORDER_ALREADY_SEND(40, "已发货"),
        ORDER_TRADE_SUCCESS(50, "交易成功"),
        ORDER_TRADE_CLOSED(60, "交易关闭");
        private Integer code;
        private String msg;

        OrderStatus(java.lang.Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public java.lang.Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
        public static String getMsgByCode(Integer code){
            for(OrderStatus os:values()){
                if( code == os.getCode()){
                    return os.getMsg();
                }
            }
            throw new RuntimeException("没有该枚举类型");
        }
    }

    enum PaymentType {
        ON_LINE(1, "在线支付");
        private Integer code;
        private String msg;

        PaymentType(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public static String getMsgByCode(Integer code){
            for(PaymentType pt:values()){
                if( code == pt.getCode()){
                    return pt.getMsg();
                }
            }
            throw new RuntimeException("没有该枚举类型");
        }
    }
}
