package com.ssm.mall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerRes<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    //构造函数的类型如何设计(最左前缀原则)
    private ServerRes(int status) {
        this.status = status;
    }
    private ServerRes(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    private ServerRes(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    //封装返回正确信息时的方法
    @JsonIgnore
    public static<T> int success(){
        return Result.RESULT_SUCCESS.getStatus();
    }
    public static<T> ServerRes<T> success(Result result) {
        return new ServerRes<T>(result.getStatus(), result.getMsg());
    }
    public static<T> ServerRes<T> success(Result result,T data){
        return new ServerRes<T>(result.getStatus(),result.getMsg(),data);
    }

    //封装返回错误信息时的方法
    public static<T> int error(){
        return Result.RESULT_ERROR.getStatus();
    }
    public static<T> ServerRes<T> error(Result result) {
        return new ServerRes<T>(result.getStatus(), result.getMsg());
    }

    //定义测试使用的toString

    @Override
    public String toString() {
        return "ServerRes{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}