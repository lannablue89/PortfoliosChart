package com.lanna.android.portfolioschart.presenter;

import com.lanna.android.portfolioschart.model.PcPortfolio;

import java.util.List;

/**
 * Created by lanna on 4/26/17.
 */

public interface LineChartContract {

    interface View {
        void showLoadingProgress();
        void hideLoadingProgress();

        void onLoadSuccess(List<PcPortfolio> portfolios);

        void onLoadError(Throwable e);
    }

    interface Presenter {
        void loadData();

        void reportByDays();
        void reportByMonths();
        void reportByQuarters();

        void reportTotalForEachDay();
    }
}
