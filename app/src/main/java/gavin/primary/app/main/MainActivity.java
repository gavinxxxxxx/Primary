package gavin.primary.app.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.primary.app.setting.PermissionFragment;
import gavin.primary.base.BaseActivity;
import gavin.primary.R;
import gavin.primary.databinding.ActMainBinding;

public class MainActivity extends BaseActivity<ActMainBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.holder, PermissionFragment.newInstance());
        }
    }
}
