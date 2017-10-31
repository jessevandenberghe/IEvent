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
                /* String sDate = etStartDate.getText().toString();
                String[] sDateSplit = sDate.split(" ");
                String[] DateSplit = sDateSplit[0].split("/");
                String[] TimeSplit = sDateSplit[1].split(":"); */

                makeEvent(etTitle.getText().toString(), spCategory.getSelectedItem().toString()
                        , etOrganisator.getText().toString()
                        , new Date(2017,10,11,9,00), new Date(2017,10,11,12,00)
                        , etLocationName.getText().toString() + ", " + etLocation.getText().toString());
            }
        });
    }

    private void makeEvent(String title, String category, String organisator, Date startDate, Date endDate, String locationName) {
        int nextID = (int) ((mRealm.where(Event.class).max("id")==null? 0 : mRealm.where(Event.class).max("id").intValue()) + 1);

        final Event event = new Event();

        event.setId(nextID);
        event.setName(title);
        event.setCategory(category);
        event.setOrganisator(organisator);
        event.setStartDateTime(startDate.getTime());
        event.setEndDateTime(endDate.getTime());
        event.setLocationName(locationName);
        event.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
                "\n" +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. \n" +
                "\n" +
                "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        event.setMaxSubscriptions(25);
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
