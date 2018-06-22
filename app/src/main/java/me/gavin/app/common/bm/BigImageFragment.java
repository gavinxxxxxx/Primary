package me.gavin.app.common.bm;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.gavin.app.common.Image;
import me.gavin.base.BindingFragment;
import me.gavin.base.BundleKey;
import me.gavin.base.RxTransformers;
import me.gavin.R;
import me.gavin.databinding.FragBigImageBinding;

/**
 * 查看大图
 *
 * @author gavin.xiong 2017/8/15
 */
public class BigImageFragment extends BindingFragment<FragBigImageBinding, BigImageViewModel>
        implements Toolbar.OnMenuItemClickListener {

    private List<Image> imageList;

    private LinearLayoutManager layoutManager;

    private String imageUrl;

    private ImageView ivActionLove;

    private AnimatorSet scaleAnim;

    public static BigImageFragment newInstance(@NonNull ArrayList<Image> imageList, int position, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleKey.IMAGE_URL, imageList);
        bundle.putInt(BundleKey.POSITION, position);
        bundle.putInt(BundleKey.PAGE_TYPE, requestCode);
        BigImageFragment fragment = new BigImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void bindViewModel(@Nullable Bundle savedInstanceState) {
        mViewModel = new BigImageViewModel(getContext(), this, mBinding);
        mViewModel.setRequestCode(getArguments().getInt(BundleKey.PAGE_TYPE));
        imageList = (List) getArguments().getSerializable(BundleKey.IMAGE_URL);
        mViewModel.setImageList(imageList);
        mViewModel.afterCreate();
        mBinding.setVm(mViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_big_image;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        init();
    }

    @Override
    public boolean onBackPressedSupport() {
        mViewModel.toPop();
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDownload:
                mViewModel.operateImage(imageUrl, 0);
                break;
            case R.id.actionShare:
                mViewModel.operateImage(imageUrl, 1);
                break;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (scaleAnim != null) {
            scaleAnim.cancel();
        }
    }

    private void init() {
        mBinding.toolbar.setNavigationOnClickListener(v -> mViewModel.toPop());
        mBinding.toolbar.inflateMenu(R.menu.action_image_option);
        mBinding.toolbar.setOnMenuItemClickListener(this);

        ivActionLove = mBinding.toolbar.getMenu().findItem(R.id.actionCollect).getActionView().findViewById(R.id.ivActionLove);
        ivActionLove.setOnClickListener(v -> doLoveAnim(imageUrl));

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mBinding.recycler);
        layoutManager = (LinearLayoutManager) mBinding.recycler.getLayoutManager();
        mBinding.recycler.scrollToPosition(getArguments().getInt(BundleKey.POSITION));
        mBinding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (layoutManager.findFirstVisibleItemPosition() < imageList.size()) {
                        mBinding.toolbar.getMenu().findItem(R.id.actionCollect).setVisible(true);
                        mBinding.toolbar.getMenu().findItem(R.id.actionDownload).setVisible(true);
                        mBinding.toolbar.getMenu().findItem(R.id.actionShare).setVisible(true);

                        imageUrl = imageList.get(layoutManager.findFirstVisibleItemPosition()).getUrl();
                        Observable.just(imageUrl)
                                .map(s -> mViewModel.isImageCollected(s))
                                .compose(RxTransformers.applySchedulers())
                                .subscribe(ivActionLove::setSelected);
                    } else {
                        mBinding.toolbar.getMenu().findItem(R.id.actionCollect).setVisible(false);
                        mBinding.toolbar.getMenu().findItem(R.id.actionDownload).setVisible(false);
                        mBinding.toolbar.getMenu().findItem(R.id.actionShare).setVisible(false);
                    }
                }
            }
        });

        imageUrl = imageList.get(getArguments().getInt(BundleKey.POSITION)).getUrl();
        Observable.just(imageUrl)
                .map(s -> mViewModel.isImageCollected(s))
                .compose(RxTransformers.applySchedulers())
                .subscribe(ivActionLove::setSelected);
    }

    /**
     * 初始化属性动画
     */
    private void initAnim() {
        scaleAnim = new AnimatorSet().setDuration(700);
        scaleAnim.setInterpolator(new AnticipateOvershootInterpolator());
        scaleAnim.playTogether(
                ObjectAnimator.ofFloat(ivActionLove, View.SCALE_X, 1f, 0f, 1f),
                ObjectAnimator.ofFloat(ivActionLove, View.SCALE_Y, 1f, 0f, 1f));
    }

    /**
     * 喜欢/取消喜欢动画
     */
    private void doLoveAnim(String image) {
        Observable.timer(350, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    ivActionLove.setEnabled(false);
                    if (scaleAnim == null) {
                        initAnim();
                    }
                    scaleAnim.start();
                })
                .map(arg0 -> {
                    mViewModel.toggleCollect(image);
                    ivActionLove.setSelected(!ivActionLove.isSelected());
                    return arg0;
                })
                .delay(350, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(arg0 -> ivActionLove.setEnabled(true));
    }
}
