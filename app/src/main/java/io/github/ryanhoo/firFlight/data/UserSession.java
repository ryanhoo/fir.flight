package io.github.ryanhoo.firFlight.data;

import io.github.ryanhoo.firFlight.data.model.Token;
import io.github.ryanhoo.firFlight.data.model.User;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/15/16
 * Time: 11:22 PM
 * Desc: UserSession
 */
public class UserSession {

    private static UserSession sInstance;

    private Token mToken;
    private User mUser;

    private UserSession() {
        // Empty Constructor
    }

    public static UserSession getInstance() {
        if (sInstance == null) {
            synchronized (UserSession.class) {
                if (sInstance != null)
                    sInstance = new UserSession();
            }
        }
        return sInstance;
    }

    public boolean isSignedIn() {
        return mToken != null && mToken.getAccessToken() != null;
    }

    // Getters & Setters

    public Token getToken() {
        return mToken;
    }

    public void setToken(Token token) {
        this.mToken = token;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }
}
