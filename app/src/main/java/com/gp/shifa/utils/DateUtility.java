package com.gp.shifa.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.gp.shifa.R;
import com.gp.shifa.data.models.TimeModel;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtility {
    public static Date stringToDate(String aDate, String aFormat) {

        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        return simpledateformat.parse(aDate, pos);
    }

    public static String getDateOnly(String mDateTime) {
        String dateOnly = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //pattern
        try {
            Date date = dateFormatter.parse(mDateTime); //2019-08-28 20:31:24 to parser of pattern
            //Get date only
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateOnly = dateFormat.format(date); //2019-08-28
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOnly;
    }

    public static String getDateArabicFormat(String mDateTime) {
        String dateOnly = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); //pattern
        dateOnly = new SimpleDateFormat("dd " +
                new SimpleDateFormat("MM", new Locale("ar"))
                        .format(new Date()) + " yyyy", Locale.US)
                .format(new Date());
        return dateOnly;
    }


    public static String getDateFormatted(String mDateTime) {
        String dateOnly = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); //pattern
        try {
            Date date = dateFormatter.parse(mDateTime); //2019-08-28 20:31:24 to parser of pattern
            //Get date only
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            dateOnly = dateFormat.format(date); //2019-08-28
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOnly;
    }


    public static String getDateOnlyTFormat(String mDateTime) {
        String dateOnly = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); //pattern
        try {
            Date date = dateFormatter.parse(mDateTime); //2019-08-28 20:31:24 to parser of pattern
            //Get date only
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateOnly = dateFormat.format(date); //2019-08-28
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOnly;
    }

    public static TimeModel getTimeOnly(String mDateTime, Context context) {
        String timeOnly = "";
        TimeModel time = new TimeModel();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //pattern
        try {
            Date date = dateFormatter.parse(mDateTime); //2019-08-28 20:31:24 to parser of pattern
            //Get time only
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            timeOnly = dateFormat.format(date); //05:30
            if (timeOnly.contains("AM")) {
                String[] arr = timeOnly.split(" ");
                time.setTime(arr[0]);
                time.setAmOrpm((String) context.getText(R.string.am_text));
            } else if (timeOnly.contains("PM")) {
                String[] arr = timeOnly.split(" ");
                time.setTime(arr[0]);
                time.setAmOrpm((String) context.getText(R.string.pm_text));
            }
            Log.e("Time", "getTimeOnly: " + timeOnly);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static TimeModel getTimeOnlyTFormat(String mDateTime, Context context) {
        String timeOnly = "";
        TimeModel time = new TimeModel();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); //pattern
        try {
            Date date = dateFormatter.parse(mDateTime); //2019-08-28 20:31:24 to parser of pattern
            //Get time only
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            timeOnly = dateFormat.format(date); //05:30
            if (timeOnly.contains("AM")) {
                String[] arr = timeOnly.split(" ");
                time.setTime(arr[0]);
                time.setAmOrpm((String) context.getText(R.string.am_text));
            } else if (timeOnly.contains("PM")) {
                String[] arr = timeOnly.split(" ");
                time.setTime(arr[0]);
                time.setAmOrpm((String) context.getText(R.string.pm_text));
            }
            Log.e("Time", "getTimeOnly: " + timeOnly);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    //This method returns date and time : 25-01-2020 - 02:10 AM
    public static String getDateTime(String dateTime, Context context) {
        TimeModel time = DateUtility.getTimeOnlyTFormat(dateTime, context);
        return DateUtility.getDateOnlyTFormat(dateTime) + " - " + time.getTime() + " " + time.getAmOrpm();
    }

    public static String getTimeFormatHS(String mTime) {
        String timeOnly = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss"); //pattern
        try {
            Date date = dateFormatter.parse(mTime); //20:31:24 to parser of pattern
            //Get time only
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.US);
            timeOnly = dateFormat.format(date); //05:30
            Log.e("Time", "getTimeOnly: " + timeOnly);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeOnly;
    }


    public static String getCurrentTimeStamp() {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAfterToday(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        Calendar myDate = Calendar.getInstance();

        myDate.set(year, month, day);

        return !myDate.before(today);
    }

    public static long getRemainingTime(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        return different / hoursInMilli;
    }

    public static String getDateTimeSpaceFormat(String mDateTime, Context context) {
        String dateOnly = "";
        String dateTime = "";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //pattern
        try {
            Date date = dateFormatter.parse(mDateTime); //2019-08-28 20:31:24 to parser of pattern
            //Get date only
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateOnly = dateFormat.format(date); //2019-08-28
            TimeModel timeModel = getTimeOnly(mDateTime, context);
            dateTime = dateOnly + " - " + timeModel.getTime() + " " + timeModel.getAmOrpm();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }
}
