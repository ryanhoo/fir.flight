package io.github.ryanhoo.firFlight.account;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import io.github.ryanhoo.firFlight.analytics.FlightAnalytics;
import io.github.ryanhoo.firFlight.analytics.FlightEvent;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import retrofit2.Call;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 4/2/16
 * Time: 4:46 PM
 * Desc: AsyncSignInTask
 */
public class AsyncSignInTask extends AsyncTask<String, Integer, Boolean> {

    private static final String TAG = "AsyncSignInTask";

    private WeakReference<Context> mContextRef;

    public AsyncSignInTask(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    private String email;
    private String password;
    private SignInCallback signInCallback;

    private void onSignInFail(Throwable throwable, String message) {
        Log.e(TAG, String.format("doInBackground: Sign in failed with email %s and password %s", email, password), throwable);
        if (message != null) {
            Log.e(TAG, message);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (params != null && params.length == 2) {
            email = params[0];
            password = params[1];
        }
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            onSignInFail(null, "Email or password is empty");
            return false;
        }
        RetrofitService retrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);
        try {
            Token token = new Token();
            // Step 1: Access Token
            Call<Token> accessTokenCall = retrofitService.accessToken(email, password);
            Token accessToken = accessTokenCall.execute().body();
            if (accessToken == null || TextUtils.isEmpty(accessToken.getAccessToken())) {
                onSignInFail(null, "Access token is null");
                return false;
            }
            token.setAccessToken(accessToken.getAccessToken());
            UserSession.getInstance().setAccessToken(accessToken.getAccessToken());
            Log.d(TAG, "doInBackground: accessToken is " + accessToken.getAccessToken());
            // Step 2: Api Token
            Call<Token> apiTokenCall = retrofitService.apiToken();
            Token apiToken = apiTokenCall.execute().body();
            if (apiToken == null || TextUtils.isEmpty(apiToken.getApiToken())) {
                Call<Token> refreshApiTokenCall = retrofitService.refreshApiToken();
                apiToken = refreshApiTokenCall.execute().body();
                if (apiToken == null || TextUtils.isEmpty(apiToken.getApiToken())) {
                    onSignInFail(null, "Api token is null, refresh api token failed");
                    FlightAnalytics.onEvent(new FlightEvent(FlightEvent.EVENT_API_TOKEN)
                            .putCustomAttribute(FlightEvent.KEY_EMAIL, email)
                    );
                    return false;
                }
            }
            token.setApiToken(apiToken.getApiToken());
            UserSession.getInstance().setApiToken(apiToken.getApiToken());
            Log.d(TAG, "doInBackground: api token is " + apiToken.getApiToken());
            // Step 3: User
            Call<User> userCall = retrofitService.user();
            User user = userCall.execute().body();
            if (user == null) {
                onSignInFail(null, "User is null");
                return false;
            }
            Log.d(TAG, String.format("doInBackground: user is %s\n%s\n%s\n%s",
                    user.getId(), user.getName(), user.getEmail(), user.getGravatar()));
            UserSession.getInstance().setUser(user);

            // Add Account and Session
            Context context = mContextRef.get();
            if (context != null) {
                Account account = new Account(user.getEmail());
                UserSession.getInstance().setAccount(account);
                UserSession.getInstance().storeSession(account);
                AccountManager.addAccount(context, account);
            }
        } catch (IOException e) {
            onSignInFail(e, null);
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (signInCallback == null && mContextRef.get() == null) return;
        if (result) {
            signInCallback.success();
        } else {
            signInCallback.fail();
        }
    }

    public AsyncSignInTask setEmail(String email) {
        this.email = email;
        return this;
    }

    public AsyncSignInTask setPassword(String password) {
        this.password = password;
        return this;
    }

    public AsyncSignInTask callback(SignInCallback callback) {
        this.signInCallback = callback;
        return this;
    }
}
