package me.gavin.util.okhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.io.IOException;

import me.gavin.inject.component.ApplicationComponent;
import me.gavin.inject.module.ClientAPIModule;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * OkHttp3 缓存拦截器 使用{@link ClientAPIModule}
 * 配合 {@link retrofit2.http.Header}、{@link retrofit2.http.Headers}
 *
 * @author gavin.xiong 2017/4/28
 */
public final class OKHttpCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isNetworkAvailable(ApplicationComponent.Instance.get().getApplication())) {
            // 网络不可用时强制使用缓存
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        } else if (TextUtils.isEmpty(request.header("Cache-Control"))) {
            // 网络可用 && 未设置复用时间 -> 默认复用时间为 10s
            request = request.newBuilder()
                    .header("Cache-Control", "private, max-stale=10")
                    .build();
        }
        Response response = chain.proceed(request);
        if (response.code() == 504) {
            throw new IOException("网络连接不可用");
        }
        return response.newBuilder()
                .removeHeader("Pragma")
                .build();
    }

    /**
     * 判断网络是否有效
     */
    private boolean isNetworkAvailable(Context context) {
        NetworkInfo netInfo = getNetworkInfo(context);
        return netInfo != null && netInfo.isAvailable();
    }

    /**
     * 获取当前网络信息
     */
    private NetworkInfo getNetworkInfo(Context context) {
        if (null != context) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return mConnectivityManager.getActiveNetworkInfo();
        }
        return null;
    }
}
