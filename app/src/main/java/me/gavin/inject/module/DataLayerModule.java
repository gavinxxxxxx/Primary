package me.gavin.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gavin.service.DailyManager;
import me.gavin.service.GankManager;
import me.gavin.service.SettingManager;
import me.gavin.service.base.DataLayer;

/**
 * DataLayerModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class DataLayerModule {

    @Singleton
    @Provides
    public DailyManager provideDailyManager() {
        return new DailyManager();
    }

    @Singleton
    @Provides
    public GankManager provideGankManager() {
        return new GankManager();
    }

    @Singleton
    @Provides
    public SettingManager provideSettingManager() {
        return new SettingManager();
    }

    @Singleton
    @Provides
    public DataLayer provideDataLayer(DailyManager dailyManager,
                                      GankManager gankManager,
                                      SettingManager settingManager) {
        return new DataLayer(dailyManager, gankManager, settingManager);
    }
}
