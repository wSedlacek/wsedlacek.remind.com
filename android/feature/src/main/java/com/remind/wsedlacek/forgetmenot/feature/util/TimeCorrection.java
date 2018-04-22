package com.remind.wsedlacek.forgetmenot.feature.util;

import android.content.Context;
import android.provider.Settings;

import com.remind.wsedlacek.forgetmenot.feature.util.telemetry.Debug;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeCorrection {
    private static String TAG = "TimeCorrection";

    private static boolean s24Hr;

    public static void init(final Context tContext) {
        s24Hr = Settings.System.getString(tContext.getContentResolver(), Settings.System.TIME_12_24).equals("24");
        Debug.Log(TAG,  "24 Hour Format: " + s24Hr);
    }

    public static boolean use24Hr() {
        return s24Hr;
    }

    public static Calendar convertStringToDate(String tTimeStr) throws Exception {
        Debug.Log(TAG,  "Converting " + tTimeStr + " to Date...");
        Calendar tDate = Calendar.getInstance();
        tDate.setTime(new SimpleDateFormat(getTimeFormat(), Locale.US).parse(tTimeStr));
        correctToCurrentDate(tDate);
        return tDate;
    }

    public static void correctToCurrentDate(Calendar tTime) {
        Calendar tDate = Calendar.getInstance();
        Debug.Log(TAG,  "Correcting time for current date [" + tDate.getTime() + "]...");
        tTime.set(Calendar.YEAR, tDate.get(Calendar.YEAR));
        tTime.set(Calendar.MONTH, tDate.get(Calendar.MONTH));
        tTime.set(Calendar.DAY_OF_MONTH, tDate.get(Calendar.DAY_OF_MONTH));
    }

    public static void correctUTC(Calendar tTime) {
        long tTimeMs = tTime.getTimeInMillis();
        long tOffset = tTime.get(Calendar.ZONE_OFFSET);
        long tCorrected = tTimeMs - tOffset;
        Debug.Log(TAG, "Correcting UTC... Offset is " + tOffset + ". Current MS is " + tTimeMs + ". New MS is " + tCorrected + ".");
        tTime.setTime(new Date(tCorrected));
    }

    private static String getTimeFormat() {
        return use24Hr() ? "H:mm" : "h:mm a";
    }

    public String convertDateToString(Calendar tDate) {
        Debug.Log(TAG,  "Converting " + tDate.getTime() + " to Time String");
        return new SimpleDateFormat(getTimeFormat(), Locale.US).format(tDate.getTime());
    }

    public static String getCorrectDateFormat(int tYear, int tMonth, int tDay) {
        return tMonth + "/" + tDay + "/" + tYear;
    }

    public static String getCorrectTimeFormat(int tHr, int tMin) {
        return s24Hr ? convertTo24HrString(tHr, tMin) : convertTo12HrString(tHr, tMin);
    }

    private static String convertTo12HrString(int tHr, int tMin) {
        String tSet = "";

        switch (tHr) {
            case 12: tSet = "PM"; break;
            case 0:  tSet = "AM"; tHr = 12; break;
            default:
                tSet = tHr > 12 ? "PM" : "AM";
                tHr = tHr > 12 ? tHr - 12 : tHr;
        }

        String tHrStr = String.valueOf(tHr);
        String tMinStr = tMin < 10 ? "0" + tMin : String.valueOf(tMin);
        return tHrStr + ":" + tMinStr + " " + tSet;
    }

    private static String convertTo24HrString(int tHr, int tMin) {
        String tHrStr = String.valueOf(tHr);
        String tMinStr = tMin < 10 ? "0" + tMin : String.valueOf(tMin);
        return tHrStr + ":" + tMinStr;
    }

    public static long stringToFreq(String tString) {
        if (tString == null) return 1;
        switch (tString) {
            case "Hourly":
                return 3600000;
            case "Daily":
                return 86400000;
            case "Weekly":
                return 604800000;
        }
        return 1;
    }
}
