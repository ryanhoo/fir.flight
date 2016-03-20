package io.github.ryanhoo.firFlight.ui.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.UserSession;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.AppInstallInfo;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.network.download.AsyncDownloadTask;
import io.github.ryanhoo.firFlight.network.download.DownloadListener;
import io.github.ryanhoo.firFlight.ui.base.BaseAdapter;
import io.github.ryanhoo.firFlight.util.IntentUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/17/16
 * Time: 2:11 AM
 * Desc: AppAdapter
 */
public class AppAdapter extends BaseAdapter<App, AppAdapter.ViewHolder> {

    private static final String TAG = "AppAdapter";

    private HashMap<String, AsyncDownloadTask> mTasks;

    public AppAdapter(Context context, List<App> apps) {
        super(context, apps);
        mTasks = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_app, parent, false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        App app = getItem(position);
        Glide.with(mContext)
                .load(app.getIconUrl())
                .placeholder(R.color.ff_apps_icon_placeholder)
                .into(holder.imageView);
        holder.textViewName.setText(app.getName());
        holder.textViewVersion.setText(String.format("%s(%s)",
                app.getMasterRelease().getVersion(),
                app.getMasterRelease().getBuild()
        ));
        holder.textViewBundleId.setText(app.getBundleId());

        if (mTasks.containsKey(app.getId())) {
            AsyncDownloadTask task = mTasks.get(app.getId());
            holder.buttonAction.setText(String.format("%d%%", (int) (task.getProgress() * 100)));
            holder.buttonAction.setEnabled(false);
        } else {
            holder.buttonAction.setEnabled(true);
            holder.appInfo = new AppInfo(mContext, app);
            holder.buttonAction.setText(!holder.appInfo.isInstalled
                    ? R.string.ff_apps_install
                    : holder.appInfo.isUpToDate ? R.string.ff_apps_open : R.string.ff_apps_update
            );
        }
    }

    public void addTask(String appId, AsyncDownloadTask task) {
        mTasks.put(appId, task);
    }

    public void removeTask(String appId) {
        mTasks.remove(appId);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.image_view_icon)
        ImageView imageView;
        @Bind(R.id.text_view_name)
        TextView textViewName;
        @Bind(R.id.text_view_version)
        TextView textViewVersion;
        @Bind(R.id.text_view_bundle_id)
        TextView textViewBundleId;
        @Bind(R.id.button_action)
        Button buttonAction;

        AppInfo appInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            buttonAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (view instanceof Button) {
                if (appInfo != null) {
                    if (appInfo.isUpToDate) {
                        mContext.startActivity(appInfo.launchIntent);
                    } else {
                        requestInstallUrl(this, appInfo.app, position);
                    }
                }
            } else {
                onItemClick(getItem(position), position);
            }
        }
    }

    private void requestInstallUrl(final ViewHolder holder, final App app, final int position) {
        holder.buttonAction.setEnabled(false);
        Call<AppInstallInfo> call = RetrofitClient.defaultInstance().create(RetrofitService.class)
                .appInstallInfo(app.getId(), UserSession.getInstance().getToken().getApiToken());
        call.enqueue(new RetrofitCallback<AppInstallInfo>() {
            @Override
            public void onSuccess(Call<AppInstallInfo> call, Response httpResponse, AppInstallInfo appInstallInfo) {
                downloadApp(position, app.getId(), appInstallInfo.getInstallUrl());
            }

            @Override
            public void onFailure(Call<AppInstallInfo> call, NetworkError error) {
                Toast.makeText(mContext, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                holder.buttonAction.setEnabled(true);
            }
        });
    }

    private void downloadApp(final int position, final String appId, final String installUrl) {
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
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onFinish(Uri uri) {
                        Log.d(TAG, "onFinish: ");
                        IntentUtils.install(mContext, uri);
                        removeTask(appId);
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onFail() {
                        Log.e(TAG, "onFail: ");
                        removeTask(appId);
                        notifyItemChanged(position);
                    }
                });
        addTask(appId, downloadTask);
        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
