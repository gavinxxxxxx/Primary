package gavin.primary.app.demo;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import gavin.primary.BR;
import gavin.primary.base.RecyclerHeaderFooterAdapter;
import gavin.primary.databinding.FooterLoadingBinding;

/**
 * 分页 ViewModel 基类
 *
 * @author gavin.xiong 2017/7/11
 */
public abstract class PagingViewModel<T, A extends RecyclerHeaderFooterAdapter>
        extends BaseViewModel implements SwipeRefreshLayout.OnRefreshListener {

    private boolean refreshing;
    private String text;

    protected boolean haveMore = false;
    private boolean loading = false;
    protected int limit = 15;
    protected int offset = 1;
    protected int preCount = 0;

    protected List<T> list;
    public A adapter;
    protected FooterLoadingBinding loadingBinding;

    public PagingViewModel(Context context, ViewDataBinding binding) {
        super(context, binding);
        getData(false);
    }

    @Override
    public void onRefresh() {
        getData(false);
    }

    protected abstract void getData(boolean isMore);

    @Bindable
    public boolean isRefreshing() {
        return refreshing;
    }

    @Bindable
    public String getText() {
        return list == null || list.isEmpty() ? text : null;
    }

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
                if (lastPosition > recyclerView.getLayoutManager().getItemCount() - 2 - preCount) {
                    if (haveMore && !loading) {
                        loading = true;
                        getData(true);
                    }
                }
            }
        }
    };

    /**
     * 更多数据加载中
     */
    protected void doOnSubscribe(boolean isMore) {
        loading = true;

        text = null;
        notifyPropertyChanged(BR.text);

        if (isMore) {
            loadingBinding.root.setVisibility(View.VISIBLE);
            loadingBinding.progressBar.setVisibility(View.VISIBLE);
            loadingBinding.textView.setText("加载中...");
        } else {
            refreshing = true;
            notifyPropertyChanged(BR.refreshing);

            offset = 0;
        }

        offset++;
    }

    protected void accept(boolean isMore, List<T> newList) {
        if (!isMore) list.clear();
        if (newList != null) list.addAll(newList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 完成加载更多
     */
    protected void doOnComplete(boolean isMore) {
        refreshing = false;
        notifyPropertyChanged(BR.refreshing);

        if (isMore) {
            loadingBinding.progressBar.setVisibility(View.GONE);
            loadingBinding.textView.setText(haveMore ? "发呆中..." : "再也没有了...");
        }

        loading = false;
    }

    protected void doOnError(boolean isMore, Throwable e) {
        refreshing = false;
        notifyPropertyChanged(BR.refreshing);

        if (isMore) {
            loadingBinding.progressBar.setVisibility(View.GONE);
            loadingBinding.textView.setText("玩坏了...");
        } else {
            text = "被玩坏了...";
            notifyPropertyChanged(BR.text);
        }

        offset--;
        loading = false;

        Snackbar.make(mBinding.get().getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
    }
}
