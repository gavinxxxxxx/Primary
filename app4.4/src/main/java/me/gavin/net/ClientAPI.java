package me.gavin.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * ClientAPI
 *
 * @author gavin.xiong 2016/12/9
 */
public interface ClientAPI {

    /* **************************************************************************** *
     * *********************************** 设置 ************************************ *
     * **************************************************************************** */


    // @Headers("Cache-Control: max-stale=60")
    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String url);

}
