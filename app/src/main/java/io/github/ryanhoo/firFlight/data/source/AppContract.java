package io.github.ryanhoo.firFlight.data.source;

import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.AppInstallInfo;
import rx.Observable;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 6/26/16
 * Time: 12:17 AM
 * Desc: AppContract
 */
public interface AppContract {

    interface Local {

        Observable<List<App>> apps();

        boolean save(App app);

        int save(List<App> apps);

        boolean delete(App app);

        int delete(List<App> apps);

        int deleteAll();
    }

    interface Remote {

        Observable<List<App>> apps();

        Observable<AppInstallInfo> appInstallInfo(String appId);
    }

    Observable<List<App>> apps();

    Observable<List<App>> apps(boolean forceUpdate);

    Observable<AppInstallInfo> appInstallInfo(String appId);

}
