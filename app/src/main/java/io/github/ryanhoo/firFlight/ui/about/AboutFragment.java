package io.github.ryanhoo.firFlight.ui.about;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.ryanhoo.firFlight.BuildConfig;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.webview.WebViewHelper;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/24/16
 * Time: 12:33 PM
 * Desc: AboutFragment
 */
public class AboutFragment extends BaseFragment {

    private static final String TAG = "AboutFragment";

    private final static String URL_FIR_IM = "http://fir.im";
    private final static String URL_ACKNOWLEDGEMENTS = "http://github.com/square";
    private final static String URL_AUTHOR = "https://ryanhoo.github.io";

    @Bind(R.id.text_view_app_version)
    TextView textViewViewVersion;
    @Bind(R.id.text_view_flavor_name)
    TextView textViewFlavorName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    @SuppressLint("DefaultLocale")
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {
            PackageInfo packageInfo = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            textViewViewVersion.setText(String.format("%s(%d)", versionName, versionCode));
        } catch (Exception e) {
            Log.e(TAG, "Get app version info", e);
        }
        textViewFlavorName.setText(BuildConfig.FLAVOR);
    }

    @OnClick({R.id.button_fir, R.id.button_acknowledgements, R.id.button_author, R.id.text_view_app_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_fir:
                WebViewHelper.openUrl(getActivity(), URL_FIR_IM);
                break;
            case R.id.button_acknowledgements:
                WebViewHelper.openUrl(getActivity(), URL_ACKNOWLEDGEMENTS);
                break;
            case R.id.button_author:
                WebViewHelper.openUrl(getActivity(), URL_AUTHOR);
                break;
            case R.id.text_view_app_version:
                if (textViewFlavorName.getVisibility() == View.VISIBLE) {
                    textViewFlavorName.setVisibility(View.GONE);
                } else {
                    textViewFlavorName.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
