package io.github.ryanhoo.firFlight.data.source;

import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.User;
import rx.Observable;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 5/31/16
 * Time: 11:51 AM
 * Desc: AppDataSource
 */
public interface DataSourceContract {

    Observable<User> user();

    Observable<List<App>> apps();
}
