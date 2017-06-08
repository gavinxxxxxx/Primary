package gavin.primary.app.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import gavin.primary.R;
import gavin.primary.base.BindingAdapter;
import gavin.primary.base.BindingFragment;
import gavin.primary.databinding.LayoutToolbarRecyclerBinding;
import gavin.primary.util.JsonUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 开源许可
 *
 * @author gavin.xiong 2017/4/24
 */
public class LicenseFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private Disposable disposable;

    public static LicenseFragment newInstance() {
        return new LicenseFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void init() {
        binding.includeToolbar.toolbar.setTitle("开源许可");
        binding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        binding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());

        binding.refreshLayout.setEnabled(false);

        binding.recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
    }

    private void getData() {
        Observable.just("license.json")
                .map(s -> AssetsUtils.readText(_mActivity, s))
                .map(s -> JsonUtil.toList(s, new TypeToken<List<License>>() { }))
                .doOnSubscribe(disposable -> this.disposable = disposable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    BindingAdapter adapter = new BindingAdapter<>(_mActivity, list, R.layout.item_license);
                    adapter.setOnItemClickListener(position -> {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri content_url = Uri.parse(((License) (list.get(position))).getUrl());
                        intent.setData(content_url);
                        startActivity(intent);
                    });
                    binding.recycler.setAdapter(adapter);
                }, e -> Snackbar.make(binding.recycler, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show());
    }
}
