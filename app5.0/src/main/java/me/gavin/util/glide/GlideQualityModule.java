package me.gavin.util.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import me.gavin.util.CacheHelper;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * 自定义Glide模块 - 图片质量
 * -
 * - 需在 Manifest.xml 中配置
 * -    <meta-data android:name="xxx.GlideQualityModule" android:value="GlideModule" />
 * - 只能同时存在一个 Module 若要与 OkHttpGlideModule 等同时使用需合并)
 *
 * @author gavin.xiong 2016/9/14
 */
public class GlideQualityModule implements GlideModule {

    private volatile Call.Factory internalClient;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // builder.setMemoryCache(new LruResourceCache((int) Runtime.getRuntime().maxMemory() / 8)); // 内存缓存大小
        builder.setDiskCache(new DiskLruCacheFactory(CacheHelper.getCacheDir(context), "glide", 1024 * 1024 * 250)); // 磁盘缓存目录及大小
        // builder.setBitmapPool(new LruBitmapPool((int) Runtime.getRuntime().maxMemory() / 8)); // BitmapPool缓存内存大小
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565); // 图片解码格式

    }

    /**
     * {@link com.bumptech.glide.integration.okhttp3.OkHttpGlideModule}
     */
    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(getInternalClient())); // 使用 okhttp 下载图片
    }

    /**
     * OkHttp 客户端单例对象
     * todo 使用 Dagger2
     */
    private Call.Factory getInternalClient() {
        if (internalClient == null) {
            synchronized (GlideQualityModule.class) {
                if (internalClient == null) {
                    internalClient = new OkHttpClient.Builder()
//                            .addNetworkInterceptor(new OKHttpRefererNetworkInterceptor())
                            .build();
                }
            }
        }
        return internalClient;
    }
}
