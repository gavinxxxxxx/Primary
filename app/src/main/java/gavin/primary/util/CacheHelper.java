package gavin.primary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 缓存助手
 *
 * @author gavin.xiong 2017/4/28
 */
public class CacheHelper {

    /**
     * 获取缓存文件夹 ( sdCard
     * -    ? /storage/sdcard0/Android/data/com.example.dir/cache
     * -    : /data/data/com.example.dir/cache )
     *
     * @param context Context
     * @return cachePath
     */
    public static String getCacheDir(Context context) {
        String cachePath;
        // if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable())
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 获取缓存文件夹 ( sdCard
     * -    ? /storage/sdcard0/Android/data/com.example.dir/files
     * -    : /data/data/com.example.dir/files )
     *
     * @param context Context
     * @param type    传空在 files 根目录下， 传值在 files 下创建对应文件夹
     * @return cachePath
     */
    public static String getFilesDir(Context context, String type) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && context.getExternalFilesDir(type) != null) {
            cachePath = context.getExternalFilesDir(type).getPath();
        }
        return cachePath;
    }

    /**
     * 获取sd卡根目录
     *
     * @return String
     */
    private static String getAppDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 首先保存图片
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Sensual" + File.separator;
            File appDir = new File(path);
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            return path;
        } else {
            return null;
        }
    }

    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap Bitmap
     * @param name String
     */
    public static String saveBitmap(Bitmap bitmap, String name) throws Exception {
        String path = getAppDir() + name + ".jpg";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
}
