package io.github.ryanhoo.firFlight.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.UserSession;
import io.github.ryanhoo.firFlight.data.model.Message;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.MultiPageResponse;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import io.github.ryanhoo.firFlight.ui.base.OnItemClickListener;
import io.github.ryanhoo.firFlight.ui.common.WebViewActivity;
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
public class MessageListFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener<Message> {

    private static final String TAG = "MessageListFragment";

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

        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new MessageAdapter(getActivity(), null);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        requestSystemNotifications();
    }

    @Override
    public void onItemClick(Message message, int position) {
        if (message.getContent().containsKey("link")) {
            String link = (String) message.getContent().get("link");
            startActivity(
                    new Intent(getActivity(), WebViewActivity.class)
                            .putExtra(WebViewActivity.EXTRA_URL, link)
            );
        }
    }

    private void requestSystemNotifications() {
        final String token = UserSession.getInstance().getToken().getAccessToken();
        Call<MultiPageResponse<Message>> call = mRetrofitService.systemNotifications(token);
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
