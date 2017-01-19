package hexanome.agenda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hexanome.agenda.R;
import hexanome.agenda.model.Event;
import hexanome.agenda.model.ListEvent;
import hexanome.agenda.model.ListRemind;

public class AddEventActivity extends AppCompatActivity {

    DateTime startDate = new DateTime();
    DateTime endDate = new DateTime();
    int pickedColor = -11751600; // md_green_500

    boolean startDateSelected = false;
    boolean startTimeSelected = false;
    boolean endDateSelected = false;
    boolean endTimeSelected = false;

    boolean editionMode = false;
    private String oldTitle = "";
    private String description = "";
    private String profesors = "";
    private EditText title_et;
    private EditText place_et;
    private EditText description_et;
    private EditText profesor_et;
    private TextView start_date_tv;
    private TextView start_time_tv;
    private TextView end_date_tv;
    private TextView end_time_tv;
    private Button color_bt;
    private Spinner remind_SP;

    private Event currentEvent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        title_et = (EditText) findViewById(R.id.edit_text_new_event_name);
        place_et = (EditText) findViewById(R.id.edit_text_new_event_place);
        profesor_et = (EditText) findViewById(R.id.AE_profesors_ET);
        description_et = (EditText) findViewById(R.id.AE_description_ET);
        start_date_tv = (Button) findViewById(R.id.button_date_start);
        start_time_tv = (Button) findViewById(R.id.button_time_start);
        end_date_tv = (Button) findViewById(R.id.button_date_end);
        end_time_tv = (Button) findViewById(R.id.button_time_end);
        remind_SP = (Spinner) findViewById(R.id.AE_RemindSpinner);
        color_bt = (Button) findViewById(R.id.button_color);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ListRemind.reminds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remind_SP.setAdapter(adapter);

        // If it's a modification, then when we take the intent data
        Intent intent = getIntent();
        if(intent.hasExtra("idEvent")) {
            editionMode = true;
            currentEvent = null; //TODO  peut etre a suppr
            long idEvent = intent.getLongExtra("idEvent", 0);
            for (Event event : ListEvent.events) {
                if (event.getId() == idEvent) {
                    currentEvent = event;
                    break;
                }
            }

            if(currentEvent!=null){
                DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd/MM/yyyy");
                DateTimeFormatter formatterHour = DateTimeFormat.forPattern("hh:mm");
                title_et.setText(currentEvent.title);
                place_et.setText(currentEvent.lieu);
                profesor_et.setText(currentEvent.profesors);
                description_et.setText(currentEvent.description);
                remind_SP.setSelection(currentEvent);
                startDate = currentEvent.startTime;
                start_date_tv.setText(formatterDay.print(startDate));
                start_time_tv.setText(formatterHour.print(startDate));
                endDate = currentEvent.endTime;
                end_date_tv.setText(formatterDay.print(endDate));
                end_time_tv.setText(formatterHour.print(endDate));
                color_bt.setBackgroundColor(currentEvent.color); // -11751600=>md_green_500
                startDateSelected = true;
                startTimeSelected = true;
                endDateSelected = true;
                endTimeSelected = true;
            }
        }

        final Button buttonColor = (Button) findViewById(R.id.button_color);
        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChooserDialog dialog = new ColorChooserDialog(AddEventActivity.this);
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        buttonColor.setBackgroundColor(color);
                        pickedColor = color;
                    }
                });
                //customize the dialog however you want
                dialog.show();
            }
        });

        final TextView textViewDateStart = (TextView) findViewById(R.id.button_date_start);
        textViewDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                startDate = new DateTime(year, monthOfYear + 1, dayOfMonth, startDate.getHourOfDay(), startDate.getMinuteOfHour());
                                DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
                                ((TextView) AddEventActivity.this.findViewById(R.id.button_date_start)).setText(dtfOut.print(startDate));
                                startDateSelected = true;

                                if(!endDateSelected){
                                    endDate = new DateTime(year, monthOfYear + 1, dayOfMonth, endDate.getHourOfDay(), endDate.getMinuteOfHour());
                                    ((Button) AddEventActivity.this.findViewById(R.id.button_date_end)).setText(dtfOut.print(endDate));
                                    endDateSelected = true;
                                }
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });

        final Button buttonDateEnd = (Button) findViewById(R.id.button_date_end);
        buttonDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                endDate = new DateTime(year, monthOfYear + 1, dayOfMonth, endDate.getHourOfDay(), endDate.getMinuteOfHour());
                                ;
                                DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy");
                                ((Button) AddEventActivity.this.findViewById(R.id.button_date_end)).setText(dtfOut.print(endDate));
                                endDateSelected = true;
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        final TextView textViewTimeStart = (TextView) findViewById(R.id.button_time_start);
        textViewTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                startDate = new DateTime(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth(), hourOfDay, minute);
                                DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
                                ((TextView) AddEventActivity.this.findViewById(R.id.button_time_start)).setText(dtf.print(startDate));
                                startTimeSelected = true;

                                if(!endTimeSelected){
                                    endDate = new DateTime(endDate.getYear(), endDate.getMonthOfYear(), endDate.getDayOfMonth(), hourOfDay, minute);
                                    endDate = endDate.plusHours(1);
                                    ((TextView) AddEventActivity.this.findViewById(R.id.button_time_end)).setText(dtf.print(endDate));
                                    endTimeSelected = true;
                                }
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                dpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        final TextView textViewTimeEnd = (TextView) findViewById(R.id.button_time_end);
        textViewTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                endDate = new DateTime(endDate.getYear(), endDate.getMonthOfYear(), endDate.getDayOfMonth(), hourOfDay, minute);
                                DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm");
                                ((TextView) AddEventActivity.this.findViewById(R.id.button_time_end)).setText(dtf.print(endDate));
                                endTimeSelected = true;
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                dpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });



        Button validationButton = (Button) findViewById(R.id.button_add_event);
        validationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = ((EditText) findViewById(R.id.edit_text_new_event_name)).getText().toString();

                boolean error = false;
                String errorMsg = "";
                if (eventName.trim().length() == 0) {
                    error = true;
                    errorMsg = "Le nom d'évènement doit être complété";
                }
                if (!startDateSelected || !endDateSelected || !startTimeSelected || !endTimeSelected) {
                    error = true;
                    errorMsg = "Les dates de l'évènement doivent être complétées";
                }

                if (error) {
                    Toast.makeText(AddEventActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
                else if(editionMode){
                    Event currentEvent = null;
                    for (Event e: ListEvent.events){
                        if(e.title.equals(oldTitle)){
                            currentEvent = e;
                            break;
                        }
                    }

                    //if the event doesn't exist
                    if(currentEvent.equals(null)){
                        setResult(RESULT_CANCELED);
                        finish();
                    }

                    //else we update its datas
                    currentEvent.title = title_et.getText().toString();
                    currentEvent.color = pickedColor;
                    currentEvent.startTime = startDate;
                    currentEvent.endTime = endDate;
                    currentEvent.lieu = place_et.getText().toString();
                    currentEvent.description = description;
                    currentEvent.profesors = profesors;
                    currentEvent.remind = remind_SP.getSelectedItemPosition();

                    Intent i = new Intent();
                    i.putExtra("idEvent", currentEvent.getId());
                    setResult(RESULT_OK, i);
                    finish();
                }
                else {
                    ListEvent.events.add(new Event(pickedColor, startDate, endDate, eventName));
                    Toast.makeText(AddEventActivity.this, "Evenement ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right_anim, R.anim.right_to_left_anim);
    }
}
