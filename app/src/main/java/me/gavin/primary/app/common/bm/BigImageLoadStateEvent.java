package me.gavin.primary.app.common.bm;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * 大图页加载更多状态更新事件
 *
 * @author gavin.xiong 2017/5/22
 */
public class BigImageLoadStateEvent<T> {

    final int requestCode;
    final Disposable disposable;
    final Throwable throwable;
    final T t;
    final List<T> ts;
    final Boolean haveMore;

    /**
     * 大图页加载更多状态更新事件
     *
     * @param requestCode 页面类型 此处用来源 Fragment 的 hashCode 作判断 每次必传
     * @param disposable  订阅时的 Disposable
     * @param throwable   出错时的 Throwable
     * @param t           成功时的结果 - 分条
     * @param ts          成功时的结果 - 整页
     * @param haveMore    有无更多判断
     */
    public BigImageLoadStateEvent(int requestCode, Disposable disposable, Throwable throwable, T t, List<T> ts, Boolean haveMore) {
        this.requestCode = requestCode;
        this.disposable = disposable;
        this.throwable = throwable;
        this.t = t;
        this.ts = ts;
        this.haveMore = haveMore;
    }

}
