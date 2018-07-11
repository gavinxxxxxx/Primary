package me.gavin.inject.module;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import dagger.Module;
import dagger.Provides;
import me.gavin.base.App;
import me.gavin.net.ClientAPI;
import me.gavin.BuildConfig;
import me.gavin.util.CacheHelper;
import me.gavin.util.okhttp.HttpRequestHeaderInterceptor;
import me.gavin.util.okhttp.OKHttpCacheInterceptor;
import me.gavin.util.okhttp.OKHttpCacheNetworkInterceptor;
import me.gavin.util.okhttp.OKHttpLoggingInterceptor;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ClientAPIModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class ClientAPIModule {

    private static final String BASE_URL = "http://test.chainfor.com:8099/";
    // private static final String BASE_URL = "https://testmobile.chainfor.com:8061";
    // private static final String BASE_URL = "https://mobile.chainfor.com:8061";

    /**
     * 创建一个ClientAPI的实现类单例对象
     *
     * @param client           OkHttpClient
     * @param converterFactory Converter.Factory
     * @return ClientAPI
     */
    @Singleton
    @Provides
    ClientAPI provideClientApi(OkHttpClient client, Converter.Factory converterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ClientAPI.class);
    }

    /**
     * Gson 转换器单例对象
     *
     * @param gson Gson
     * @return Converter.Factory
     */
    @Singleton
    @Provides
    Converter.Factory provideConverter(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    /**
     * Gson 单例对象
     *
     * @return Gson
     */
    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
//                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
//                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
//                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
//                .setPrettyPrinting() //对json结果格式化.
//                .setVersion(1.0)
//                .disableHtmlEscaping()//默认是GSON把HTML 转义的，但也可以设置不转义
//                .serializeNulls()//把null值也转换，默认是不转换null值的，可以选择也转换,为空时输出为{a:null}，而不是{}
                .create();
    }

    /**
     * OkHttp 客户端单例对象
     *
     * @param logging HttpLoggingInterceptor
     * @param cache   Cache
     * @return OkHttpClient
     */
    @Singleton
    @Provides
    OkHttpClient provideClient(HttpLoggingInterceptor logging,
                               OKHttpLoggingInterceptor logging2,
                               OKHttpCacheInterceptor cacheInterceptor,
                               OKHttpCacheNetworkInterceptor cacheNetworkInterceptor,
                               Cache cache) {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpRequestHeaderInterceptor())
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheNetworkInterceptor)
                .addInterceptor(logging)
                // .addInterceptor(logging2)
                // .sslSocketFactory(setCertificates(getIs()))
                .cache(cache)
                .build();
    }

    public static SSLSocketFactory setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream getIs() {
        try {
            InputStream is = App.get().getAssets().open("chainfor.cer");
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日志拦截器单例对象,用于OkHttp层对日志进行处理
     *
     * @return HttpLoggingInterceptor
     */
    @Singleton
    @Provides
    HttpLoggingInterceptor provideLogger() {
        return new HttpLoggingInterceptor().setLevel(BuildConfig.LOG_DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);
    }

    /**
     * 日志拦截器单例对象,用于OkHttp层对日志进行处理
     *
     * @return HttpLoggingInterceptor
     */
    @Singleton
    @Provides
    OKHttpLoggingInterceptor provideOKHttpLogger() {
        return new OKHttpLoggingInterceptor();
    }

    /**
     * OKHttp 缓存拦截器
     *
     * @return OKHttpCacheInterceptor
     */
    @Singleton
    @Provides
    OKHttpCacheInterceptor provideCacheInterceptor() {
        return new OKHttpCacheInterceptor();
    }

    /**
     * OKHttp 缓存网络拦截器
     *
     * @return OKHttpCacheNetworkInterceptor
     */
    @Singleton
    @Provides
    OKHttpCacheNetworkInterceptor provideCacheNetworkInterceptor() {
        return new OKHttpCacheNetworkInterceptor();
    }

    /**
     * OkHttp缓存 50 MiB
     *
     * @return Cache
     */
    @Singleton
    @Provides
    Cache provideCache(Application application) {
        return new Cache(new File(CacheHelper.getCacheDir(application), "responses"), 50 * 1024 * 1024);
    }
}
