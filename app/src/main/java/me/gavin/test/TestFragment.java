package me.gavin.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.base.BindingFragment;
import me.gavin.R;
import me.gavin.databinding.TestLayoutBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/12/28
 */
public class TestFragment extends BindingFragment<TestLayoutBinding, TestViewModel> {

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new TestViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_layout;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {

    }
}
