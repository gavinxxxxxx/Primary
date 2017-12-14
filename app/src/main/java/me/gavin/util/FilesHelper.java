package me.gavin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.gavin.inject.component.ApplicationComponent;

/**
 * 文件缓存与读取
 *
 * @author gavin.xiong 2017/10/24
 */
public final class FilesHelper {

    public static void write(String name, String content) {
        String path = getFilesDir() + File.separator + name;
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String name) {
        StringBuilder sb = new StringBuilder();
        String path = getFilesDir() + File.separator + name;
        try (InputStream is = new FileInputStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void delete(String name) {
        String path = getFilesDir() + File.separator + name;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    private static String getFilesDir() {
        return CacheHelper.getFilesDir(ApplicationComponent.Instance.get().getApplication(), null);
    }

}
