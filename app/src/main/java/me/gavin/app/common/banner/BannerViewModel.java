package me.gavin.app.common.banner;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.gavin.base.RxTransformers;
import me.gavin.primary.R;
import me.gavin.base.FragViewModel;
import me.gavin.base.RxBus;
import me.gavin.primary.databinding.FragBannerBinding;
import me.gavin.util.DisplayUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 轮播
 *
 * @author gavin.xiong 2017/8/14
 */
public class BannerViewModel<T> extends FragViewModel<BannerFragment, FragBannerBinding> {

    private int mPageType;

    private List<BannerModel<T>> mList = new ArrayList<>();
    public BannerAdapter adapter;
    private LinearLayoutManager layoutManager;

    private List<ImageView> bannerTipList;

    private Disposable disposable;

    BannerViewModel(Context context, BannerFragment fragment, FragBannerBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    public void afterCreate() {
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mBinding.get().recycler);
        layoutManager = (LinearLayoutManager) mBinding.get().recycler.getLayoutManager();
        adapter = new BannerAdapter<>(mContext.get(), mList);
    }

    void setPageType(int pageType) {
        this.mPageType = pageType;
        subscribeEvent();
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(BannerChangeEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(event -> mPageType == event.type)
                .subscribe(event -> {
                    mList.clear();
                    if (event.list != null) mList.addAll(event.list);
                    adapter.notifyDataSetChanged();
                    if (mList != null && !mList.isEmpty()) {
                        mBinding.get().recycler.scrollToPosition(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mList.size());
                        showTip();
                        initTimer();
                    }
                });
    }

    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (RecyclerView.SCROLL_STATE_IDLE == newState
                    && layoutManager.findFirstVisibleItemPosition() == layoutManager.findLastVisibleItemPosition()) {
                int position = layoutManager.findLastVisibleItemPosition() % mList.size();
                refreshTip(position);
                initTimer();
            }
        }
    };

    /**
     * 创建通知栏下面的页码小图标*
     */
    private void showTip() {
        mBinding.get().linear.removeAllViews();
        if (mList.size() < 2) return;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dp2px(5), DisplayUtil.dp2px(5));
        params.setMargins(5, 0, 5, 0);
        bannerTipList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            ImageView img = new ImageView(mContext.get());
            img.setLayoutParams(params);
            bannerTipList.add(img);
            mBinding.get().linear.addView(img);
            img.setImageResource(R.drawable.sn_banner_tip);
            img.setEnabled(false);
        }
        refreshTip(0);
    }

    private void refreshTip(int position) {
        if (mList.size() < 2 || position < 0) return;
        for (ImageView img : bannerTipList)
            img.setEnabled(false);
        bannerTipList.get(position).setEnabled(true);
    }

    private void initTimer() {
        Observable.timer(5, TimeUnit.SECONDS)
                .compose(RxTransformers.applySchedulers())
                .filter(arg0 -> mList != null && mList.size() > 1)
                .doOnSubscribe(disposable -> {
                    cancelTimer();
                    this.disposable = disposable;
                })
                .subscribe(aLong -> mBinding.get().recycler.smoothScrollBy(DisplayUtil.getScreenWidth(), 0));
    }

    private void cancelTimer() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    void onSupportInvisible() {
        cancelTimer();
    }

    void onSupportVisible() {
        initTimer();
    }
}
