package be.pxl.ievent;

import android.app.Application;
import android.support.multidex.MultiDex;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jessevandenberghe on 26/09/2017.
 */

public class App extends Application {

    private static String userMail = "";

    public static String getUserMail() {
        return userMail;
    }

    public static void setUserMail(String userMail) {
        App.userMail = userMail;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

}
