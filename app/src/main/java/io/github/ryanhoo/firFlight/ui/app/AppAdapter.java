package io.github.ryanhoo.firFlight.ui.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.network.download.AsyncDownloadTask;
import io.github.ryanhoo.firFlight.ui.base.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/17/16
 * Time: 2:11 AM
 * Desc: AppAdapter
 */
public class AppAdapter extends BaseAdapter<App, AppAdapter.ViewHolder> {

    private Map<String, AsyncDownloadTask> mTasks;

    public AppAdapter(Context context, List<App> apps) {
        super(context, apps);
        mTasks = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_app, parent, false));
    }

    @Override
    @SuppressLint("DefaultLocale")
    public void onBindViewHolder(ViewHolder holder, int position) {
        App app = getItem(position);
        holder.appInfo = new AppInfo(mContext, app);

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

        // Hide old version when app is not installed or already up-to-date
        holder.textViewLocalVersion.setVisibility(
                (holder.appInfo.isUpToDate || !holder.appInfo.isInstalled) ? View.GONE : View.VISIBLE);
        if (holder.appInfo.isInstalled && !holder.appInfo.isUpToDate) {
            holder.textViewLocalVersion.setText(String.format("%s(%s)",
                    holder.appInfo.localVersionName, holder.appInfo.localVersionCode));
        }

        if (mTasks.containsKey(app.getId())) {
            AsyncDownloadTask task = mTasks.get(app.getId());
            holder.buttonAction.setText(String.format("%d%%", (int) (task.getProgress() * 100)));
            holder.buttonAction.setEnabled(false);
        } else {
            holder.buttonAction.setEnabled(true);
            holder.buttonAction.setText(!holder.appInfo.isInstalled
                    ? R.string.ff_apps_install
                    : holder.appInfo.isUpToDate ? R.string.ff_apps_open : R.string.ff_apps_update
            );
        }
    }

    @SuppressLint("DefaultLocale")
    public void onButtonProgress(@NonNull ViewHolder holder) {
        App app = holder.appInfo.app;
        if (mTasks.containsKey(app.getId())) {
            AsyncDownloadTask task = mTasks.get(app.getId());
            holder.buttonAction.setText(String.format("%d%%", (int) (task.getProgress() * 100)));
        }
    }

    public void addTask(String appId, AsyncDownloadTask task) {
        mTasks.put(appId, task);
    }

    public void removeTask(String appId) {
        mTasks.remove(appId);
    }

    public Map<String, AsyncDownloadTask> getTasks() {
        return mTasks;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.image_view_icon)
        ImageView imageView;
        @Bind(R.id.text_view_name)
        TextView textViewName;
        @Bind(R.id.text_view_local_version)
        TextView textViewLocalVersion;
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
            textViewLocalVersion.setPaintFlags(textViewLocalVersion.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemView.setOnClickListener(this);
            buttonAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (view instanceof Button) {
                if (getOnItemClickListener() instanceof AppItemClickListener) {
                    ((AppItemClickListener) getOnItemClickListener()).onButtonClick(this, appInfo, position);
                }
            } else {
                onItemClick(getItem(position), position);
            }
        }
    }

    interface AppItemClickListener extends OnItemClickListener<App> {
        @Override
        void onItemClick(App app, int position);

        void onButtonClick(ViewHolder viewHolder, AppInfo appInfo, int position);
    }
}
