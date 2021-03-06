package com.lanna.android.portfolioschart.model;


import com.google.gson.annotations.SerializedName;
import com.lanna.android.portfolioschart.util.DateUtils;

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


//    private int dayOfYear = -1;

    public PcNav(String date, float amount) {
        this.date = date;
        this.amount = amount;
    }

    public PcNav(PcNav o) {
        this(o.getDate(), o.getAmount());
    }


    @Override
    public String toString() {
        return "{" + date
//                + ", " + amount
                + "}"
                ;
    }

    public String getDate() {
        return date;
    }

    public Float getAmount() {
        return amount == null ? 0 : amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void addAmount(float amount) {
        this.amount += amount;
    }

    public int getMonth() {
        return date != null ? Integer.parseInt(date.substring(5, 7)) : -1;
    }

    /*
        for quarterly data, take the last available day for the following months: Mar, Jun, Sep, Dec
     */
    public int getQuarter() {
        int month = getMonth();
        return month > 0 ? ((month-1)/3 + 1) : -1;
    }

    public int getDayOfYear() {
        return DateUtils.getDayOfYear(this.date);
    }
}
