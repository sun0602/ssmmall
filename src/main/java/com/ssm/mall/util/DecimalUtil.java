package com.ssm.mall.util;

import java.math.BigDecimal;

public class DecimalUtil {
    public static BigDecimal add(double x,double y){
        BigDecimal bx = new BigDecimal(Double.valueOf(x));
        BigDecimal by = new BigDecimal(Double.valueOf(y));
        return bx.add(by);
    }
    public static BigDecimal sub(double x,double y){
        BigDecimal bx = new BigDecimal(Double.valueOf(x));
        BigDecimal by = new BigDecimal(Double.valueOf(y));
        return bx.subtract(by);
    }
    public static BigDecimal mul(double x,double y){
        BigDecimal bx = new BigDecimal(Double.valueOf(x));
        BigDecimal by = new BigDecimal(Double.valueOf(y));
        return bx.multiply(by);
    }
    public static BigDecimal div(double x,double y){
        BigDecimal bx = new BigDecimal(Double.valueOf(x));
        BigDecimal by = new BigDecimal(Double.valueOf(y));
        return bx.divide(by);
    }
    public static BigDecimal divRoundUp(double x,double y){
        BigDecimal bx = new BigDecimal(Double.valueOf(x));
        BigDecimal by = new BigDecimal(Double.valueOf(y));
        //除法，四舍五入，保留2位小数
        return bx.divide(by,2,BigDecimal.ROUND_UP);
    }
}
