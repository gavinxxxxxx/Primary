package gavin.primary.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自动加载 RecyclerView
 *
 * @author gavin.xiong 2016/9/23
 */
public class AutoLoadRecyclerView extends RecyclerView {

    public boolean haveMore = false;
    public boolean loading = false;
    public int limit = 15;
    public int offset = 1;
    public int preCount = 0;

    private OnLoadListener onLoadListener;

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void loadData(boolean isMore) {
        loading = true;
        if (isMore) {
            offset++;
        } else {
            offset = 1;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addOnScrollListener(onScrollListener);
    }

    // 使用 DiffUtil 可解决 当数据量少时下拉刷新后不会再触发加载事件
    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //得到当前显示的最后一个item的view
            View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
            if (lastChildView != null) {
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                //判断lastPosition是不是最后一个position
                if (lastPosition > recyclerView.getLayoutManager().getItemCount() - 2 - preCount) {
                    if (onLoadListener != null && haveMore && !loading) {
                        loading = true;
                        onLoadListener.onLoad();
                    }
                }
            }
        }
    };

    public interface OnLoadListener {
        void onLoad();
    }
}
