package com.lanna.android.portfolioschart.domain;

import android.support.annotation.IntDef;

/**
 * Created by lanna on 4/26/17.
 */

public interface Constant {

    @IntDef(value = {FilterMode.FILTER_BY_DAY, FilterMode.FILTER_BY_MONTH, FilterMode.FILTER_BY_QUARTER})
    @interface FilterMode {
        int FILTER_BY_DAY = 0;
        int FILTER_BY_MONTH = 1;
        int FILTER_BY_QUARTER = 2;
    }
}
