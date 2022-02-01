package com.gp.health.data.firebase;

import android.annotation.SuppressLint;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@SuppressLint("LogNotTimber")
public class ItemsFirebase {

    private static final String TAG = "ItemsFirebase";

    public static void addItemLocation(int id, double latitude, double longitude) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Items");
        GeoFire geoFire = new GeoFire(reference);
        geoFire.setLocation(String.valueOf(id), new GeoLocation(latitude, longitude));
        Log.e(TAG, "addItemLocation: " + "Saved!");
    }

    public static void removeItemLocation(int id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Items");
        GeoFire geoFire = new GeoFire(reference);
        geoFire.removeLocation(String.valueOf(id));
        Log.e(TAG, "removeItemLocation: " + "Removed!");
    }

    public static void addCommercialLocation(int id, double latitude, double longitude) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commercials");
        GeoFire geoFire = new GeoFire(reference);
        geoFire.setLocation(String.valueOf(id), new GeoLocation(latitude, longitude));
        Log.e(TAG, "addCommercialLocation: " + "Saved!");
    }

    public static void removeCommercialLocation(int id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commercials");
        GeoFire geoFire = new GeoFire(reference);
        geoFire.removeLocation(String.valueOf(id));
        Log.e(TAG, "removeCommercialLocation: " + "Removed!");
    }
}
