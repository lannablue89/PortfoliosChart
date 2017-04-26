package com.lanna.android.portfolioschart.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.lanna.android.portfolioschart.domain.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lanna on 4/26/17.
 *
 * {
     "date": "2017-01-01",
     "amount": 9837.51504
 }
 */

public class PcNav {

    @SerializedName("date")
    private String date;

    @SerializedName("amount")
    private Float amount; // can be null

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private static Calendar calendar = Calendar.getInstance();

    public PcNav(String date, float amount) {
        this.date = date;
        this.amount = amount;
    }


    @Override
    public String toString() {
        return "{date=" + date
                + ", amount=" + amount
                + "}";
    }

    public String getDate() {
        return date;
    }

    public Float getAmount() {
        return amount == null ? 0 : amount;
    }

    public int getMonth() {
        return !TextUtils.isEmpty(date) ? Integer.parseInt(date.substring(5, 7)) : -1;
    }

    /*
        for quarterly data, take the last available day for the following months: Mar, Jun, Sep, Dec
     */
    public int getQuarter() {
        int month = getMonth();
        return month > 0 ? ((month-1)/3 + 1) : -1;
    }

    public int getDayOfYear() {
        Date datetime = null;
        try {
            datetime = format.parse(this.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (datetime != null) {
            calendar.setTime(datetime);
            return calendar.get(Calendar.DAY_OF_YEAR);
        }

        return -1;
    }
}
