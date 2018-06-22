package me.gavin.base.recycler;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ViewDataBinding;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.gavin.base.BaseFragment;
import me.gavin.base.FragViewModel;
import me.gavin.databinding.FooterLoadingBinding;

/**
 * 分页 ViewModel 基类
 *
 * @author gavin.xiong 2017/7/11
 */
public abstract class PagingViewModel<T, A extends RecyclerHFAdapter>
        extends FragViewModel<BaseFragment, ViewDataBinding> implements SwipeRefreshLayout.OnRefreshListener {

    public final ObservableBoolean refreshable = new ObservableBoolean(true);

    protected FooterViewModel mFooterViewModel;

    protected boolean pagingHaveMore = false;   // 有无更多
    protected boolean pagingLoading = false;    // 分页数据加载中
    protected int pagingLimit = 15;             // 分页数据大小
    protected int pagingOffset = 1;             // 分页页码
    protected int pagingPreCount = 0;           // 分页数据预加载 item 数量

    protected List<T> mList = new ArrayList<>(); // 有无更多
    public A adapter; // 有无更多

    /*
     * 数据分条更新时刷新清空标记
     */
    private boolean clearFlag;

    public PagingViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    public void afterCreate() {
        initAdapter();
        initFooterBinding();
        notifyChange();
        getData(false);
    }

    protected void initFooterBinding() {
        FooterLoadingBinding loadingBinding = FooterLoadingBinding.inflate(LayoutInflater.from(mContext.get()));
        mFooterViewModel = new FooterViewModel(mContext.get());
        mFooterViewModel.afterCreate();
        loadingBinding.setVm(mFooterViewModel);
        adapter.addFooter(loadingBinding);
    }

    /**
     * 通过这个方法初始化列表适配器
     */
    protected abstract void initAdapter();

    /**
     * 通过这个方法加载分页数据
     */
    protected abstract void getData(boolean isMore);

    /**
     * SwipeRefreshLayout.OnRefreshListener 触发事件
     */
    @Override
    public void onRefresh() {
        getData(false);
    }

    /**
     * 分页数据加载前
     */
    protected void doOnSubscribe(boolean isMore) {
        pagingLoading = true;
        if (isMore) {
            mFooterViewModel.notifyStateChanged(FooterViewModel.STATE_LOADING);
        } else {
            clearFlag = true;
            loading.set(true);
            empty.set(false);
            error.set(false);
            pagingOffset = 0;
        }
        pagingOffset++;
    }

    /**
     * 分页数据接收展示 - 单次单页
     */
    protected void accept(boolean isMore, List<T> newList) {
        if (!isMore) mList.clear();
        if (newList != null) mList.addAll(newList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 分页数据接收展示 - 单次单条
     */
    protected void accept(boolean isMore, T t) {
        if (!isMore && clearFlag) {
            clearFlag = false;
            mList.clear();
            adapter.notifyDataSetChanged();
        }
        mList.add(t);
        adapter.notifyItemInserted(mList.size() - 1);
    }

    /**
     * 分页数据加载完成
     */
    protected void doOnComplete(boolean isMore) {
        loading.set(false);
        mFooterViewModel.notifyStateChanged(pagingHaveMore ? FooterViewModel.STATE_IDLE : FooterViewModel.STATE_PERIOD);
        pagingLoading = false;
    }

    /**
     * 分页数据加载出错
     */
    protected void doOnError(boolean isMore, Throwable e) {
        loading.set(false);
        notifyMsg(e.getMessage());
        if (isMore) {
            mFooterViewModel.notifyStateChanged(FooterViewModel.STATE_ERROR);
        } else {
            error.set(true);
        }
        pagingOffset--;
        pagingLoading = false;
    }

    /**
     * RecyclerView 自定加载更多触发装置
     */
    // 使用 DiffUtil 可解决 当数据量少时下拉刷新后不会再触发加载事件
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //得到当前显示的最后一个item的view
            View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
            if (lastChildView != null) {
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                //判断lastPosition是不是最后一个position
                if (lastPosition > recyclerView.getLayoutManager().getItemCount() - 2 - pagingPreCount) {
                    performPagingLoad();
                }
            }
        }
    };

    /**
     * 执行加载更多
     */
    public void performPagingLoad() {
        if (pagingHaveMore && !pagingLoading) {
            pagingLoading = true;
            getData(true);
        }
    }

}
