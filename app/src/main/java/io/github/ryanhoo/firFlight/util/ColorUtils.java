package io.github.ryanhoo.firFlight.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/24/16
 * Time: 9:34 PM
 * Desc: ColorUtils
 */
public class ColorUtils {

    @ColorInt
    @SuppressWarnings("deprecation")
    public static int getColorCompat(Context context, @ColorRes int color) {
        if (context == null) return 0;

        int colorInt = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorInt = context.getResources().getColor(color, context.getTheme());
        } else {
            colorInt = context.getResources().getColor(color);
        }
        return colorInt;
    }
}
