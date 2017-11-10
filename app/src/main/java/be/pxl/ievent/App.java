package be.pxl.ievent;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import be.pxl.ievent.models.Event;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jessevandenberghe on 26/09/2017.
 */

public class App extends Application {

    private static String userMail = "";

    private static App app = null;

    private static int eventToAttend = 1;

    private static boolean inGeoFence = false;

    private static List<Event> subscribedEvents;

    private static boolean updateGeofence = false;

    public static Realm getmRealm() {
        return mRealm;
    }

    public static void setmRealm(Realm mRealm) {
        App.mRealm = mRealm;
    }

    private static Realm mRealm;

    public static List<Event> getSubscribedEvents() {
        return subscribedEvents;
    }

    public static int getEventToAttend() {
        return eventToAttend;
    }

    public static void setEventToAttend(int eventToAttend) {
        App.eventToAttend = eventToAttend;
    }

    public static void setSubscribedEvents(List<Event> subscribedEvents) {
        App.subscribedEvents = subscribedEvents;
    }

    public static void addEvent(Event e){
        Event event = new Event();
        event.copyEvent(e, e.getLocation());
        App.updateGeofence = true;
        App.subscribedEvents.add(event);
    }

    public static void removeEvents(Event e){

        Event eventToRemove = null;

        for (Event event: App.subscribedEvents) {
            if(event.getId() == e.getId()){
                eventToRemove = event;
            }
        }

        App.subscribedEvents.remove(eventToRemove);

        App.updateGeofence = true;
    }

    public static boolean getUpdateGeofence() {
        return updateGeofence;
    }

    public static void setUpdateGeofence(boolean updateGeofence) {
        App.updateGeofence = updateGeofence;
    }

    public static boolean getInGeoFence() {
        return inGeoFence;
    }

    public static void setInGeoFence(boolean b) {
        inGeoFence = b;
    }

    public static String getUserMail() {
        return userMail;
    }

    public static void setUserMail(String userMail) {
        App.userMail = userMail;
    }

    public static Context getContext() {
        return app;
    }

    public static boolean isStudent() {
        if(userMail.contains("@student.pxl.be")){
            return true;
        }
        return false;
    }

    public static boolean isTeacher() {
        if(userMail.contains("@pxl.be")){
            return true;
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        app = this;
        App.subscribedEvents = new ArrayList<Event>();
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        mRealm = Realm.getDefaultInstance();
    }

}
