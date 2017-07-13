package gavin.primary.app.demo;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import gavin.primary.R;
import gavin.primary.base.BindingFooterAdapter;
import gavin.primary.databinding.FooterLoadingBinding;
import gavin.primary.util.L;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 日报 ViewModel
 *
 * @author gavin.xiong 2017/5/5
 */
public class DailyViewModel extends PagingViewModel<Daily.Story, BindingFooterAdapter> {

    DailyViewModel(Context context, ViewDataBinding binding) {
        super(context, binding);
    }

    @Override
    public void init() {
        list = new ArrayList<>();
        adapter = new BindingFooterAdapter<>(mContext.get(), list, R.layout.item_daily);
        loadingBinding = FooterLoadingBinding.inflate(LayoutInflater.from(mContext.get()));
        adapter.setFooterBinding(loadingBinding);
        notifyChange();
    }

    @Override
    public void getData(boolean isMore) {
        getDataLayer().getDailyService().getDaily(isMore ? offset : 0)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    doOnSubscribe(isMore);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> doOnComplete(isMore))
                .doOnError(e -> doOnError(isMore, e))
                .doOnNext(daily -> {
                    daily.getStories().get(0).setDate(!isMore ? "今日热文" : daily.getDate());
                    // 知乎日报的生日为 2013 年 5 月 19 日
                    haveMore = !autoLoadMore(isMore, daily) && Integer.parseInt(daily.getDate()) > 20130519;
                })
                .doAfterNext(daily -> {
                    if (autoLoadMore(isMore, daily)) getData(true);
                })
                .subscribe(daily -> accept(isMore, daily.getStories()), L::e);
    }

    /**
     * 满足什么条件时自动加载下一页
     * 解决今日热文过少时下拉刷新后上拉加载更多失效问题
     *
     * @param isMore isMore
     * @param daily  Daily
     * @return boolean
     */
    private boolean autoLoadMore(boolean isMore, Daily daily) {
        return !isMore && daily.getStories().size() < 10;
    }
}
