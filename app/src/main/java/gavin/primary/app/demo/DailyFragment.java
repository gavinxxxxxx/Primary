package gavin.primary.app.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import gavin.primary.R;
import gavin.primary.app.main.StartFragmentEvent;
import gavin.primary.app.setting.LicenseFragment;
import gavin.primary.base.BindingFragment;
import gavin.primary.base.RxBus;
import gavin.primary.databinding.FragDailyBinding;
import gavin.primary.widget.AutoLoadRecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 知乎日报 - 列表
 * frag_daily.xml
 *
 * @author gavin.xiong 2017/4/26
 */
public class DailyFragment extends BindingFragment<FragDailyBinding>
        implements AutoLoadRecyclerView.OnLoadListener {

    private DailyViewModel mViewModel;

    public static DailyFragment newInstance() {
        return new DailyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_daily;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        getDaily(0);
    }

    @Override
    public void onLoad() {
        getDaily(binding.recycler.offset);
    }

    private void init() {
        mViewModel = new DailyViewModel(_mActivity, binding);

        binding.toolbar.setNavigationOnClickListener((v) -> {
            // TODO: 2017/6/8
        });
        binding.toolbar.setOnMenuItemClickListener(item -> {
            RxBus.get().post(new StartFragmentEvent(LicenseFragment.newInstance()));
            return true;
        });

        binding.banner.setOnItemClickListener(i -> {
            // TODO: 2017/6/8
        });

        binding.refreshLayout.setOnRefreshListener(() -> getDaily(0));
        binding.recycler.setOnLoadListener(this);
        mViewModel.adapter.setOnItemClickListener(i -> {
            // TODO: 2017/6/8
        });
    }

    private void getDaily(int dayDiff) {
        getDataLayer().getDailyService().getDaily(dayDiff)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    mViewModel.doOnSubscribe(dayDiff);
                    binding.recycler.loadData(dayDiff != 0);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(daily -> {
                    mViewModel.doOnNext(dayDiff, daily);
                    // 知乎日报的生日为 2013 年 5 月 19 日
                    binding.recycler.haveMore = !autoLoadMore(dayDiff, daily) && Integer.parseInt(daily.getDate()) > 20130519;
                })
                .doOnComplete(() -> {
                    mViewModel.doOnComplete();
                    binding.recycler.loading = false;
                })
                .doOnError(throwable -> {
                    mViewModel.doOnError();
                    binding.recycler.loading = false;
                    binding.recycler.offset--;
                })
                .doAfterNext(daily -> {
                    if (autoLoadMore(dayDiff, daily))
                        onLoad();
                })
                .subscribe(daily -> mViewModel.onNext(dayDiff, daily),
                        e -> mViewModel.onError(e, dayDiff));
    }

    /**
     * 满足什么条件时自动加载下一页
     * 解决今日热文过少时下拉刷新后上拉加载更多失效问题
     *
     * @param dayDiff dayDiff
     * @param daily   Daily
     * @return boolean
     */
    private boolean autoLoadMore(int dayDiff, Daily daily) {
        return dayDiff == 0 && daily.getStories().size() < 10;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.onDestroy();
    }
}
