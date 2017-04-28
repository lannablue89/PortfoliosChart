package com.lanna.android.portfolioschart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lanna.android.portfolioschart.domain.Constant;
import com.lanna.android.portfolioschart.domain.Constant.FilterMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanna on 4/26/17.
 {
    "portfolioId": "0d515913-20e9-4cf0-83e9-a53532ee60d6",
     "navs": [
         {
         "date": "2017-01-01",
         "amount": 9837.51504
         },
         ...
     ]
 }
 */

public class PcPortfolio {

    @SerializedName("portfolioId")
    private String id;

    @SerializedName("navs")
    private List<PcNav> navs;

    @Expose(serialize = false, deserialize = false)
    private @FilterMode int filterMode;
    @Expose(serialize = false, deserialize = false)
    private List<PcNav> filterredNavs;


    @Override
    public String toString() {
        return ""
//                + "id=" + id
//                + (navs != null ? navs.size() : 0)
//                + ", navs=" + navs
                + "filterredNavs=" + (filterredNavs != null ? filterredNavs.size() : 0)
//                + "filterredNavs=" + LogUtils.toLogStrings(true, filterredNavs)
                ;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Gets, Sets
    ///////////////////////////////////////////////////////////////////////////

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PcNav> getFullNavs() {
        if (navs == null) { // make sure not null
            navs = new ArrayList<>();
        }
        return navs;
    }

//    public void setNavs(List<PcNav> navs) {
//        this.navs = navs;
//    }

    public List<PcNav> getFilterredNavs() {
        if (filterredNavs == null) { // make sure not null
            filterredNavs = new ArrayList<>();
        }
        return filterredNavs;
    }

    public void setFilterredNavs(List<PcNav> filterredNavs) {
        this.filterredNavs = filterredNavs;
    }

    public void setFilterMode(int filterMode) {
        this.filterMode = filterMode;
        filterredNavs = getNavsByFilter();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Other functions
    ///////////////////////////////////////////////////////////////////////////

    /**
        The app should have options to display data in daily/monthly/quarterly format.
        For monthly data, take the last available day of the month;
        for quarterly data, take the last available day for the following months: Mar, Jun, Sep, Dec.
     */
    public List<PcNav> getNavsByFilter() {
        if (navs == null || navs.isEmpty()) {
            return null;
        }

        List<PcNav> result = new ArrayList<>();
        switch (filterMode) {

            case FilterMode.FILTER_BY_QUARTER:
            case FilterMode.FILTER_BY_MONTH:
                int count = navs.size();
                PcNav item, nextItem;
                int periodDate;
                for (int i = 0; i < count; i++) {
                    item = navs.get(i);
                    nextItem = i + 1 < count ? navs.get(i + 1) : null;
                    periodDate = getPeriodTimeByFilterMode(item, filterMode);

                    // end of month
                    if (item.getAmount() > 0 && (
                            // the last available day of month/quarter
                            i == count - 1 // last index
                            || (nextItem.getAmount() <= 0 // next value not available
                                    || periodDate < getPeriodTimeByFilterMode(nextItem, filterMode)) // next item is from next month/quarter
                    )) {
                        if (!result.isEmpty() && getPeriodTimeByFilterMode(result.get(result.size()-1), filterMode) == periodDate) {
                            result.remove(result.size()-1);
                        }
                        result.add(item);
                    }
                }
                break;

            case FilterMode.FILTER_BY_DAY:
            default:
                for (PcNav nav : navs) {
                    if (nav.getAmount() > 0) {
                        result.add(nav);
                    }
                }
                break;
        }
        return result;
    }

    private int getPeriodTimeByFilterMode(PcNav item, @Constant.FilterMode int filterMode) {
        return filterMode == FilterMode.FILTER_BY_MONTH ? item.getMonth() : item.getQuarter();
    }

    public int getTimeIndex(PcNav pcNav) {
        switch (filterMode) {

            case FilterMode.FILTER_BY_MONTH:
                return pcNav.getMonth();

            case FilterMode.FILTER_BY_QUARTER:
                return pcNav.getQuarter();

            case FilterMode.FILTER_BY_DAY:
            default:
                return pcNav.getDayOfYear();
        }
    }
}
