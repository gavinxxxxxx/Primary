package me.gavin.primary.app.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;

import me.gavin.primary.R;
import me.gavin.primary.base.BaseFragment;
import me.gavin.primary.base.recycler.BindingHeaderFooterAdapter;
import me.gavin.primary.base.recycler.PagingViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 开源许可
 *
 * @author gavin.xiong 2017/8/14
 */
class LicenseViewModel extends PagingViewModel<License, BindingHeaderFooterAdapter<License>> {

    LicenseViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
        refreshable.set(false);
    }

    @Override
    protected void initAdapter() {
        adapter = new BindingHeaderFooterAdapter<>(mContext.get(), mList, R.layout.item_license);
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse((mList.get(position)).getUrl());
            intent.setData(content_url);
            mFragment.get().startActivity(intent);
        });
    }

    @Override
    protected void getData(boolean isMore) {
        getDataLayer().getSettingService().getLicense()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    loading.set(true);
                })
                .doOnComplete(() -> loading.set(false))
                .doOnError(throwable -> loading.set(false))
                .subscribe(list -> {
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                }, throwable -> notifyMsg(throwable.getMessage()));
    }
}
