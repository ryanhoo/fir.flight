package io.github.ryanhoo.firFlight.ui.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.model.Release;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/20/16
 * Time: 4:19 PM
 * Desc: AppInfo
 */
public class AppInfo {

    private static final String TAG = "AppInfo";

    public boolean isInstalled;
    public boolean isUpToDate;

    public String localVersionName;
    public int localVersionCode;

    public Intent launchIntent;

    public App app;

    public AppInfo(Context context, App app) {
        this.app = app;
        Release onlineRelease = app.getMasterRelease();
        String packageName = app.getBundleId();
        String build = onlineRelease.getBuild();
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            if (packageInfo == null) {
                isInstalled = false;
                isUpToDate = false;
            } else {
                isInstalled = true;
                int onlineVersionCode = Integer.parseInt(build);
                localVersionName = packageInfo.versionName;
                localVersionCode = packageInfo.versionCode;
                isUpToDate = localVersionCode >= onlineVersionCode;
                if (isUpToDate) {
                    // http://stackoverflow.com/a/3422824/2290191
                    launchIntent = packageManager.getLaunchIntentForPackage(packageName);
                }
            }
        } catch (PackageManager.NameNotFoundException ignore) {
            // It means app is not installed, no need to throw or log out errors
        } catch (Exception e) {
            Log.w(TAG, String.format("isAppUpToDate %s: [%s, %s]", app.getName(), packageName, build), e);
        }
    }
}
