package io.github.ryanhoo.firFlight;

import android.util.Log;
import io.github.ryanhoo.firFlight.account.UserSession;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class NetworkUnitTest {

    private static final String TAG = "NetworkUnitTest";

    static RetrofitService mRetrofitService;
    static UserSession mSession;

    @BeforeClass
    public static void beforeClass() {
        mRetrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);
        mSession = UserSession.getInstance();
    }

    @Test
    public void signIn() {
        System.out.println("signIn");
        Call<Token> call = mRetrofitService.signIn("", "");
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.d(TAG, "apiToken#onResponse: accessToken is " + response.body().getAccessToken());
                mSession.setToken(response.body());
            }

            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                Log.e(TAG, "signIn#onFailure: " + call, throwable);
            }
        });
    }

    @Test
    public void apiToken() {
        if (mSession.isSignedIn()) {
            Call<Token> call = mRetrofitService.apiToken(mSession.getToken().getAccessToken());
            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    Log.d(TAG, "apiToken#onResponse: apiToken is " + response.body().getApiToken());
                    mSession.getToken().setApiToken(response.body().getApiToken());
                }

                @Override
                public void onFailure(Call<Token> call, Throwable throwable) {
                    Log.e(TAG, "apiToken#onFailure: " + call, throwable);
                }
            });
        } else {
            Log.e(TAG, "apiToken: User hasn't sign in yet.");
        }
    }

    @AfterClass
    public static void afterClass() {
        Log.d(TAG, String.format("afterClass: UserSession: [%s, %s]",
                mSession.getToken().getAccessToken(), mSession.getToken().getApiToken()));
    }
}