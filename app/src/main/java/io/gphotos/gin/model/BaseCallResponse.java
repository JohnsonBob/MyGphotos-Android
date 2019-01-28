package io.gphotos.gin.model;

public class BaseCallResponse<T> {
    public int err;
    public String msg;
    public T res;
}
