package gavin.primary.app.demo;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import gavin.primary.R;
import gavin.primary.base.BindingFooterAdapter;
import gavin.primary.base.BindingViewModel;
import gavin.primary.databinding.FooterLoadingBinding;
import gavin.primary.databinding.FragDailyBinding;
import gavin.primary.widget.banner.BannerModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 日报 ViewModel
 *
 * @author gavin.xiong 2017/5/5
 */
public class DailyViewModel extends BindingViewModel<FragDailyBinding> {

    private WeakReference<Context> mContext;

    List<Daily.Story> topStoryList;
    List<Daily.Story> storyList = new ArrayList<>();
    BindingFooterAdapter adapter;
    private FooterLoadingBinding loadingBinding;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    DailyViewModel(Context context, FragDailyBinding binding) {
        super(binding);
        this.mContext = new WeakReference<>(context);
        init();
    }

    private void init() {
        binding.setViewModel(this);
        binding.toolbar.setTitle("知乎日报");
        binding.toolbar.setNavigationIcon(R.drawable.vt_menu_24dp);
        binding.toolbar.inflateMenu(R.menu.action_more);

        binding.refreshLayout.setColorSchemeResources(R.color.colorVector);

        adapter = new BindingFooterAdapter<>(mContext.get(), storyList, R.layout.item_daily);
        binding.recycler.setAdapter(adapter);
        loadingBinding = FooterLoadingBinding.inflate(LayoutInflater.from(mContext.get()));
        adapter.setFooterBinding(loadingBinding);
    }

    void doOnSubscribe(int dayDiff) {
        if (dayDiff == 0) {
            binding.refreshLayout.setRefreshing(true);
        } else {
            loadingBinding.root.setVisibility(View.VISIBLE);
            loadingBinding.progressBar.setVisibility(View.VISIBLE);
            loadingBinding.textView.setText("加载中...");
        }
    }

    void doOnNext(int dayDiff, Daily daily) {
        daily.getStories().get(0).setDate(dayDiff == 0 ? "今日热文" : daily.getDate());
        if (dayDiff == 0) {
            topStoryList = daily.getTopStories();
            initBanner();
        }
    }

    void doOnComplete() {
        binding.refreshLayout.setRefreshing(false);
        loadingBinding.progressBar.setVisibility(View.GONE);
        loadingBinding.textView.setText(binding.recycler.haveMore ? "发呆中..." : "再也没有了...");
    }

    void doOnError() {
        binding.refreshLayout.setRefreshing(false);
    }

    void onNext(int dayDiff, Daily daily) {
        if (dayDiff == 0 && storyList.isEmpty()) {
            storyList.addAll(daily.getStories());
            adapter.notifyDataSetChanged();
            return;
        }
        List<Daily.Story> newList = new ArrayList<>();
        if (dayDiff != 0) newList.addAll(storyList);
        newList.addAll(daily.getStories());
        Observable.just(newList)
                .map(stories -> DiffUtil.calculateDiff(new DiffCallback(storyList, stories)))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(compositeDisposable::add)
                // 使用 DiffUtil 刷新数据时 adapter 数据列表在 dispatchUpdatesTo 后更新有可能会报 IndexOutOfBoundsException
                // 将 adapter 更新数据放在 dispatchUpdatesTo 前面，待跟进
                .doOnNext(diffResult -> {
                    if (dayDiff == 0) storyList.clear();
                    storyList.addAll(daily.getStories());
                })
                .subscribe(diffResult -> diffResult.dispatchUpdatesTo(adapter));
    }

    void onError(Throwable e, int dayDiff) {
        if (dayDiff != 0) {
            loadingBinding.progressBar.setVisibility(View.GONE);
            loadingBinding.textView.setText("玩坏了...");
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void initBanner() {
        List<BannerModel> modelList = new ArrayList<>();
        for (Daily.Story t : topStoryList) {
            modelList.add(new BannerModel(null, t.getImageUrl(), t.getTitle()));
        }
        binding.banner.setModelList(modelList);
    }

    void onDestroy() {
        compositeDisposable.dispose();
    }

}
