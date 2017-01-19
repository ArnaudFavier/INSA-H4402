package hexanome.agenda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.turkialkhateeb.materialcolorpicker.ColorListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

import hexanome.agenda.R;
import hexanome.agenda.customui.ColorChooserEditedDialog;
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
    private Event eventEdit = null;
    private String description = "";
    private EditText editTextTitle;
    private EditText editTextPlace;
    private EditText editTextDescription;
    private TextView buttonDateStart;
    private TextView buttonTimeStart;
    private TextView buttonDateEnd;
    private TextView buttonTimeEnd;
    private Button buttonColor;
    private Spinner spinnerRemind;
    private Button validationButton;

    private Event currentEvent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        editTextTitle = (EditText) findViewById(R.id.edit_text_new_event_name);
        editTextPlace = (EditText) findViewById(R.id.edit_text_new_event_place);
        editTextDescription = (EditText) findViewById(R.id.edit_text_description);
        buttonDateStart = (Button) findViewById(R.id.button_date_start);
        buttonTimeStart = (Button) findViewById(R.id.button_time_start);
        buttonDateEnd = (Button) findViewById(R.id.button_date_end);
        buttonTimeEnd = (Button) findViewById(R.id.button_time_end);
        spinnerRemind = (Spinner) findViewById(R.id.spinner_remind);
        buttonColor = (Button) findViewById(R.id.button_color);
        validationButton = (Button) findViewById(R.id.button_add_event);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ListRemind.reminds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRemind.setAdapter(adapter);

        // If it's a modification, then when we take the intent data
        Intent intent = getIntent();
        if (intent.hasExtra("idEvent")) {
            editionMode = true;
            currentEvent = null; //TODO  peut etre a suppr
            long idEvent = intent.getLongExtra("idEvent", 0);
            for (Event event : ListEvent.events) {
                if (event.getId() == idEvent) {
                    currentEvent = event;
                    break;
                }
            }

            if (currentEvent != null) {
                DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd/MM/yyyy");
                DateTimeFormatter formatterHour = DateTimeFormat.forPattern("hh:mm");
                editTextTitle.setText(currentEvent.title);
                editTextPlace.setText(currentEvent.lieu);
                editTextDescription.setText(currentEvent.description);
                spinnerRemind.setSelection(currentEvent.remind);
                startDate = currentEvent.startTime;
                buttonDateStart.setText(formatterDay.print(startDate));
                buttonTimeStart.setText(formatterHour.print(startDate));
                endDate = currentEvent.endTime;
                buttonDateEnd.setText(formatterDay.print(endDate));
                buttonTimeEnd.setText(formatterHour.print(endDate));
                buttonColor.setBackgroundColor(currentEvent.color); // -11751600=>md_green_500
                pickedColor = currentEvent.color;
                startDateSelected = true;
                startTimeSelected = true;
                endDateSelected = true;
                endTimeSelected = true;
                eventEdit = currentEvent;

                validationButton.setText("Modifier");
            }
        }

        final Button buttonColor = (Button) findViewById(R.id.button_color);
        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChooserEditedDialog dialog = new ColorChooserEditedDialog(AddEventActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        buttonColor.setBackgroundColor(color);
                        pickedColor = color;
                    }
                });

                dialog.show();

            }
        });

        buttonDateStart.setOnClickListener(new View.OnClickListener() {
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

                                if (!endDateSelected) {
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

        buttonTimeStart.setOnClickListener(new View.OnClickListener() {
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

                                if (!endTimeSelected) {
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

        buttonTimeEnd.setOnClickListener(new View.OnClickListener() {
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
                } else if (editionMode) {
                    //if the event doesn't exist
                    if (eventEdit == null) {
                        setResult(RESULT_CANCELED);
                        finish();
                    } else {
                        //else we update its datas
                        eventEdit.title = editTextTitle.getText().toString();
                        eventEdit.color = pickedColor;
                        eventEdit.startTime = startDate;
                        eventEdit.endTime = endDate;
                        eventEdit.lieu = editTextPlace.getText().toString();
                        eventEdit.description = description;
                        eventEdit.remind = spinnerRemind.getSelectedItemPosition();

                        Intent i = new Intent();
                        Toast.makeText(AddEventActivity.this, "Evenement modifié avec succès", Toast.LENGTH_SHORT).show();
                        i.putExtra("idEvent", eventEdit.getId());
                        setResult(RESULT_OK, i);
                        finish();
                    }
                } else {
                    ListEvent.events.add(new Event(pickedColor, startDate, endDate, eventName));
                    Toast.makeText(AddEventActivity.this, "Evenement ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.left_to_right_anim, R.anim.right_to_left_anim);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right_anim, R.anim.right_to_left_anim);
    }
}
