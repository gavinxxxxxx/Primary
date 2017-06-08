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
public abstract class RecyclerHeaderFooterAdapter<T, B extends ViewDataBinding,
        HB extends ViewDataBinding, FB extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOOTER = 2;

    private HB headerBinding;
    private FB footerBinding;
    protected Context mContext;
    protected List<T> mList;
    private int layoutId;

    public RecyclerHeaderFooterAdapter(Context context, List<T> mList, @LayoutRes int layoutId) {
        this.mContext = context;
        this.mList = mList;
        this.layoutId = layoutId;
    }

    public void setHeaderBinding(HB headerBinding) {
        this.headerBinding = headerBinding;
    }

    public void setFooterBinding(FB footerBinding) {
        this.footerBinding = footerBinding;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerBinding == null && footerBinding == null) return TYPE_NORMAL;
        if (headerBinding != null && position == 0) return TYPE_HEADER;
        if (footerBinding != null && position == mList.size() + (headerBinding == null ? 0 : 1))
            return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerBinding != null && viewType == TYPE_HEADER) {
            return new RecyclerHolder<>(headerBinding);
        } else if (footerBinding != null && viewType == TYPE_FOOTER) {
            return new RecyclerHolder<>(footerBinding);
        } else {
            B bing = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, parent, false);
            return new RecyclerHolder<>(bing);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
            return;
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
                    return (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp == null) return;
        if (lp.getClass() == RecyclerView.LayoutParams.class) {
            lp.width = RecyclerView.LayoutParams.MATCH_PARENT; // 线性布局头尾全屏 (仅纵向有限，且所有 item 并非头尾有限)
        }
        if (headerBinding != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0 || p.isFullSpan());
        }
        if (footerBinding != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == mList.size() + (headerBinding == null ? 0 : 1) || p.isFullSpan());
        }
    }

    private int getRealPosition(RecyclerHolder holder) {
        int position = holder.getLayoutPosition();
        return headerBinding == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            if (headerBinding != null && footerBinding != null) {
                return 2;
            } else if (headerBinding != null || footerBinding != null) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (headerBinding != null && footerBinding != null) {
                return mList.size() + 2;
            } else if (headerBinding != null || footerBinding != null) {
                return mList.size() + 1;
            } else {
                return mList.size();
            }
        }
    }

    public abstract void onBind(RecyclerHolder<B> holder, int position, T t);

}
