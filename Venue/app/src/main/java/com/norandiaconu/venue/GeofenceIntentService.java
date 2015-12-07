package com.norandiaconu.venue;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by NORAN on 9/17/2015.
 */
public class GeofenceIntentService extends IntentService {

    String notText = "";
    String globalId = "";
    String globalName = "";
    String globalUrl = "";
    boolean dwelling = false;

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
                    notificationTitle = "Geofence Entered";
                    dwelling = true;
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    notificationTitle = "Geofence Exited";
                    break;
                default:
                    notificationTitle = "Geofence Unknown";
            }
            sendNotification(this, getTriggeringGeofences(intent), notificationTitle);
            if(dwelling == true){
                openEntrance();
            }
        }
    }

    private void sendNotification(Context context, String notificationText, String notificationTitle) {
        notText = notificationText;
        //PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //PowerManager.WakeLock wakeLock = pm.newWakeLock(
        //        PowerManager.PARTIAL_WAKE_LOCK, "");
        //wakeLock.acquire();

        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setDefaults(Notification.DEFAULT_ALL).setAutoCancel(false);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

        //wakeLock.release();
    }

    public String getTriggeringGeofences(Intent intent) {
        GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
        List<Geofence> geofences = geofenceEvent
                .getTriggeringGeofences();

        String[] geofenceIds;
        geofenceIds = new String[geofences.size()];

        for (int i = 0; i < geofences.size(); i++) {
            geofenceIds[i] = geofences.get(i).getRequestId();
            globalName = geofences.get(i).getRequestId();
        }
        moreLocations();
        String combined = TextUtils.join(", ", geofenceIds);
        //String note = combined + globalId;
        String note = combined;
        return note;
    }

    public void moreLocations(){
        SyncHttpClient client = new SyncHttpClient();
        client.setBasicAuth("admin", "superstrongpassword");
        client.get("https://venue-api.herokuapp.com/locations/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray locations) {
                Log.v("", "STATUS2: " + statusCode);
                Log.v("", locations.toString());
                Log.v("", "LENGTH: " + locations.length());
                int locationsLength = locations.length();
                for (int i = 0; i < locationsLength; i++) {
                    try {
                        Log.v("", "ENTER LOOP TIME: " + i);

                        JSONObject location = locations.getJSONObject(i);
                        Log.i("", location.toString());
                        String name = location.getString("name");
                        String id = location.getString("id");
                        String url = location.getString("messages");
                        Log.v("", "ID: " + id);
                        Log.v("", "GN: " + globalName + "; NAME: " + name);
                        if(globalName.equals(name)){
                            globalId = id;
                            globalUrl = url;
                        }
                    } catch (JSONException e) {
                        Log.v("", "catch");
                    }
                }
            }
        });
    }

    public void openEntrance() {
        Log.v("", "FGN: " + globalName);
        Log.v("", "FGID: " + globalId);
        String [] array = new String[3];
        array[0] = globalName;
        array[1] = globalId;
        array[2] = globalUrl;
        Bundle bundleName = new Bundle();
        bundleName.putStringArray("key", array);
        Intent intent = new Intent(this, EntranceMessage.class);
        intent.putExtras(bundleName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}