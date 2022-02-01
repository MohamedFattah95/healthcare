package com.gp.health.utils.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.gp.health.R;
import com.gp.health.utils.ImageUtils;
import com.gp.health.utils.polyline_animation.MapAnimator;
import com.utsman.smartmarker.SmartMarker;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("LogNotTimber")
public class PolylineProvider {
    private static final String TAG = "PolylineProvider";
    private GeoApiContext mGeoApiContext;
    private Context context;
    private GoogleMap mGoogleMap;

    public PolylineProvider(Context context, GoogleMap mGoogleMap) {
        this.context = context;
        this.mGoogleMap = mGoogleMap;

        mGeoApiContext = new GeoApiContext.Builder()
                .apiKey(context.getString(R.string.google_map_api_key))
                .build();
    }

    //----------------------- Calculate Directions Method -----------------------------------
    //Calculating Directions with Google Directions API
    public void drawPolyline(Marker destinationMarker, Marker originMarker, boolean drawAllRoutes) {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                destinationMarker.getPosition().latitude,
                destinationMarker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true); //show multiple routes to destination if exists
        directions.origin(
                new com.google.maps.model.LatLng(
                        originMarker.getPosition().latitude,
                        originMarker.getPosition().longitude
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                //add polylines to the map with the DirectionsResult.
                addPolylinesToMap(result, drawAllRoutes);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());

            }
        });
    }
    //----------------------- Calculate Directions Method --------------------------------------------


    //----------------------- Adding Polylines to the map Method --------------------------------------------
    private ArrayList<PolylineData> mPolyLinesData = new ArrayList<>();

    private void addPolylinesToMap(final DirectionsResult result, boolean drawAllRoutes) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                //we need to clear the old polylines in case we want to get directions for another user
                if (mPolyLinesData.size() > 0) {
                    for (PolylineData polylineData : mPolyLinesData) {
                        polylineData.getPolyline().remove(); //remove polylines from the map
                    }
                    mPolyLinesData.clear(); //clear the list to add the new polylines for the new selected user directions.
                    mPolyLinesData = new ArrayList<>();
                }

                if (drawAllRoutes)
                    drawAllRoutes(result); //Draw All possible Polylines (Routes) if exists.
                else
                    drawOneRoute(result.routes[0]); //Draw Single Polyline (Route)
            }
        });
    }


    @SuppressLint("ResourceType")
    private void drawOneRoute(DirectionsRoute route) {
        double duration = 999999999;
        Log.d(TAG, "run: leg: " + route.legs[0].toString());
        List<LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
        List<com.google.android.gms.maps.model.LatLng> pathPointsList = new ArrayList<>();
        // This loops through all the LatLng coordinates of ONE polyline.
        for (com.google.maps.model.LatLng latLng : decodedPath) {
            pathPointsList.add(new com.google.android.gms.maps.model.LatLng(latLng.lat, latLng.lng));
        }
        Polyline polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(pathPointsList));
        polyline.setColor(ContextCompat.getColor(context, android.R.color.transparent));
        //polyline.setClickable(true);
        mPolyLinesData.add(new PolylineData(polyline, route.legs[0])); //add polyline data to list
        //make the user marker invisible and add the new marker to the fastest route (less duration)
        double tempDuration = route.legs[0].duration.inSeconds;
        if (tempDuration < duration) {
            duration = tempDuration;
            //onPolylineClick(polyline);
            zoomRoute(pathPointsList); //zoom camera to the route
        }


        //Animate Route
        MapAnimator mapAnimator = new MapAnimator();
        mapAnimator.animateRoute(mGoogleMap, pathPointsList);

    }

    private void drawAllRoutes(DirectionsResult result) {
        double duration = 999999999;
        Log.e(TAG, "drawAllRoutes: " + result.routes.length);
        for (DirectionsRoute route : result.routes) {
            drawOneRoute(route);
        }
    }

    private float v;
    private double lat, lng;
    private com.google.android.gms.maps.model.LatLng startPosition, endPosition;
    private int index, next;
    Marker marker;

    public void addCarMarker(int userId) {
        Log.e(TAG, "addCarMarker: IN");
        marker = mGoogleMap.addMarker(new MarkerOptions()
                .visible(false)
                .position(new com.google.android.gms.maps.model.LatLng(0, 0))
                .title(context.getString(R.string.location)));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Locations");
        reference.child(String.valueOf(userId)).child("l").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String lat = "", lng = "";
                    Log.e(TAG, "onDataChange: " + dataSnapshot.toString());
                    if (dataSnapshot.hasChild("0"))
                        lat = dataSnapshot.child("0").getValue().toString();
                    if (dataSnapshot.hasChild("1"))
                        lng = dataSnapshot.child("1").getValue().toString();

                    BitmapDescriptor icon = ImageUtils.bitmapDescriptorFromVector(context, R.drawable.ic_car);
                    //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_car_uber);

                    endPosition = new com.google.android.gms.maps.model.LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    //marker.setPosition(latLng);
                    marker.setAnchor(0.5f, 0.5f);
                    marker.setIcon(icon);
                    marker.setVisible(true);
                    marker.setFlat(true);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endPosition, 15));

                    SmartMarker.moveMarkerSmoothly(marker, endPosition);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "DatabaseError: " + databaseError.getMessage());
            }
        });

    }


    private float getBearing(com.google.android.gms.maps.model.LatLng begin, com.google.android.gms.maps.model.LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        double v = Math.toDegrees(Math.atan(lng / lat));
        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) v;
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - v) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (v + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - v) + 270);
        return -1;
    }

    //----------------- Zoom camera to selected route method ---------------
    public void zoomRoute(List<com.google.android.gms.maps.model.LatLng> lstLatLngRoute) {

        if (mGoogleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (com.google.android.gms.maps.model.LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 120;
        LatLngBounds latLngBounds = boundsBuilder.build();

        mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding),
                600,
                null
        );
    }
    //----------------- Zoom camera to selected route method ---------------

}
