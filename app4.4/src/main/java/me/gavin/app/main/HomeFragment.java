package me.gavin.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.R;
import me.gavin.base.BindingFragment;
import me.gavin.databinding.MainFragmentHomeBinding;

/**
 * 首页
 *
 * @author gavin.xiong 2018/6/29
 */
public class HomeFragment extends BindingFragment<MainFragmentHomeBinding> {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment_home;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setTitle(R.string.app_name);
        mBinding.includeToolbar.toolbar.inflateMenu(R.menu.test);
        mBinding.includeToolbar.toolbar.setOnMenuItemClickListener(item -> {

            return true;
        });

    }

}
