package gavin.primary.app.demo;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import gavin.primary.inject.component.ApplicationComponent;
import gavin.primary.service.base.DataLayer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * ViewModel 基类
 *
 * @author gavin.xiong 2017/7/13
 */
public abstract class BaseViewModel extends BaseObservable implements Disposable {

    @Inject
    DataLayer mDataLayer;

    @Inject
    protected CompositeDisposable mCompositeDisposable;

    protected WeakReference<Context> mContext;
    protected WeakReference<ViewDataBinding> mBinding;

    public BaseViewModel(Context context, ViewDataBinding binding) {
        mContext = new WeakReference<>(context);
        mBinding = new WeakReference<>(binding);
        ApplicationComponent.Instance.get().inject(this);

        init();
        getData();

//        addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int id) {
//                if (id == BR.item) {
//
//                } else if (id == BR.text) {
//
//                }
//            }
//        });
    }

    public DataLayer getDataLayer() {
        return mDataLayer;
    }

    @Override
    public boolean isDisposed() {
        return mCompositeDisposable.isDisposed();
    }

    @Override
    public void dispose() {
        mCompositeDisposable.dispose();
    }

    /**
     * ViewModel 初始化
     */
    protected abstract void init();

    /**
     * ViewModel 获取数据
     */
    protected void getData() {

    }
}
