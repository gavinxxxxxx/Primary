package me.gavin.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.Lazy;
import me.gavin.inject.component.ApplicationComponent;
import io.reactivex.disposables.CompositeDisposable;
import me.gavin.service.base.DataLayer;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * BaseFragment
 *
 * @author gavin.xiong 2016/12/30  2016/12/30
 */
public abstract class BaseFragment extends SupportFragment {

    @Inject
    Lazy<DataLayer> mDataLayer;
    @Inject
    protected CompositeDisposable mCompositeDisposable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ApplicationComponent.Instance.get().inject(this);
        afterCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.dispose();
    }

    protected DataLayer getDataLayer() {
        return mDataLayer.get();
    }

    /**
     * 提供 Fragment 布局文件
     */
    protected abstract int getLayoutId();

    /**
     * Fragment 创建后
     */
    protected abstract void afterCreate(@Nullable Bundle savedInstanceState);

}
