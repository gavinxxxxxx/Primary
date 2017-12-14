package me.gavin.app.common.banner;

import java.io.Serializable;

/**
 * 轮播 model
 *
 * @author gavin.xiong 2017/5/10
 */
public class BannerModel<T> implements Serializable {

    private String url;
    private String title;
    private T src;

    public BannerModel(String url, String title, T src) {
        this.url = url;
        this.title = title;
        this.src = src;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public T get() {
        return src;
    }
}
