package be.pxl.ievent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
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
import io.realm.Realm;
import io.realm.RealmList;


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

        Spinner dropdown = (Spinner)findViewById(R.id.spinner_edit_category);
        String[] items = new String[]{"Applicatie Ontwikkeling", "Software Management", "Systemen en Netwerken"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        setup();
        setupFAB();
    }

    private void setup() {
        mEvent = mRealm.where(Event.class).equalTo("id", getIntent().getIntExtra("eventId",0)).findFirst();

        Date startDate = mEvent.getStartDateTime();
        Date endDate = mEvent.getEndDateTime();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy HH:mm");

        etTitle.setText(mEvent.getName());
        etOrganisator.setText(mEvent.getOrganisator());
        etAmount.setText(String.valueOf(mEvent.getMaxSubscriptions()));
        etLocationName.setText(mEvent.getLocationName());
        etStartDate.setText(simpleDate.format(startDate));
        etEndDate.setText(simpleDate.format(endDate));
        etLocation.setText(mEvent.getLocationName());
        etDescription.setText(mEvent.getDescription());

        switch (mEvent.getCategory()){
            case "Application Development":
                spCategory.setSelection(0);
                break;
            case "AON":
                spCategory.setSelection(0);
                break;
            case "Software Management":
                spCategory.setSelection(1);
                break;
            case "SWM":
                spCategory.setSelection(1);
                break;
            case "Systemen en Netwerken":
                spCategory.setSelection(2);
                break;
            case "SNB":
                spCategory.setSelection(2);
                break;
        }

    }

    private void save() {
        try{
            updateEvent(etTitle.getText().toString(), spCategory.getSelectedItem().toString(), etOrganisator.getText().toString(),
                    toDate(etStartDate.getText().toString()), toDate(etEndDate.getText().toString()),
                            etLocationName.getText().toString(), etDescription.getText().toString(), Integer.parseInt(etAmount.getText().toString()));

            Toast.makeText(EditEventActivity.this, "Bewerken succesvol!", Toast.LENGTH_SHORT).show();
            finish();
        }
        catch (Exception e){
            Toast.makeText(EditEventActivity.this, "Bewerken mislukt!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEvent(String title, String category, String organisator, Date startDate, Date endDate, String locationName,
                           String description, Integer maxSubscribers) {
        final Event event = new Event();

        event.setId(mEvent.getId());
        event.setName(title);
        event.setCategory(category);
        event.setOrganisator(organisator);
        event.setStartDateTime(startDate);
        event.setEndDateTime(endDate);
        event.setLocationName(locationName);
        event.setDescription(description);
        event.setMaxSubscriptions(maxSubscribers);
        event.setSubscribers(mEvent.getSubscribers());

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(event);
            }
        });
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

    private void setupFAB() {
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.fab_edit_event);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    save();
                }
            });
    }
}
