package com.gp.shifa.utils;

import static android.content.Context.LOCATION_SERVICE;
import static com.gp.shifa.BaseApp.getContext;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.firebase.geofire.GeoLocation;
import com.gp.shifa.R;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class LocationUtils {

    public static String getAddress(double lat, double lng, Context context) {
        String address;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addresses = geocoder.getFromLocation(lat, lng, 1);
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            address = addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            address = (String) context.getText(R.string.no_address_found);
        }
        return address;
    }


    public static float getDistance(double firstLat, double firstLng, double secondLat, double secondLng) {
        Location fLocation = new Location("FirstLocation");
        fLocation.setLatitude(firstLat);
        fLocation.setLongitude(firstLng);
        Location sLocation = new Location("SecondLocation");
        sLocation.setLatitude(secondLat);
        sLocation.setLongitude(secondLng);
        float distance = (fLocation.distanceTo(sLocation)) / 1000; //returns distance in kilos (remove "/1000" >>> Meter)
        return round(distance, 2);
    }


    public static float getDistance(double userLat, double userLng, GeoLocation driverGeoLocation) {
        Location userLocation = new Location("UserLocation");
        userLocation.setLatitude(userLat);
        userLocation.setLongitude(userLng);
        Location driverLocation = new Location("DriverLocation");
        driverLocation.setLatitude(driverGeoLocation.latitude);
        driverLocation.setLongitude(driverGeoLocation.longitude);
        float distance = (userLocation.distanceTo(driverLocation)) / 1000; //returns distance in kilos (remove "/1000" >>> Meter)
        return round(distance, 2);
    }


    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        String dis = bd.toString();
        return Float.valueOf(dis);
    }


    public static void goToMapsDirections(String lat, String lng, Context context) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }

    public static void goToMaps(String lat, String lng, Context context) {
        String uri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public static boolean isLatLngValid(String latStr, String lngStr) {
        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);

        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            Toasty.error(getContext(), getContext().getString(R.string.invalid_region), Toast.LENGTH_LONG, true).show();
            return false;
        }
        return true;
    }

    public static boolean checkLocationPermission(Activity activity, int REQUEST_LOCATION_CODE) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }

    public static boolean isGPSEnabled(Activity activity) {
        LocationManager lm = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }
        return gps_enabled;
    }
}
