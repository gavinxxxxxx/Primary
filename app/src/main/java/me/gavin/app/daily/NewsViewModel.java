package me.gavin.app.daily;

import android.content.Context;
import android.databinding.ObservableField;

import me.gavin.base.FragViewModel;
import me.gavin.databinding.FragNewsBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 日报详情
 *
 * @author gavin.xiong 2017/8/14
 */
public class NewsViewModel extends FragViewModel<NewsFragment, FragNewsBinding> {

    public final ObservableField<News> news = new ObservableField<>();

    private long newsId;

    NewsViewModel(Context context, NewsFragment fragment, FragNewsBinding binding, long newsId) {
        super(context, fragment, binding);
        this.newsId = newsId;
    }

    @Override
    public void afterCreate() {
        getData();
    }

    private void getData() {
        getDataLayer().getDailyService().getNews(newsId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    loading.set(true);
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> loading.set(false))
                .doOnError(e -> loading.set(false))
                .subscribe(news -> {
                    NewsViewModel.this.news.set(news);
                    String htmlData = HtmlUtil.createHtmlData(news);
                    mBinding.get().webView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
                }, e -> notifyMsg(e.getMessage()));
    }

//    @BindingAdapter({"newsHtml"})
//    public static void newsHtml(WebView webView, News news) {
//        if (news != null) {
//            String htmlData = HtmlUtil.createHtmlData(news);
//            webView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
//        }
//    }

}
