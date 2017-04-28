package com.lanna.android.portfolioschart.presenter;

import com.lanna.android.portfolioschart.domain.Constant.FilterMode;
import com.lanna.android.portfolioschart.domain.DataService;
import com.lanna.android.portfolioschart.domain.SchedulerProvider;
import com.lanna.android.portfolioschart.model.PcNav;
import com.lanna.android.portfolioschart.model.PcPortfolio;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by lanna on 4/26/17.
 */

public class LineChartPresenter implements LineChartContract.Presenter {

    private LineChartContract.View view;
    private DataService dataService;
    private SchedulerProvider schedulerProvider;

    private List<PcPortfolio> portfolios;
    private @FilterMode int currentFilterMode = FilterMode.FILTER_BY_DAY;

//    private PcPortfolio portfolioTotalByDays;

    public LineChartPresenter(LineChartContract.View view, DataService dataService,
                              SchedulerProvider schedulerProvider) {
        this.view = view;
        this.dataService = dataService;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void loadData() {
        view.showLoadingProgress();
        dataService.getListPortfolio()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(new Observer<List<PcPortfolio>>() {
                    @Override public void onSubscribe(@NonNull Disposable d) {}
                    @Override public void onComplete() {}

                    @Override
                    public void onNext(@NonNull List<PcPortfolio> portfolios) {
                        LineChartPresenter.this.portfolios = portfolios;
                        view.hideLoadingProgress();
                        setFilterMode(portfolios, currentFilterMode);
                        view.onLoadSuccess(portfolios);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideLoadingProgress();
                        view.onLoadError(e);
                    }
                });
    }

    @Override
    public void reportByDays() {
        this.currentFilterMode = FilterMode.FILTER_BY_DAY;
        notifyData();
    }

    @Override
    public void reportByMonths() {
        this.currentFilterMode = FilterMode.FILTER_BY_MONTH;
        notifyData();
    }

    @Override
    public void reportByQuarters() {
        this.currentFilterMode = FilterMode.FILTER_BY_QUARTER;
        notifyData();
    }

    private void notifyData() {
        if (portfolios == null || portfolios.isEmpty()) {
            loadData();
        }
        else {
            setFilterMode(portfolios, currentFilterMode);
            view.onLoadSuccess(portfolios);
        }
    }

    @Override
    public void reportTotalForEachDay() {
        this.currentFilterMode = FilterMode.FILTER_BY_DAY;
        if (portfolios == null || portfolios.isEmpty()) {
            loadData();
        }
        else {
            List<PcPortfolio> result = new ArrayList<>();
            result.add(filterDataByTotal(portfolios));
            view.onLoadSuccess(result);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Filter Functions
    ///////////////////////////////////////////////////////////////////////////

    private void setFilterMode(List<PcPortfolio> portfolios, int filterMode) {
        for (PcPortfolio portfolio : portfolios) {
            portfolio.setFilterMode(filterMode);
        }
    }

    private PcPortfolio filterDataByTotal(List<PcPortfolio> portfolios) {
//        if (portfolioTotalByDays != null) {
//            return portfolioTotalByDays;
//        }

        PcPortfolio result = new PcPortfolio();
        PcNav[] resultNavs = new PcNav[366];

        int dayIndex;
        for (PcPortfolio portfolio : portfolios) {
            for (PcNav pcNav : portfolio.getFullNavs()) {
                dayIndex = pcNav.getDayOfYear();
                if (resultNavs[dayIndex] == null) { // init
                    resultNavs[dayIndex] = new PcNav(pcNav);
                } else { // sum
                    resultNavs[dayIndex].addAmount(pcNav.getAmount());
                }
            }
        }

        List<PcNav> resultNavList = new ArrayList<>();
        for (PcNav resultNav : resultNavs) {
            if (resultNav != null) {
                resultNavList.add(resultNav);
            }
        }

        result.setFilterredNavs(resultNavList);
        return result;
    }
}
