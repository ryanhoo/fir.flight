package io.github.ryanhoo.firFlight.network;

import com.google.gson.annotations.SerializedName;
import io.github.ryanhoo.firFlight.data.model.App;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 3/19/16
 * Time: 9:21 PM
 * Desc: MultiPageResponse
 */
/*
{
  "apps_count": 2,
  "page_size": 20,
  "items": [app, app]
}
*/
public class AppResponse {

    @SerializedName("apps_count")
    private int appsCount;

    @SerializedName("page_size")
    private int pageSize;

    @SerializedName("items")
    private List<App> apps;

    public int getAppsCount() {
        return appsCount;
    }

    public void setAppsCount(int appsCount) {
        this.appsCount = appsCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }
}
