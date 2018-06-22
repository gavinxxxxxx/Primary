package me.gavin.app.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.R;
import me.gavin.base.BindingFragment;
import me.gavin.base.BundleKey;
import me.gavin.databinding.FragNewsBinding;

/**
 * 日报详情页
 *
 * @author gavin.xiong 2017/8/14
 */
public class NewsFragment extends BindingFragment<FragNewsBinding, NewsViewModel> {

    public static NewsFragment newInstance(long newsId) {
        Bundle bundle = new Bundle();
        bundle.putLong(BundleKey.NEWS_ID, newsId);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new NewsViewModel(getContext(), this, mBinding,
                getArguments().getLong(BundleKey.NEWS_ID));
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_news;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.toolbar.setNavigationOnClickListener(v -> pop());
    }
}
