package be.pxl.ievent.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import be.pxl.ievent.App;
import io.realm.Realm;

/**
 * Created by jessevandenberghe on 24/08/2017.
 */

public class GeofenceBroadcastReciever extends BroadcastReceiver {
    private Realm realm;
    private Context context;
    
    private static final String TAG = GeofenceBroadcastReciever.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        String transition = mapTransition(event.getGeofenceTransition());
        List<Geofence> geofenceList = event.getTriggeringGeofences();
        String name = geofenceList.get(0).getRequestId();

        Log.i(TAG, "onReceive: Intent recieved: " + transition);

        logGeofence(name, transition);
    }

    private String mapTransition(int event) {
        switch (event) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "ENTER";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "EXIT";
            default:
                return "UNKNOWN";
        }
    }

    private void logGeofence(String name, String transition) {
        if (transition == "ENTER"){
            App.setInGeoFence(true);
            Log.i(TAG, "logGeofence: entered");
        } else {
            App.setInGeoFence(false);
            Log.i(TAG, "logGeofence: exited");
        }


    }
}
