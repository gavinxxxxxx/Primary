package gavin.primary.test;

import android.os.Bundle;
import android.support.annotation.Nullable;

import gavin.primary.R;
import gavin.primary.base.BaseFragment;
import gavin.primary.databinding.TestFragBinding;

/**
 * 测试
 *
 * @author gavin.xiong 2017/4/25
 */
public class TestFragment extends BaseFragment<TestFragBinding> {

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_frag;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {

    }
}
