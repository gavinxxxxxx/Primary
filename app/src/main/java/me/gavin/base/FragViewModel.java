package me.gavin.base;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.lang.ref.WeakReference;

/**
 * Fragment ViewModel 基类
 *
 * @author gavin.xiong 2017/8/15
 */
public abstract class FragViewModel<F extends BaseFragment, B extends ViewDataBinding> extends BaseViewModel {

    protected WeakReference<F> mFragment;
    protected WeakReference<B> mBinding;

    public FragViewModel(Context context, F fragment, B binding) {
        super(context);
        mFragment = new WeakReference<>(fragment);
        mBinding = new WeakReference<>(binding);
    }
}
