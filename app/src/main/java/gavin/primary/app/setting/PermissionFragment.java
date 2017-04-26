package gavin.primary.app.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import gavin.primary.base.BaseFragment;
import gavin.primary.base.RequestCode;
import gavin.primary.R;
import gavin.primary.app.main.NavigationFragment;
import gavin.primary.databinding.LayoutBlankBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Android 6.0 需要时权限
 *
 * @author gavin.xiong 2017/4/25
 */
public class PermissionFragment extends BaseFragment<LayoutBlankBinding> {

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
            checkPermission();
        }
    }

    private void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(_mActivity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                || !(ContextCompat.checkSelfPermission(_mActivity, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)) {
            requestPermission();
        } else {
            Observable.just(NavigationFragment.newInstance())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::startWithPop);
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(_mActivity, Manifest.permission.READ_PHONE_STATE)) {
            Snackbar.make(binding.root, "应用需要获取您的设备信息", Snackbar.LENGTH_INDEFINITE).show();
        }
        requestPermissions(new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.INTERNET
        }, RequestCode.PERMISSION_NEEDFUL);
    }

    private void showDialog() {
//        Snackbar.make(binding.root, "应用缺少必要权限", Snackbar.LENGTH_INDEFINITE)
//                .setAction("设置", (v) -> openAppSetting())
//                .show();
        new AlertDialog.Builder(_mActivity)
                .setTitle("应用缺少必要权限")
                .setMessage("是否前往设置页打开权限？")
                .setCancelable(false)
                .setNegativeButton("退出", (a0, a1) -> {
                    _mActivity.finish();
                    System.exit(0);
                })
                .setPositiveButton("设置", (a0, a1) -> openAppSetting())
                .setOnKeyListener((inter, i, event) -> {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        inter.cancel();
                        _mActivity.finish();
                        System.exit(0);
                    }
                    return true;
                })
                .show();
    }

    private void openAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + _mActivity.getPackageName()));
        startActivityForResult(intent, RequestCode.PERMISSION_SETTING);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode.PERMISSION_NEEDFUL) {
            for (int result : grantResults) {
                if (result == -1) {
                    showDialog();
                    return;
                }
            }
            checkPermission();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkPermission();
    }
}
