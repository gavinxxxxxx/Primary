package me.gavin.service.base;

import android.support.v4.app.Fragment;

import java.util.List;

import io.reactivex.Observable;
import me.gavin.app.collection.Collection;
import me.gavin.app.common.Image;
import me.gavin.app.daily.Daily;
import me.gavin.app.daily.News;
import me.gavin.app.setting.License;
import okhttp3.ResponseBody;

/**
 * DataLayer
 *
 * @author gavin.xiong 2017/4/28
 */
public class DataLayer {

    private DailyService mDailyService;
    private GankService mGankService;
    private CollectionService mCollectionService;
    private SettingService mSettingService;

    public DataLayer(
            DailyService dailyService,
            GankService gankService,
            CollectionService collectionService,
            SettingService settingService) {
        mDailyService = dailyService;
        mGankService = gankService;
        mCollectionService = collectionService;
        mSettingService = settingService;
    }

    public DailyService getDailyService() {
        return mDailyService;
    }

    public GankService getGankService() {
        return mGankService;
    }

    public CollectionService getCollectionService() {
        return mCollectionService;
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

    public interface CollectionService {

        void save(String image);

        void delete(String image);

        boolean hasCollected(String image);

        void toggle(String image);

        List<Collection> queryDesc(int offset);
    }

    public interface SettingService {
        Observable<ResponseBody> download(String url);

        Observable<List<License>> getLicense();
    }

}
