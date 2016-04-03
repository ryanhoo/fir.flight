package io.github.ryanhoo.firFlight.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.account.UserSession;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.data.service.RetrofitService;
import io.github.ryanhoo.firFlight.network.RetrofitClient;
import io.github.ryanhoo.firFlight.ui.app.AppInfo;
import io.github.ryanhoo.firFlight.ui.main.MainActivity;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan@whitedew.me
 * Date: 3/23/16
 * Time: 8:35 PM
 * Desc: FlightService
 * - https://guides.codepath.com/android/Managing-Threads-and-Custom-Services#threading-within-the-service
 * - https://guides.codepath.com/android/Starting-Background-Services
 */
public class FlightService extends IntentService {

    private static final String TAG = "FlightService";

    NotificationManager notificationManager;

    public FlightService() {
        super("FlightService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: on thread #" + Thread.currentThread().getName());
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: " + intent.getAction() + " on thread #" + Thread.currentThread().getName());
        if (!UserSession.getInstance().isSignedIn()) return;

        RetrofitService retrofitService = RetrofitClient.defaultInstance().create(RetrofitService.class);
        Call<List<App>> call = retrofitService.apps();
        try {
            Response<List<App>> response = call.execute();
            List<App> apps = response.body();
            for (final App app : apps) {
                AppInfo appInfo = new AppInfo(this, app);
                if (appInfo.isInstalled && !appInfo.isUpToDate) {
                    onAppNewVersion(app);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Request app list: " + call.request().url(), e);
        }
    }

    private void onAppNewVersion(App app) {
        // Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = app.getName();
        String content = getString(R.string.ff_notification_app_new_version_message,
                app.getMasterRelease().getVersion(), app.getMasterRelease().getBuild());
        Log.d(TAG, String.format("%s \t %s", title, content));
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        int notificationId = app.getId().hashCode();
        notificationManager.notify(notificationId, notification);
    }
}
