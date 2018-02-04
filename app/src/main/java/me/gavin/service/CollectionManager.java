package me.gavin.service;

import java.util.List;

import me.gavin.app.collection.Collection;
import me.gavin.db.dao.CollectionDao;
import me.gavin.service.base.BaseManager;
import me.gavin.service.base.DataLayer;

/**
 * CollectionManager
 *
 * @author gavin.xiong 2017/4/28
 */
public class CollectionManager extends BaseManager implements DataLayer.CollectionService {

    @Override
    public void save(String image) {
        Collection t = new Collection();
        t.setImage(image);
        t.setTime(System.currentTimeMillis());
        getDaoSession().getCollectionDao().save(t);
    }

    @Override
    public void delete(String image) {
        getDaoSession().getCollectionDao()
                .queryBuilder()
                .where(CollectionDao.Properties.Image.eq(image))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    @Override
    public boolean hasCollected(String image) {
        return getDaoSession().getCollectionDao()
                .queryBuilder()
                .where(CollectionDao.Properties.Image.eq(image))
                .limit(1)
                .buildCount()
                .count() > 0;
    }

    @Override
    public void toggle(String image) {
        if (hasCollected(image)) {
            delete(image);
        } else {
            save(image);
        }
    }

    @Override
    public List<Collection> queryDesc(int offset) {
        return getDaoSession()
                .getCollectionDao()
                .queryBuilder()
                .orderDesc(CollectionDao.Properties.Id)
                .limit(10)
                .offset(offset * 10)
                .list();
    }
}
