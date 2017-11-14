package me.gavin.primary.service;

import java.util.Calendar;
import java.util.Date;

import me.gavin.primary.app.daily.Daily;
import me.gavin.primary.app.daily.News;
import me.gavin.primary.service.base.BaseManager;
import me.gavin.primary.service.base.DataLayer;
import me.gavin.primary.util.DateUtil;
import io.reactivex.Observable;

/**
 * DailyManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class DailyManager extends BaseManager implements DataLayer.DailyService {

    @Override
    public Observable<Daily> getDaily(int dayDiff) {
        if (dayDiff == 0) {
            return getApi().getDaily();
        } else {
            Date date = DateUtil.offsetDate(new Date(), Calendar.DAY_OF_YEAR, -dayDiff + 1);
            return getApi().getDailyBefore(DateUtil.format(date, "yyyyMMdd"));
        }
    }

    @Override
    public Observable<News> getNews(long newsId) {
        return getApi().getNews(newsId);
    }
}
