package com.lanna.android.portfolioschart.presenter;

import com.lanna.android.portfolioschart.domain.Constant.FilterMode;
import com.lanna.android.portfolioschart.domain.DataService;
import com.lanna.android.portfolioschart.domain.SchedulerProvider;
import com.lanna.android.portfolioschart.model.PcPortfolio;

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

    public LineChartPresenter(LineChartContract.View view, DataService dataService,
                              SchedulerProvider schedulerProvider) {
        this.view = view;
        this.dataService = dataService;
        this.schedulerProvider = schedulerProvider;
    }

    private List<PcPortfolio> filterData(List<PcPortfolio> portfolios, int filterMode) {
        List<PcPortfolio> result = portfolios;//new ArrayList<>();
        for (PcPortfolio portfolio : portfolios) {
            portfolio.setFilterMode(filterMode);
//            PcPortfolio item = new PcPortfolio();
//            item.setId(portfolio.getId());
//            item.setNavs(portfolio.getNavsByFilter());
//            result.add(item);
        }
        return result;
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
                        view.onLoadSuccess(filterData(portfolios, currentFilterMode));
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
            view.onLoadSuccess(filterData(portfolios, currentFilterMode));
        }
    }
}
