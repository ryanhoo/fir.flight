package io.github.ryanhoo.firFlight.ui.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.data.model.impl.SystemMessageContent;
import io.github.ryanhoo.firFlight.data.source.MessageRepository;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.common.DefaultItemDecoration;
import io.github.ryanhoo.firFlight.ui.common.adapter.OnItemClickListener;
import io.github.ryanhoo.firFlight.ui.helper.SwipeRefreshHelper;
import io.github.ryanhoo.firFlight.webview.WebViewHelper;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 8:42 PM
 * Desc: MessageListFragment
 */
public class MessagesFragment extends BaseFragment
        implements MessageContract.View, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.empty_view)
    View emptyView;

    MessageAdapter mAdapter;
    MessageContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        SwipeRefreshHelper.setRefreshIndicatorColorScheme(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new MessageAdapter(getActivity(), null);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DefaultItemDecoration(
                ContextCompat.getColor(getContext(), R.color.ff_white),
                ContextCompat.getColor(getContext(), R.color.ff_divider),
                getResources().getDimensionPixelSize(R.dimen.ff_padding_large)
        ));

        new MessagePresenter(MessageRepository.getInstance(), this).subscribe();
    }

    @Override
    public void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        mPresenter.loadMessages();
    }

    @Override
    public void onItemClick(int position) {
        Message message = mAdapter.getItem(position);
        if (message.getContent() instanceof SystemMessageContent) {
            SystemMessageContent messageContent = (SystemMessageContent) message.getContent();
            final String link = messageContent.getLink();
            if (TextUtils.isEmpty(link)) return;

            WebViewHelper.openUrl(getActivity(), link);
        }
    }

    // MVP View

    @Override
    public void showMessages(List<Message> messages) {
        mAdapter.setData(messages);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showOrHideEmptyView() {
        boolean isEmpty = mAdapter.getItemCount() == 0;
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoadingIndicator() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
