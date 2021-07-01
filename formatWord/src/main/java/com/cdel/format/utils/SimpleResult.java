package com.cdel.format.utils;

public class SimpleResult<T> {
    private boolean success;
    private T Data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public static <T> SimpleResult<T> success(T data) {
        SimpleResult<T> result = new SimpleResult<>();
        result.setData(data);
        result.setSuccess(true);
        return result;
    }
}
