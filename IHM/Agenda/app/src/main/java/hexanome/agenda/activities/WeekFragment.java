package hexanome.agenda.activities;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.model.Event;
import hexanome.agenda.model.ListEvent;

public class WeekFragment extends Fragment {

    private  WeekView mWeekView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View createdView = inflater.inflate(R.layout.fragment_week, container, false);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) createdView.findViewById(R.id.weekView);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                Intent intent = new Intent(getActivity(), EventOverviewActivity.class);
                intent.putExtra("idEvent", event.getId());
                startActivityForResult(intent, MainActivity.CONSULT_EVENT_ACTIVITY);
            }
        });

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                List<WeekViewEvent> events = new ArrayList<>(ListEvent.events.size());
                for (Event event : ListEvent.events) {
                    if ((event.startTime.getYear() == newYear || event.endTime.getYear() == newYear) &&
                            (event.startTime.getMonthOfYear() == newMonth || event.endTime.getMonthOfYear() == newMonth)) {
                        events.add(event.parseToWeekEvent());
                    }
                }
                return events;
            }
        });

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

            }
        });

        return createdView;
    }

    public void refresh() {
        if(mWeekView != null) {
            mWeekView.notifyDatasetChanged();
        }
    }
}
