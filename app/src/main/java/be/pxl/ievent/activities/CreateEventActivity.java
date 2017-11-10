package be.pxl.ievent.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Date;

import be.pxl.ievent.App;
import be.pxl.ievent.R;
import be.pxl.ievent.api.ApiManager;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.models.RealmString;
import be.pxl.ievent.models.apiResponses.GeocodeResponseWrap;
import be.pxl.ievent.models.apiResponses.Location;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateEventActivity extends BaseActivity {

    @BindView(R.id.et_create_event_name) EditText etTitle;
    @BindView(R.id.et_create_event_organisor) EditText etOrganisator;
    @BindView(R.id.et_create_event_location) EditText etLocation;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_create_event_header);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                        , etLocation.getText().toString()
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

        String searchQuery = "";

        if(locationName.toLowerCase().contains("pxl")){
            searchQuery = "PXL Hasselt";
        }
        else if(locationName.toLowerCase().contains("corda")){
            searchQuery = "Corda Campus";
        }
        else{
            searchQuery = locationName;
        }

        ApiManager
                .getGeocode(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GeocodeResponseWrap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("APITEST", "onError: ", e);
                        Toast.makeText(CreateEventActivity.this, "IEvent heeft de locatie niet kunnen vastleggen. Probeer een duidelijkere beschrijving.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(GeocodeResponseWrap geocodeResponseWrap) {
                        Log.i("APITEST", "onNext: " + geocodeResponseWrap.getResults().get(0).getFormattedAddress());
                        Location loc = new Location();
                        loc.setLat(geocodeResponseWrap.getResults().get(0).getGeometry().getLocation().getLat());
                        loc.setLng(geocodeResponseWrap.getResults().get(0).getGeometry().getLocation().getLng());
                        event.setLocation(loc);

                        Realm realm = App.getmRealm();

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(event);
                            }
                        });

                        realm.close();
                    }
                });

    }
}
