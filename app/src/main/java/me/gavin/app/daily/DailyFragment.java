package me.gavin.app.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.app.main.DrawerStateEvent;
import me.gavin.R;
import me.gavin.app.common.banner.BannerFragment;
import me.gavin.app.main.DrawerEnableEvent;
import me.gavin.base.BindingFragment;
import me.gavin.base.RxBus;
import me.gavin.databinding.FragDailyBinding;

/**
 * 知乎日报 - 列表
 *
 * @author gavin.xiong 2017/8/11
 */
public class DailyFragment extends BindingFragment<FragDailyBinding, DailyViewModel> {

    private int mBannerType;

    public static DailyFragment newInstance() {
        return new DailyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_daily;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mBannerType = hashCode();
        mViewModel = new DailyViewModel(getContext(), this, mBinding, mBannerType);
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        loadRootFragment(R.id.bannerHolder, BannerFragment.newInstance(mBannerType));

        mBinding.toolbar.setNavigationOnClickListener(v ->
                RxBus.get().post(new DrawerStateEvent(true)));
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        RxBus.get().post(new DrawerEnableEvent(false));
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        RxBus.get().post(new DrawerEnableEvent(true));
    }
}
