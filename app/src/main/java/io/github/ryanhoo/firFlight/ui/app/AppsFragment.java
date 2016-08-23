package io.github.ryanhoo.firFlight.ui.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.source.AppRepository;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.common.DefaultItemDecoration;
import io.github.ryanhoo.firFlight.ui.helper.SwipeRefreshHelper;
import io.github.ryanhoo.firFlight.util.AppUtils;
import io.github.ryanhoo.firFlight.util.IntentUtils;
import io.github.ryanhoo.firFlight.webview.WebViewHelper;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 12:29 AM
 * Desc: AppListFragment
 */
public class AppsFragment extends BaseFragment
        implements AppContract.View, SwipeRefreshLayout.OnRefreshListener, AppAdapter.AppItemClickListener {

    private static final String TAG = "AppListFragment";

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    @Bind(R.id.empty_view)
    View emptyView;

    AppAdapter mAdapter;

    AppContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        SwipeRefreshHelper.setRefreshIndicatorColorScheme(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new AppAdapter(getActivity(), null);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DefaultItemDecoration(
                ContextCompat.getColor(getContext(), R.color.ff_white),
                ContextCompat.getColor(getContext(), R.color.ff_divider),
                getContext().getResources().getDimensionPixelSize(R.dimen.ff_padding_large)
        ));

        new AppPresenter(AppRepository.getInstance(), this).subscribe();

        // Listen for app install/update/remove broadcasts
        registerBroadcast();
    }

    @Override
    public void onDestroy() {
        // Done with listening app install/update/remove broadcasts
        unregisterBroadcast();
        super.onDestroy();
    }

    // MVP View

    @Override
    public void onAppsLoaded(List<App> apps) {
        mAdapter.setData(apps);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadAppStarted() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onLoadAppCompleted() {
        swipeRefreshLayout.setRefreshing(false);
        boolean isEmpty = mAdapter.getItemCount() == 0;
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void addTask(String appId, AppDownloadTask task) {
        mAdapter.addTask(appId, task);
    }

    @Override
    public void removeTask(String appId) {
        mAdapter.removeTask(appId);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateAppInfo(String appId, int position) {
        updateItemView(appId, position);
    }

    @Override
    public void installApk(Uri apkUri) {
        IntentUtils.install(getActivity(), apkUri);
    }

    @Override
    public void setPresenter(AppContract.Presenter presenter) {
        mPresenter = presenter;
    }

    // SwipeRefreshListener

    @Override
    public void onRefresh() {
        mPresenter.loadApps();
    }

    // AppItemClickListener

    @Override
    public void onItemClick(int position) {
        App app = mAdapter.getItem(position);
        WebViewHelper.openUrl(getActivity(), app.getName(), AppUtils.getAppUrlByShort(app.getShortUrl()));
    }

    @Override
    public void onButtonClick(final AppItemView itemView, final int position) {
        final AppInfo appInfo = itemView.appInfo;
        if (appInfo != null) {
            if (appInfo.isUpToDate) {
                startActivity(appInfo.launchIntent);
            } else {
                mPresenter.requestInstallUrl(itemView, position);
            }
        }
    }

    // Update app

    private void updateItemView(final String appId, final int position) {
        // Only update view holder if it's visible on the screen
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

        if (position < firstVisibleItem || position > lastVisibleItem) return;

        // Get the view holder by position
        View itemView = layoutManager.getChildAt(position - firstVisibleItem);

        if (itemView instanceof AppItemView) {
            AppItemView appView = (AppItemView) itemView;
            if (appView.appInfo == null) return;
            if (appView.appInfo.app == null || !appId.equals(appView.appInfo.app.getId())) return;

            mAdapter.onButtonProgress(appView);
        }
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
