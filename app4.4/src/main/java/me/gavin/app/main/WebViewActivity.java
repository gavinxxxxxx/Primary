package me.gavin.app.main;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;

import me.gavin.R;
import me.gavin.base.BindingActivity;
import me.gavin.databinding.MainActivityWebViewBinding;
import me.gavin.util.L;

/**
 * 活动页面
 *
 * @author gavin.xiong 2018/7/6 0006
 */
public class WebViewActivity extends BindingActivity<MainActivityWebViewBinding> {

    private static final String TAG = "WebViewActivity";

    private AgentWeb mAgentWeb;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_web_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(0);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        // String url = getIntent().getStringExtra(BundleKey.URL);
        String url = "https://test.chainfor.com/answer/demo.html"; // H5交互测试页面

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mBinding.holder, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorAccent))
                .addJavascriptInterface("chainfor", this)
                .setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        if (mBinding.progressBar.getVisibility() == View.VISIBLE) {
                            mBinding.progressBar.bringToFront();
                        }
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        mBinding.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        mBinding.progressBar.setVisibility(View.GONE);
                    }
                })
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAgentWeb.getWebLifeCycle().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAgentWeb.getWebLifeCycle().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mAgentWeb.handleKeyEvent(keyCode, event)
                || super.onKeyDown(keyCode, event);
    }

    /**
     * 返回上一页
     */
    @JavascriptInterface
    public void back(Object arg0) {
        L.e(TAG, "back() - " + arg0);
        if (!mAgentWeb.back()) finish();
    }

    /**
     * 登录
     *
     * @param type 类型 1：直接取authorization 2：强制登录
     */
    @JavascriptInterface
    public void login(int type) {
        L.e(TAG, "login() - " + type);
    }

    /**
     * 内容图片点击
     *
     * @param url 图片字符串base64
     */
    @JavascriptInterface
    public void onImgClick(String url) {
        L.e(TAG, "onImgClick() - " + url);
    }


}
