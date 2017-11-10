package be.pxl.ievent.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import be.pxl.ievent.App;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.models.RealmString;
import be.pxl.ievent.notification.SeminarNotification;
import io.realm.Realm;

/**
 * Created by jessevandenberghe on 24/08/2017.
 */

public class AttendService extends Service {

    private static final String TAG = AttendService.class.getSimpleName();
    private static Realm realm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AttendEvent();

        SeminarNotification.cancel(App.getContext());

        return super.onStartCommand(intent,flags,startId);
    }

    private void AttendEvent() {
        Realm realm = App.getmRealm();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Event event = realm.where(Event.class).equalTo("id", App.getEventToAttend()).findFirst();

                event.addAttendSubscribers(new RealmString(App.getUserMail()));

                App.removeEvents(event);

                realm.copyToRealmOrUpdate(event);
            }
        });

        realm.close();

    }
}
