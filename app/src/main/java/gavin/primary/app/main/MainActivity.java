package gavin.primary.app.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import gavin.primary.R;
import gavin.primary.app.setting.PermissionFragment;
import gavin.primary.base.BindingActivity;
import gavin.primary.base.RxBus;
import gavin.primary.databinding.ActMainBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends BindingActivity<ActMainBinding> {

    public boolean isResumed;
    private StartFragmentEvent startFragmentEvent;
    private StartFragmentWithPopEvent startFragmentWithPopEvent;
    private PopFragmentEvent popFragmentEvent;
    private PopToFragmentEvent popToFragmentEvent;

    @Override
    protected int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        subscribeEvent();
        // 状态栏深色图标
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (savedInstanceState == null) {
            loadRootFragment(R.id.holder, PermissionFragment.newInstance());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;

        if (startFragmentEvent != null) {
            start(startFragmentEvent.targetFragment);
            startFragmentEvent = null;
        } else if (startFragmentWithPopEvent != null) {
            startWithPop(startFragmentWithPopEvent.targetFragment);
            startFragmentWithPopEvent = null;
        } else if (popFragmentEvent != null) {
            pop();
            popFragmentEvent = null;
        } else if (popToFragmentEvent != null) {
            popTo(popToFragmentEvent.clazz, popToFragmentEvent.includeTargetFragment);
            popFragmentEvent = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(StartFragmentEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> {
                    if (isResumed) {
                        start(event.targetFragment);
                    } else {
                        this.startFragmentEvent = event;
                    }
                });
        RxBus.get().toObservable(StartFragmentWithPopEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> {
                    if (isResumed) {
                        startWithPop(event.targetFragment);
                    } else {
                        this.startFragmentWithPopEvent = event;
                    }
                });
        RxBus.get().toObservable(PopFragmentEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> {
                    if (isResumed) {
                        pop();
                    } else {
                        this.popFragmentEvent = event;
                    }
                });
        RxBus.get().toObservable(PopToFragmentEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(event -> {
                    if (isResumed) {
                        popTo(event.clazz, event.includeTargetFragment);
                    } else {
                        this.popToFragmentEvent = event;
                    }
                });
    }
}
