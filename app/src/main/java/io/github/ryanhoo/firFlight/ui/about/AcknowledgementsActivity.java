package io.github.ryanhoo.firFlight.ui.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.network.NetworkSubscriber;
import io.github.ryanhoo.firFlight.ui.base.BaseActivity;
import io.github.ryanhoo.firFlight.ui.common.DefaultItemDecoration;
import io.github.ryanhoo.firFlight.ui.common.alert.FlightDialog;
import io.github.ryanhoo.firFlight.webview.WebViewHelper;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/17/16
 * Time: 11:29 AM
 * Desc: Acknowledgements
 */
public class AcknowledgementsActivity extends BaseActivity implements AcknowledgementAdapter.AcknowledgementItemClickListener {

    private static final String TAG = "Acknowledgements";
    private static final String DATA_PATH = "thirdparty/list.json";
    private static final String LICENSE_PATH = "thirdparty/";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    FlightDialog mProgressDialog;

    AcknowledgementAdapter mAdapter;
    Map<String, String> mLicenseCache = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledgements);
        ButterKnife.bind(this);
        supportActionBar(toolbar);

        mProgressDialog = FlightDialog.defaultLoadingDialog(this);

        mAdapter = new AcknowledgementAdapter(this, null);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DefaultItemDecoration(
                ContextCompat.getColor(this, R.color.ff_white),
                ContextCompat.getColor(this, R.color.ff_divider),
                getResources().getDimensionPixelSize(R.dimen.ff_padding_large)
        ));

        loadAcknowledgements();
    }

    // OnItemClickListener

    @Override
    public void onItemClick(int position) {
        String url = mAdapter.getItem(position).url;
        WebViewHelper.openUrl(this, url);
    }

    @Override
    public void openLicense(int position) {
        openLicense(mAdapter.getItem(position));
    }

    // Acknowledgements and License

    private void loadAcknowledgements() {
        mProgressDialog.show();
        Subscription subscription = Observable.just(DATA_PATH)
                .flatMap(new Func1<String, Observable<List<Acknowledgement>>>() {
                    @Override
                    public Observable<List<Acknowledgement>> call(String path) {
                        try {
                            InputStream in = getAssets().open(path);
                            List<Acknowledgement> acknowledgements = new Gson().fromJson(
                                    new InputStreamReader(in),
                                    new TypeToken<List<Acknowledgement>>() {
                                    }.getType()
                            );
                            return Observable.just(acknowledgements);
                        } catch (IOException e) {
                            return Observable.error(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetworkSubscriber<List<Acknowledgement>>(this) {
                    @Override
                    public void onNext(List<Acknowledgement> acknowledgements) {
                        mAdapter.setData(acknowledgements);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onUnsubscribe() {
                        mProgressDialog.dismiss();
                    }
                });
        addSubscription(subscription);
    }

    private void openLicense(final Acknowledgement acknowledgement) {
        final String licensePath = LICENSE_PATH + acknowledgement.licensePath;
        if (mLicenseCache.containsKey(licensePath)) {
            showLicenseDialog(acknowledgement.name, mLicenseCache.get(licensePath));
            return;
        }
        mProgressDialog.show();
        Observable.just(licensePath)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String path) {
                        BufferedReader reader = null;
                        try {
                            StringBuilder stringBuilder = new StringBuilder();
                            InputStream in = getAssets().open(path);
                            reader = new BufferedReader(new InputStreamReader(in));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                stringBuilder.append(line);
                                stringBuilder.append('\n');
                            }
                            return Observable.just(stringBuilder.toString());
                        } catch (IOException e) {
                            return Observable.error(e);
                        } finally {
                            try {
                                if (reader != null)
                                    reader.close();
                            } catch (IOException e) {
                                Log.e(TAG, "While reading license at: " + licensePath, e);
                            }
                        }
                    }
                })
                .subscribe(new NetworkSubscriber<String>(this) {
                    @Override
                    public void onNext(String licenseContent) {
                        mLicenseCache.put(licensePath, licenseContent);
                        showLicenseDialog(acknowledgement.name, licenseContent);
                    }

                    @Override
                    public void onUnsubscribe() {
                        mProgressDialog.dismiss();
                    }
                });
    }

    private void showLicenseDialog(String title, String content) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.ff_close, null)
                .show();
    }
}
