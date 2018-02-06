package me.gavin.base;

import android.databinding.BindingAdapter;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import me.gavin.primary.R;
import me.gavin.util.DateUtil;
import me.gavin.util.DisplayUtil;
import me.gavin.util.ImageLoader;

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

    @BindingAdapter({"time"})
    public static void setFriendTime(TextView textView, String dateTimeString) {
        textView.setText(DateUtil.getFriendlyTime(DateUtil.parse(dateTimeString, "yyyy-MM-dd HH:mm:ss")));
    }

    @BindingAdapter({"statusBarCoverExAnchor"})
    public static void statusBarCoverExAnchor(CoordinatorLayout coordinatorLayout, View anchor) {
        if (coordinatorLayout.findViewWithTag("statusBarCoverExCover") == null) {
            final Space space = new Space(coordinatorLayout.getContext());
            space.setFitsSystemWindows(false);
            coordinatorLayout.addView(space);

            final View cover = new View(coordinatorLayout.getContext());
            CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout
                    .LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, DisplayUtil.getStatusHeight());
            cover.setLayoutParams(layoutParams);
            ViewCompat.setElevation(cover, ViewCompat.getElevation(anchor));
            cover.setBackgroundColor(ContextCompat.getColor(coordinatorLayout.getContext(), R.color.colorPrimaryDark));
            cover.setFitsSystemWindows(true);
            cover.setTag("statusBarCoverExCover");
            coordinatorLayout.addView(cover);

            coordinatorLayout.addOnLayoutChangeListener((v, l, t, r, b, ol, ot, or, ob) -> {
                cover.setVisibility(space.getTop() > 0 ? View.VISIBLE : View.GONE);
                ViewCompat.setElevation(cover, ViewCompat.getElevation(anchor));
            });
        }
    }

}