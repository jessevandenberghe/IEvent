
package be.pxl.ievent.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;

public class BaseActivity extends AppCompatActivity {

    protected Realm mRealm;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                &&(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }

        mRealm = Realm.getDefaultInstance();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
        mRealm = null;
    }


    public Realm getRealm() {
        return mRealm;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
