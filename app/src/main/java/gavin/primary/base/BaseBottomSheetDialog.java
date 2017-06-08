package gavin.primary.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;

import javax.inject.Inject;

import gavin.primary.inject.component.ApplicationComponent;
import gavin.primary.service.base.DataLayer;

/**
 * BottomSheetDialog 基类
 *
 * @author gavin.xiong 2017/5/27
 */
public abstract class BaseBottomSheetDialog extends BottomSheetDialog {

    @Inject
    DataLayer mDataLayer;

    public BaseBottomSheetDialog(@NonNull Context context) {
        super(context);
        ApplicationComponent.Instance.get().inject(this);
    }

    public DataLayer getDataLayer() {
        return mDataLayer;
    }
}
