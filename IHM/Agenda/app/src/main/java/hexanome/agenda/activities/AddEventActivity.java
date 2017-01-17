package hexanome.agenda.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import hexanome.agenda.R;

public class AddEventActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        final Button buttonColor = (Button)findViewById(R.id.button_color);
        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChooserDialog dialog = new ColorChooserDialog(AddEventActivity.this);
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        
                    }
                });
                //customize the dialog however you want
                dialog.show();
            }
        });

        final TextView textViewDateStart = (TextView)findViewById(R.id.txtview_date_start);
        textViewDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AddEventActivity.this);

                dialog.setContentView(R.layout.date_picker_custom);
                dialog.show();

            }
        });

        final TextView textViewDateEnd = (TextView)findViewById(R.id.txtview_date_end);
        textViewDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AddEventActivity.this);

                dialog.setContentView(R.layout.date_picker_custom);
                dialog.show();

            }
        });

        final TextView textViewTimeStart = (TextView)findViewById(R.id.txtview_time_start);
        textViewTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AddEventActivity.this);

                dialog.setContentView(R.layout.time_picker_custom);
                dialog.show();

            }
        });

        final TextView textViewTimeEnd = (TextView)findViewById(R.id.txtview_time_end);
        textViewTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(AddEventActivity.this);

                dialog.setContentView(R.layout.time_picker_custom);
                dialog.show();

            }
        });
    }

}
