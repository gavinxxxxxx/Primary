package me.gavin.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.R;
import me.gavin.base.BindingActivity;
import me.gavin.databinding.ActMainBinding;
import me.gavin.test.TestFragment;

public class MainActivity extends BindingActivity<ActMainBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.holder, TestFragment.newInstance());
        }
    }
}
