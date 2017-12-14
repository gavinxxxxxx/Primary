package me.gavin.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * dataBinding & viewModel
 *
 * @author gavin.xiong 2017/8/15
 */
public abstract class BindingFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment {

    protected DB mBinding;
    protected VM mViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        bindViewModel(savedInstanceState);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null && !mViewModel.isDisposed()) {
            mViewModel.dispose();
        }
    }

    /**
     * 绑定 ViewModel
     */
    protected abstract void bindViewModel(@Nullable Bundle savedInstanceState);
}
