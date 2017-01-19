package hexanome.agenda.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thehayro.view.InfinitePagerAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.customui.CalendarMonthView;
import hexanome.agenda.model.Event;
import hexanome.agenda.model.ListEvent;

/**
 * Activity showing events of a particular day
 */
public class DayFragment extends Fragment {

    /**
     * The day to show events
     */
    private DateTime day = new DateTime();

    private ViewPager mPager;
    private DayInfinitePagerAdapter mPagerAdapter;
    private FragmentManager mFragmentManager;

    public DayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View createdView = inflater.inflate(R.layout.fragment_day, container, false);
        super.onCreate(savedInstanceState);

        refresh();
        String month = day.monthOfYear().getAsText();
        String dayString = day.dayOfWeek().getAsText();
        int dayInt = day.getDayOfMonth();
        month = month.substring(0, 1).toUpperCase() + month.substring(1);
        dayString = dayString.substring(0, 1).toUpperCase() + dayString.substring(1);
        getActivity().setTitle(dayString + " " + dayInt + " " + month);

        mPager = (ViewPager) createdView.findViewById(R.id.day_pager);

        mPagerAdapter = new DayInfinitePagerAdapter(0);
        mPager.setAdapter(mPagerAdapter);

        mPager.setCurrentItem(1);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pageSelected) {
                DateTime chosenDay = day.plusDays(mPagerAdapter.getIndicator());
                String month = chosenDay.monthOfYear().getAsText();
                String day = chosenDay.dayOfWeek().getAsText();
                int dayInt = chosenDay.getDayOfMonth();
                month = month.substring(0, 1).toUpperCase() + month.substring(1);
                day = day.substring(0, 1).toUpperCase() + day.substring(1);
                getActivity().setTitle(day + " " + dayInt + " " + month);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return createdView;
    }

    private ArrayList<Event> getListEventsDay(int offset) {
        ArrayList<Event> events = new ArrayList<>();

        DateTime date = day.plusDays(offset);
        for (Event event : ListEvent.events) {
            if (event.startTime.withTimeAtStartOfDay().isEqual(date.withTimeAtStartOfDay())) {
                events.add(event);
            }
        }
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.startTime.compareTo(e2.startTime);
            }
        });
        return events;
    }

    public void refresh() {
        if(mPagerAdapter != null) {
            List<ViewGroup> pages = mPagerAdapter.getViewGroups();
            for (ViewGroup viewGroup : pages) {
                if (viewGroup != null) {
                    RecyclerView recyclerViewDayEvents = (RecyclerView) viewGroup.findViewById(R.id.recycler_view_day_events);
                    EventAdapter adapter = (EventAdapter) recyclerViewDayEvents.getAdapter();
                    if (adapter != null) {
                        adapter.refresh();
                    }
                }
            }
        }
    }

    public void setDay(DateTime day) {
        this.day = day;
    }

    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        ArrayList<Event> eventList;
        private final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
        private final int offset;

        public EventAdapter(int offset) {
            this.offset = offset;
            eventList = getListEventsDay(offset);
        }

        public void refresh(){
            eventList =  getListEventsDay(offset);
            notifyDataSetChanged();
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list_view_item, parent, false);
            EventViewHolder viewHolder = new EventViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            Event event = eventList.get(position);

            holder.textStartTime.setText(timeFormatter.print(event.startTime));
            holder.textEndTime.setText(timeFormatter.print(event.endTime));
            holder.textEventName.setText(event.title);

            fillFieldOrHideIfNull(holder.textEventDescription, event.description);
            fillFieldOrHideIfNull(holder.textEventPlace, event.lieu);

            holder.colorView.setBackgroundColor(event.color);
        }

        private void fillFieldOrHideIfNull(TextView textView, String text) {
            if (text != null) {
                textView.setText(text);
                textView.setVisibility(View.VISIBLE);
            }
            else {
                textView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        class EventViewHolder extends RecyclerView.ViewHolder {
            TextView textStartTime;
            TextView textEndTime;
            TextView textEventName;
            TextView textEventDescription;
            TextView textEventPlace;
            View colorView;

            EventViewHolder(View itemView) {
                super(itemView);
                textStartTime = (TextView) itemView.findViewById(R.id.day_item_start_hour);
                textEndTime = (TextView) itemView.findViewById(R.id.day_item_end_hour);
                textEventName = (TextView) itemView.findViewById(R.id.day_item_event_name);
                textEventDescription = (TextView) itemView.findViewById(R.id.day_item_event_description);
                textEventPlace = (TextView) itemView.findViewById(R.id.day_item_event_place);
                colorView = itemView.findViewById(R.id.list_item_color_view);
            }
        }
    }

    private class DayInfinitePagerAdapter extends InfinitePagerAdapter<Integer> {

        public DayInfinitePagerAdapter(Integer initValue) {
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
            final LinearLayout layout = (LinearLayout) ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.day_list_view, null);
            layout.setTag(indicator);
            DateTime selectedMonth = new DateTime().plusMonths(indicator);

            RecyclerView recyclerViewDayEvents = (RecyclerView) layout.findViewById(R.id.recycler_view_day_events);

            EventAdapter adapter = new EventAdapter(indicator);
            recyclerViewDayEvents.setAdapter(adapter);
            recyclerViewDayEvents.setLayoutManager(new LinearLayoutManager(getActivity()));

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
}
