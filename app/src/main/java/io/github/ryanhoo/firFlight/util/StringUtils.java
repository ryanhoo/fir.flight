package io.github.ryanhoo.firFlight.util;

import java.util.Collection;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/20/16
 * Time: 6:42 PM
 * Desc: ArrayUtils
 */
public class StringUtils {

    public static <T> String join(T[] array, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (T item : array) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(item.toString());
        }
        return stringBuilder.toString();
    }

    public static <T> String join(Collection<T> array, String delimiter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (T item : array) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(item.toString());
        }
        return stringBuilder.toString();
    }
}
