package me.gavin.base.recycler;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import me.gavin.primary.R;
import me.gavin.base.function.IntConsumer;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class BindingHeaderFooterAdapter<T> extends RecyclerHeaderFooterAdapter<T, ViewDataBinding> {

    private IntConsumer mListener;

    public BindingHeaderFooterAdapter(Context context, List<T> list, @LayoutRes int layoutId) {
        super(context, list, layoutId);
    }

    public void setOnItemClickListener(IntConsumer listener) {
        this.mListener = listener;
    }

    @Override
    protected void onBind(RecyclerHolder<ViewDataBinding> holder, int position, T t) {
        holder.binding.setVariable(BR.item, t);
        holder.binding.executePendingBindings();
        if (mListener != null) {
            holder.itemView.findViewById(R.id.item).setOnClickListener(v -> mListener.accept(position));
        }
    }

}
