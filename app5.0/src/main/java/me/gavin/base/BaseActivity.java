package me.gavin.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.Lazy;
import me.gavin.inject.component.ApplicationComponent;
import io.reactivex.disposables.CompositeDisposable;
import me.gavin.service.base.DataLayer;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * BaseActivity
 *
 * @author gavin.xiong 2016/12/30  2016/12/30
 */
public abstract class BaseActivity extends SupportActivity {

    @Inject
    Lazy<DataLayer> mDataLayer;
    @Inject
    protected CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ApplicationComponent.Instance.get().inject(this);
        afterCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultVerticalAnimator();
    }

    protected DataLayer getDataLayer() {
        return mDataLayer.get();
    }

    public abstract void setContentView();

    protected abstract void afterCreate(@Nullable Bundle savedInstanceState);

}
