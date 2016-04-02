package io.github.ryanhoo.firFlight.ui.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.data.model.impl.SystemMessageContent;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.MultiPageResponse;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.ui.base.BaseAdapter;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.helper.SwipeRefreshHelper;
import io.github.ryanhoo.firFlight.webview.WebViewHelper;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/19/16
 * Time: 8:42 PM
 * Desc: MessageListFragment
 */
public class MessagesFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, BaseAdapter.OnItemClickListener<Message> {

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    MessageAdapter mAdapter;
    RetrofitService mRetrofitService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRetrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);

        SwipeRefreshHelper.setRefreshIndicatorColorScheme(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new MessageAdapter(getActivity(), null);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        SwipeRefreshHelper.refresh(swipeRefreshLayout, new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        requestSystemNotifications();
    }

    @Override
    public void onItemClick(Message message, int position) {
        if (message.getContent() instanceof SystemMessageContent) {
            SystemMessageContent messageContent = (SystemMessageContent) message.getContent();
            final String link = messageContent.getLink();
            if (TextUtils.isEmpty(link)) return;

            WebViewHelper.openUrl(getActivity(), link);
        }
    }

    private void requestSystemNotifications() {
        Call<MultiPageResponse<Message>> call = mRetrofitService.systemNotifications();
        call.enqueue(new RetrofitCallback<MultiPageResponse<Message>>() {
            @Override
            public void onSuccess(Call<MultiPageResponse<Message>> call, Response httpResponse, MultiPageResponse<Message> messageMultiPageResponse) {
                List<Message> messages = messageMultiPageResponse.getData();
                mAdapter.setData(messages);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MultiPageResponse<Message>> call, NetworkError error) {
                Toast.makeText(getActivity(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
