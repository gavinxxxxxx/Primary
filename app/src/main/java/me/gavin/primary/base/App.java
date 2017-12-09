package me.gavin.primary.base;

import android.app.Application;

import me.gavin.primary.db.util.DbCore;
import me.gavin.primary.inject.component.ApplicationComponent;
import me.gavin.primary.inject.component.DaggerApplicationComponent;
import me.gavin.primary.inject.module.ApplicationModule;

/**
 * 自定义 Application
 *
 * @author gavin.xiong 2017/4/25
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        DbCore.init(this);
    }

    private void initDagger() {
        ApplicationComponent.Instance.init(DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build());
    }
}
