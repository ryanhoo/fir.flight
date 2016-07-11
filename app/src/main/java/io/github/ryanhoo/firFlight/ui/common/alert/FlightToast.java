package io.github.ryanhoo.firFlight.ui.common.alert;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import io.github.ryanhoo.firFlight.R;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/10/16
 * Time: 6:28 PM
 * Desc: FlightToast
 */
public class FlightToast {

    public static final long MAX_DURATION = 2000; // Toast.LENGTH_LONG

    private Context mContext;
    private Handler mHandler;

    private boolean showLoading;
    private int mIconRes;
    private int mMessageRes;
    private String mMessage;
    private long mDuration = MAX_DURATION; // Default duration

    private FlightToast(Context context) {
        // Avoid direct instantiation
        mContext = context;
        mHandler = new Handler();
    }

    public void show() {
        final Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(customView());
        toast.setDuration(Toast.LENGTH_LONG);
        if (mDuration < MAX_DURATION && mDuration > 0) {
            // Cancel at a specific time
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, mDuration);
        }
        toast.show();
    }

    private View customView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_common_dialog, null, false);
        View contentView = view.findViewById(R.id.content_view);
        View layoutIcon = view.findViewById(R.id.layout_icon);
        ImageView imageViewIcon = (ImageView) view.findViewById(R.id.image_view_icon);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        TextView textViewMessage = (TextView) view.findViewById(R.id.text_view_message);
        int padding;
        if (TextUtils.isEmpty(mMessage) && mMessageRes == 0) {
            padding = mContext.getResources().getDimensionPixelSize(R.dimen.ff_padding_larger);
            contentView.setBackgroundResource(R.drawable.bg_common_dialog_circle);
            textViewMessage.setVisibility(View.GONE);
        } else {
            padding = mContext.getResources().getDimensionPixelSize(R.dimen.ff_padding_large);
            contentView.setBackgroundResource(R.drawable.bg_common_dialog_rounded_rect);
            textViewMessage.setVisibility(View.VISIBLE);
            if (mMessage == null) {
                textViewMessage.setText(mMessageRes);
            } else {
                textViewMessage.setText(mMessage);
            }
        }
        contentView.setPadding(padding, padding, padding, padding);
        if (showLoading) {
            layoutIcon.setVisibility(View.VISIBLE);
            imageViewIcon.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else if (mIconRes == 0) {
            layoutIcon.setVisibility(View.GONE);
            textViewMessage.setPadding(0, 0, 0, 0);
        } else {
            layoutIcon.setVisibility(View.VISIBLE);
            imageViewIcon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            imageViewIcon.setImageResource(mIconRes);
        }
        return view;
    }

    // Setters

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public void setIcon(int icon) {
        this.mIconRes = icon;
    }

    public void setMessage(int message) {
        this.mMessageRes = message;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public static class Builder {

        private Context context;
        private boolean showLoading;
        private int iconRes;
        private int messageRes;
        private String message;
        private long duration;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder showLoading(boolean showLoading) {
            this.showLoading = showLoading;
            return this;
        }

        public Builder icon(int icon) {
            this.iconRes = icon;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder message(int messageRes) {
            this.messageRes = messageRes;
            return this;
        }

        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public FlightToast build() {
            FlightToast toast = new FlightToast(context);
            toast.setShowLoading(showLoading);
            toast.setIcon(iconRes);
            toast.setMessage(message);
            toast.setMessage(messageRes);
            toast.setDuration(duration);
            return toast;
        }

        public void show() {
            this.build().show();
        }
    }
}
