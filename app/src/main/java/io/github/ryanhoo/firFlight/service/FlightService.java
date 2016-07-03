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
import io.github.ryanhoo.firFlight.data.source.AppRepository;
import io.github.ryanhoo.firFlight.ui.app.AppInfo;
import io.github.ryanhoo.firFlight.ui.main.MainActivity;
import rx.Subscriber;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
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

        Log.d(TAG, "Start requesting apps...");
        AppRepository.getInstance().apps(true)
                .subscribe(new Subscriber<List<App>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Request apps completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Request apps: ", e);

                    }

                    @Override
                    public void onNext(List<App> apps) {
                        for (final App app : apps) {
                            AppInfo appInfo = new AppInfo(FlightService.this, app);
                            if (appInfo.isInstalled && !appInfo.isUpToDate) {
                                onAppNewVersion(app);
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroy Update Service");
        super.onDestroy();
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
