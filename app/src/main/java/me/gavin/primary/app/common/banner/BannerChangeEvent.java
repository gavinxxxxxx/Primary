package me.gavin.primary.app.common.banner;

import java.util.List;

/**
 * 轮播数据改变事件
 *
 * @author gavin.xiong 2017/8/14
 */
public class BannerChangeEvent<T> {

    public int type;
    public List<BannerModel<T>> list;

    public BannerChangeEvent(int type, List<BannerModel<T>> list) {
        this.type = type;
        this.list = list;
    }
}
