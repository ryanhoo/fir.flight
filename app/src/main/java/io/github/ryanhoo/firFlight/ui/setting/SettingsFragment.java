package io.github.ryanhoo.firFlight.ui.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 4/3/16
 * Time: 5:10 PM
 * Desc: SettingsFragment
 */
public class SettingsFragment extends BaseFragment {

    @Bind(R.id.text_view_image_cache)
    TextView textViewImageCache;
    @Bind(R.id.text_view_version)
    TextView textViewVersion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


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
                Glide.get(getActivity()).clearDiskCache();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Must call this on UI thread
                Glide.get(getActivity()).clearMemory();
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
