package io.github.ryanhoo.firFlight.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
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

    private static final String PREFS_SESSION = "user-session";
    private static final String KEY_USER = "user";
    private static final String KEY_TOKEN = "token";

    private static UserSession sInstance;

    private static Context sContext;

    private Token mToken;
    private User mUser;

    private Gson mGson;
    private RetrofitService mRetrofitService;

    private UserSession() {
        mGson = new Gson();
        mRetrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);
        restoreSession();
    }

    public static UserSession getInstance() {
        if (sContext == null) {
            throw new RuntimeException("Please init context in Application's onCreate by using UserSession.init(this)");
        }
        if (sInstance == null) {
            synchronized (UserSession.class) {
                if (sInstance == null)
                    sInstance = new UserSession();
            }
        }
        return sInstance;
    }

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public boolean isSignedIn() {
        return mToken != null && mToken.getAccessToken() != null;
    }

    public void signIn(final String email, final String password, final RetrofitCallback<Token> callback) {
        requestSignIn(email, password, callback);
    }

    public void signOut() {
        mToken = null;
        mUser = null;
        sContext.getSharedPreferences(PREFS_SESSION, Context.MODE_PRIVATE)
                .edit().clear().apply();
        // TODO Send global notification
    }

    // Analytics Configs

    private void configAnalytics() {
        FlightAnalytics.configUserSession(this);
    }

    // Requests

    // SignIn Step 1: Request access token
    private void requestSignIn(final String email, final String password, final RetrofitCallback<Token> callback) {
        Call<Token> call = mRetrofitService.signIn(email, password);
        call.enqueue(new RetrofitCallback<Token>() {
            @Override
            public void onSuccess(Call<Token> call, Response httpResponse, Token token) {
                Log.d(TAG, "apiToken#onSuccess: accessToken is " + token.getAccessToken());
                setAccessToken(token.getAccessToken());
                requestApiToken(callback);
                // requestRefreshApiToken(token.getAccessToken(), callback);
            }

            @Override
            public void onFailure(Call<Token> call, NetworkError error) {
                Log.e(TAG, "signIn#onFailure: " + error.getErrorMessage());
                if (callback != null) {
                    callback.onFailure(call, error);
                }
            }
        });
    }

    // SignIn Step 2: Request api token

    private void requestApiToken(final RetrofitCallback<Token> callback) {
        Call<Token> call = mRetrofitService.apiToken();
        call.enqueue(new RetrofitCallback<Token>() {
            @Override
            public void onSuccess(Call<Token> call, Response httpResponse, Token token) {
                // If you've never generate a token in the web page, you won't get any api token
                if (token.getApiToken() == null) {
                    requestRefreshApiToken(callback);
                } else {
                    Log.d(TAG, "apiToken#onSuccess: apiToken is " + token.getApiToken());
                    setApiToken(token.getApiToken());
                    storeSession();
                    // TODO Send global notification
                    if (callback != null) {
                        callback.onSuccess(call, httpResponse, token);
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, NetworkError error) {
                Log.e(TAG, "apiToken#onFailure: " + error.getErrorMessage());
                if (callback != null) {
                    callback.onFailure(call, error);
                }
            }
        });
    }

    // SignIn Step 3: Refresh api token only if necessary, even though refreshing api token in every signIn action
    // would make everything easier and make the code looks better

    private void requestRefreshApiToken(final RetrofitCallback<Token> callback) {
        Call<Token> call = mRetrofitService.refreshApiToken();
        call.enqueue(new RetrofitCallback<Token>() {
            @Override
            public void onSuccess(Call<Token> call, Response httpResponse, Token token) {
                Log.d(TAG, "requestRefreshApiToken#onSuccess: apiToken is " + token.getApiToken());
                setApiToken(token.getApiToken());
                storeSession();
                // TODO Send global notification
                if (callback != null) {
                    callback.onSuccess(call, httpResponse, token);
                }
            }

            @Override
            public void onFailure(Call<Token> call, NetworkError error) {
                Log.e(TAG, "requestRefreshApiToken#onFailure: " + error.getErrorMessage());
                if (callback != null) {
                    callback.onFailure(call, error);
                }
            }
        });
    }

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
                storeSession();
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

    private void storeSession() {
        // TODO encrypt
        sContext.getSharedPreferences(PREFS_SESSION, Context.MODE_PRIVATE).edit()
                .putString(KEY_TOKEN, mGson.toJson(mToken))
                .putString(KEY_USER, mGson.toJson(mUser))
                .apply();
        configAnalytics();
    }

    private void restoreSession() {
        SharedPreferences preferences = sContext.getSharedPreferences(PREFS_SESSION, Context.MODE_PRIVATE);
        String token = preferences.getString(KEY_TOKEN, null);
        String user = preferences.getString(KEY_USER, null);
        if (token != null) {
            mToken = mGson.fromJson(token, Token.class);
            mUser = mGson.fromJson(user, User.class);
        }
        configAnalytics();
    }

    // Getters & Setters

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
