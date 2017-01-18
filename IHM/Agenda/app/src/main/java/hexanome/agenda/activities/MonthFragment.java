package hexanome.agenda.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.thehayro.view.InfinitePagerAdapter;

import org.joda.time.DateTime;

import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.customui.CalendarMonthView;
import hexanome.agenda.model.ListEvent;

public class MonthFragment extends Fragment {
    private ViewPager mPager;
    private MonthInfinitePagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View createdView = inflater.inflate(R.layout.fragment_month, container, false);

        mPager = (ViewPager) createdView.findViewById(R.id.month_pager);
        // mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPagerAdapter = new MonthInfinitePagerAdapter(0);
        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem(1);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pageSelected) {
                DateTime currentMonth = new DateTime();
                DateTime chosenMonth = currentMonth.plusMonths(mPagerAdapter.getIndicator());
                String month = chosenMonth.monthOfYear().getAsText();
                month = month.substring(0, 1).toUpperCase() + month.substring(1);
                getActivity().setTitle(month + " " + chosenMonth.year().get());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return createdView;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    private class MonthInfinitePagerAdapter extends InfinitePagerAdapter<Integer> {

        public MonthInfinitePagerAdapter(Integer initValue) {
            super(initValue);
        }

        public Integer getIndicator() {
            return getCurrentIndicator();
        }

        public Integer getNextIndicator() {
            return getCurrentIndicator() + 1;
        }

        public Integer getPreviousIndicator() {
            return getCurrentIndicator() - 1;
        }

        @Override
        public ViewGroup instantiateItem(Integer indicator) {
            final FrameLayout layout = (FrameLayout) ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.month_view, null);
            layout.setTag(indicator);
            DateTime selectedMonth = new DateTime().plusMonths(indicator);
            CalendarMonthView calendarMonthView = (CalendarMonthView) layout.findViewById(R.id.calendar_month_view);
            calendarMonthView.setMonth(selectedMonth);
            calendarMonthView.setEvents(ListEvent.events);
            calendarMonthView.setFragManager(mFragmentManager);

            return layout;
        }

        @Override
        public String getStringRepresentation(final Integer currentIndicator) {
            return String.valueOf(currentIndicator);
        }

        @Override
        public Integer convertToIndicator(final String representation) {
            return Integer.valueOf(representation);
        }
    }

    public void refresh() {
        List<ViewGroup> pages = mPagerAdapter.getViewGroups();
        for (ViewGroup viewGroup : pages) {
            CalendarMonthView calendarMonthView = (CalendarMonthView) viewGroup.findViewById(R.id.calendar_month_view);
            calendarMonthView.setEvents(ListEvent.events);
        }
    }
}
