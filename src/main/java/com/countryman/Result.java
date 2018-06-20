package com.countryman;

/**
 * @author countryman
 * @mail chris_jay_9111@sina.com
 * @create 2018-06-19 14:36
 * @description
 **/
public /*abstract*/ class Result<T> {

    static final String SUCCEED_CODE = "0";
    static final String FAIL_CODE = "-1";
    static final String SUCCEED_MESSAGE = "OK";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected String code;
    protected String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    protected T data;

    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result fail(String message){
        return new Result(FAIL_CODE, message, null);
    }

    public static Result fail(String code, String message){
        return new Result(code, message, null);
    }

    public static <T> Result<T> succeed(T data){
        return new Result(SUCCEED_CODE, SUCCEED_MESSAGE, data);
    }
}
