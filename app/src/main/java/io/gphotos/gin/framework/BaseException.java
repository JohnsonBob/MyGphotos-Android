package io.gphotos.gin.framework;

public class BaseException extends Exception {
    public BaseException(String str) {
        super(str);
    }

    public BaseException(Throwable th) {
        super(th);
    }

    public BaseException(String str, Throwable th) {
        super(str, th);
    }
}
