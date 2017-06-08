package gavin.primary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;

import gavin.primary.inject.component.ApplicationComponent;
import gavin.primary.service.base.DataLayer;
import io.reactivex.disposables.CompositeDisposable;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Fragment 基类
 *
 * @author gavin.xiong 2016/12/30  2016/12/30
 */
public abstract class BaseFragment extends SupportFragment {

    @Inject
    DataLayer mDataLayer;
    @Inject
    protected CompositeDisposable mCompositeDisposable;

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

    public DataLayer getDataLayer() {
        return mDataLayer;
    }

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
