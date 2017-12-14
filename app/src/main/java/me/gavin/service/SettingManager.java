package me.gavin.service;

import java.util.List;

import me.gavin.app.setting.License;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * SettingManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class SettingManager extends BaseManager implements DataLayer.SettingService {

    @Override
    public Observable<ResponseBody> download(String url) {
        return getApi().download(url);
    }

    @Override
    public Observable<List<License>> getLicense() {
        return getApi().getLicense();
    }
}
