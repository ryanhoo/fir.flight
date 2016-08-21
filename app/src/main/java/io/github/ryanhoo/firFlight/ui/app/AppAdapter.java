package io.github.ryanhoo.firFlight.ui.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.ui.common.adapter.ListAdapter;
import io.github.ryanhoo.firFlight.ui.common.adapter.OnItemClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 8/21/16
 * Time: 10:48 PM
 * Desc: AppAdapterV2
 */
/* package */ class AppAdapter extends ListAdapter<App, AppItemView> {

    private Map<String, AppDownloadTask> mTasks;
    private AppItemClickListener mItemClickListener;

    /* package */ AppAdapter(Context context, List<App> data) {
        super(context, data);
        mTasks = new HashMap<>();
    }

    @Override
    protected AppItemView createView(Context context) {
        return new AppItemView(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);
        if (holder.itemView instanceof AppItemView && mItemClickListener != null) {
            final AppItemView itemView = (AppItemView) holder.itemView;
            itemView.buttonAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onButtonClick(itemView, holder.getAdapterPosition());
                }
            });
        }
        return holder;
    }

    @Override
    @SuppressLint("DefaultLocale")
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder.itemView instanceof AppItemView) {
            App app = getItem(position);
            AppItemView itemView = (AppItemView) holder.itemView;

            if (mTasks.containsKey(app.getId())) {
                AppDownloadTask.DownloadInfo downloadInfo = mTasks.get(app.getId()).getDownloadInfo();
                // Has downloading task, show progress
                itemView.buttonAction.setText(String.format("%d%%", (int) (downloadInfo.progress * 100)));
                itemView.buttonAction.setEnabled(false);
            } else {
                itemView.buttonAction.setEnabled(true);
                itemView.buttonAction.setText(!itemView.appInfo.isInstalled
                        ? R.string.ff_apps_install
                        : itemView.appInfo.isUpToDate ? R.string.ff_apps_open : R.string.ff_apps_update
                );
            }
        }
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
        if (listener instanceof AppItemClickListener) {
            mItemClickListener = (AppItemClickListener) listener;
        }
    }

    @SuppressLint("DefaultLocale")
    /* package */ void onButtonProgress(@NonNull AppItemView appItemView) {
        App app = appItemView.appInfo.app;
        if (mTasks.containsKey(app.getId())) {
            AppDownloadTask.DownloadInfo downloadInfo = mTasks.get(app.getId()).getDownloadInfo();
            appItemView.buttonAction.setText(String.format("%d%%", (int) (downloadInfo.progress * 100)));
        }
    }

    /* package */ void addTask(String appId, AppDownloadTask task) {
        mTasks.put(appId, task);
    }

    /* package */ void removeTask(String appId) {
        mTasks.remove(appId);
    }

    /* package */ Map<String, AppDownloadTask> getTasks() {
        return mTasks;
    }

    interface AppItemClickListener extends OnItemClickListener {

        @Override
        void onItemClick(int position);

        void onButtonClick(AppItemView itemView, int position);
    }
}
