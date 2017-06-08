package gavin.primary.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;

import gavin.primary.BR;
import gavin.primary.R;
import gavin.primary.databinding.FooterLoadingBinding;

import java.util.List;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class BindingFooterAdapter<T> extends RecyclerHeaderFooterAdapter<T, ViewDataBinding, ViewDataBinding, FooterLoadingBinding> {

    private OnItemClickListener mListener;

    public BindingFooterAdapter(Context context, List<T> mData, @LayoutRes int layoutId) {
        super(context, mData, layoutId);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    @Override
    public void onBind(RecyclerHolder<ViewDataBinding> holder, int position, T t) {
        holder.binding.setVariable(BR.item, t);
        holder.binding.executePendingBindings();
        if (mListener != null) {
            holder.itemView.findViewById(R.id.item).setOnClickListener((v) -> mListener.onItemClick(position));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
