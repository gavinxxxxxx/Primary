package me.gavin.db.util;

import android.content.Context;

import org.greenrobot.greendao.query.QueryBuilder;

import me.gavin.db.dao.DaoMaster;
import me.gavin.db.dao.DaoSession;

/**
 * greenDao 核心
 *
 * @author lizhangqu(513163535@qq.com) 2015/9/01
 */
public class DbCore {

    private static final String DEFAULT_DB_NAME = "primary.db";
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private static String DB_NAME;

    public static void init(Context context) {
        init(context, DEFAULT_DB_NAME);
    }

    public static void init(Context context, String dbName) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        DB_NAME = dbName;
        enableQueryBuilderLog();
    }

    private static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    private static void enableQueryBuilderLog() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }
}
