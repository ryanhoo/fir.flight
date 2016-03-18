package io.github.ryanhoo.firFlight.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.ui.base.OnItemClickListener;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/17/16
 * Time: 2:11 AM
 * Desc: AppAdapter
 */
public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private Context mContext;
    private List<App> mApps;

    private OnItemClickListener<App> mOnItemClickListener;

    public AppAdapter(Context context, List<App> apps) {
        mContext = context;
        mApps = apps;
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
        // holder.textViewDescription.setText(new Gson().toJson(app));
    }

    @Override
    public int getItemCount() {
        if (mApps == null)
            return 0;
        return mApps.size();
    }

    public App getItem(int position) {
        if (mApps == null || position >= mApps.size()) return null;
        return mApps.get(position);
    }

    public void setData(List<App> apps) {
        mApps = apps;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<App> listener) {
        mOnItemClickListener = listener;
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
        @Bind(R.id.text_view_description)
        TextView textViewDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                int position = getAdapterPosition();
                mOnItemClickListener.onItemClick(getItem(position), position);
            }
        }
    }
}
