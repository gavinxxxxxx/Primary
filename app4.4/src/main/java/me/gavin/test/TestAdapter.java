package me.gavin.test;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import me.gavin.R;
import me.gavin.base.recycler.RecyclerAdapter;
import me.gavin.base.recycler.RecyclerHolder;
import me.gavin.databinding.TestItemBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/7/3 0003
 */
public class TestAdapter extends RecyclerAdapter<String, TestItemBinding> {

    public TestAdapter(Context context, @NonNull List<String> list) {
        super(context, list, R.layout.test_item);
    }

    @Override
    protected void onBind(RecyclerHolder<TestItemBinding> holder, int position, String s) {
        holder.binding.setItem(s);
        holder.binding.executePendingBindings();
        holder.binding.item.setOnClickListener(v -> {

        });
    }
}
