package com.lanna.android.portfolioschart;

import com.lanna.android.portfolioschart.domain.Constant;
import com.lanna.android.portfolioschart.model.PcNav;
import com.lanna.android.portfolioschart.model.PcPortfolio;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class)
public class PcPortfolioTest extends ApplicationTestCase {

    private PcPortfolio model;

    @Before
    public void setUp() throws Exception {
        model = spy(new PcPortfolio());
    }

    private void verfyDates(@Constant.FilterMode int filterMode,
                            String dateResult, int expectedSize, PcNav... navs) {
        model.getFullNavs().clear();
        Collections.addAll(model.getFullNavs(), navs);
        model.setFilterMode(filterMode);

        assertThat(expectedSize, is(model.getFilterredNavs().size()));
        assertThat(dateResult, is(model.getFilterredNavs().get(0).getDate()));
    }

    @Test
    public void testAvailableDates_ByDay() {
        // available day
        testSingleDates("2017-01-20", 2,
                new PcNav("2017-01-20", 1), // expected
                new PcNav("2017-01-21", 1) // expected
        );
        assertThat("2017-01-21", is(model.getFilterredNavs().get(1).getDate())); // item 2

        // unavailable day
        testSingleDates("2017-01-20", 1,
                new PcNav("2017-01-20", 1), // expected
                new PcNav("2017-02-21", 0)
        );
    }

    private void testSingleDates(String dateResult, int expectedSize, PcNav... navs) {
        verfyDates(Constant.FilterMode.FILTER_BY_DAY, dateResult, expectedSize, navs);
    }

    @Test
    public void testMethodGenerateFilterredListIsCalled() throws Exception {
        model.setFilterMode(Constant.FilterMode.FILTER_BY_MONTH);
        verify(model).getNavsByFilter();

        assertThat(0, is(model.getFilterredNavs().size()));
    }

    @Test
    public void testAvailableDates_ByMonth() {
        // same month
        testMonthDates("2017-01-22", 1,
                new PcNav("2017-01-20", 1),
                new PcNav("2017-01-21", 1),
                new PcNav("2017-01-22", 1) // expected
        );

        // different month
        testMonthDates("2017-01-20", 2,
                new PcNav("2017-01-20", 1), // expected
                new PcNav("2017-02-21", 1) // expected
        );
        assertThat("2017-02-21", is(model.getFilterredNavs().get(1).getDate())); // item 2

        // next unavailable date
        testMonthDates("2017-01-20", 1,
                new PcNav("2017-01-20", 1), // expected
                new PcNav("2017-01-21", 0)
        );

        // an unavailable date in middle - same month
        testMonthDates("2017-01-22", 1,
                new PcNav("2017-01-20", 1),
                new PcNav("2017-01-21", 0),
                new PcNav("2017-01-22", 1) // expected
        );

        // an unavailable date in middle - different month
        testMonthDates("2017-01-22", 2,
                new PcNav("2017-01-20", 1),
                new PcNav("2017-01-21", 0),
                new PcNav("2017-01-22", 1), // expected
                new PcNav("2017-02-21", 1) // expected
        );
        assertThat("2017-02-21", is(model.getFilterredNavs().get(1).getDate())); // item 2
    }

    private void testMonthDates(String dateResult, int expectedSize, PcNav... navs) {
        verfyDates(Constant.FilterMode.FILTER_BY_MONTH, dateResult, expectedSize, navs);
    }

    @Test
    public void testAvailableDates_ByQuarter() {
        // same quarter: Mar, Jun, Sep, Dec
        testQuarterDates("2017-03-01", 1,
                new PcNav("2017-01-01", 1),
                new PcNav("2017-02-01", 1),
                new PcNav("2017-03-01", 1) // expected
        );
        testQuarterDates("2017-06-01", 1,
                new PcNav("2017-04-01", 1),
                new PcNav("2017-05-01", 1),
                new PcNav("2017-06-01", 1) // expected
        );
        testQuarterDates("2017-12-01", 1,
                new PcNav("2017-10-01", 1),
                new PcNav("2017-11-01", 1),
                new PcNav("2017-12-01", 1) // expected
        );

        // different quarter
        testQuarterDates("2017-02-01", 2,
                new PcNav("2017-02-01", 1), // expected
                new PcNav("2017-04-01", 1) // expected
        );
        assertThat("2017-04-01", is(model.getFilterredNavs().get(1).getDate())); // item 2

        // next unavailable date
        testQuarterDates("2017-01-01", 1,
                new PcNav("2017-01-01", 1), // expected
                new PcNav("2017-02-01", 0)
        );

        // an unavailable date in middle - same quarter
        testQuarterDates("2017-03-01", 1,
                new PcNav("2017-01-01", 1),
                new PcNav("2017-02-01", 0),
                new PcNav("2017-03-01", 1) // expected
        );

        // an unavailable date in middle - different quarter
        testQuarterDates("2017-03-01", 2,
                new PcNav("2017-01-01", 1),
                new PcNav("2017-02-01", 0),
                new PcNav("2017-03-01", 1), // expected
                new PcNav("2017-04-01", 1) // expected
        );
        assertThat("2017-04-01", is(model.getFilterredNavs().get(1).getDate())); // item 2
    }

    private void testQuarterDates(String dateResult, int expectedSize, PcNav... navs) {
        verfyDates(Constant.FilterMode.FILTER_BY_QUARTER, dateResult, expectedSize, navs);
    }
}