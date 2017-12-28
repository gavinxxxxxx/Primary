package me.gavin.app.common.banner;

import android.content.Context;

import java.util.List;

import me.gavin.app.daily.Daily;
import me.gavin.app.daily.NewsFragment;
import me.gavin.app.main.StartFragmentEvent;
import me.gavin.base.RxBus;
import me.gavin.base.recycler.RecyclerAdapter;
import me.gavin.base.recycler.RecyclerHolder;
import me.gavin.primary.R;
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
    protected void onBind(RecyclerHolder<ItemBannerBinding> holder, int position, BannerModel<T> t) {
        holder.binding.setItem(t);
        holder.binding.executePendingBindings();
        holder.binding.imageView.setOnClickListener(v ->
                RxBus.get().post(new StartFragmentEvent(NewsFragment.newInstance(((Daily.Story) t.get()).getId()))));
    }

    @Override
    public void onBindViewHolder(RecyclerHolder<ItemBannerBinding> holder, int position) {
        if (mList.isEmpty()) return;
        final int realPosition = position % mList.size();
        onBind(holder, realPosition, mList.get(realPosition));
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

}
