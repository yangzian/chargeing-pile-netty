package com.chargeingpile.netty.chargeingpilenetty.config;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@JsonInclude
public class ServerResponse<T> implements Serializable {


    @ApiModelProperty("返回状态码，0-成功，非0-失败")
    private int code;

    @ApiModelProperty("返回消息")
    @JSONField(serialzeFeatures = SerializerFeature.WriteNullStringAsEmpty)
    private String message;

    @ApiModelProperty("返回的数据")
    private T data;

    public ServerResponse() {
    }

    private ServerResponse(int code) {
        this.code = code;
    }

    private ServerResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ServerResponse(int code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    private ServerResponse(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    //使之不在json序列化结果当中
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return this.code == ResponseCode.SUCCESS.getCode();

    }

    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isSSOSuccess() {
        return this.code == 200;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }


    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByError(int errorCode, String errorMessage) {
        return new ServerResponse<T>(errorCode, errorMessage);
    }

    public static <T> ServerResponse<T> createError(ResponseCode code) {
        return new ServerResponse<T>(code.getCode(), code.getDesc());
    }

}
