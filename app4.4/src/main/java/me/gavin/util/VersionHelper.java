package me.gavin.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

import me.gavin.BuildConfig;

/**
 * 版本升级助手
 *
 * @author gavin.xiong
 */
public class VersionHelper {

    private static long downloadID;

    /**
     * 获取当前app版本号
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return -1;
    }

    /**
     * 获取版本号
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        } else {
            return "";
        }
    }

    /**
     * 获取应用信息
     *
     * @return 当前应用的 PackageInfo
     */
    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 下载APK
     *
     * @param context  context
     * @param filePath 下载路径
     * @param fileName 下载至本地的文件名
     */
    public static void downloadApk(Context context, String filePath, String fileName) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(filePath));
            //设置允许使用的网络类型，这里是移动网络和wifi都可以
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
            // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setTitle(fileName);
            request.setDescription(filePath);
            /*
             * 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错,
             * 下载后的文件在/mnt/sdcard/Android/data/packageName/files目录下面
             * 不设置，下载后的文件在/cache这个目录下面
             */
            request.setDestinationInExternalFilesDir(context, null, fileName);

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            registerReceiver(context);
            // 把id保存好，在接收者里面要用，最好保存在Preferences里面
            downloadID = downloadManager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(AppContext.getApplication(), "版本更新失败", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 注册（下载完成）广播监听器
     *
     * @param context context
     */
    private static void registerReceiver(Context context) {
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(completeReceiver, dynamic_filter);
    }

    /**
     * 注销广播监听器
     *
     * @param context context
     */
    private static void unRegisterReceiver(Context context) {
        context.unregisterReceiver(completeReceiver);
    }

    /**
     * 安装apk
     *
     * @param context context
     * @param apkFile apkUrl
     */
    private static void installAPK(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 下载完成监听器
     */
    private static BroadcastReceiver completeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())
                    && downloadID == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {
                L.v("VersionHelper", "下载完成。。。");
                unRegisterReceiver(context);

                DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
                myDownloadQuery.setFilterById(downloadID);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor myDownload = downloadManager.query(myDownloadQuery);

                if (myDownload.moveToFirst()) {
                    int fileUriIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    String fileUri = myDownload.getString(fileUriIdx);
                    installAPK(context, new File(fileUri.replace("file://", "")));
                }
                myDownload.close();
            }
        }
    };

}
