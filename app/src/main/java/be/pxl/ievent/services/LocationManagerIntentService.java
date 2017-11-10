package be.pxl.ievent.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.List;

import be.pxl.ievent.App;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.models.GeoFence;
import be.pxl.ievent.notification.SeminarNotification;
import be.pxl.ievent.reciever.GeofenceBroadcastReciever;
import io.realm.Realm;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import pl.charmas.android.reactivelocation.observables.GoogleAPIConnectionException;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static be.pxl.ievent.services.LocationManagerIntentService.Actions.NOACTION;
import static be.pxl.ievent.services.LocationManagerIntentService.Actions.NOTIFY;

/**
 * Created by jessevandenberghe on 25/08/2017.
 */

public class LocationManagerIntentService extends IntentService {

    private static final String TAG = LocationManagerIntentService.class.getSimpleName();
    private static int counter = 0;
    private static Realm realm;
    private static LocationManager locationManager;
    private static Location userLocation;
    private static ReactiveLocationProvider reactiveLocationProvider;
    private static boolean init = true;

    private static States state = States.MEDIUM;
    private static String enteredEvent = "";

    public enum Actions{NOACTION,GEOOFFQUEUE,GEOON,NOTIFY}

    public enum States{
        FAR{
            public Actions process(){
                Log.i("FAR", "process: ...........................");

                Log.i(TAG, "process: In geofence: " + App.getInGeoFence());
                if(App.getInGeoFence()){
                    state = States.MEDIUM;
                    return Actions.GEOON;
                }

                return NOACTION;
            }
        }
        ,MEDIUM{
            public Actions process(){
                Log.i("MEDIUM", "process: ...........................");
                try {

                    List<Location> monitoredLocations = new ArrayList<Location>();


                    for (Event e: App.getSubscribedEvents()) {
                        Location loc = new Location(e.getName());
                        loc.setLatitude(e.getLocation().getLat());
                        loc.setLongitude(e.getLocation().getLng());
                        monitoredLocations.add(loc);
                    }

                    if(userLocation != null){
                        boolean close = false;
                        for (int i = 0; i < monitoredLocations.size(); i++) {
                            Location loc = monitoredLocations.get(i);
                            close |= loc.distanceTo(userLocation) < 50;
                            Log.i(TAG, "process: " + loc.distanceTo(userLocation));
                            enteredEvent = App.getSubscribedEvents().get(i).getName();
                            App.setEventToAttend(App.getSubscribedEvents().get(i).getId());
                        }
                        if(close) {
                            return NOTIFY;
                        }
                    }

                }
                catch (Exception e){
                    Log.e("MEDIUM", "process: ", e);
                }
                Log.i(TAG, "process: In geofence: " + App.getInGeoFence());
                if(App.getInGeoFence()){
                    state = States.FAR;
                    return Actions.GEOON;
                }
                return NOACTION;
            }
        };
        public abstract Actions process();
    }





    public LocationManagerIntentService() {
        super(TAG);
        Log.i(TAG, "LocationManagerIntentService: running");
        // Acquire a reference to the system Location Manager
        LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(Integer.MAX_VALUE)
                .setInterval(1000);

        try {
            reactiveLocationProvider = new ReactiveLocationProvider(App.getContext());
        }
        catch (GoogleAPIConnectionException e){;
        }

        if(init) {
            final ArrayList<GeoFence> geoFenceList = new ArrayList<GeoFence>();

            List<Event> eventList = App.getSubscribedEvents();

            for (Event e: eventList) {
                GeoFence geofence = new GeoFence(e.getName(), e.getLocation().getLat(), e.getLocation().getLng(), 100);
                geoFenceList.add(geofence);
            }
            addGeofence(geoFenceList);

            init = false;
        }

        if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        reactiveLocationProvider.getLastKnownLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        userLocation = location;
                    }
                });
        Subscription subscription = reactiveLocationProvider.getUpdatedLocation(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        userLocation = location;
                    }
                });
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(App.getUpdateGeofence()){
            updateGeofences();
            App.setUpdateGeofence(false);
        }

        Actions action = state.process();

        switch (action) {
            case NOACTION:
                break;
            case GEOOFFQUEUE:
                clearGeofence();
                break;
            case NOTIFY:
                sendNotification();
                break;
            default:
                break;
        }

        scheduleNextUpdate();

    }

    private void updateGeofences() {
        clearGeofence();

        final ArrayList<GeoFence> geoFenceList = new ArrayList<GeoFence>();

        for (Event e: App.getSubscribedEvents()) {
            GeoFence geofence = new GeoFence(e.getName(), e.getLocation().getLat(), e.getLocation().getLng(), 100);
            geoFenceList.add(geofence);
        }
        addGeofence(geoFenceList);
    }

    private void sendNotification() {
        SeminarNotification.notify(App.getContext(), "Gelieve uw aanwezigheid op " + enteredEvent + " te bevestigen.");
    }

    private void endService() {
        realm = Realm.getDefaultInstance();
        realm.close();
        SeminarNotification.cancel(App.getContext());
        clearGeofence();
        state = States.MEDIUM;
        init = true;
        stopSelf();
    }

    private void scheduleNextUpdate() {
        Intent intent = new Intent(this, this.getClass());
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long currentTimeMillis = System.currentTimeMillis();
        //long nextUpdateTimeMillis = currentTimeMillis + 1 * DateUtils.MINUTE_IN_MILLIS;
        long nextUpdateTimeMillis = currentTimeMillis + 1 * DateUtils.SECOND_IN_MILLIS;
        Time nextUpdateTime = new Time();
        nextUpdateTime.set(nextUpdateTimeMillis);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent);
    }

    private void addGeofence(ArrayList<GeoFence> geoFenceArrayList) {
        final GeofencingRequest geofencingRequest = createGeofencingRequest(geoFenceArrayList);
        if (geofencingRequest == null) return;

        for (GeoFence geofence: geoFenceArrayList) {
            Log.i(TAG, "addGeofence: " + geofence.getName() + ": " + geofence.getLat() + "lt - " + geofence.getLng() + "lng");
        }

        final PendingIntent pendingIntent = createNotificationBroadcastPendingIntent();
        if (ActivityCompat.checkSelfPermission(App.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            reactiveLocationProvider
                    .addGeofences(pendingIntent, geofencingRequest)
                    .subscribe(new Action1<Status>() {
                        @Override
                        public void call(Status addGeofenceResult) {
                            Log.i(TAG, "call: GeoFence Added");
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e(TAG, "Error adding geofence.", throwable);
                        }
                    });
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearGeofence() {
        try {
            reactiveLocationProvider.removeGeofences(createNotificationBroadcastPendingIntent()).subscribe(new Action1<Status>() {
                @Override
                public void call(Status status) {
                    Log.i(TAG, "call: Geofence removed");
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Log.e(TAG, "Error removing geofences", throwable);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private PendingIntent createNotificationBroadcastPendingIntent() {
        Intent intent = new Intent(App.getContext(), GeofenceBroadcastReciever.class);
        return PendingIntent.getBroadcast(App.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest createGeofencingRequest(ArrayList<GeoFence> geoFenceArrayList) {
        try {
            ArrayList<Geofence> geofenceList = new ArrayList<Geofence>();
            for (GeoFence geoFence : geoFenceArrayList) {
                Geofence geofence = new Geofence.Builder()
                        .setRequestId(geoFence.getName())
                        .setCircularRegion(geoFence.getLat(), geoFence.getLng(), geoFence.getRadius())
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build();
                geofenceList.add(geofence);
            }

            return new GeofencingRequest.Builder().addGeofences(geofenceList).build();
        } catch (Exception ex) {
            return null;
        }
    }
}
