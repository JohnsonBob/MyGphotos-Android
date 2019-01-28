package io.gphotos.gin.event;

public class ReqEvent {
    public int err;
    public String msg;

    public ReqEvent(int i, String str) {
        this.err = i;
        this.msg = str;
    }

    public boolean notLogin() {
        return this.err == 1100013;
    }
}
