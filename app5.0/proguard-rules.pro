# ----------------------------------------------------------------------------
# 混淆的压缩比例，0-7
-optimizationpasses 5
# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
# 指定混淆是采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
# 指定外部模糊字典
-obfuscationdictionary proguard-dic-O0o.txt
# 指定class模糊字典
-classobfuscationdictionary proguard-dic-O0o.txt
# 指定package模糊字典
-packageobfuscationdictionary proguard-dic-O0o.txt
# 忽略警告
-ignorewarning
# ----------------------------------------------------------------------------
# 保持 Parcelable 不被混淆
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
# 保持 Serializable 不被混淆 并且enum 类也不被混淆
#-keep class * implements java.io.Serializable {*;}
# ----------------------------------------------------------------------------
-keep class me.gavin.app.collection.Collection { *; }
-keep class me.gavin.app.common.Image { *; }
-keep class me.gavin.app.setting.License { *; }
# ----------------------------------------------------------------------------
# SearchView
-keep class android.support.v7.widget.SearchView {*;}
# ----------------------------------------------------------------------------
# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# GreenDao @link {http://greenrobot.org/greendao/documentation/updating-to-greendao-3-and-annotations/}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.greenrobot.greendao.database.**
-dontwarn rx.**
# photoView
-keepclassmembers class com.github.chrisbanes.photoview.PhotoViewAttacher {
    private com.github.chrisbanes.photoview.CustomGestureDetector mScaleDragDetector;
}
-keepclassmembers class com.github.chrisbanes.photoview.CustomGestureDetector {
    private final float mTouchSlop;
}
# ----------------------------------------------------------------------------




