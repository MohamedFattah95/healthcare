package com.gp.health.utils.polyline_animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapAnimator {

    private static MapAnimator mapAnimator;

    private Polyline backgroundPolyline;

    private Polyline foregroundPolyline;

    private PolylineOptions optionsForeground;

    private AnimatorSet firstRunAnimSet;

    private AnimatorSet secondLoopRunAnimSet;

    static final int BACK_COLOR = Color.parseColor("#FFC23615");
    static final int FORE_COLOR = Color.parseColor("#FFE27C7C");

    private GoogleMap googleMap;

    public MapAnimator(){

    }

    public static MapAnimator getInstance(){
        if(mapAnimator == null) mapAnimator = new MapAnimator();
        return mapAnimator;
    }


    public void animateRoute(GoogleMap googleMap, List<LatLng> bangaloreRoute) {
        if (firstRunAnimSet == null){
            firstRunAnimSet = new AnimatorSet();
        } else {
            firstRunAnimSet.removeAllListeners();
            firstRunAnimSet.end();
            firstRunAnimSet.cancel();

            firstRunAnimSet = new AnimatorSet();
        }
        if (secondLoopRunAnimSet == null) {
            secondLoopRunAnimSet = new AnimatorSet();
        } else {
            secondLoopRunAnimSet.removeAllListeners();
            secondLoopRunAnimSet.end();
            secondLoopRunAnimSet.cancel();

            secondLoopRunAnimSet = new AnimatorSet();
        }
        //Reset the polylines
        if (foregroundPolyline != null) foregroundPolyline.remove();
        if (backgroundPolyline != null) backgroundPolyline.remove();

        this.googleMap = googleMap;

        PolylineOptions optionsBackground = new PolylineOptions().add(bangaloreRoute.get(0)).color(BACK_COLOR).width(10);
        backgroundPolyline = googleMap.addPolyline(optionsBackground);

        optionsForeground = new PolylineOptions().add(bangaloreRoute.get(0)).color(FORE_COLOR).width(10);
        foregroundPolyline = googleMap.addPolyline(optionsForeground);

        final ValueAnimator percentageCompletion = ValueAnimator.ofInt(0, 100);
        percentageCompletion.setDuration(3000);
        percentageCompletion.setInterpolator(new DecelerateInterpolator());
        percentageCompletion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                List<LatLng> foregroundPoints = backgroundPolyline.getPoints();

                int percentageValue = (int) animation.getAnimatedValue();
                int pointcount = foregroundPoints.size();
                int countTobeRemoved = (int) (pointcount * (percentageValue / 100.0f));
                List<LatLng> subListTobeRemoved = foregroundPoints.subList(0, countTobeRemoved);
                subListTobeRemoved.clear();

                foregroundPolyline.setPoints(foregroundPoints);
            }
        });
        percentageCompletion.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                foregroundPolyline.setColor(BACK_COLOR);
                foregroundPolyline.setPoints(backgroundPolyline.getPoints());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), BACK_COLOR, FORE_COLOR);
        colorAnimation.setInterpolator(new AccelerateInterpolator());
        colorAnimation.setDuration(3000); // milliseconds

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                foregroundPolyline.setColor((int) animator.getAnimatedValue());
            }

        });

        ObjectAnimator foregroundRouteAnimator = ObjectAnimator.ofObject(this, "routeIncreaseForward", new RouteEvaluator(), bangaloreRoute.toArray());
        foregroundRouteAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        foregroundRouteAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                backgroundPolyline.setPoints(foregroundPolyline.getPoints());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        foregroundRouteAnimator.setDuration(3000);
//        foregroundRouteAnimator.start();

        firstRunAnimSet.playSequentially(foregroundRouteAnimator,
                percentageCompletion);
        firstRunAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                secondLoopRunAnimSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        secondLoopRunAnimSet.playSequentially(colorAnimation, percentageCompletion);
        secondLoopRunAnimSet.setStartDelay(200);
        secondLoopRunAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                secondLoopRunAnimSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        firstRunAnimSet.start();
    }

    /**
     * This will be invoked by the ObjectAnimator multiple times. Mostly every 16ms.
     **/
    public void setRouteIncreaseForward(LatLng endLatLng) {
        List<LatLng> foregroundPoints = foregroundPolyline.getPoints();
        foregroundPoints.add(endLatLng);
        foregroundPolyline.setPoints(foregroundPoints);
    }

}
