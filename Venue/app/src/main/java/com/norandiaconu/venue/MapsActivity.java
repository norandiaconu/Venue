package com.norandiaconu.venue;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceActivity;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.loopj.android.http.*;//JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnCameraChangeListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ArrayList<Geofence> mGeofenceList;
    //private PendingIntent mGeofencePendingIntent;
    //private Geofence geo;
    ArrayList<LatLng> mGeofenceCoordinates;
    ArrayList<Integer> mGeofenceRadius;
    GeofenceStore mGeofenceStore;
    //Context contex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mGeofenceList = new ArrayList<Geofence>();
        mGeofenceCoordinates = new ArrayList<LatLng>();
        mGeofenceRadius = new ArrayList<Integer>();
        mGeofenceCoordinates.add(new LatLng(36.2141684, -81.6808903));
        //mGeofenceCoordinates.add(new LatLng(36.181147, -81.635509));
        mGeofenceRadius.add(100);
        //mGeofenceRadius.add(100);
        mGeofenceList.add(new Geofence.Builder()
                //geo = new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("building")

                //.setCircularRegion(mGeofenceCoordinates.get(0).latitude, mGeofenceCoordinates.get(0).longitude, mGeofenceRadius.get(0).intValue()
                .setCircularRegion(0,0,1
                        //36.2141684,
                        //-81.6808903,
                        //100
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(//Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLoiteringDelay(100)
                .build());



/*        mGeofenceList.add(new Geofence.Builder()
                .setRequestId("@string/apartment")
                .setCircularRegion(mGeofenceCoordinates.get(1).latitude, mGeofenceCoordinates.get(1).longitude, mGeofenceRadius.get(1).intValue()
                        //36.181147,
                        //-81.635509,
                        //100
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(//Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_DWELL |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLoiteringDelay(100)
                .build());
*/      Log.v("", "LOOOOOOK");
        Log.v("", "" + this);
        getLocations();
        int boo = mGeofenceList.size();
        Log.v("", "SUPERFINALLLL: " + boo);
        //mGeofenceStore = new GeofenceStore(this, mGeofenceList);
    }




    public void getLocations() {// throws JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("admin", "superstrongpassword");
        client.get("https://venue-api.herokuapp.com/locations/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray locations) {//byte[] responseBody) {
                Log.v("", "STATUS1: " + statusCode);
                Log.v("", locations.toString());
                for (int i = 0; i < locations.length(); i++) {
                    try {
                        Log.v("", "ENTER LOOP TIME: " + i);
                        //int coorSize = mGeofenceCoordinates.size();
                        //Log.v("", "COOR: " + coorSize);
                        JSONObject location = locations.getJSONObject(i);
                        Log.i("", location.toString());
                        Double longi = location.getDouble("longitude");
                        Double lati = location.getDouble("latitude");
                        Double rad = location.getDouble("radius");
                        String name = location.getString("name");
                        Log.v("", "" + name+ " " + lati + " " + longi);
                        mGeofenceCoordinates.add(new LatLng(lati, longi));
                        mGeofenceRadius.add(200);
                        //int listSize = mGeofenceList.size();
                        //Log.v("", "LIST: " + listSize);
                        mGeofenceList.add(new Geofence.Builder()
                                .setRequestId(name)
                                        //.setCircularRegion(lati, longi, 200)
                                .setCircularRegion(mGeofenceCoordinates.get(i + 1).latitude, mGeofenceCoordinates.get(i + 1).longitude, 200)//mGeofenceRadius.get(i+1).intValue())
                                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT)
                                .setLoiteringDelay(100)
                                .build());
                    } catch (JSONException e) {
                        Log.v("", "catch");
                    }
                }
                int finalSize = mGeofenceList.size();
                Log.v("", "FINAL LIST: " + finalSize);
                for (int i = 0; i < mGeofenceCoordinates.size(); i++) {
                    mMap.addCircle(new CircleOptions().center(mGeofenceCoordinates.get(i))
                            .radius(100)//mGeofenceRadius.get(i).intValue())
                            .fillColor(0x40ff0000)
                            .strokeColor(Color.TRANSPARENT).strokeWidth(2));
                }
                mGeofenceStore = new GeofenceStore(MapsActivity.this, mGeofenceList);
            }
        });
        //client.cancelAllRequests(true);
        //client.setTimeout(1);
        mGeofenceStore = new GeofenceStore(this, mGeofenceList);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        //mGeofenceStore.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            setUpMapIfNeeded();
        } else {
            GooglePlayServicesUtil.getErrorDialog(
                    GooglePlayServicesUtil.isGooglePlayServicesAvailable(this),
                    this, 0);
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(36.2143919, -81.6712084)).title("ASU"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                36.2143919, -81.6712084), 14));
        //(apt)        36.181147, -81.635509), 14));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraChangeListener(this);

    }

    @Override
    public void onCameraChange(CameraPosition position) {
        // Makes sure the visuals remain when zoom changes.
        for(int i = 0; i < mGeofenceCoordinates.size(); i++) {
            mMap.addCircle(new CircleOptions().center(mGeofenceCoordinates.get(i))
                    .radius(mGeofenceRadius.get(i).intValue())
                    .fillColor(0x40ff0000)
                    .strokeColor(Color.TRANSPARENT).strokeWidth(2));
        }
    }

}
/*
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }
    */
