package me.gavin.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }

    /**
     * 获取sd卡根目录
     *
     * @return String
     */
    public static String getAppDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 首先保存图片
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Primary" + File.separator;
            File appDir = new File(path);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            return path;
        } else {
            return null;
        }
    }

    /**
     * 循环压缩并保存 bitmap
     *
     * @param bitmap    bitmap
     * @param name      文件名
     * @param maxLength 最大长度
     * @return 文件路径
     */
    private static String saveBitmap(Bitmap bitmap, String name, long maxLength) throws Exception {
        String path = getAppDir() + name + ".jpg";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 85;
            while (maxLength > 0 && baos.toByteArray().length > maxLength && options > 1) {
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 10;
            }
            fos = new FileOutputStream(path);
            baos.writeTo(fos);
            fos.flush();
            bitmap.recycle();
            return path;
        } finally {
            baos.close();
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static String saveImageStream(InputStream is, String name) throws Exception {
        String path = null;
        OutputStream os = null;
        try {
            byte[] buffer = new byte[1024 * 4];
            int read;
            boolean isHead = true;
            while ((read = is.read(buffer)) != -1) {
                if (isHead) {
                    isHead = false;
                    String type = getImageTypeByHead(buffer);
                    if (type == null) {
                        throw new IOException("图片格式错误");
                    }
                    path = CacheHelper.getAppDir() + name + "." + type;
                    File file = new File(path);
                    if (file.getParentFile() != null && !file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    os = new FileOutputStream(file);
                }
                os.write(buffer, 0, read);
            }
            if (os != null) {
                os.flush();
            }
            return path;
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    public static Uri file2Uri(Context context, File imageFile) {
        String path = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 通知相册更新
     */
    public static void updateAlbum(Context context, String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.parse("file://" + path));
        context.sendBroadcast(intent);
    }

    /**
     * 根据文件头获取图片类型
     */
    private static String getImageTypeByHead(byte[] bytes) {
        // TODO: 2017/10/9 svg
        switch (bytes[0] & 0xFF) {
            case 0xFF:
                return "jpeg";
            case 0x89:
                return "png";
            case 0x47:
                return "gif";
            case 0x42:
                return "bmp";
            case 0x49:
            case 0x4D:
                return "tiff";
            case 0x52:
                if (bytes.length < 12) {
                    return null;
                }
                byte[] bs = new byte[12];
                System.arraycopy(bytes, 0, bs, 0, 12);
                String s = new String(bs);
                if (s.startsWith("RIFF") && s.endsWith("WEBP")) {
                    return "webp";
                }
                return null;
            default:
                return null;
        }
    }
}
