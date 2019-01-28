package io.gphotos.gin.api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetryableCallback<T> implements Callback<T> {
    private final String TAG = getClass().getSimpleName();
    private final Call<T> call;
    private int mCount = 0;
    private int mRetryCount = 3;

    public void onFinalFailure(Call<T> call, Throwable th) {
    }

    public void onFinalResponse(Call<T> call, Response<T> response) {
    }

    public void onRetry() {
    }

    public RetryableCallback(Call<T> call) {
        this.call = call;
    }

    public RetryableCallback(Call<T> call, int i) {
        this.call = call;
        this.mRetryCount = i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001b  */
    public void onResponse(retrofit2.Call<T> r4, retrofit2.Response<T> r5) {
        /*
        r3 = this;
        r0 = r5.isSuccessful();
        r1 = 1;
        if (r0 == 0) goto L_0x0018;
    L_0x0007:
        r0 = r5.body();
        r2 = r0 instanceof io.gphotos.gin.model.BaseCallResponse;
        if (r2 == 0) goto L_0x0016;
    L_0x000f:
        r0 = (io.gphotos.gin.model.BaseCallResponse) r0;
        r0 = r0.err;
        if (r0 == 0) goto L_0x0016;
    L_0x0015:
        goto L_0x0018;
    L_0x0016:
        r0 = 0;
        goto L_0x0019;
    L_0x0018:
        r0 = 1;
    L_0x0019:
        if (r0 <= 0) goto L_0x002a;
    L_0x001b:
        r0 = r3.mCount;
        r0 = r0 + r1;
        r3.mCount = r0;
        r0 = r3.mCount;
        r1 = r3.mRetryCount;
        if (r0 >= r1) goto L_0x002a;
    L_0x0026:
        r3.retryCall();
        return;
    L_0x002a:
        r3.onFinalResponse(r4, r5);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.gphotos.gin.api.RetryableCallback.onResponse(retrofit2.Call, retrofit2.Response):void");
    }

    public void onFailure(Call<T> call, Throwable th) {
        if (th.getMessage() == null || th.getMessage().contains("Server")) {
            this.mCount++;
            if (this.mCount < this.mRetryCount) {
                retryCall();
                return;
            }
        }
        onFinalFailure(call, th);
    }

    private void retryCall() {
        if (this.call != null) {
            onRetry();
            this.call.clone().enqueue(this);
        }
    }
}
