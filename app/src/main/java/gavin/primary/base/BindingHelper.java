package gavin.primary.base;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gavin.primary.R;
import gavin.primary.util.DateUtil;
import gavin.primary.util.ImageLoader;

//@BindingMethods({ // 使用 BindingMethod 指定属性的绑定方法
//        @BindingMethod(
//                type = android.widget.ImageView.class,
//                attribute = "android:tint",
//                method = "setImageTintList")
//})

//@InverseBindingMethods({ // 使用 InverseBindingMethod 实现动态更新
//        @InverseBindingMethod(
//                type = android.widget.TextView.class,
//                attribute = "android:text",
//                method = "getText",                   // 默认会根据attribute name获取get
//                event = "android:textAttrChanged")})  // 默认根据attribute增加AttrChanged

public class BindingHelper {

//    @BindingConversion // 使用 BindingConversion 转换绑定属性
//    public static ColorDrawable convertColorToDrawable(int color) {
//        return new ColorDrawable(color);
//    }

    /**
     * 使用DataBinding来加载图片
     * 使用@BindingAdapter注解，注解值（这里的imageUrl）可任取，注解值将成为自定义属性
     * 此自定义属性可在xml布局文件中使用，自定义属性的值就是这里定义String类型url
     * 《说明》：
     * 1. 方法名可与注解名一样，也可不一样
     * 2. 第一个参数必须是View，就是自定义属性所在的View
     * 3. 第二个参数就是自定义属性的值，与注解值对应。这是数组，可多个
     * 这里需要INTERNET权限，别忘了
     *
     * @param imageView ImageView控件
     * @param url       图片网络地址
     */
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
        }
    }

    @BindingAdapter({"height"})
    public static void setLayoutHeight(View view, int height) {
        view.getLayoutParams().height = height;
    }

    @BindingAdapter({"time"})
    public static void setFriendTime(TextView textView, String dateTimeString) {
        textView.setText(DateUtil.getFriendlyTime(dateTimeString));
    }

}