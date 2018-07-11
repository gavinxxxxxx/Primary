package me.gavin.service.base;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * DataLayer
 *
 * @author gavin.xiong 2017/4/28
 */
public class DataLayer {

    private MainService mMainService;
    private SettingService mSettingService;

    public DataLayer(MainService mainService, SettingService settingService) {
        mMainService = mainService;
        mSettingService = settingService;
    }

    public MainService getMainService() {
        return mMainService;
    }

    public SettingService getSettingService() {
        return mSettingService;
    }

    public interface MainService {

    }

    public interface SettingService {
        Observable<ResponseBody> download(String url);
    }

}
