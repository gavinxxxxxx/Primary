package me.gavin.inject.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gavin.service.CollectionManager;
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
    DailyManager provideDailyManager() {
        return new DailyManager();
    }

    @Singleton
    @Provides
    GankManager provideGankManager() {
        return new GankManager();
    }

    @Singleton
    @Provides
    CollectionManager provideCollectionManager() {
        return new CollectionManager();
    }

    @Singleton
    @Provides
    SettingManager provideSettingManager() {
        return new SettingManager();
    }

    @Singleton
    @Provides
    DataLayer provideDataLayer(
            DailyManager dailyManager,
            GankManager gankManager,
            CollectionManager collectionManager,
            SettingManager settingManager) {
        return new DataLayer(dailyManager, gankManager, collectionManager, settingManager);
    }
}
