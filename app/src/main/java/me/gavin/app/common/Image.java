package me.gavin.app.common;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import java.util.concurrent.ExecutionException;

import me.gavin.util.ImageLoader;

public class Image {

    private String id;
    private String time;
    private String url;

    private int width;
    private int height;
    private boolean error;

    public boolean haveMore = true;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isError() {
        return error;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public static Image newImage(Fragment fragment, String url) {
        Image image = new Image();
        Bitmap bm = getBitmap(fragment, url);
        if (bm == null) {
            url = url.replace(url.substring(url.lastIndexOf(".") + 1), "jpeg");
            bm = getBitmap(fragment, url);
        }
        if (bm == null) {
            url = url.replace(url.substring(url.lastIndexOf(".") + 1), "png");
            bm = getBitmap(fragment, url);
        }
        if (bm != null) {
            image.setWidth(bm.getWidth());
            image.setHeight(bm.getHeight());
        } else {
            image.error = true;
            image.setWidth(500);
            image.setHeight(500);
        }
        image.url = url;
        return image;
    }

    private static Bitmap getBitmap(Fragment fragment, String url) {
        try {
            return ImageLoader.getBitmap(fragment, url);
        } catch (InterruptedException | ExecutionException e) {
            return null;
        } catch (Exception e1) {
            return null;
        }
    }
}
