package be.pxl.ievent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import be.pxl.ievent.R;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.models.RealmString;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import io.realm.Realm;
import io.realm.RealmList;

public class CreateEventActivity extends BaseActivity {

    @BindView(R.id.et_create_event_name) EditText etTitle;
    @BindView(R.id.et_create_event_organisor) EditText etOrganisator;
    @BindView(R.id.et_create_event_location) EditText etLocation;
    @BindView(R.id.et_create_event_location_name) EditText etLocationName;
    @BindView(R.id.et_create_event_start_date) EditText etStartDate;
    @BindView(R.id.et_create_event_end_date) EditText etEndDate;
    @BindView(R.id.et_create_event_description) EditText etDescription;
    @BindView(R.id.et_create_event_amount) EditText etAmount;
    @BindView(R.id.spinner_category) Spinner spCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner_category);
        String[] items = new String[]{"Applicatie Ontwikkeling", "Software Management", "Systemen en Netwerken"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        setupFAB();
    }

    private void setupFAB(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_create_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                makeEvent(etTitle.getText().toString(), spCategory.getSelectedItem().toString()
                        , etOrganisator.getText().toString()
                        , toDate(etStartDate.getText().toString())
                        , toDate(etEndDate.getText().toString())
                        , etLocationName.getText().toString() + ", " + etLocation.getText().toString()
                        , etDescription.getText().toString(), Integer.parseInt(etAmount.getText().toString()));
                finish();
            }
            catch(Exception e){
                Toast.makeText(CreateEventActivity.this, "Zorg dat alle velden correct ingevuld zijn!", Toast.LENGTH_SHORT).show();
                }
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
            Toast.makeText(CreateEventActivity.this, "Datum en tijd zijn niet correct ingevuld, bv. 1/1/1990 20:00", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    private void makeEvent(String title, String category, String organisator, Date startDate, Date endDate, String locationName,
                           String description, Integer maxSubscribers) {
        int nextID = (int) ((mRealm.where(Event.class).max("id")==null? 0 : mRealm.where(Event.class).max("id").intValue()) + 1);

        final Event event = new Event();

        event.setId(nextID);
        event.setName(title);
        event.setCategory(category);
        event.setOrganisator(organisator);
        event.setStartDateTime(startDate);
        event.setEndDateTime(endDate);
        event.setLocationName(locationName);
        event.setDescription(description);
        event.setMaxSubscriptions(maxSubscribers);
        event.setSubscribers(new RealmList<RealmString>());

        RealmList<RealmString> subscriberList = new RealmList<RealmString>();

        if(nextID == 1) {
            subscriberList.add(new RealmString("11501253@student.pxl.be"));
            event.setSubscribers(subscriberList);
        }

        if(nextID == 3){
            for (int i = 0; i < 25; i++){
                subscriberList.add(new RealmString(""));
            }
            event.setSubscribers(subscriberList);
        }

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(event);
            }
        });
    }
}
