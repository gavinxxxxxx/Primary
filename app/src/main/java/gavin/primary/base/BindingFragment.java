package gavin.primary.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gavin.primary.BR;
import gavin.primary.app.demo.BaseViewModel;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/1/4  2017/1/4
 */
public abstract class BindingFragment<T extends ViewDataBinding> extends BaseFragment {

    protected T binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        viewModel = getViewModel();
        binding.setVariable(BR.viewModel, viewModel);
        return binding.getRoot();
    }

    protected abstract BaseViewModel getViewModel();

    protected abstract int getLayoutId();
}
