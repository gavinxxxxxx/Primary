package me.gavin.primary.app.gank;

import android.content.Context;
import android.databinding.ViewDataBinding;

import me.gavin.primary.app.common.Image;
import me.gavin.primary.app.common.ImageViewModel;
import me.gavin.primary.base.BaseFragment;
import io.reactivex.Observable;

/**
 * 干货集中营
 *
 * @author gavin.xiong 2017/8/11
 */
class GankViewModel extends ImageViewModel {

    GankViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return getDataLayer().getGankService().getImage(mFragment.get(), pagingLimit, isMore ? pagingOffset + 1 : 1);
    }

}
