package me.gavin.primary.app.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;

import me.gavin.primary.base.FragViewModel;
import me.gavin.primary.databinding.FragAboutBinding;
import me.gavin.primary.util.L;
import me.gavin.primary.util.VersionHelper;

/**
 * 关于
 *
 * @author gavin.xiong 2017/8/16
 */
public class AboutViewModel extends FragViewModel<AboutFragment, FragAboutBinding> {

    public final ObservableField<String> versionCode = new ObservableField<>();

    AboutViewModel(Context context, AboutFragment fragment, FragAboutBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    public void afterCreate() {
        versionCode.set(VersionHelper.getVersionName(mContext.get()));
    }

    public void license() {
        mFragment.get().start(LicenseFragment.newInstance());
    }

    public void contact() {
        try {
            Intent data = new Intent(Intent.ACTION_SENDTO);
            data.setData(Uri.parse("mailto:gavinxxxxxx@gmail.com"));
            mFragment.get().startActivity(data);
        } catch (Exception e) {
            L.e(e);
        }
    }

//    public void share() {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_TEXT, "fewfeawfewa");
//        intent.setType("text/plain");
//        mFragment.get().startActivity(Intent.createChooser(intent, "分享"));
//    }

}
