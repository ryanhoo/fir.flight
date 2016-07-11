package io.github.ryanhoo.firFlight.ui.common.alert;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.github.ryanhoo.firFlight.R;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/10/16
 * Time: 4:11 PM
 * Desc: FlightDialog
 */
public class FlightDialog extends Dialog {

    private Context mContext;

    private boolean showLoading = false;
    private int mIconRes;
    private String mMessage;
    private int mMessageRes;

    // Avoid direct instantiation, use Builder to create dialogs
    private FlightDialog(Context context, int style) {
        super(context, style);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0x00FFFFFF));
        setContentView(R.layout.layout_common_dialog);
        View contentView = findViewById(R.id.content_view);
        View layoutIcon = findViewById(R.id.layout_icon);
        ImageView imageViewIcon = (ImageView) findViewById(R.id.image_view_icon);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        TextView textViewMessage = (TextView) findViewById(R.id.text_view_message);
        int padding;
        if (TextUtils.isEmpty(mMessage) && mMessageRes == 0) {
            padding = mContext.getResources().getDimensionPixelSize(R.dimen.ff_padding_larger);
            contentView.setBackgroundResource(R.drawable.bg_common_dialog_circle);
            textViewMessage.setVisibility(View.GONE);
        } else {
            padding = mContext.getResources().getDimensionPixelSize(R.dimen.ff_padding_larger);
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
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public void setIcon(int iconRes) {
        this.mIconRes = iconRes;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public void setMessageRes(int messageRes) {
        this.mMessageRes = messageRes;
    }

    public static FlightDialog defaultLoadingDialog(Context context) {
        return new Builder(context)
                .showLoading(true)
                .animated(true)
                .build();
    }

    public static class Builder implements OnShowListener, OnDismissListener {

        private Context context;
        private boolean animated;
        private boolean showLoading;
        private long dismissAfter;
        private int dialogIconRes;
        private String message;
        private int messageRes;

        // Dialogs are cancelable by default
        private boolean cancelable = true;

        private DialogListener listener;

        private Handler handler;
        private Runnable autoDismissCallback;

        public Builder(Context context) {
            this.context = context;
            this.handler = new Handler();
        }

        public Builder animated(boolean animated) {
            this.animated = animated;
            return this;
        }

        public Builder showLoading(boolean showLoading) {
            this.showLoading = showLoading;
            return this;
        }

        public Builder dismissAfter(long milliseconds) {
            this.dismissAfter = milliseconds;
            return this;
        }

        public Builder icon(int iconRes) {
            this.dialogIconRes = iconRes;
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

        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setListener(DialogListener listener) {
            this.listener = listener;
            return this;
        }

        public FlightDialog build() {
            final FlightDialog dialog;
            if (animated) {
                dialog = new FlightDialog(context, R.style.Common_Dialog_Animated);
            } else {
                dialog = new FlightDialog(context, R.style.Common_Dialog);
            }
            dialog.setShowLoading(showLoading);
            dialog.setIcon(dialogIconRes);
            dialog.setMessage(message);
            dialog.setMessageRes(messageRes);
            dialog.setCancelable(cancelable);
            dialog.setOnShowListener(this);
            dialog.setOnDismissListener(this);
            return dialog;
        }

        public void show() {
            this.build().show();
        }

        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            if (this.listener != null) {
                this.listener.onDismiss();
            }
            // If user forces dismiss this dialog before the scheduled time, cancel that task
            if (autoDismissCallback != null) {
                this.handler.removeCallbacks(autoDismissCallback);
            }
        }

        @Override
        public void onShow(final DialogInterface dialogInterface) {
            if (this.listener != null) {
                this.listener.onShow();
            }
            // If this dialog should be dismissed later automatically, launch a scheduled task
            if (dismissAfter > 0) {
                autoDismissCallback = new Runnable() {
                    @Override
                    public void run() {
                        dialogInterface.dismiss();
                    }
                };
                this.handler.postDelayed(autoDismissCallback, dismissAfter);
            }
        }
    }

    public interface DialogListener {

        void onShow();

        void onDismiss();
    }
}
