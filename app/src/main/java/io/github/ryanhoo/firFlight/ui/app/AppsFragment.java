package io.github.ryanhoo.firFlight.ui.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.UserSession;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.ui.base.BaseAdapter;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.helper.SwipeRefreshHelper;
import io.github.ryanhoo.firFlight.ui.webview.WebViewActivity;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/19/16
 * Time: 12:29 AM
 * Desc: AppListFragment
 */
public class AppsFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.OnItemClickListener<App> {

    private static final String TAG = "AppListFragment";

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    AppAdapter mAdapter;
    RetrofitService mRetrofitService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRetrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);

        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new AppAdapter(getActivity(), null);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        SwipeRefreshHelper.refresh(swipeRefreshLayout, new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });

        // Listen for app install/update/remove broadcasts
        registerBroadcast();
    }

    @Override
    public void onDestroy() {
        // Done with listening app install/update/remove broadcasts
        unregisterBroadcast();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        requestApps();
    }

    @Override
    public void onItemClick(App item, int position) {
        startActivity(
                new Intent(getActivity(), WebViewActivity.class)
                        .putExtra(WebViewActivity.EXTRA_TITLE, item.getName())
                        .putExtra(WebViewActivity.EXTRA_URL, item.getAppUrl())
        );
    }

    private void requestApps() {
        Call<List<App>> call = mRetrofitService.apps(UserSession.getInstance().getToken().getAccessToken());
        call.enqueue(new RetrofitCallback<List<App>>() {
            @Override
            public void onSuccess(Call<List<App>> call, Response httpResponse, List<App> apps) {
                mAdapter.setData(apps);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<App>> call, NetworkError error) {
                Log.e(TAG, "onFailure: " + error.getErrorMessage());
                Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    // Broadcasts

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        getActivity().registerReceiver(receiver, intentFilter);
    }

    private void unregisterBroadcast() {
        getActivity().unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent.getAction());
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
