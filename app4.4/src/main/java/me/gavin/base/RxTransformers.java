package me.gavin.base;

import java.io.IOException;
import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.gavin.net.Pager;
import me.gavin.net.Result;
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

    /**
     * http 结果过滤 - 根据 code
     */
    public static <T> ObservableTransformer<Result<T>, Result<T>> filterResultC() {
        return upstream -> upstream
                .map(result -> {
                    if (!result.isSuccess())
                        throw new IOException(result.getMsg());
                    return result;
                });
    }

    /**
     * http 结果过滤 -  根据 code & data
     */
    public static <T> ObservableTransformer<Result<T>, T> filterResultCD() {
        return upstream -> upstream
                .map(result -> {
                    if (!result.isSuccess() || result.getData() == null)
                        throw new IOException(result.getMsg());
                    return result.getData();
                });
    }

    /**
     * http 结果过滤 -  根据 code & data & list
     */
    public static <T> ObservableTransformer<Result<Pager<T>>, List<T>> filterResultCDP() {
        return upstream -> upstream
                .map(result -> {
                    if (!result.isSuccess() || result.getData() == null
                            || result.getData().getList() == null)
                        throw new IOException(result.getMsg());
                    return result.getData().getList();
                });
    }

}
