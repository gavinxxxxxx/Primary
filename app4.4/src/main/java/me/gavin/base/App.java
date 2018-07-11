package me.gavin.base;

import android.app.Application;

import me.gavin.inject.component.ApplicationComponent;
import me.gavin.inject.component.DaggerApplicationComponent;
import me.gavin.inject.module.ApplicationModule;
import me.gavin.util.L;

/**
 * 自定义 Application
 *
 * @author gavin.xiong 2017/4/25
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        L.e("onCreate");
        initDagger();
    }

    private void initDagger() {
        ApplicationComponent.Instance.set(DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build());
    }

    public static App get() {
        return (App) ApplicationComponent.Instance.get().getApplication();
    }
}
