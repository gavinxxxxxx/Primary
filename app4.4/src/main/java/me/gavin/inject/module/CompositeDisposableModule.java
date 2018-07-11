package me.gavin.inject.module;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * CompositeDisposableModule
 *
 * @author gavin.xiong 2017/8/11
 */
@Module
public class CompositeDisposableModule {

    /**
     * 创建一个 CompositeDisposable
     * @return CompositeDisposable
     */
    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
