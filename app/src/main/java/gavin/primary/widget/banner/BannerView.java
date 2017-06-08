package gavin.primary.widget.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gavin.primary.databinding.WidgetBannerViewBinding;
import gavin.primary.R;
import gavin.primary.util.DisplayUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/10
 */
public class BannerView extends FrameLayout {

    private Disposable disposable;

    private WidgetBannerViewBinding binding;
    private LinearLayoutManager linearLayoutManager;
    private List<ImageView> bannerTipList;

    private List<BannerModel> modelList;

    private OnItemClickListener mListener;

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        binding = WidgetBannerViewBinding.inflate(LayoutInflater.from(context));
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(binding.recycler);
        linearLayoutManager = (LinearLayoutManager) binding.recycler.getLayoutManager();
        this.addView(binding.getRoot(), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setModelList(List<BannerModel> modelList) {
        this.modelList = modelList;
        if (modelList != null && !modelList.isEmpty()) {
            initBanner();
            showTip();
            initTimer();
        }
    }

    private void initBanner() {
        binding.recycler.setAdapter(new BannerAdapter(getContext(), modelList, this));
        binding.recycler.scrollToPosition(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % modelList.size());
        binding.recycler.removeOnScrollListener(onScrollListener);
        binding.recycler.addOnScrollListener(onScrollListener);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (RecyclerView.SCROLL_STATE_IDLE == newState
                    && linearLayoutManager.findFirstVisibleItemPosition() == linearLayoutManager.findLastVisibleItemPosition()
                    && hasWindowFocus()) {
                int position = linearLayoutManager.findLastVisibleItemPosition() % modelList.size();
                refreshTip(position);
                initTimer();
            }
        }
    };

    /**
     * 创建通知栏下面的页码小图标*
     */
    private void showTip() {
        binding.linear.removeAllViews();
        if (modelList.size() < 2) return;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtil.dp2px(5), DisplayUtil.dp2px(5));
        params.setMargins(5, 0, 5, 0);
        bannerTipList = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            ImageView img = new ImageView(getContext());
            img.setLayoutParams(params);
            bannerTipList.add(img);
            binding.linear.addView(img);
            img.setImageResource(R.drawable.sn_banner_tip);
            img.setEnabled(false);
        }
        refreshTip(0);
    }

    private void refreshTip(int position) {
        if (modelList.size() < 2 || position < 0) return;
        for (ImageView img : bannerTipList)
            img.setEnabled(false);
        bannerTipList.get(position).setEnabled(true);
    }

    private void initTimer() {
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    cancelTimer();
                    this.disposable = disposable;
                })
                .subscribe(aLong -> binding.recycler.smoothScrollBy(DisplayUtil.getScreenWidth(), 0));
    }

    private void cancelTimer() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (modelList != null && modelList.size() > 1) {
            cancelTimer();
            if (VISIBLE == visibility) {
                initTimer();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (modelList != null && modelList.size() > 1) {
            cancelTimer();
            if (hasWindowFocus) {
                initTimer();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelTimer();
    }

    public void onItemClick(BannerModel model) {
        if (mListener != null) {
            mListener.onItemClick(modelList.indexOf(model));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
