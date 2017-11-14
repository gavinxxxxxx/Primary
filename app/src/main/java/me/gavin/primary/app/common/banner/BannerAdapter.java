package me.gavin.primary.app.common.banner;

import android.content.Context;

import java.util.List;

import me.gavin.primary.R;
import me.gavin.primary.app.daily.Daily;
import me.gavin.primary.app.daily.NewsFragment;
import me.gavin.primary.app.main.StartFragmentEvent;
import me.gavin.primary.base.RxBus;
import me.gavin.primary.base.recycler.RecyclerAdapter;
import me.gavin.primary.base.recycler.RecyclerHolder;
import me.gavin.primary.databinding.ItemBannerBinding;

/**
 * 轮播适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class BannerAdapter<T> extends RecyclerAdapter<BannerModel<T>, ItemBannerBinding> {

    BannerAdapter(Context context, List<BannerModel<T>> list) {
        super(context, list, R.layout.item_banner);
    }

    @Override
    protected void onBind(RecyclerHolder<ItemBannerBinding> holder, BannerModel<T> t, final int position) {
        holder.binding.setItem(t);
        holder.binding.executePendingBindings();
        holder.binding.imageView.setOnClickListener(v ->
                RxBus.get().post(new StartFragmentEvent(NewsFragment.newInstance(((Daily.Story) t.get()).getId()))));
    }

    @Override
    public void onBindViewHolder(RecyclerHolder<ItemBannerBinding> holder, int position) {
        if (mList == null || mList.isEmpty()) return;
        final int realPosition = position % mList.size();
        final BannerModel<T> t = mList.get(realPosition);
        onBind(holder, t, realPosition);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : Integer.MAX_VALUE;
    }

}
