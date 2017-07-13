package gavin.primary.app.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import gavin.primary.R;
import gavin.primary.base.BindingFragment;
import gavin.primary.databinding.FragDailyBinding;

/**
 * 知乎日报 - 列表
 * frag_daily.xml
 *
 * @author gavin.xiong 2017/4/26
 */
public class DailyFragment extends BindingFragment<FragDailyBinding> {

    public static DailyFragment newInstance() {
        return new DailyFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_daily;
    }

    @Override
    protected BaseViewModel getViewModel() {
        return new DailyViewModel(_mActivity, binding);
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
    }

    private void init() {
        binding.toolbar.setNavigationOnClickListener(v ->
                Snackbar.make(binding.recycler, "显示点什么好呢~", Snackbar.LENGTH_LONG).show());
    }

}
