package hexanome.agenda.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import hexanome.agenda.R;
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

    public DayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View createdView = inflater.inflate(R.layout.fragment_day, container, false);
        super.onCreate(savedInstanceState);

        getActivity().setTitle(day.toString("EEEE d MMMM"));

        List<Event> events = getListEventToday();

        return createdView;
    }

    /**
     * Get the list of events during the day
     * @return A list of events during the day
     */
    private List<Event> getListEventToday() {
        List<Event> events = new ArrayList<>();

        for (Event e : ListEvent.events)
            if (e.startTime.isEqual(this.day))
                events.add(e);

        return events;
    }
}
