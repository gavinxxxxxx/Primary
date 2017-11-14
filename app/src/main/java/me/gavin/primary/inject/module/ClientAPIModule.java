package me.gavin.primary.inject.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gavin.primary.BuildConfig;
import me.gavin.primary.inject.component.ApplicationComponent;
import me.gavin.primary.net.ClientAPI;
import me.gavin.primary.util.CacheHelper;
import me.gavin.primary.util.okhttp.OKHttpCacheInterceptor;
import me.gavin.primary.util.okhttp.OKHttpCacheNetworkInterceptor;
import me.gavin.primary.util.okhttp.OKHttpLoggingInterceptor;
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

    private static final String BASE_URL = "http://news-at.zhihu.com/api/4/";

    /**
     * 创建一个ClientAPI的实现类单例对象
     *
     * @param client           OkHttpClient
     * @param converterFactory Converter.Factory
     * @return ClientAPI
     */
    @Singleton
    @Provides
    public ClientAPI provideClientApi(OkHttpClient client, Converter.Factory converterFactory) {
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
    public Converter.Factory provideConverter(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    /**
     * Gson 单例对象
     *
     * @return Gson
     */
    @Singleton
    @Provides
    public Gson provideGson() {
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
    public OkHttpClient provideClient(HttpLoggingInterceptor logging,
                                      OKHttpLoggingInterceptor logging2,
                                      OKHttpCacheInterceptor cacheInterceptor,
                                      OKHttpCacheNetworkInterceptor cacheNetworkInterceptor,
                                      Cache cache) {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(logging2)
                .addInterceptor(cacheInterceptor)
                .addNetworkInterceptor(cacheNetworkInterceptor)
                .cache(cache)
                .build();
    }

    /**
     * 日志拦截器单例对象,用于OkHttp层对日志进行处理
     *
     * @return HttpLoggingInterceptor
     */
    @Singleton
    @Provides
    public HttpLoggingInterceptor provideLogger() {
        return new HttpLoggingInterceptor()
                .setLevel(BuildConfig.LOG_DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    /**
     * 日志拦截器单例对象,用于OkHttp层对日志进行处理
     *
     * @return HttpLoggingInterceptor
     */
    @Singleton
    @Provides
    public OKHttpLoggingInterceptor provideOKHttpLogger() {
        return new OKHttpLoggingInterceptor();
    }

    /**
     * OKHttp 缓存拦截器
     *
     * @return OKHttpCacheInterceptor
     */
    @Singleton
    @Provides
    public OKHttpCacheInterceptor provideCacheInterceptor() {
        return new OKHttpCacheInterceptor();
    }

    /**
     * OKHttp 缓存网络拦截器
     *
     * @return OKHttpCacheNetworkInterceptor
     */
    @Singleton
    @Provides
    public OKHttpCacheNetworkInterceptor provideCacheNetworkInterceptor() {
        return new OKHttpCacheNetworkInterceptor();
    }

    /**
     * OkHttp缓存 50 MiB
     *
     * @return Cache
     */
    @Singleton
    @Provides
    public Cache provideCache() {
        return new Cache(new File(CacheHelper.getCacheDir(ApplicationComponent.Instance.get().getApplication()), "responses"), 50 * 1024 * 1024);
    }
}
