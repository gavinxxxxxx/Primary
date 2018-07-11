package me.gavin.util;

import android.widget.ImageView;

/**
 * 图片加载器
 *
 * @author gavin.xiong 2017/3/15
 */
public class ImageLoader {

    /**
     * 正常加载图片
     */
    public static void loadImage(ImageView imageView, String url) {

    }

    /**
     * 加载头像
     */
    public static void loadHead(ImageView imageView, String url) {
//        Glide.with(imageView.getContext())
//                .load(url)
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .override(200, 200)
//                .transform(new GlideCircleTransformation(imageView.getContext()))
//                .into(imageView);
    }

    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(ImageView imageView, String url) {
//        Glide.with(imageView.getContext())
//                .load(url)
//                .transform(new GlideCircleTransformation(imageView.getContext()))
//                .into(imageView);
    }

}
