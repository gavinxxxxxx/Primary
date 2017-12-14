package me.gavin.app.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.primary.R;
import me.gavin.base.BindingFragment;
import me.gavin.primary.databinding.FragAboutBinding;

/**
 * 关于
 *
 * @author gavin.xiong 2017/8/16
 */
public class AboutFragment extends BindingFragment<FragAboutBinding, AboutViewModel> {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new AboutViewModel(getContext(), this, mBinding);
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_about;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle("关于");
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
    }

}
