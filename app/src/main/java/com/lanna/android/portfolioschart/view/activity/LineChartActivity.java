
package com.lanna.android.portfolioschart.view.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lanna.android.portfolioschart.R;
import com.lanna.android.portfolioschart.domain.DataService;
import com.lanna.android.portfolioschart.domain.SchedulerProvider;
import com.lanna.android.portfolioschart.model.PcNav;
import com.lanna.android.portfolioschart.model.PcPortfolio;
import com.lanna.android.portfolioschart.presenter.LineChartContract;
import com.lanna.android.portfolioschart.presenter.LineChartPresenter;
import com.lanna.android.portfolioschart.util.LogUtils;
import com.lanna.android.portfolioschart.util.UiUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineChartActivity extends DemoBase
        implements LineChartContract.View, OnChartValueSelectedListener {

    private ProgressDialog progressDialog;

    private LineChart mChart;
    private LineChartContract.Presenter presenter;

    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart);

        mChart = (LineChart) findViewById(R.id.chart);
        initViews();

        random = new Random();

        presenter = new LineChartPresenter(this, new DataService(this), new SchedulerProvider());
        presenter.loadData();
    }

    private void initViews() {
        progressDialog = new ProgressDialog(this);

        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(LegendForm.LINE);
        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.DKGRAY);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        // time
        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.DKGRAY);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        // amount
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());

        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setDrawLabels(false); // hide only the labels
        rightAxis.setEnabled(false); // hiding the whole right axis
//        rightAxis.setTypeface(mTfLight);
//        rightAxis.setTextColor(Color.RED);
//        rightAxis.setAxisMaximum(900);
//        rightAxis.setAxisMinimum(-200);
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setDrawZeroLine(false);
//        rightAxis.setGranularityEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reportByDays:
                presenter.reportByDays();
                break;

            case R.id.reportByMonths:
                presenter.reportByMonths();
                break;

            case R.id.reportByQuarters:
                presenter.reportByQuarters();
                break;
        }
        return true;
    }

    private void bindData(List<PcPortfolio> portfolios) { // int mode: day, month, quarter, year
        Log.i("app", "bindData: portfolios=\n" + LogUtils.toLogStrings(true, portfolios));

        int lastLinesCount = mChart.getData() != null ? mChart.getData().getDataSetCount() : 0;
        int newLinesCount = portfolios.size();
        int count = Math.max(lastLinesCount, newLinesCount);
        LineDataSet portfolioLineDataSet;
        List<Entry> yVals;
        LineData lineData = null;
        float maxAmount = 0;

        for (int i = 0; i < count; i++) {
            if (i < newLinesCount) {
                yVals = new ArrayList<>(); // must generate new list
                maxAmount = convertAndFilterListToAndGetMaxAmount(yVals, portfolios.get(i), maxAmount);

                if (i < lastLinesCount) { // reuse last UI lines
                    portfolioLineDataSet = (LineDataSet) mChart.getData().getDataSetByIndex(i);
                    portfolioLineDataSet.setValues(yVals);
                }
                else {
                    // create a data object with the data-sets
                    if (lineData == null) {
                        lineData = new LineData();
                        mChart.setData(lineData);
                    }
                    lineData.addDataSet(newLineDataSet(i, newLinesCount, yVals));
                    lineData.setValueTextColor(Color.DKGRAY);
                    lineData.setValueTextSize(9f);
                    lineData.setValueFormatter(new MyValueFormatter());
//                    lastLinesCount++;
                }
            }
            else if (i < lastLinesCount) {
                // remove redundant last items
                mChart.getData().removeDataSet(i);
//                lastLinesCount--;
            }
        }

        mChart.getAxisLeft().setAxisMaximum(maxAmount);
        mChart.getXAxis().setAxisMaximum(portfolios.get(0).getFilterredNavs().size());
        Log.i("app", "bindData: getAxisLeft().setAxisMaximum=" + maxAmount
                + ", getXAxis().setAxisMaximum=" + portfolios.get(0).getFilterredNavs().size());

        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    private float convertAndFilterListToAndGetMaxAmount(List<Entry> yVals, PcPortfolio portfolio, float maxAmount) {
        List<PcNav> navs = portfolio.getFilterredNavs();
        int count = navs.size();
        float amount;
        for (int i = 0; i < count; i++) {
            amount = navs.get(i).getAmount();
            int index = portfolio.getTimeIndex(navs.get(i));
            if (index >= 0) {
                yVals.add(new Entry(index, navs.get(i).getAmount()));
                if (maxAmount < amount) {
                    maxAmount = amount;
                }
            }
        }
        return maxAmount;
    }

    private LineDataSet newLineDataSet(int setIndex, int setSize, List<Entry> yVals) {
        LineDataSet set = new LineDataSet(yVals, "Set " + (setIndex+1));

//        int color = ColorTemplate.MATERIAL_COLORS[setIndex%ColorTemplate.MATERIAL_COLORS.length];
        int color = generateDarkColor(setSize, setIndex);
        set.setAxisDependency(AxisDependency.LEFT); // RIGHT
        set.setColor(color);
        set.setCircleColor(Color.DKGRAY);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);

        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(color);
        set.setHighLightColor(Color.rgb(244, 117, 117));

        return set;
    }

    private int generateDarkColor(int size, int index) {
        int offset = 55 + 200/size * (index);
        int r = generateSingleColor(offset);
        int g = generateSingleColor(offset);
        int b = generateSingleColor(offset);
        Log.i("app", "generateDarkColor: offset: " + offset + ", color: " + r + "," + g + "," + b);
        return Color.argb(200, r, g, b);
    }

    private int generateSingleColor(int offset) {
//        return random.nextInt(8)*32;
        return offset + random.nextInt(128); // 127
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        mChart.centerViewToAnimated(e.getX(), e.getY(),
                mChart.getData().getDataSetByIndex(h.getDataSetIndex()).getAxisDependency(), 500);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    ///////////////////////////////////////////////////////////////////////////
    // LineChartContract.View
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void showLoadingProgress() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onLoadSuccess(List<PcPortfolio> portfolios) {
        Log.i("app", "onLoadSuccess: " + portfolios);
        bindData(portfolios);
    }

    @Override
    public void onLoadError(Throwable e) {
        UiUtils.showSnackMessage(this, e.getMessage(), true);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Value Formatters
    ///////////////////////////////////////////////////////////////////////////

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter () {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // write your logic here
            // access the YAxis object to get more information
            return mFormat.format(value);
        }
    }

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.00000"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                        ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value);
        }
    }
}
