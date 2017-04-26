package gavin.primary.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Fragment基类
 *
 * @author gavin.xiong 2017/2/28
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends SupportFragment {

    protected T binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterCreate(savedInstanceState);
    }

    /**
     * 提供布局资源id
     */
    protected abstract int getLayoutId();

    /**
     * 页面加载
     */
    protected abstract void afterCreate(@Nullable Bundle savedInstanceState);

    /**
     * 显示加载对话框
     */
    protected void showProgressDialog() {
        ((BaseActivity) _mActivity).showProgressDialog();
    }

    /**
     * 隐藏加载对话框
     */
    protected void dismissProgressDialog() {
        ((BaseActivity) _mActivity).dismissProgressDialog();
    }
}


