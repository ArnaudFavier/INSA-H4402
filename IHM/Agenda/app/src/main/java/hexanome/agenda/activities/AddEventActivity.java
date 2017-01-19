package hexanome.agenda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

import hexanome.agenda.R;
import hexanome.agenda.model.Event;
import hexanome.agenda.model.ListEvent;

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
    private TextView start_date_tv;
    private TextView start_time_tv;
    private TextView end_date_tv;
    private TextView end_time_tv;
    private Button color_bt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // If it's a modification, then when we take the intent data
        Intent intent = getIntent();
        if(intent.hasExtra("title")) {
            title_et = (EditText) findViewById(R.id.edit_text_new_event_name);
            oldTitle = intent.getStringExtra("title");
            title_et.setText(oldTitle);
            editionMode = true;
        }
        if(intent.hasExtra("place") && editionMode){
            place_et = (EditText) findViewById(R.id.edit_text_new_event_place);
            place_et.setText(intent.getStringExtra("place"));
        }
        if(intent.hasExtra("color") && editionMode){
            color_bt = (Button) findViewById(R.id.button_color);
            color_bt.setBackgroundColor(intent.getIntExtra("color", -11751600)); // -11751600=>md_green_500
        }
        if(intent.hasExtra("startDateTime") && editionMode){
            start_date_tv = (TextView) findViewById(R.id.txtview_date_start);
            start_time_tv = (TextView) findViewById(R.id.txtview_time_start);
            long startDateTimeMillis = intent.getLongExtra("startDateTime", 0);
            startDate = new DateTime(startDateTimeMillis);
            DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd/MM/yyyy");
            DateTimeFormatter formatterHour = DateTimeFormat.forPattern("hh:mm");
            start_date_tv.setText(formatterDay.print(startDate));
            start_time_tv.setText(formatterHour.print(startDate));
            startDateSelected = true;
            startTimeSelected = true;
        }
        if(intent.hasExtra("endDateTime") && editionMode){
            end_date_tv = (TextView) findViewById(R.id.txtview_date_end);
            end_time_tv = (TextView) findViewById(R.id.txtview_time_end);
            long endDateTimeMillis = intent.getLongExtra("endDateTime", 0);
            endDate = new DateTime(endDateTimeMillis);
            DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd/MM/yyyy");
            DateTimeFormatter formatterHour = DateTimeFormat.forPattern("hh:mm");
            end_date_tv.setText(formatterDay.print(endDate));
            end_time_tv.setText(formatterHour.print(endDate));
            endDateSelected = true;
            endTimeSelected = true;
        }
        if(intent.hasExtra("description") && editionMode){
            description = intent.getStringExtra("description");
            //TODO: Add a textview for the description and set it.
        }
        if(intent.hasExtra("profesors") && editionMode){
            profesors = intent.getStringExtra("profesors");
            //TODO: Add a textview for profesors and set it.
        }
        /*if(intent.hasExtra("remind") && editionMode){
            //TODO: get the index of selected choice and set the spinner with it...
        }
         */
        //

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

        final TextView textViewDateStart = (TextView) findViewById(R.id.txtview_date_start);
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
                                ((TextView) AddEventActivity.this.findViewById(R.id.txtview_date_start)).setText(dtfOut.print(startDate));
                                startDateSelected = true;

                                if(!endDateSelected){
                                    endDate = new DateTime(year, monthOfYear + 1, dayOfMonth, endDate.getHourOfDay(), endDate.getMinuteOfHour());
                                    ((TextView) AddEventActivity.this.findViewById(R.id.txtview_date_end)).setText(dtfOut.print(endDate));
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

        final TextView textViewDateEnd = (TextView) findViewById(R.id.txtview_date_end);
        textViewDateEnd.setOnClickListener(new View.OnClickListener() {
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
                                ((TextView) AddEventActivity.this.findViewById(R.id.txtview_date_end)).setText(dtfOut.print(endDate));
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

        final TextView textViewTimeStart = (TextView) findViewById(R.id.txtview_time_start);
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
                                ((TextView) AddEventActivity.this.findViewById(R.id.txtview_time_start)).setText(dtf.print(startDate));
                                startTimeSelected = true;

                                if(!endTimeSelected){
                                    endDate = new DateTime(endDate.getYear(), endDate.getMonthOfYear(), endDate.getDayOfMonth(), hourOfDay, minute);
                                    endDate = endDate.plusHours(1);
                                    ((TextView) AddEventActivity.this.findViewById(R.id.txtview_time_end)).setText(dtf.print(endDate));
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

        final TextView textViewTimeEnd = (TextView) findViewById(R.id.txtview_time_end);
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
                                ((TextView) AddEventActivity.this.findViewById(R.id.txtview_time_end)).setText(dtf.print(endDate));
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

                    Intent i = new Intent();
                    i.putExtra("title", title_et.getText().toString());
                    i.putExtra("place", place_et.getText().toString());
                    i.putExtra("startDateTime", startDate.getMillis());
                    i.putExtra("endDateTime", endDate.getMillis());
                    i.putExtra("color", pickedColor);
                    i.putExtra("profesors", profesors);
                    i.putExtra("description", description);
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

}
