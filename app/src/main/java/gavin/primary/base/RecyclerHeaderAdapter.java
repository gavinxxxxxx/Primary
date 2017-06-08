package gavin.primary.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Recycler 基类适配器
 * <p>
 * Created by gavin.xiong on 2016/5/15.
 */
public abstract class RecyclerHeaderAdapter<T, B extends ViewDataBinding,
        HB extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;

    private HB headerBinding;
    protected Context mContext;
    protected List<T> mList;
    private int layoutId;

    public RecyclerHeaderAdapter(Context context, List<T> mList, @LayoutRes int layoutId) {
        this.mContext = context;
        this.mList = mList;
        this.layoutId = layoutId;
    }

    public void setHeaderBinding(HB headerBinding) {
        this.headerBinding = headerBinding;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerBinding == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerBinding != null && viewType == TYPE_HEADER) {
            return new RecyclerHolder<>(headerBinding);
        } else {
            B bing = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, parent, false);
            return new RecyclerHolder<>(bing);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        final T data = mList.get(pos);
        onBind(holder, pos, data);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (headerBinding != null && lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    private int getRealPosition(RecyclerHolder holder) {
        int position = holder.getLayoutPosition();
        return headerBinding == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return headerBinding == null ? 0 : 1;
        } else {
            return headerBinding == null ? mList.size() : mList.size() + 1;
        }
    }

    public abstract void onBind(RecyclerHolder<B> holder, int position, T t);

}
