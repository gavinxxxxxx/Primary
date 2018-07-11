package me.gavin.app.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.gavin.R;
import me.gavin.base.BindingActivity;
import me.gavin.base.BundleKey;
import me.gavin.base.RxTransformers;
import me.gavin.databinding.MainActivitySplashBinding;
import me.gavin.net.Result;
import me.gavin.util.L;
import me.gavin.util.SPUtil;

/**
 * 启动页
 *
 * @author gavin.xiong 2018/6/28 0028
 */
public class SplashActivity extends BindingActivity<MainActivitySplashBinding> {

    private ViewPropertyAnimator mAnimator;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        fullscreen();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        initAnim();
        getDataAndJump();

        mBinding.ivForeground.setOnClickListener(v -> getDataAndJump());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fullscreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    private void fullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initAnim() {
        mAnimator = mBinding.ivForeground
                .animate()
                .alpha(1)
                .setStartDelay(200)
                .setDuration(1200);
        mAnimator.start();
    }

    /**
     * 拉取下发配置
     * 最快1.4s & 出错每0.5s重试 & 5秒超时
     */
    private void getDataAndJump() {
        long time = System.currentTimeMillis();
        getConfig()
                .zipWith(Observable.timer(1400, TimeUnit.MILLISECONDS),
                        (result, arg1) -> result)
                .retryWhen(tObservable -> tObservable.flatMap(t ->
                        Observable.timer(500, TimeUnit.MILLISECONDS)))
                .timeout(5000, TimeUnit.MILLISECONDS)
                .compose(RxTransformers.applySchedulers())
                .map(Result::getData)
                .subscribe(config -> {
                    L.e("onNext - " + (System.currentTimeMillis() - time));
                    jumpWithConfig(config);
                }, throwable -> {
                    L.e("onError - " + (System.currentTimeMillis() - time));
                    try {
                        String json = SPUtil.getString(BundleKey.CONFIG);
                        Object config = new Gson().fromJson(json, Object.class);
                        jumpWithConfig(config);
                    } catch (Exception e) {
                        jumpWithConfig(null);
                    }
                });
    }

    private void jumpWithConfig(@Nullable Object config) {
        if (config == null) {
            Toast.makeText(this, "网络链接错误，请检查网络", Toast.LENGTH_LONG).show();
            finish();
        } else {
            SPUtil.saveString(BundleKey.CONFIG, new Gson().toJson(config));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private Observable<Result<Object>> getConfig() {
        long time = (long) (Math.random() * 10L);
        L.e("random - " + time);
        Result<Object> result = new Result<>();
        result.setData(new Object());
        return Observable.just(result).delay(time, TimeUnit.SECONDS);
    }

}
