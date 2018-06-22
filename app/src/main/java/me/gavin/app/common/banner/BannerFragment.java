package me.gavin.app.common.banner;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.R;
import me.gavin.base.BindingFragment;
import me.gavin.base.BundleKey;
import me.gavin.databinding.FragBannerBinding;

/**
 * 轮播
 *
 * @author gavin.xiong 2017/8/14
 */
public class BannerFragment extends BindingFragment<FragBannerBinding, BannerViewModel> {

    public static BannerFragment newInstance(int pageType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKey.PAGE_TYPE, pageType);
        BannerFragment fragment = new BannerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new BannerViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_banner;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mViewModel.setPageType(getArguments().getInt(BundleKey.PAGE_TYPE));
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mViewModel.onSupportInvisible();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mViewModel.onSupportVisible();
    }
}
