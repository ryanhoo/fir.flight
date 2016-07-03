package io.github.ryanhoo.firFlight.ui.helper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import io.github.ryanhoo.firFlight.R;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/22/16
 * Time: 1:10 PM
 * Desc: SwipeRefreshHelper
 */
public class SwipeRefreshHelper {

    // Give time for refresh indicator to perform animation
    private static final long REFRESH_RUNNABLE_DELAY = 1000;

    /**
     * SwipeRefreshLayout indicator can't be triggered manually in onCreate
     * - https://code.google.com/p/android/issues/detail?id=77712
     */
    public static void refresh(SwipeRefreshLayout swipeRefreshLayout, Runnable refreshRunnable) {
        swipeRefreshLayout.setProgressViewOffset(
                false,
                0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        swipeRefreshLayout.getResources().getDisplayMetrics())
        );
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.postDelayed(refreshRunnable, REFRESH_RUNNABLE_DELAY);
    }

    public static void setRefreshIndicatorColorScheme(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_colorscheme_first,
                R.color.swiperefresh_colorscheme_secondary,
                R.color.swiperefresh_colorscheme_third
        );
    }
}
