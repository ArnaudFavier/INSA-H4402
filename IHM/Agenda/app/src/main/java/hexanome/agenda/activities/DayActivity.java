package hexanome.agenda.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.model.Event;
import hexanome.agenda.model.ListEvent;

/**
 * Activity showing events of a particular day
 */
public class DayActivity extends AppCompatActivity {

    /**
     * The day to show events
     */
    private DateTime day;

    /**
     * Constructor with the day to show events
     * @param day The day to show events
     */
    public DayActivity(DateTime day) {
        initialize(day);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        initialize(null);

        getSupportActionBar().setTitle(day.toString("EEEE d MMMM"));

        List<Event> events = getListEventToday();
        // ToDo : Put into the view
    }

    /**
     * Save the day to show events
     * @param day The day to show events
     */
    private void initialize(DateTime day) {
        if (day == null)
            day = new DateTime();
        this.day = day;
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
