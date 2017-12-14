package me.gavin.service;

import android.support.v4.app.Fragment;

import me.gavin.app.common.Image;
import me.gavin.app.gank.Result;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;
import io.reactivex.Observable;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class GankManager extends BaseManager implements DataLayer.GankService {

    @Override
    public Observable<Image> getImage(Fragment fragment, int limit, int no) {
        return getApi().getGankImage(limit, no)
                .map(Result::getResults)
                .flatMap(Observable::fromIterable)
                .map(image -> Image.newImage(fragment, image.getUrl()));
    }
}
