package gavin.primary.net;

import gavin.primary.app.demo.Daily;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * ClientAPI
 *
 * @author gavin.xiong 2016/12/9
 */
public interface ClientAPI {

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
     * 获取往期日报
     *
     * @param date yyyyMMdd
     * @return Daily
     */
    @Headers("Cache-Control: max-stale=86400")
    @GET("news/before/{date}")
    Observable<Daily> getDailyBefore(@Path("date") String date);
}
