package me.gavin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * 可监听滚动事件的横向滚动的 HorizontalScroll
 *
 * @author gavin.xiong 2018/7/3 0003
 */
public class HorizontalScrollViewEx extends HorizontalScrollView {

    private OnScrollChangeListener mOnScrollChangeListener;

    public HorizontalScrollViewEx(Context context) {
        super(context);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int ol, int ot) {
        super.onScrollChanged(l, t, ol, ot);
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChanged(l, t, ol, ot);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.mOnScrollChangeListener = onScrollChangeListener;
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(int l, int t, int ol, int ot);
    }
}
