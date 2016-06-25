package io.github.ryanhoo.firFlight.ui.helper;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 2/4/16
 * Time: 9:41 PM
 * Desc: GlobalLayoutHelper
 */
public class GlobalLayoutHelper implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "GlobalLayoutHelper";

    private WeakReference<View> mViewReference;
    private Runnable mOnGlobalLayoutRunnable;

    public GlobalLayoutHelper() {
    }

    public void attachView(View view, Runnable onGlobalLayoutRunnable) {
        if (view == null || onGlobalLayoutRunnable == null) {
            Log.e(TAG, "attachView: view is " + view + ", layoutRunnable is " + onGlobalLayoutRunnable);
            return;
        }
        mViewReference = new WeakReference<>(view);
        mOnGlobalLayoutRunnable = onGlobalLayoutRunnable;
        view.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onGlobalLayout() {
        View view = mViewReference.get();
        if (view == null) return;

        view.post(mOnGlobalLayoutRunnable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        else
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }
}
