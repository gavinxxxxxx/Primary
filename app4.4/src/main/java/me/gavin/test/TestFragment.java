package me.gavin.test;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import me.gavin.R;
import me.gavin.base.BindingFragment;
import me.gavin.base.recycler.BindingAdapter;
import me.gavin.databinding.TestLayoutBinding;

/**
 * 测试
 *
 * @author gavin.xiong 2017/12/28
 */
public class TestFragment extends BindingFragment<TestLayoutBinding> {

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.test_layout;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list2.add("ITEM_" + i);
        }
        mBinding.recyclerTag.setAdapter(new BindingAdapter<>(_mActivity, list2, R.layout.test_item_two));

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i + " - 这就是理想主义，对彼此，对现实的妥协 - 这就是理想主义，对彼此，对现实的妥协 - 这就是理想主义，对彼此，对现实的妥协");
        }
        mBinding.recycler.setAdapter(new TestAdapter(_mActivity, list));


        aaa(mBinding.recyclerTag, mBinding.recycler);
        aaa(mBinding.recycler, mBinding.recyclerTag);
    }

    private void aaa(RecyclerView r1, RecyclerView r2) {
        r1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    recyclerView.setTag(true);
                    r2.setTag(null);
                    r2.stopScroll();
                    r2.stopNestedScroll();
                    r2.dispatchTouchEvent(MotionEvent.obtain(
                            SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN, r2.getLeft()+5, r2.getTop()+5, 0));
                    r2.dispatchTouchEvent(MotionEvent.obtain(
                            SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_UP, r2.getLeft()+5, r2.getTop()+5, 0));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getTag() != null) {
                    r2.scrollBy(dx, dy);
                }
            }
        });
    }


}
