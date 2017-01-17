package hexanome.agenda.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.events.Event;

public class DayActivity extends AppCompatActivity {

    private DateTime day;
    private List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        this.initialize();

        getSupportActionBar().setTitle(day.toString("EEEE d MMMM"));
    }

    private void initialize() {
        day = new DateTime();
        events = new ArrayList<>();

        /* Tests */
        events.add(new Event(2, new DateTime().withTime(8,0,0,0), new DateTime().withTime(12,0,0,0), "IHM"));
        events.add(new Event(3, new DateTime().withTime(14,0,0,0), new DateTime().withTime(16,0,0,0), "LV1"));
        events.add(new Event(4, new DateTime().withTime(16,0,0,0), new DateTime().withTime(18,0,0,0), "PLD Mars"));
    }
}
