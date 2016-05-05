package io.github.ryanhoo.firFlight.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import io.github.ryanhoo.firFlight.FlightApplication;
import io.github.ryanhoo.firFlight.analytics.FlightAnalytics;
import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.model.User;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.NetworkError;
import io.github.ryanhoo.firFlight.network.RetrofitCallback;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/15/16
 * Time: 11:22 PM
 * Desc: UserSession
 */
public class UserSession {

    private static final String TAG = "UserSession";

    private static final String PREFS_SESSION_FORMAT = "session-%s";
    private static final String KEY_USER = "user";
    private static final String KEY_TOKEN = "token";

    private static UserSession sInstance;

    private static Context sContext;

    private Account mAccount;
    private Token mToken;
    private User mUser;

    private Gson mGson;
    private RetrofitService mRetrofitService;

    private UserSession() {
        sContext = FlightApplication.getInstance().getApplicationContext();
        mGson = new Gson();
        mRetrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);
        mAccount = AccountManager.getCurrentAccount(sContext);
        if (mAccount != null) {
            restoreSession(mAccount);
        }
    }

    public static UserSession getInstance() {
        if (sInstance == null) {
            synchronized (UserSession.class) {
                if (sInstance == null)
                    sInstance = new UserSession();
            }
        }
        return sInstance;
    }

    public boolean isSignedIn() {
        return mToken != null && mToken.getAccessToken() != null;
    }

    public void signIn(final String email, final String password, final SignInCallback callback) {
        new AsyncSignInTask(sContext)
                .setEmail(email)
                .setPassword(password)
                .callback(callback)
                .executeOnExecutor(AsyncSignInTask.THREAD_POOL_EXECUTOR);
    }

    public void signOut() {
        getSharedPreferences(mAccount).edit().clear().apply();
        AccountManager.removeAccount(sContext, mAccount);
        mAccount = null;
        mToken = null;
        mUser = null;
        // TODO Send global notification
    }

    // Analytics Configs

    private void configAnalytics() {
        FlightAnalytics.configUserSession(this);
    }

    // Requests

    public void updateUser(final RetrofitCallback<User> callback) {
        if (!isSignedIn()) {
            signOut();
            return;
        }
        Call<User> call = mRetrofitService.user();
        call.enqueue(new RetrofitCallback<User>() {
            @Override
            public void onSuccess(Call<User> call, Response httpResponse, User user) {
                Log.d(TAG, "updateUser#onSuccess: user is " + user.getName());
                mUser = user;
                storeSession(mAccount);
                // TODO Send global notification
                if (callback != null) {
                    callback.onSuccess(call, httpResponse, user);
                }
            }

            @Override
            public void onFailure(Call<User> call, NetworkError error) {
                Log.e(TAG, "updateUser#onFailure: " + error.getErrorMessage());
                if (callback != null) {
                    callback.onFailure(call, error);
                }
            }
        });
    }

    // Session Store & Restore

    private SharedPreferences getSharedPreferences(Account account) {
        return sContext.getSharedPreferences(String.format(PREFS_SESSION_FORMAT,
                account.getName()), Context.MODE_PRIVATE);
    }

    /* package */ void storeSession(Account account) {
        // TODO encrypt
        getSharedPreferences(account).edit()
                .putString(KEY_TOKEN, mGson.toJson(mToken))
                .putString(KEY_USER, mGson.toJson(mUser))
                .apply();
        configAnalytics();
    }

    /* package */ void restoreSession(Account account) {
        SharedPreferences preferences = getSharedPreferences(account);
        String token = preferences.getString(KEY_TOKEN, null);
        String user = preferences.getString(KEY_USER, null);
        if (token != null) {
            mToken = mGson.fromJson(token, Token.class);
            mUser = mGson.fromJson(user, User.class);
        }
        configAnalytics();
    }

    /* package */ User getUserOfAccount(Account account) {
        SharedPreferences preferences = getSharedPreferences(account);
        String user = preferences.getString(KEY_USER, null);
        return mGson.fromJson(user, User.class);
    }

    // Getters & Setters

    public Account getAccount() {
        return mAccount;
    }

    public void setAccount(Account account) {
        this.mAccount = account;
    }

    public Token getToken() {
        return mToken;
    }

    public void setToken(Token token) {
        this.mToken = token;
    }

    public void setAccessToken(String accessToken) {
        if (mToken == null) {
            mToken = new Token();
        }
        mToken.setAccessToken(accessToken);
    }

    public void setApiToken(String apiToken) {
        if (mToken == null) {
            mToken = new Token();
        }
        mToken.setApiToken(apiToken);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
}
