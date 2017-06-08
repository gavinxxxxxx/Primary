package gavin.primary.base;

import android.databinding.ViewDataBinding;

/**
 * DataBinding ViewModel 基类
 *
 * @author gavin.xiong 2017/5/5
 */
public abstract class BindingViewModel<B extends ViewDataBinding> {

    protected B binding;

    public BindingViewModel(B binding) {
        this.binding = binding;
    }
}
