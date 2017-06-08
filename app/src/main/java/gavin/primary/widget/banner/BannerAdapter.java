package gavin.primary.widget.banner;

import android.content.Context;

import java.util.List;

import gavin.primary.R;
import gavin.primary.base.RecyclerAdapter;
import gavin.primary.base.RecyclerHolder;
import gavin.primary.databinding.WidgetBannerItemBinding;

/**
 * DataBinding 基类适配器
 *
 * @author gavin.xiong 2016/12/28
 */
public class BannerAdapter extends RecyclerAdapter<BannerModel, WidgetBannerItemBinding> {

    private BannerView callback;

    BannerAdapter(Context context, List<BannerModel> list, BannerView callback) {
        super(context, list, R.layout.widget_banner_item);
        this.callback = callback;
    }

    @Override
    public void onBind(RecyclerHolder<WidgetBannerItemBinding> holder, BannerModel t, final int position) {
        holder.binding.setItem(t);
        holder.binding.setCallback(callback);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder<WidgetBannerItemBinding> holder, int position) {
        final BannerModel t = mList.get(position % mList.size());
        onBind(holder, t, position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : Integer.MAX_VALUE;
    }

}
