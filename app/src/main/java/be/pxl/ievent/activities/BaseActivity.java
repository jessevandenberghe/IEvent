
package be.pxl.ievent.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;

public class BaseActivity extends AppCompatActivity {

    protected Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
