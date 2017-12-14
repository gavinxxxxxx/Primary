package me.gavin.app.common.bm;

import android.content.Context;
import android.view.ViewConfiguration;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.lang.reflect.Field;
import java.util.List;

import me.gavin.primary.R;
import me.gavin.app.common.Image;
import me.gavin.base.BaseFragment;
import me.gavin.base.RxBus;
import me.gavin.base.recycler.RecyclerHeaderFooterAdapter;
import me.gavin.base.recycler.RecyclerHolder;
import me.gavin.primary.databinding.ItemBigImageBinding;
import me.gavin.util.L;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/17
 */
class BigImageAdapter extends RecyclerHeaderFooterAdapter<Image, ItemBigImageBinding> {

    private BaseFragment fragment;
    private int requestCode;

    BigImageAdapter(Context context, BaseFragment fragment, List<Image> mList) {
        super(context, mList, R.layout.item_big_image);
        this.fragment = fragment;
    }

    void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    protected void onBind(RecyclerHolder<ItemBigImageBinding> holder, int position, Image t) {

        PhotoViewAttacher pvt = holder.binding.photoView.getAttacher();
        if (!holder.binding.photoView.isSelected()) {
            fixPhotoView(pvt);
            holder.binding.photoView.setSelected(true);
        }
        initScaleLevels(pvt, t.getWidth(), t.getHeight());

        pvt.setOnPhotoTapListener((v, x, y) -> toPop(holder.getAdapterPosition()));
        pvt.setOnOutsidePhotoTapListener(v -> toPop(holder.getAdapterPosition()));

        Glide.with(fragment)
                .load(t.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.binding.photoView);
    }

    /**
     * 退出查看大图，进入页滑动到当前图片所在位置
     */
    private void toPop(int position) {
        RxBus.get().post(new BigImageLoadMoreEvent(requestCode, position));
        fragment.pop();
    }

    private void fixPhotoView(PhotoViewAttacher att) {
        try {
            Field mScaleDragDetector = att.getClass().getDeclaredField("mScaleDragDetector");
            mScaleDragDetector.setAccessible(true);
            Object customGestureDetector = mScaleDragDetector.get(att);
            float touchShop = ViewConfiguration.get(mContext).getScaledTouchSlop();
            Field mTouchSlop = customGestureDetector.getClass().getDeclaredField("mTouchSlop");
            mTouchSlop.setAccessible(true);
            mTouchSlop.set(customGestureDetector, touchShop * 3);
        } catch (Exception e) {
            L.e(e);
        }
    }

    /**
     * 重置放大级别 - 最大边长 & 长宽比
     */
    private void initScaleLevels(PhotoViewAttacher pvt, int width, int height) {
        float MAX_SCALE = 3.0f;
        float MID_SCALE = 1.75f;
        float MIN_SCALE = 1.0f;

        float larger = Math.max(width, height);
        float lesser = Math.min(width, height);

        float x = larger / 1000;
        float y = larger / lesser;

        if (x * y > 3) {
            MAX_SCALE = x * y;
            MID_SCALE = (float) Math.sqrt(MAX_SCALE / MIN_SCALE);
        }

        pvt.setScaleLevels(MIN_SCALE, MID_SCALE, MAX_SCALE);
    }
}
