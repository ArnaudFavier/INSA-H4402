package hexanome.agenda.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.model.Event;
import hexanome.agenda.model.ListEvent;

public class EventOverviewActivity extends AppCompatActivity {

    private TextView hours_TV;
    private TextView profesors_TV;
    private TextView description_TV;
    private TextView place_TV;
    private Spinner remind_SP;

    private String title;
    private String hours;
    private String place;
    private String profesors;
    private String description;
    private DateTime startTime;
    private DateTime endTime;
    private int color;

    private static final int UPDATE_EVENT_INTENT_CODE = 81;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_overview);

        hours_TV = (TextView) findViewById(R.id.EOV_Hours);
        profesors_TV = (TextView) findViewById(R.id.EOV_Profesors);
        place_TV = (TextView) findViewById(R.id.EOV_Place);
        description_TV = (TextView) findViewById(R.id.EOV_Description);
        remind_SP = (Spinner) findViewById(R.id.EOV_RemindSpinner);

        List remindChoicesList = new ArrayList<String>();
        remindChoicesList.add("5 minutes before");
        remindChoicesList.add("10 minutes before");
        remindChoicesList.add("15 minutes before");
        remindChoicesList.add("30 minutes before");
        remindChoicesList.add("1 hour before");
        remindChoicesList.add("2 hours before");
        remindChoicesList.add("1 day before");

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, remindChoicesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remind_SP.setAdapter(adapter);

        Event event = ListEvent.events.get(0);
        title = event.title;
        startTime = event.startTime;
        endTime = event.endTime;

        DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd/MM/yy");
        if(startTime.getYear()==endTime.getYear() && startTime.getMonthOfYear() == endTime.getMonthOfYear()
                && startTime.getDayOfMonth()==endTime.getDayOfMonth()) {
            hours = formatterDay.print(startTime) + "\nde " +startTime.getHourOfDay() + "h";
            hours += startTime.getMinuteOfHour() == 0 ? "" : startTime.getMinuteOfHour();
            hours += " à " + endTime.getHourOfDay() + "h";
            hours += endTime.getMinuteOfHour() == 0 ? "" : endTime.getMinuteOfHour();
        }else{
            hours = "de " +formatterDay.print(startTime) + ", " +startTime.getHourOfDay() + "h";
            hours += startTime.getMinuteOfHour() == 0 ? "" : startTime.getMinuteOfHour();
            hours += "\nà " + formatterDay.print(endTime) + ", " + endTime.getHourOfDay() + "h";
            hours += endTime.getMinuteOfHour() == 0 ? "" : endTime.getMinuteOfHour();
        }
        color = event.color;
        place = event.lieu;
        profesors = "Léa Laporte";
        description = event.description;

        hours_TV.setText(hours);
        place_TV.setText(place);
        profesors_TV.setText(profesors);
        description_TV.setText(description);

    }

    public void goToEditableMode(View view){
        Intent i = new Intent(EventOverviewActivity.this, AddEventActivity.class);
        i.putExtra("title", title);
        i.putExtra("place", place);
        i.putExtra("startDateTime", startTime.getMillis());
        i.putExtra("endDateTime", endTime.getMillis());
        i.putExtra("color", color);
        i.putExtra("remind", remind_SP.getSelectedItemPosition());
        i.putExtra("profesors", profesors);
        i.putExtra("description", description);
        startActivityForResult(i, UPDATE_EVENT_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == UPDATE_EVENT_INTENT_CODE && resultCode == RESULT_OK){
            //Event has been updated
            if(data.hasExtra("title")) {
                title = data.getStringExtra("title");
                //TODO set Title bar
            }
            if(data.hasExtra("place")){
                place = data.getStringExtra("place");
                place_TV.setText(place);
            }
            if(data.hasExtra("color")){
                //TODO set color bar
            }
            if(data.hasExtra("startDateTime") && data.hasExtra("endDateTime")){
                long startDateTimeMillis = data.getLongExtra("startDateTime", 0);
                long endDateTimeMillis = data.getLongExtra("endDateTime", 0);
                startTime = new DateTime(startDateTimeMillis);
                endTime = new DateTime(endDateTimeMillis);
                DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd/MM/yyyy");
                DateTimeFormatter formatterHour = DateTimeFormat.forPattern("hh:mm");
                if(formatterDay.print(startTime).equals(formatterDay.print(endTime))){
                    hours = formatterHour.print(startTime) + " - " + formatterHour.print(endTime);
                }else{
                    hours = formatterDay.print(startTime) + " " + formatterHour.print(startTime) + "\n" +
                            formatterDay.print(endTime) + " " + formatterHour.print(endTime);
                }
                hours_TV.setText(hours);
            }
            if(data.hasExtra("description")){
                description = data.getStringExtra("description");
                description_TV.setText(description);
            }
            if(data.hasExtra("profesors")){
                profesors = data.getStringExtra("profesors");
                profesors_TV.setText(profesors);
            }
            /*if(data.hasExtra("remind")){
                //TODO: get the index of selected choice and set the spinner with it...
            }
             */
        }
    }
}
