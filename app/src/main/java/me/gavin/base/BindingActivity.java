package me.gavin.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/1/4  2017/1/4
 */
public abstract class BindingActivity<T extends ViewDataBinding> extends BaseActivity {

    protected T binding;

    @Override
    public void setContentView() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    protected abstract int getLayoutId();
}
