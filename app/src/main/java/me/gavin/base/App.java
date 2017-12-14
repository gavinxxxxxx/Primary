package me.gavin.base;

import android.app.Application;

import me.gavin.db.util.DbCore;
import me.gavin.inject.component.ApplicationComponent;
import me.gavin.inject.component.DaggerApplicationComponent;
import me.gavin.inject.module.ApplicationModule;

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
