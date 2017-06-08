package gavin.primary.inject.module;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * VersionAPIModule
 *
 * @author gavin.xiong 2017/4/28
 */
@Module
public class CompositeDisposableModule {

    /**
     * 创建一个 CompositeDisposable
     * @return CompositeDisposable
     */
    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
