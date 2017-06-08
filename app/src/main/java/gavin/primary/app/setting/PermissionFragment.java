package gavin.primary.app.setting;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.tbruyelle.rxpermissions2.RxPermissions;

import gavin.primary.R;
import gavin.primary.app.demo.DailyFragment;
import gavin.primary.base.BindingFragment;
import gavin.primary.base.RequestCode;
import gavin.primary.databinding.LayoutBlankBinding;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Android 6.0 需要时权限 - 不建议在启动时获取所有权限，应在需要时再申请
 *
 * @author gavin.xiong 2017/4/25
 */
public class PermissionFragment extends BindingFragment<LayoutBlankBinding> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static PermissionFragment newInstance() {
        return new PermissionFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_blank;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            requestPermission();
        }
        binding.root.setOnClickListener(v -> requestPermission());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestPermission();
    }

    private void requestPermission() {
        new RxPermissions(_mActivity)
                .requestEach(Manifest.permission.READ_PHONE_STATE)
                .doOnSubscribe(compositeDisposable::add)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(permission -> {
                    if (permission.granted) {
                        startWithPop(DailyFragment.newInstance());
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        Snackbar.make(binding.root, "应用需要获取您的设备信息", Snackbar.LENGTH_INDEFINITE)
                                .setAction("重试", v -> requestPermission())
                                .show();
                    } else {
                        Snackbar.make(binding.root, "应用缺少必要权限", Snackbar.LENGTH_INDEFINITE)
                                .setAction("开启", v -> openAppSetting())
                                .show();
                    }
                });
    }

    private void openAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + _mActivity.getPackageName()));
        startActivityForResult(intent, RequestCode.PERMISSION_SETTING);
    }
}
