package gavin.primary.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.primary.base.BaseFragment;
import gavin.primary.R;
import gavin.primary.databinding.TestFargScrollingBinding;

/**
 * 测试
 *
 * @author gavin.xiong 2017/4/25
 */
public class ScrollingFragment extends BaseFragment<TestFargScrollingBinding> {

    public static ScrollingFragment newInstance() {
        return new ScrollingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_farg_scrolling;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        binding.toolbar.inflateMenu(R.menu.activity_main_drawer);
    }
}
