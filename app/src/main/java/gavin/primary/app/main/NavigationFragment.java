package gavin.primary.app.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;

import gavin.primary.R;
import gavin.primary.base.BaseFragment;
import gavin.primary.databinding.FragNavigationBinding;
import gavin.primary.test.ScrollingFragment;

/**
 * 侧滑菜单页
 *
 * @author gavin.xiong 2017/4/25
 */
public class NavigationFragment extends BaseFragment<FragNavigationBinding> implements NavigationView.OnNavigationItemSelectedListener {

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_navigation;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.holder, ScrollingFragment.newInstance());
        }
        init();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (binding.drawer.isDrawerOpen(Gravity.START)) {
            binding.drawer.closeDrawer(Gravity.START);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawer.closeDrawer(Gravity.START);
        switch (item.getItemId()) {
            case R.id.nav_notice:
                return true;
            case R.id.nav_tip:
                return true;
            case R.id.nav_switch:
                return true;
            case R.id.nav_msg:
                return true;
            case R.id.nav_behavior:
                return true;
        }
        return false;
    }

    private void init() {
        TextView textView = (TextView) binding.navigation.getMenu().findItem(R.id.nav_tip).getActionView();
        textView.setText("9+");
        TextView textView2 = (TextView) binding.navigation.getMenu().findItem(R.id.nav_msg).getActionView().findViewById(R.id.textView);
        textView2.setText("提示信息");
        SwitchCompat switchCompat = (SwitchCompat) binding.navigation.getMenu().findItem(R.id.nav_switch).getActionView();
        switchCompat.setChecked(true);
        binding.navigation.setNavigationItemSelectedListener(this);
    }

}
