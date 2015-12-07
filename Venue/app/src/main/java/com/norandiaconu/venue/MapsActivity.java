package com.norandiaconu.venue;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnCameraChangeListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ArrayList<Geofence> mGeofenceList;
    ArrayList<LatLng> mGeofenceCoordinates;
    ArrayList<Integer> mGeofenceRadius;
    GeofenceStore mGeofenceStore;
    ArrayList<OneVenue> alocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        alocations = new ArrayList<OneVenue>();
        mGeofenceList = new ArrayList<Geofence>();
        mGeofenceCoordinates = new ArrayList<LatLng>();
        mGeofenceRadius = new ArrayList<Integer>();
        mGeofenceCoordinates.add(new LatLng(0, 0));
        mGeofenceRadius.add(100);

        mGeofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this geofence.
                .setRequestId("building")
                .setCircularRegion(0, 0, 1)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .setLoiteringDelay(100)
                .build());
        getLocations();
    }




    public void getLocations() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("admin", "superstrongpassword");
        client.get("https://venue-api.herokuapp.com/locations/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray locations) {
                Log.v("", "STATUS1: " + statusCode);
                Log.v("", locations.toString());
                for (int i = 0; i < locations.length(); i++) {
                    try {
                        Log.v("", "ENTER LOOP TIME: " + i);
                        JSONObject location = locations.getJSONObject(i);
                        Log.i("", location.toString());
                        Double longi = location.getDouble("longitude");
                        Double lati = location.getDouble("latitude");
                        Double rad = location.getDouble("radius");
                        String name = location.getString("name");
                        int id = location.getInt("id");
                        String messages = location.getString("messages");
                        String created = location.getString("created");
                        Log.v("", "" + name + " " + lati + " " + longi);
                        mGeofenceCoordinates.add(new LatLng(lati, longi));
                        //mGeofenceRadius.add(200);
                        OneVenue l = new OneVenue(id, messages, name, longi, lati, rad,
                                created);
                        alocations.add(l);
                        mMap.addCircle(new CircleOptions().center(new LatLng(lati, longi))
                                .radius(rad)
                                .fillColor(0x40ff0000)
                                .strokeColor(Color.TRANSPARENT).strokeWidth(2));

                        mGeofenceList.add(new Geofence.Builder()
                                .setRequestId(name)
                                .setCircularRegion(mGeofenceCoordinates.get(i + 1).latitude, mGeofenceCoordinates.get(i + 1).longitude, rad.floatValue())
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
                mGeofenceStore = new GeofenceStore(MapsActivity.this, mGeofenceList, alocations);
            }
        });
        mGeofenceStore = new GeofenceStore(this, mGeofenceList, alocations);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraChangeListener(this);
    }

    @Override
    public void onCameraChange(CameraPosition position) {}
}
