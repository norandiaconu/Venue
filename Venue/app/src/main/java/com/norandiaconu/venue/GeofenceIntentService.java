package com.norandiaconu.venue;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

/**
 * Created by NORAN on 9/17/2015.
 */
public class GeofenceIntentService extends IntentService {
    public GeofenceIntentService() {
        super("GeofenceIntentService");
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(!geofencingEvent.hasError()) {
            int transition = geofencingEvent.getGeofenceTransition();
            String notificationTitle;

            switch (transition) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    notificationTitle = "Geofence Entered";
                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    notificationTitle = "Geofence Dwell";
                    openEntrance();
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    notificationTitle = "Geofence Exit";
                    break;
                default:
                    notificationTitle = "Geofence Unknown";
            }
            sendNotification(this, getTriggeringGeofences(intent), notificationTitle);
        }
    }

    private void sendNotification(Context context, String notificationText, String notificationTitle) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();

        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(false);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        wakeLock.release();
    }

    private String getTriggeringGeofences(Intent intent) {
        GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
        List<Geofence> geofences = geofenceEvent
                .getTriggeringGeofences();

        String[] geofenceIds = new String[geofences.size()];

        for (int i = 0; i < geofences.size(); i++) {
            geofenceIds[i] = geofences.get(i).getRequestId();
        }

        return TextUtils.join(", ", geofenceIds);
    }

    public void openEntrance() {
        Intent intent = new Intent(this, EntranceMessage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
