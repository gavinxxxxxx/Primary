package me.gavin.net;

import java.util.List;

import me.gavin.app.daily.News;
import me.gavin.app.daily.Daily;
import me.gavin.app.gank.Result;
import me.gavin.app.setting.License;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * ClientAPI
 *
 * @author gavin.xiong 2016/12/9
 */
public interface ClientAPI {


    /* **************************************************************************** *
     * *********************************** 知乎日报 ******************************** *
     * **************************************************************************** */


    /**
     * 获取今日日报新闻列表 ( 最长缓存一天 60 * 60 * 24 )
     *
     * @return Daily
     */
    // 指定返回复用时间为 60s
    @Headers("Cache-Control: max-stale=60")
    @GET("news/latest")
    Observable<Daily> getDaily();

    /**
     * 获取新闻
     *
     * @param newsId long
     * @return News
     */
    @Headers("Cache-Control: max-stale=3600")
    @GET("news/{newsId}")
    Observable<News> getNews(@Path("newsId") long newsId);

    /**
     * 获取往期日报
     *
     * @param date yyyyMMdd
     * @return Daily
     */
    @Headers("Cache-Control: max-stale=86400")
    @GET("news/before/{date}")
    Observable<Daily> getDailyBefore(@Path("date") String date);


    /* **************************************************************************** *
     * *********************************** 干货集中营福利 *************************** *
     * **************************************************************************** */


    /**
     * 获取福利
     *
     * @param limit 分页大小
     * @param no    页码
     * @return Result
     */
    @Headers("Cache-Control: max-stale=1800")
    @GET("http://gank.io/api/data/福利/{limit}/{no}")
    Observable<Result> getGankImage(@Path("limit") int limit, @Path("no") int no);


    /* **************************************************************************** *
     * *********************************** 设置 ************************************ *
     * **************************************************************************** */

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    @Headers("Cache-Control: max-stale=2419200")
    @GET("https://raw.githubusercontent.com/gavinxxxxxx/Sensual/master/json/license.json")
    Observable<List<License>> getLicense();

}
