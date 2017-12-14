package me.gavin.service.base;

import android.support.v4.app.Fragment;

import java.util.List;

import me.gavin.app.common.Image;
import me.gavin.app.daily.News;
import me.gavin.app.daily.Daily;
import me.gavin.app.setting.License;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * DataLayer
 *
 * @author gavin.xiong 2017/4/28
 */
public class DataLayer {

    private DailyService mDailyService;
    private GankService mGankService;
    private SettingService mSettingService;

    public DataLayer(DailyService dailyService,
                     GankService gankService,
                     SettingService settingService) {
        mDailyService = dailyService;
        mGankService = gankService;
        mSettingService = settingService;
    }

    public DailyService getDailyService() {
        return mDailyService;
    }

    public GankService getGankService() {
        return mGankService;
    }

    public SettingService getSettingService() {
        return mSettingService;
    }

    public interface DailyService {

        /**
         * 获取最新日报新闻列表
         *
         * @return Daily
         */
        Observable<Daily> getDaily(int dayDiff);

        /**
         * 获取新闻
         *
         * @param newsId long
         * @return News
         */
        Observable<News> getNews(long newsId);
    }

    public interface GankService {
        Observable<Image> getImage(Fragment fragment, int limit, int no);
    }

    public interface SettingService {
        Observable<ResponseBody> download(String url);

        Observable<List<License>> getLicense();
    }

}
