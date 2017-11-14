package me.gavin.primary.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gavin.primary.service.DailyManager;
import me.gavin.primary.service.GankManager;
import me.gavin.primary.service.SettingManager;
import me.gavin.primary.service.base.DataLayer;

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
