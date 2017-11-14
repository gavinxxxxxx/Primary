package me.gavin.primary.app.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.primary.R;
import me.gavin.primary.app.common.banner.BannerFragment;
import me.gavin.primary.app.main.DrawerEnableEvent;
import me.gavin.primary.app.main.DrawerStateEvent;
import me.gavin.primary.base.BindingFragment;
import me.gavin.primary.base.RxBus;
import me.gavin.primary.databinding.FragDailyBinding;

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
