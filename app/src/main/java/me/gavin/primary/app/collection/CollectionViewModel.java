package me.gavin.primary.app.collection;

import android.content.Context;
import android.databinding.ViewDataBinding;

import java.util.concurrent.TimeUnit;

import me.gavin.primary.app.common.Image;
import me.gavin.primary.app.common.ImageViewModel;
import me.gavin.primary.base.BaseFragment;
import me.gavin.primary.db.util.DbUtil;
import io.reactivex.Observable;

/**
 * 收藏
 *
 * @author gavin.xiong 2017/8/16
 */
class CollectionViewModel extends ImageViewModel {

    CollectionViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    protected Observable<Image> getDataSrc(boolean isMore) {
        return Observable.just(isMore)
                .delay(500, TimeUnit.MILLISECONDS)
                .map(arg0 -> arg0 ? pagingOffset - 1 : 0)
                .flatMap(offset -> Observable.just(DbUtil.getCollectionService().queryDesc(offset)))
                .map(list -> {
                    pagingHaveMore = list.size() == 10;
                    return list;
                })
                .flatMap(Observable::fromIterable)
                .map(Collection::getImage)
                .map(s -> Image.newImage(mFragment.get(), s));
    }
}
