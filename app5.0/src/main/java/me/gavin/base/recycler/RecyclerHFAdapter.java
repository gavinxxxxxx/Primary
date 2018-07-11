package me.gavin.base.recycler;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycler 基类适配器
 * type: 0:TYPE_NORMAL -:TYPE_HEADER + TYPE_FOOTER
 *
 * @author gavin.xiong 2017/8/15
 */
public abstract class RecyclerHFAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerHolder> {

    protected final Context mContext;
    private final int layoutId;

    protected final List<T> mList;

    private final List<ViewDataBinding> mHeaders;
    private final List<ViewDataBinding> mFooters;

    public RecyclerHFAdapter(Context context, @NonNull List<T> list, @LayoutRes int layoutId) {
        this.mContext = context;
        this.mList = list;
        this.layoutId = layoutId;
        this.mHeaders = new ArrayList<>();
        this.mFooters = new ArrayList<>();
    }

    public void addHeader(ViewDataBinding binding) {
        this.mHeaders.add(binding);
    }

    public void addFooter(ViewDataBinding binding) {
        this.mFooters.add(binding);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaders.size()) { // 以 -1 作为 header 的第一个下标
            return -1 - position;
        } else if (position >= mHeaders.size() + mList.size()) { // 以 1 作为 footer 的第一个下标
            return 1 + position - mHeaders.size() - mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType < 0) { // header
            return new RecyclerHolder<>(mHeaders.get(-1 - viewType));
        } else if (viewType > 0) { // footer
            return new RecyclerHolder<>(mFooters.get(viewType - 1));
        } else { // 0: normal
            return new RecyclerHolder<>(DataBindingUtil.inflate(
                    LayoutInflater.from(mContext), layoutId, parent, false));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            final int realPosition = holder.getAdapterPosition() - mHeaders.size();
            onBind(holder, realPosition, mList.get(realPosition));
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == 0 ? 1 : gridManager.getSpanCount();
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerHolder holder) {
        super.onViewAttachedToWindow(holder);
        final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp == null) return;
        if (lp.getClass() == RecyclerView.LayoutParams.class) {
            lp.width = RecyclerView.LayoutParams.MATCH_PARENT; // 线性布局头尾全屏 (仅纵向有效，且所有 item 并非头尾有效)
        } else if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            final StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(getItemViewType(holder.getLayoutPosition()) != 0 || p.isFullSpan());
        }
    }

    @Override
    public int getItemCount() {
        return mHeaders.size() + mList.size() + mFooters.size();
    }

    protected abstract void onBind(RecyclerHolder<B> holder, int position, T t);

}
