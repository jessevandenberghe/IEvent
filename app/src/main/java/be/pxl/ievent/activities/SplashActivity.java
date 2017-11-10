package be.pxl.ievent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Date;

import be.pxl.ievent.R;
import be.pxl.ievent.api.ApiManager;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.models.RealmString;
import be.pxl.ievent.models.apiResponses.GeocodeResponseWrap;
import be.pxl.ievent.models.apiResponses.Location;
import io.realm.Realm;
import io.realm.RealmList;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }, 3000);

        setupDummyEvents();
    }

    private void setupDummyEvents() {
        if(mRealm.where(Event.class).count() == 0){
            ApiManager
                    .getGeocode("Corda Campus")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GeocodeResponseWrap>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("APITEST", "onError: ", e);
                        }

                        @Override
                        public void onNext(GeocodeResponseWrap geocodeResponseWrap) {
                            Log.i("APITEST", "onNext: " + geocodeResponseWrap.getResults().get(0).getFormattedAddress());
                            Location loc = new Location();
                            loc.setLat(geocodeResponseWrap.getResults().get(0).getGeometry().getLocation().getLat());
                            loc.setLng(geocodeResponseWrap.getResults().get(0).getGeometry().getLocation().getLng());
                            makeEvent("Kotlin", "AON", "JIDOKA", new Date(2017,10,11,9,00), new Date(2017,10,11,12,00), "Corda, IClassroom",loc);
                        }
                    });
            ApiManager
                    .getGeocode("Corda Campus")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GeocodeResponseWrap>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("APITEST", "onError: ", e);
                        }

                        @Override
                        public void onNext(GeocodeResponseWrap geocodeResponseWrap) {
                            Log.i("APITEST", "onNext: " + geocodeResponseWrap.getResults().get(0).getFormattedAddress());
                            Location loc = new Location();
                            loc.setLat(geocodeResponseWrap.getResults().get(0).getGeometry().getLocation().getLat());
                            loc.setLng(geocodeResponseWrap.getResults().get(0).getGeometry().getLocation().getLng());
                            makeEvent("Blockchain", "AON", "Appwise", new Date(2017,10,18,9,00), new Date(2017,10,18,12,00), "Corda, IClassroom", loc);
                        }
                    });
            ApiManager
                    .getGeocode("Corda Campus")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<GeocodeResponseWrap>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("APITEST", "onError: ", e);
                        }

                        @Override
                        public void onNext(GeocodeResponseWrap geocodeResponseWrap) {
                            Log.i("APITEST", "onNext: " + geocodeResponseWrap.getResults().get(0).getFormattedAddress());
                            Location loc = new Location();
                            loc.setLat(geocodeResponseWrap.getResults().get(0).getGeometry().getLocation().getLat());
                            loc.setLng(geocodeResponseWrap.getResults().get(0).getGeometry().getLocation().getLng());
                            makeEvent("OWASP", "SNB", "Fenego", new Date(2017,10,25,9,00), new Date(2017,10,25,12,00), "Corda Conference, Zaal 1", loc);
                        }
                    });

        }
    }

    private void makeEvent(String title, String category, String organisator, Date startDate, Date endDate, String locationName, Location loc) {
        int nextID = (int) ((mRealm.where(Event.class).max("id")==null? 0 : mRealm.where(Event.class).max("id").intValue()) + 1);

        final Event event = new Event();

        event.setId(nextID);
        event.setName(title);
        event.setCategory(category);
        event.setOrganisator(organisator);
        event.setStartDateTime(startDate);
        event.setEndDateTime(endDate);
        event.setLocationName(locationName);

        event.setLocation(loc);

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
