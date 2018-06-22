package me.gavin.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.gavin.base.FragViewModel;
import me.gavin.base.recycler.RecyclerHFAdapter;
import me.gavin.base.recycler.RecyclerHolder;
import me.gavin.R;
import me.gavin.databinding.TestHeaderFooterBinding;
import me.gavin.databinding.TestItemBinding;
import me.gavin.databinding.TestLayoutBinding;

/**
 * 知乎日报列表
 *
 * @author gavin.xiong 2017/8/11
 */
public class TestViewModel extends FragViewModel<TestFragment, TestLayoutBinding> {

    private List<String> mList;
    public Adapter adapter;

    private String[] names = {
            "处于 25-35 岁的你，开始买奢侈品了吗？",
            "有些法律上的事，非要解释给你听，你才知道理解错了",
            "要是早点知道这些，也不至于当初在校招季踩坑无数……",
            "孩子眼中的爸爸，可不只是「走，我们出去玩儿」就够了",
            "为什么胖这个字写成「月半」，不写成「月圆」或者「月全」？"
    };

    public TestViewModel(Context context, TestFragment fragment, TestLayoutBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    public void afterCreate() {
        mList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mList.add(i + " - " + names[new Random().nextInt(names.length)]);
        }
        adapter = new Adapter(mContext.get(), mList);
        for (int i = 0; i < 3; i++) {
            TestHeaderFooterBinding h = TestHeaderFooterBinding.inflate(LayoutInflater.from(mContext.get()));
            adapter.addHeader(h);
            TestHeaderFooterBinding f = TestHeaderFooterBinding.inflate(LayoutInflater.from(mContext.get()));
            adapter.addFooter(f);
        }

        mBinding.get().recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                View child = recyclerView.getLayoutManager().getChildAt(0);
                int position = recyclerView.getLayoutManager().getPosition(child);
                mBinding.get().textView.setVisibility(position > 0
                        || child.getBottom() <= mBinding.get().textView.getHeight() ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    public static class Adapter extends RecyclerHFAdapter<String, TestItemBinding> {

        Adapter(Context context, @NonNull List<String> list) {
            super(context, list, R.layout.test_item);
        }

        @Override
        protected void onBind(RecyclerHolder<TestItemBinding> holder, int position, String s) {
            holder.binding.setItem(s);
            holder.binding.executePendingBindings();
        }
    }
}
