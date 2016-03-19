package io.github.ryanhoo.firFlight.ui.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.ui.base.BaseAdapter;
import io.github.ryanhoo.firFlight.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/19/16
 * Time: 8:43 PM
 * Desc: MessageAdapter
 */
public class MessageAdapter extends BaseAdapter<Message, MessageAdapter.ViewHolder> {

    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm";

    private SimpleDateFormat mDateFormatter;

    public MessageAdapter(Context context, List<Message> data) {
        super(context, data);
        mDateFormatter = new SimpleDateFormat(DATE_FORMATTER, Locale.getDefault());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getLayoutInflater().inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = getItem(position);
        // TODO Message Content Deserializer
        String content = message.getContent().get("title").toString();
        holder.textViewMessage.setText(getPureMessageContent(content));
        List<String> urls = HtmlUtils.getUrlsFromHtml(content);
        if (urls.size() > 0) {
            message.getContent().put("link", urls.get(0));
        }
        holder.textViewTime.setText(mDateFormatter.format(message.getCreatedAt()));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.text_view_message)
        TextView textViewMessage;
        @Bind(R.id.text_view_time)
        TextView textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onItemClick(getItem(position), position);
        }
    }

    private Spannable getPureMessageContent(String originalContent) {
        Spanned spannedHtmlText = Html.fromHtml(originalContent);
        String pureContent = spannedHtmlText.toString();
        final String SPAN_TO_REMOVE = "\n\n";
        if (pureContent.endsWith(SPAN_TO_REMOVE)) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spannedHtmlText);
            stringBuilder.replace(stringBuilder.length() - SPAN_TO_REMOVE.length(), stringBuilder.length(), "");
            return stringBuilder;
        }
        return new SpannableStringBuilder(spannedHtmlText.toString().replaceAll(SPAN_TO_REMOVE, ""));
    }
}
