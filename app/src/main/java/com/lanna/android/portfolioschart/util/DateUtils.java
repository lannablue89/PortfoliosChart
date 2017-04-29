package com.lanna.android.portfolioschart.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lanna on 4/29/17.
 */

public class DateUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat formatDayMonth = new SimpleDateFormat("MM/dd");
    private static Calendar calendar = Calendar.getInstance();


    public static int getDayOfYear(String dateString) {
//        if (dayOfYear > 0) {
//            return dayOfYear;
//        }

        Date datetime = null;
        try {
            datetime = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (datetime != null) {
            calendar.setTime(datetime);
            return calendar.get(Calendar.DAY_OF_YEAR);
        }

        return -1;
    }

    public static String getDayMonthInYear(int dayOfYear) {
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return formatDayMonth.format(calendar.getTime());
    }
}
