package com.autollow.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by radsen on 8/1/17.
 */

public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    public static int getYear(String format, String value){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            Log.d(TAG, e.getMessage());
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(String format, String value){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            Log.d(TAG, e.getMessage());
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDay(String format, String value){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            Log.d(TAG, e.getMessage());
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

}
