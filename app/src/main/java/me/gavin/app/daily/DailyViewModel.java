package me.gavin.app.daily;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.gavin.R;
import me.gavin.app.common.banner.BannerChangeEvent;
import me.gavin.app.common.banner.BannerModel;
import me.gavin.app.main.StartFragmentEvent;
import me.gavin.base.BaseFragment;
import me.gavin.base.RxBus;
import me.gavin.base.recycler.BindingHFAdapter;
import me.gavin.base.recycler.PagingViewModel;
import me.gavin.util.L;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 知乎日报列表
 *
 * @author gavin.xiong 2017/8/11
 */
public class DailyViewModel extends PagingViewModel<Daily.Story, BindingHFAdapter<Daily.Story>> {

    private int mBannerType;

    DailyViewModel(Context context, BaseFragment fragment, ViewDataBinding binding, int type) {
        super(context, fragment, binding);
        this.mBannerType = type;
    }

    @Override
    protected void initAdapter() {
        adapter = new BindingHFAdapter<>(mContext.get(), mList, R.layout.item_daily);
        adapter.setOnItemClickListener(i ->
                RxBus.get().post(new StartFragmentEvent(NewsFragment.newInstance(mList.get(i).getId()))));
    }

    @Override
    protected void getData(boolean isMore) {
        getDataLayer().getDailyService().getDaily(isMore ? pagingOffset : 0)
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
                    pagingHaveMore = !autoLoadMore(isMore, daily) && Integer.parseInt(daily.getDate()) > 20130519;
                    // 轮播
                    if (!isMore) initBanner(daily.getTopStories());
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

    private void initBanner(List<Daily.Story> storyList) {
        Observable.fromIterable(storyList)
                .doOnSubscribe(mCompositeDisposable::add)
                .map(story -> new BannerModel<>(story.getImageUrl(), story.getTitle(), story))
                .toList()
                .subscribe(list -> RxBus.get().post(new BannerChangeEvent<>(mBannerType, list)));
    }
}
