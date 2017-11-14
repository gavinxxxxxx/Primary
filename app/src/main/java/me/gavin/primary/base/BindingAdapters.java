package me.gavin.primary.base;

import android.databinding.BindingAdapter;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import me.gavin.primary.R;
import me.gavin.primary.util.ImageLoader;

/**
 * 数据绑定适配器
 *
 * @author gavin.xiong 2017/8/15
 */
public class BindingAdapters {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        ImageLoader.loadImage(imageView, url);
    }

    @BindingAdapter({"headUrl"})
    public static void loadHead(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            ImageLoader.loadHead(imageView, url);
        }
    }

    @BindingAdapter({"roundImageUrl"})
    public static void loadRoundImage(ImageView imageView, String url) {
        ImageLoader.loadRoundImage(imageView, url);
    }

    @BindingAdapter({"resId"})
    public static void loadIcon(ImageView imageView, int resId) {
        if (resId <= 0) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            imageView.setImageResource(resId);
//            Glide.with(imageView.getContext()).load(resId).into(imageView);
        }
    }

    @BindingAdapter({"height"})
    public static void setLayoutHeight(View view, int height) {
        view.getLayoutParams().height = height;
    }

    @BindingAdapter({"msg"})
    public static void showMsg(View view, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        }
    }

}