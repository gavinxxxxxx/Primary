package gavin.primary.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * A FrameLayout subclass that dispatches WindowInsets to its children instead of adjusting its padding.
 * Useful for Fragment containers.
 *
 * @author Pkmmte Xeleon
 * @author  https://gist.github.com/Pkmmte/d8e983fb9772d2c91688
 * @see http://stackoverflow.com/questions/31190612/fitssystemwindows-effect-gone-for-fragments-added-via-fragmenttransaction
 */
public class WindowInsetsFrameLayout extends FrameLayout {
	public WindowInsetsFrameLayout(Context context) {
		super(context);
	}

	public WindowInsetsFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
	@Override
	public WindowInsets onApplyWindowInsets(WindowInsets insets) {
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++)
			getChildAt(index).dispatchApplyWindowInsets(insets);
		return insets;
	}
}