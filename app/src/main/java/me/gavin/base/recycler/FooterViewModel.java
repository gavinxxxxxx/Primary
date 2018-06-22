package me.gavin.base.recycler;

import android.content.Context;
import android.databinding.ObservableBoolean;

import me.gavin.base.BaseViewModel;
import me.gavin.R;

/**
 * 下拉加载更多 ViewModel
 *
 * @author gavin.xiong 2017/8/11
 */
public class FooterViewModel extends BaseViewModel {

    public static final int STATE_IDLE = 0;     // 发呆中
    public static final int STATE_LOADING = 1;  // 加载中
    public static final int STATE_PERIOD = 2;   // 没有更多
    public static final int STATE_ERROR = 3;    // 出错
    public static final int STATE_GONE = 4;     // 不显示

    public final ObservableBoolean vertical = new ObservableBoolean(true);

    public FooterViewModel(Context context) {
        super(context);
    }

    @Override
    public void afterCreate() {
        notifyStateChanged(STATE_GONE);
    }

    /**
     * 设置方向 - 是否竖向
     */
    public void setVertical(boolean v) {
        vertical.set(v);
    }

    /**
     * 更新状态
     */
    public void notifyStateChanged(int state) {
        if (state == STATE_GONE) {
            empty.set(true);
            loading.set(false);
            msg.set(null);
        } else if (state == STATE_ERROR) {
            empty.set(false);
            loading.set(false);
            msg.set(mContext.get().getString(R.string.label_loading_error));
        } else if (state == STATE_PERIOD) {
            empty.set(false);
            loading.set(false);
            msg.set(mContext.get().getString(R.string.label_loading_period));
        } else if (state == STATE_LOADING) {
            empty.set(false);
            loading.set(true);
            msg.set(mContext.get().getString(R.string.label_loading_loading));
        } else {
            empty.set(false);
            loading.set(false);
            msg.set(mContext.get().getString(R.string.label_loading_idle));
        }
    }

}
