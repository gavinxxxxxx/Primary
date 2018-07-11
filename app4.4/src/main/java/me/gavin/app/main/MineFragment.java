package me.gavin.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.R;
import me.gavin.base.BindingFragment;
import me.gavin.databinding.MainActivityMainBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/6/30.
 */
public class MineFragment extends BindingFragment<MainActivityMainBinding> {

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
    }

}
