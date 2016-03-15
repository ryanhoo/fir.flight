package io.github.ryanhoo.firFlight.ui.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.UserSession;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.ui.base.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/16/16
 * Time: 12:15 AM
 * Desc: SignInFragment
 */
public class SignInFragment extends BaseFragment {

    private static final String TAG = "SignInFragment";

    @Bind(R.id.edit_text_email)
    EditText editTextEmail;
    @Bind(R.id.edit_text_password)
    EditText editTextPassword;

    RetrofitService mRetrofitService;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRetrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.button_signin})
    public void onClick(View view) {
        if (view.getId() == R.id.button_signin) {
            String email = "tps@whitedew.me";
            String password = "";
            if (editTextEmail.length() > 0 && editTextPassword.length() > 0) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
            }
            requestSignIn(email, password);
        }
    }

    private void requestSignIn(String email, String password) {
        Call<Token> call = mRetrofitService.login(email, password);
        call.enqueue(new RetrofitCallback<Token>() {
            @Override
            public void onSuccess(Call<Token> call, Response httpResponse, Token token) {
                Log.d(TAG, "apiToken#onResponse: accessToken is " + token.getAccessToken());
                UserSession.getInstance().setAccessToken(token.getAccessToken());
                requestApiToken(token.getAccessToken());
            }

            @Override
            public void onFailure(Call<Token> call, NetworkError error) {
                Log.e(TAG, "login#onFailure: " + error.getErrorMessage());
            }
        });
    }

    private void requestApiToken(String accessToken) {
        Call<Token> call = mRetrofitService.apiToken(accessToken);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
                Log.d(TAG, "apiToken#onResponse: apiToken is " + token.getApiToken());
                UserSession.getInstance().setApiToken(token.getApiToken());
            }

            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                Log.e(TAG, "apiToken#onFailure: " + call, throwable);
            }
        });
    }
}
