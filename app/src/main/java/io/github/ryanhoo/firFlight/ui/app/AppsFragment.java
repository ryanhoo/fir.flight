package io.github.ryanhoo.firFlight.ui.app;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.AppInstallInfo;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.network.download.AsyncDownloadTask;
import io.github.ryanhoo.firFlight.network.download.DownloadListener;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.helper.SwipeRefreshHelper;
import io.github.ryanhoo.firFlight.util.IntentUtils;
import io.github.ryanhoo.firFlight.webview.WebViewHelper;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/19/16
 * Time: 12:29 AM
 * Desc: AppListFragment
 */
public class AppsFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, AppAdapter.AppItemClickListener {

    private static final String TAG = "AppListFragment";

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

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

        SwipeRefreshHelper.setRefreshIndicatorColorScheme(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new AppAdapter(getActivity(), null);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

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
        // Cancel install/update downloading tasks
        Map<String, AsyncDownloadTask> taskMap = mAdapter.getTasks();
        if (taskMap != null && !taskMap.isEmpty()) {
            for (Map.Entry<String, AsyncDownloadTask> entry : taskMap.entrySet()) {
                entry.getValue().cancel(true);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        requestApps();
    }

    @Override
    public void onItemClick(final App app, int position) {
        WebViewHelper.openUrl(getActivity(), app.getName(), app.getAppUrl());
    }

    @Override
    public void onButtonClick(AppAdapter.ViewHolder viewHolder, AppInfo appInfo, int position) {
        if (appInfo != null) {
            if (appInfo.isUpToDate) {
                startActivity(appInfo.launchIntent);
            } else {
                requestInstallUrl(viewHolder, appInfo.app, position);
            }
        }
    }

    private void requestApps() {
        Call<List<App>> call = mRetrofitService.apps();
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

    // Update app

    private void requestInstallUrl(final AppAdapter.ViewHolder holder, final App app, final int position) {
        holder.buttonAction.setEnabled(false);
        Call<AppInstallInfo> call = RetrofitClient.defaultInstance().create(RetrofitService.class)
                .appInstallInfo(app.getId());
        call.enqueue(new RetrofitCallback<AppInstallInfo>() {
            @Override
            public void onSuccess(Call<AppInstallInfo> call, Response httpResponse, AppInstallInfo appInstallInfo) {
                downloadApp(app.getId(), appInstallInfo.getInstallUrl(), position);
            }

            @Override
            public void onFailure(Call<AppInstallInfo> call, NetworkError error) {
                Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                holder.buttonAction.setEnabled(true);
            }
        });
    }

    private void downloadApp(final String appId, final String installUrl, final int position) {
        AsyncDownloadTask downloadTask = new AsyncDownloadTask()
                .setUrl(installUrl)
                .setFileDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                .setListener(new DownloadListener() {
                    @Override
                    public void onStart() {
                        Log.d(TAG, "onStart: ");
                    }

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onProgress(int current, int total, float progress) {
                        // Log.d(TAG, String.format("onProgress: [%d, %d, %.2f]", current, total, progress));
                        updateItemView(appId, position);
                    }

                    @Override
                    public void onFinish(Uri uri) {
                        Log.d(TAG, "onFinish: ");
                        IntentUtils.install(getActivity(), uri);
                        mAdapter.removeTask(appId);
                        mAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onFail() {
                        Log.e(TAG, "onFail: ");
                        mAdapter.removeTask(appId);
                        mAdapter.notifyItemChanged(position);
                    }
                });
        mAdapter.addTask(appId, downloadTask);
        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateItemView(final String appId, final int position) {
        // Only update view holder if it's visible on the screen
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

        if (position < firstVisibleItem || position > lastVisibleItem) return;

        // Get the view holder by position
        View itemView = layoutManager.getChildAt(position - firstVisibleItem);

        if (itemView == null) return;

        AppAdapter.ViewHolder viewHolder
                = (AppAdapter.ViewHolder) recyclerView.getChildViewHolder(itemView);

        if (viewHolder == null || viewHolder.appInfo == null) return;
        if (viewHolder.appInfo.app == null || !appId.equals(viewHolder.appInfo.app.getId())) return;

        mAdapter.onButtonProgress(viewHolder);
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
