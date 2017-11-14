package me.gavin.primary.app.common.bm;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;
import java.util.UUID;

import me.gavin.primary.app.common.Image;
import me.gavin.primary.base.BaseFragment;
import me.gavin.primary.base.RxBus;
import me.gavin.primary.base.recycler.PagingViewModel;
import me.gavin.primary.databinding.FragBigImageBinding;
import me.gavin.primary.db.util.DbUtil;
import me.gavin.primary.util.CacheHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 查看大图
 *
 * @author gavin.xiong 2017/8/15
 */
class BigImageViewModel extends PagingViewModel<Image, BigImageAdapter> {

    private int requestCode;

    private LinearLayoutManager linearLayoutManager;

    BigImageViewModel(Context context, BaseFragment fragment, ViewDataBinding binding) {
        super(context, fragment, binding);
    }

    void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    void setImageList(List<Image> list) {
        mList = list;
    }

    @Override
    protected void initAdapter() {
        adapter = new BigImageAdapter(mContext.get(), mFragment.get(), mList);
        adapter.setRequestCode(requestCode);
    }

    @Override
    protected void getData(boolean isMore) {
        if (isMore) {
            RxBus.get().post(new BigImageLoadMoreEvent(requestCode, null));
        } else {
            pagingPreCount = 3;
            pagingHaveMore = true;
        }
    }

    @Override
    public void afterCreate() {
        super.afterCreate();
        mFooterViewModel.setVertical(false);
        linearLayoutManager = (LinearLayoutManager) ((FragBigImageBinding) mBinding.get()).recycler.getLayoutManager();
        subscribeEvent();
    }

    void toPop() {
        RxBus.get().post(new BigImageLoadMoreEvent(requestCode, linearLayoutManager.findFirstVisibleItemPosition()));
        mFragment.get().pop();
    }

    private void subscribeEvent() {
        RxBus.get().toObservable(BigImageLoadStateEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .filter(event -> event.requestCode == requestCode)
                .subscribe(event -> {
                    if (event.disposable != null) {
                        doOnSubscribe(true);
                    } else if (event.throwable != null) {
                        doOnError(true, event.throwable);
                    } else if (event.haveMore != null) {
                        pagingHaveMore = event.haveMore;
                    } else if (event.t != null) {
                        adapter.notifyDataSetChanged();
                    } else if (event.ts != null) {
                        adapter.notifyDataSetChanged();
                    } else {
                        doOnComplete(true);
                    }
                });
    }

    boolean isImageCollected(String imageUrl) {
        return DbUtil.getCollectionService().hasCollected(imageUrl);
    }

    void toggleCollect(String imageUrl) {
        DbUtil.getCollectionService().toggle(imageUrl);
    }

    /**
     * 操作图片
     *
     * @param imageUrl 图片链接
     * @param type     操作类型（0：下载 1：分享）
     */
    void operateImage(String imageUrl, int type) {
        new RxPermissions(mFragment.get().getActivity())
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .map(granted -> {
                    if (!granted) {
                        Snackbar.make(mBinding.get().getRoot(), "保存图片需要 SDCard 卡读写权限", Snackbar.LENGTH_LONG)
                                .setAction("设置", v -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + mContext.get().getPackageName()));
                                    mFragment.get().startActivity(intent);
                                }).show();
                    }
                    return granted;
                })
                .filter(Boolean::booleanValue)
                .flatMap(granted -> getDataLayer().getSettingService().download(imageUrl))
                .map(ResponseBody::byteStream)
                .map(inputStream -> {
                    String dir = type == 0 ? "download" : "share";
                    String name = dir + "/" + UUID.randomUUID();
                    return CacheHelper.saveImageStream(inputStream, name);
                })
                .map(path -> CacheHelper.file2Uri(mContext.get(), new File(path)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uri -> {
                    if (type == 0) {
                        Snackbar.make(mBinding.get().getRoot(), "图片已保存", Snackbar.LENGTH_LONG)
                                .setAction("查看", v -> {
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(uri, "image/*");
                                        mContext.get().startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        notifyMsg("你跟我说打不开？？？");
                                    }
                                }).show();
                    } else {
                        ShareCompat.IntentBuilder
                                .from(mFragment.get().getActivity())
                                .setChooserTitle("分享图片")
                                .setType("image/*")
                                .addStream(uri)
                                .startChooser();
                    }
                }, throwable -> notifyMsg(throwable.getMessage()));
    }

}
