package io.github.ryanhoo.firFlight.ui.message;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.data.model.impl.SystemMessageContent;
import io.github.ryanhoo.firFlight.ui.common.adapter.IAdapterView;
import io.github.ryanhoo.firFlight.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/21/16
 * Time: 6:01 PM
 * Desc: MessageItemView
 */
public class MessageItemView extends RelativeLayout implements IAdapterView<Message> {
    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm";

    @Bind(R.id.text_view_message)
    TextView textViewMessage;
    @Bind(R.id.text_view_time)
    TextView textViewTime;

    private SimpleDateFormat mDateFormatter;

    public MessageItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_message, this);
        ButterKnife.bind(this);
        mDateFormatter = new SimpleDateFormat(DATE_FORMATTER, Locale.getDefault());
    }

    @Override
    public void bind(Message message, int position) {
        if (message.getContent() instanceof SystemMessageContent) {
            SystemMessageContent messageContent = (SystemMessageContent) message.getContent();
            String content = messageContent.getTitle();
            List<String> urls = HtmlUtils.getUrlsFromHtml(content);
            if (urls.size() > 0) {
                messageContent.setLink(urls.get(0));
            }
            textViewMessage.setText(getPureMessageContent(content));
        }
        textViewTime.setText(mDateFormatter.format(message.getCreatedAt()));
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
