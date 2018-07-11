package me.gavin.util.okhttp;

import java.io.IOException;

import me.gavin.inject.module.ClientAPIModule;
import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * OkHttp3 缓存网络拦截器 使用{@link ClientAPIModule}
 * 配合 {@link retrofit2.http.Header}、{@link retrofit2.http.Headers}
 * 当服务器返回 Cache-Control: must-revalidate 时使用（慎用）
 *
 * @author gavin.xiong 2017/4/28
 */
public final class OKHttpCacheNetworkInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request())
                .newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .build();
    }
}
