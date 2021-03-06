package me.gavin.base;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.gavin.util.L;

/**
 * RxTransformers
 *
 * @author gavin.xiong 2018/2/4.
 */
public class RxTransformers {

    /**
     * 线程调度
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 打印数据
     */
    public static <T> ObservableTransformer<T, T> log() {
        return upstream -> upstream.map(L::d);
    }

}
