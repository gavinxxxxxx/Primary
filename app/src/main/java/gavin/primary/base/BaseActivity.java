package gavin.primary.base;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public abstract class BaseActivity<T extends ViewDataBinding> extends SupportActivity {

    protected T binding;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 兼容vector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        afterCreate(savedInstanceState);
    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultNoAnimator();
    }

    /**
     * 提供布局资源id
     */
    protected abstract int getLayoutId();

    /**
     * 页面加载
     */
    protected abstract void afterCreate(@Nullable Bundle savedInstanceState);

    /**
     * 显示进度对话框
     */
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
        }
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
