package com.ssm.mall.util;

import java.math.BigDecimal;

public class BigDecimalUtil {
    public static BigDecimal add(BigDecimal x,BigDecimal y){
        return x.add(y);
    }
    public static BigDecimal add(Double x,Double y){
        return new BigDecimal(x.toString()).add(new BigDecimal(y.toString()));
    }
    public static BigDecimal sub(BigDecimal x,BigDecimal y){
        return x.subtract(y);
    }
    public static  BigDecimal sub(Double x,Double y){
        return new BigDecimal(x.toString()).subtract(new BigDecimal(y.toString()));
    }
    public static  BigDecimal mul(BigDecimal x,BigDecimal y){
        return x.multiply(y);
    }
    public static  BigDecimal mul(Double x,Double y){
        return new BigDecimal(x.toString()).multiply(new BigDecimal(y.toString()));
    }
    public static  BigDecimal div(BigDecimal x,BigDecimal y){
        return x.divide(y);
    }
    public static  BigDecimal div(Double x,Double y){
        return new BigDecimal(x.toString()).divide(new BigDecimal(y.toString()));
    }
}
