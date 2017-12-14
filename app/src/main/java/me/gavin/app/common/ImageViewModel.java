package me.gavin.app.common;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import me.gavin.app.common.bm.BigImageLoadStateEvent;
import me.gavin.app.common.bm.BigImageFragment;
import me.gavin.app.common.bm.BigImageLoadMoreEvent;
import me.gavin.app.main.StartFragmentEvent;
import me.gavin.base.BaseFragment;
import me.gavin.base.RxBus;
import me.gavin.base.recycler.PagingViewModel;
import me.gavin.primary.databinding.LayoutPagingBinding;
import me.gavin.primary.databinding.LayoutPagingToolbarBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片
 *
 * @author gavin.xiong 2017/8/11
 */
public abstract class ImageViewModel extends PagingViewModel<Image, ImageAdapter> {

    public ImageViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    protected void initAdapter() {
        adapter = new ImageAdapter(mContext.get(), mFragment.get(), mList);
        adapter.setListener(i -> RxBus.get().post(new StartFragmentEvent(
                BigImageFragment.newInstance((ArrayList<Image>) mList, i, mFragment.get().hashCode()))));
    }

    @Override
    public void afterCreate() {
        super.afterCreate();
        subscribeEvent();
    }

    protected abstract Observable<Image> getDataSrc(boolean isMore);

    @Override
    protected void getData(boolean isMore) {
        getDataSrc(isMore)
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    doOnSubscribe(isMore);
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), disposable, null, null, null, null));
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    doOnComplete(isMore);
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), null, null, null, null, null));
                })
                .doOnError(e -> {
                    doOnError(isMore, e);
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), null, e, null, null, null));
                })
                .doOnNext(image -> {
                    pagingHaveMore = image.haveMore;
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), null, null, null, null, pagingHaveMore));
                })
                .subscribe(image -> {
                    accept(isMore, image);
                    RxBus.get().post(new BigImageLoadStateEvent<>(mFragment.get().hashCode(), null, null, image, null, null));
                }, Throwable::printStackTrace);
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(BigImageLoadMoreEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .filter(event -> event.requestCode == mFragment.get().hashCode())
                .subscribe(event -> {
                    if (event.position == null) {
                        performPagingLoad();
                    } else {
                        if (mBinding.get() instanceof LayoutPagingBinding) {
                            ((LayoutPagingBinding) mBinding.get())
                                    .recycler.smoothScrollToPosition(event.position);
                        } else if (mBinding.get() instanceof LayoutPagingToolbarBinding) {
                            ((LayoutPagingToolbarBinding) mBinding.get())
                                    .recycler.smoothScrollToPosition(event.position);
                        }
                    }
                });
    }
}
