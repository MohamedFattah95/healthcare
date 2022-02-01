package com.gp.health.utils.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.gp.health.utils.PermissionsUtils;


public class LocationProvider {
    private static final String TAG = "LocationService";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private final static long UPDATE_INTERVAL = 4 * 1000;  /* 4 secs */
    private final static long FASTEST_INTERVAL = 2000; /* 2 sec */

    private Activity activity;
    private final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private LocationInterfaceCallback locationInterfaceCallback;

    public LocationProvider(Activity activity, LocationInterfaceCallback locationInterfaceCallback) {
        this.activity = activity;
        this.locationInterfaceCallback = locationInterfaceCallback;
        PermissionsUtils.requestLocationPermission(activity);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    @SuppressLint({"MissingPermission", "LogNotTimber"})
    public void getLocation() {
        // ---------------------------------- LocationRequest ------------------------------------
        // Create the location request to start receiving updates
        if (!PermissionsUtils.hasPermissions(activity, PERMISSIONS)){
            Log.e(TAG, "Permissions Denied!");
            return;
        }
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        //LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (!GPSChecker.isGPSEnabled(activity)) {
                    mFusedLocationClient.removeLocationUpdates(locationCallback);
                    Log.e(TAG, "GPS Disabled!");
                    return;
                }

                Log.e(TAG, "onLocationResult: got location result >>>>>>.");
                Location location = locationResult.getLastLocation();
                Log.e(TAG, "Lat: " + location.getLatitude());
                Log.e(TAG, "Lng: " + location.getLongitude());
                locationInterfaceCallback.onLocationChange(location);
            }
        };

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());

    }


    @SuppressLint("MissingPermission")
    public void getLocationOneTime(){
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        locationManager.requestSingleUpdate(criteria, new LocationListener() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onLocationChanged(Location location) {
                Log.e(TAG, "getLocationOneTime: location result >>>>>>.");
                Log.e(TAG, "Lat: " + location.getLatitude());
                Log.e(TAG, "Lng: " + location.getLongitude());
                locationInterfaceCallback.onLocationChange(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }, null);
    }
}
