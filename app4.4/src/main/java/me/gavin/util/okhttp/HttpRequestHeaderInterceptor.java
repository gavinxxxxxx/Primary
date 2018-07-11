package me.gavin.util.okhttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * token 拦截器
 */
public class HttpRequestHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request().newBuilder()
                .header("device_type", "1")
                .header("Authorization", getAuthorization())
                .build());
    }

    private String getAuthorization() {
        return "";
    }
}
