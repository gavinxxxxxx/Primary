package me.gavin.inject.module;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import org.greenrobot.greendao.query.QueryBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.gavin.base.Config;
import me.gavin.db.dao.DaoMaster;
import me.gavin.db.dao.DaoSession;
import me.gavin.primary.BuildConfig;

/**
 * DatabaseModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class DatabaseModule {

    @Singleton
    @Provides
    SQLiteOpenHelper provideSQLiteOpenHelper(Application application) {
        enableQueryBuilderLog();
        return new DaoMaster.DevOpenHelper(application, Config.DB_NAME);
    }

    @Singleton
    @Provides
    DaoMaster provideDaoMaster(SQLiteOpenHelper openHelper) {
        return new DaoMaster(openHelper.getWritableDatabase());
    }

    @Singleton
    @Provides
    DaoSession provideDaoSession(DaoMaster daoMaster) {
        return daoMaster.newSession();
    }

    private void enableQueryBuilderLog() {
        QueryBuilder.LOG_SQL = BuildConfig.LOG_DEBUG;
        QueryBuilder.LOG_VALUES = BuildConfig.LOG_DEBUG;
    }

}
