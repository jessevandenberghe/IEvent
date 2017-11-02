package be.pxl.ievent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.pxl.ievent.App;
import be.pxl.ievent.R;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.models.RealmString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EditEventActivity extends BaseActivity {

    @BindView(R.id.et_edit_event_name) EditText etTitle;
    @BindView(R.id.et_edit_event_organisor) EditText etOrganisator;
    @BindView(R.id.et_edit_event_location) EditText etLocation;
    @BindView(R.id.et_edit_event_location_name) EditText etLocationName;
    @BindView(R.id.et_edit_event_start_date) EditText etStartDate;
    @BindView(R.id.et_edit_event_end_date) EditText etEndDate;
    @BindView(R.id.et_edit_event_description) EditText etDescription;
    @BindView(R.id.et_edit_event_amount) EditText etAmount;
    @BindView(R.id.spinner_edit_category) Spinner spCategory;

    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ButterKnife.bind(this);

        setup();
        save();
    }

    private void setup() {
        mEvent = mRealm.where(Event.class).equalTo("id", getIntent().getIntExtra("eventId",0)).findFirst();

        Date startDate = new Date(mEvent.getStartDateTime());
        Date endDate = new Date(mEvent.getEndDateTime());
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleHour = new SimpleDateFormat("HH:mm");

        etTitle.setText(mEvent.getName());
        etOrganisator.setText(mEvent.getOrganisator());
        etStartDate.setText(simpleDate.format(startDate) + "" + simpleHour);
        etLocation.setText(mEvent.getLocationName());
        etDescription.setText(mEvent.getDescription());
    }
    private void save() {
        try {
            mEvent.setName(etTitle.getText().toString());
            mEvent.setOrganisator(etOrganisator.getText().toString());
            mEvent.setDescription(etDescription.getText().toString());
        }
        catch(Exception e){

        }
    }
    private Date toDate(String date){
        try {
            String sDate = date;
            String[] sDateSplit = sDate.split(" ");
            String[] DateSplit = sDateSplit[0].split("/");
            String[] TimeSplit = sDateSplit[1].split(":");

            return new Date(Integer.parseInt(DateSplit[2]), Integer.parseInt(DateSplit[1]), Integer.parseInt(DateSplit[0]),
                    Integer.parseInt(TimeSplit[0]), Integer.parseInt(TimeSplit[1]));
        }
        catch (Exception e){
            Toast.makeText(EditEventActivity.this, "Datum en tijd zijn niet correct ingevuld, bv. 1/1/1990 20:00", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
