package be.pxl.ievent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import be.pxl.ievent.R;

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
    }
}
