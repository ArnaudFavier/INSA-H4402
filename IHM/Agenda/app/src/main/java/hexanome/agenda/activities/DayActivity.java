package hexanome.agenda.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.joda.time.DateTime;

import hexanome.agenda.R;

public class DayActivity extends AppCompatActivity {

    private DateTime day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        this.initialize();

        getSupportActionBar().setTitle(day.toString("EEEE d MMMM"));
    }

    private void initialize() {
        day = new DateTime();
    }
}
