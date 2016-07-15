package io.github.ryanhoo.firFlight.ui.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 4/3/16
 * Time: 5:10 PM
 * Desc: SettingsActivity
 */
public class SettingsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.text_view_image_cache)
    TextView textViewImageCache;
    @Bind(R.id.text_view_version)
    TextView textViewVersion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        supportActionBar(toolbar);
    }

    @OnClick({R.id.layout_clear_image_cache, R.id.layout_check_updates, R.id.layout_agreements})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_clear_image_cache:
                clearImageCache();
                break;
            case R.id.layout_check_updates:
                checkForUpdates();
                break;
            case R.id.layout_agreements:
                showAgreements();
                break;
        }
    }

    private void clearImageCache() {
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Must NOT call this on UI thread
                Glide.get(SettingsActivity.this).clearDiskCache();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Must call this on UI thread
                Glide.get(SettingsActivity.this).clearMemory();
                showSnake(Snackbar.make(textViewImageCache,
                        R.string.ff_settings_message_cache_cleared, Snackbar.LENGTH_LONG));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void checkForUpdates() {
        showSnake(Snackbar.make(textViewVersion, R.string.ff_settings_message_coming_soon, Snackbar.LENGTH_LONG));
    }

    private void showAgreements() {
        showSnake(Snackbar.make(textViewVersion, R.string.ff_settings_message_coming_soon, Snackbar.LENGTH_LONG));
    }

    private void showSnake(final Snackbar snackbar) {
        snackbar.setAction(R.string.ff_dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
