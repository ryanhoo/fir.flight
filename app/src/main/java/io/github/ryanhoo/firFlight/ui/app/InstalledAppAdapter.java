package io.github.ryanhoo.firFlight.ui.app;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 4/5/16
 * Time: 10:31 PM
 * Desc: InstalledAppAdapter
 */
public class InstalledAppAdapter extends ArrayAdapter<InstalledApp> {

    public InstalledAppAdapter(Context context, InstalledApp[] objects) {
        super(context, 0, objects);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
