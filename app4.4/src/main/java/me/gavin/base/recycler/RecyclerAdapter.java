package me.gavin.base.recycler;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView 基类列表适配器
 *
 * @author gavin.xiong 2016/12/9
 */
public abstract class RecyclerAdapter<T, B extends ViewDataBinding>
        extends RecyclerView.Adapter<RecyclerHolder<B>> {

    protected final Context mContext;
    private final int layoutId;
    protected final List<T> mList;

    public RecyclerAdapter(Context context, @NonNull List<T> list, @LayoutRes int layoutId) {
        this.mContext = context;
        this.mList = list;
        this.layoutId = layoutId;
    }

    @Override
    public RecyclerHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerHolder<>(DataBindingUtil.inflate(
                LayoutInflater.from(mContext), layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerHolder<B> holder, int position) {
        onBind(holder, position, mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected abstract void onBind(RecyclerHolder<B> holder, int position, T t);
}
