package io.github.ryanhoo.firFlight.ui.app;

import android.content.Context;
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
import io.github.ryanhoo.firFlight.ui.base.BaseAdapter;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/17/16
 * Time: 2:11 AM
 * Desc: AppAdapter
 */
public class AppAdapter extends BaseAdapter<App, AppAdapter.ViewHolder> {

    public AppAdapter(Context context, List<App> apps) {
        super(context, apps);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_app, parent, false));
    }

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

        holder.appInfo = new AppInfo(mContext, app);
        holder.buttonAction.setText(!holder.appInfo.isInstalled
                ? R.string.ff_apps_install
                : holder.appInfo.isUpToDate ? R.string.ff_apps_open : R.string.ff_apps_update
        );
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
                if (appInfo != null && appInfo.isUpToDate) {
                    mContext.startActivity(appInfo.launchIntent);
                }
            } else {
                onItemClick(getItem(position), position);
            }
        }
    }
}
