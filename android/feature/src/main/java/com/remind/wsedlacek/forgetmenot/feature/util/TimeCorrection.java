package com.remind.wsedlacek.forgetmenot.feature.util;

import android.content.Context;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeCorrection {
    private static boolean s24Hr;

    public static void init(final Context tContext) {
        s24Hr = Settings.System.getString(tContext.getContentResolver(), Settings.System.TIME_12_24).equals("24");
    }

    public static boolean use24Hr() {
        return s24Hr;
    }

    public static Date convertStringToDate(String tTimeStr) throws Exception {
        return correctToCurrentDate(new SimpleDateFormat(getTimeFormat(), Locale.US).parse(tTimeStr) );
    }

    public static Date correctToCurrentDate(Date tTime) {
        Date tDate = new Date();
        tDate.setHours(tTime.getHours());
        tDate.setMinutes(tTime.getMinutes());
        tDate.setSeconds(tTime.getSeconds());
        return tDate;
    }

    public static Date correctUTC(Date tTime) {
        long tTimeMs = tTime.getTime();
        long tOffset = tTime.getTimezoneOffset() * 60 * 1000;
        return new Date(tTimeMs + tOffset);
    }

    public static String getTimeFormat() {
        return s24Hr ? "H:mm" : "h:mm a";
    }

    public String convertDateToString(Date tDate) {
        return new SimpleDateFormat(getTimeFormat(), Locale.US).format(tDate);
    }

    public static String getCorrectTimeFormat(int tHr, int tMin) {
        return s24Hr ? convertTo24HrString(tHr, tMin) : convertTo12HrString(tHr, tMin);
    }

    public static String convertTo12HrString(int tHr, int tMin) {
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

    public static String convertTo24HrString(int tHr, int tMin) {
        String tHrStr = String.valueOf(tHr);
        String tMinStr = tMin < 10 ? "0" + tMin : String.valueOf(tMin);
        return tHrStr + ":" + tMinStr;
    }
}
