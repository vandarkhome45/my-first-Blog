package com.example.blog.common;

public class Result<T> {

    private Integer code; // 200成功，500失败
    private String msg;
    private T data;

    // 构造方法
    public Result() {}

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ✅ 成功
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    // ✅ 失败（核心补这个）
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }

    // getter / setter
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}