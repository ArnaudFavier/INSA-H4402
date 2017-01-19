package hexanome.agenda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import hexanome.agenda.R;
import hexanome.agenda.model.Event;
import hexanome.agenda.model.ListEvent;
import hexanome.agenda.model.ListRemind;

public class EventOverviewActivity extends AppCompatActivity {

    private static final int UPDATE_EVENT_INTENT_CODE = 81;
    private TextView hours_TV;
    private TextView profesors_TV;
    private TextView description_TV;
    private TextView place_TV;
    private Spinner remind_SP;
    private String hours;
    private DateTime startTime;
    private DateTime endTime;
    private Event currentEvent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_overview);

        hours_TV = (TextView) findViewById(R.id.EOV_Hours);
        profesors_TV = (TextView) findViewById(R.id.EOV_Profesors);
        place_TV = (TextView) findViewById(R.id.EOV_Place);
        description_TV = (TextView) findViewById(R.id.EOV_Description);
        remind_SP = (Spinner) findViewById(R.id.EOV_RemindSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ListRemind.reminds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remind_SP.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent.hasExtra("idEvent")) {
            long idEvent = intent.getLongExtra("idEvent", 0);
            for (Event event : ListEvent.events) {
                if (event.getId() == idEvent) {
                    currentEvent = event;
                    break;
                }
            }

            if (currentEvent != null) {
                //title + color TODO
                startTime = currentEvent.startTime;
                endTime = currentEvent.endTime;

                DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd/MM/yy");
                if (startTime.getYear() == endTime.getYear() && startTime.getMonthOfYear() == endTime.getMonthOfYear()
                        && startTime.getDayOfMonth() == endTime.getDayOfMonth()) {
                    hours = formatterDay.print(startTime) + "\nde " + startTime.getHourOfDay() + "h";
                    hours += startTime.getMinuteOfHour() == 0 ? "" : startTime.getMinuteOfHour();
                    hours += " à " + endTime.getHourOfDay() + "h";
                    hours += endTime.getMinuteOfHour() == 0 ? "" : endTime.getMinuteOfHour();
                } else {
                    hours = "de " + formatterDay.print(startTime) + ", " + startTime.getHourOfDay() + "h";
                    hours += startTime.getMinuteOfHour() == 0 ? "" : startTime.getMinuteOfHour();
                    hours += "\nà " + formatterDay.print(endTime) + ", " + endTime.getHourOfDay() + "h";
                    hours += endTime.getMinuteOfHour() == 0 ? "" : endTime.getMinuteOfHour();
                }

                hours_TV.setText(hours);
                place_TV.setText(currentEvent.lieu);
                description_TV.setText(currentEvent.description);
            }

            remind_SP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    currentEvent.remind = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void goToEditableMode(View view) {
        Intent i = new Intent(EventOverviewActivity.this, AddEventActivity.class);
        i.putExtra("idEvent", currentEvent.getId());
        startActivityForResult(i, UPDATE_EVENT_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_EVENT_INTENT_CODE && resultCode == RESULT_OK) {
            //If the event has been updated
            if (data.hasExtra("idEvent")) {
                currentEvent = null; //TODO  peut etre a suppr
                long idEvent = data.getLongExtra("idEvent", 0);
                for (Event event : ListEvent.events) {
                    if (event.getId() == idEvent) {
                        currentEvent = event;
                        break;
                    }
                }

                if (currentEvent != null) {
                    //title TODO
                    place_TV.setText(currentEvent.lieu);
                    startTime = currentEvent.startTime;
                    endTime = currentEvent.endTime;
                    DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd/MM/yyyy");
                    DateTimeFormatter formatterHour = DateTimeFormat.forPattern("hh:mm");
                    if (formatterDay.print(startTime).equals(formatterDay.print(endTime))) {
                        hours = formatterHour.print(startTime) + " - " + formatterHour.print(endTime);
                    } else {
                        hours = formatterDay.print(startTime) + " " + formatterHour.print(startTime) + "\n" +
                                formatterDay.print(endTime) + " " + formatterHour.print(endTime);
                    }
                    hours_TV.setText(hours);
                    //color TODO
                    description_TV.setText(currentEvent.description);
                    remind_SP.setSelection(currentEvent.remind);
                }
            }
        }
    }
}
