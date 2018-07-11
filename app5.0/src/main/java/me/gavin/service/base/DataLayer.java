package me.gavin.service.base;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * DataLayer
 *
 * @author gavin.xiong 2017/4/28
 */
public class DataLayer {

    private SettingService mSettingService;

    public DataLayer(SettingService settingService) {
        mSettingService = settingService;
    }


    public SettingService getSettingService() {
        return mSettingService;
    }


    public interface SettingService {
        Observable<ResponseBody> download(String url);
    }

}
