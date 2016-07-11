package io.github.ryanhoo.firFlight.data.source;

import io.github.ryanhoo.firFlight.data.model.Token;
import rx.Observable;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 7/3/16
 * Time: 9:34 PM
 * Desc: TokenContract
 */
public interface TokenContract {

    interface Local {

        Token token();

        boolean save(Token token);

        boolean delete(Token token);

        boolean deleteAll();
    }

    interface Remote {

        Observable<Token> accessToken(String email, String password);

        Observable<Token> apiToken();

        Observable<Token> refreshApiToken();
    }

    // Local

    boolean storeToken(Token token);

    Token restoreToken();

    // Remote

    Observable<Token> accessToken(String email, String password);

    Observable<Token> apiToken();

    Observable<Token> refreshApiToken();

}
