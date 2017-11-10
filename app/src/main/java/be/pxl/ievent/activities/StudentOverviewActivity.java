package be.pxl.ievent.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.TabHost;
import android.widget.TextView;

import be.pxl.ievent.App;
import be.pxl.ievent.R;
import be.pxl.ievent.adapters.EventAdapter;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.notification.SeminarNotification;
import be.pxl.ievent.services.LocationManagerIntentService;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class StudentOverviewActivity extends BaseActivity {

    @BindView(R.id.th_student_tabcontainer) TabHost host;
    @BindView(R.id.rv_student_subscribed) RecyclerView rvSubscribed;
    @BindView(R.id.rv_student_open) RecyclerView rvOpen;
    @BindView(R.id.rv_student_all) RecyclerView rvAll;

    private static Intent locationManagerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_overview);
        ButterKnife.bind(this);

        SeminarNotification.notify(App.getContext(), "Kotlin");
        setupTabs();
        setupAdapters();

        locationManagerIntent = new Intent(App.getContext(), LocationManagerIntentService.class);
        startService(locationManagerIntent);
    }


    private void setupAdapters() {
        RealmResults<Event> allEvents = mRealm.where(Event.class).findAll();

        rvSubscribed.setLayoutManager(new LinearLayoutManager(this));
        rvSubscribed.setAdapter(new EventAdapter(allEvents, true, 1));

        rvOpen.setLayoutManager(new LinearLayoutManager(this));
        rvOpen.setAdapter(new EventAdapter(allEvents, true , 2));

        rvAll.setLayoutManager(new LinearLayoutManager(this));
        rvAll.setAdapter(new EventAdapter(allEvents,true, 3));
    }

    private void setupTabs() {
        host.setup();

        //Subscriber
        TabHost.TabSpec spec = host.newTabSpec("Ingeschreven");
        spec.setContent(R.id.rl_student_subscribed_tab);
        spec.setIndicator("Ingeschreven");
        host.addTab(spec);

        //Open
        spec = host.newTabSpec("Open");
        spec.setContent(R.id.rl_student_open_tab);
        spec.setIndicator("Open");
        host.addTab(spec);

        //All
        spec = host.newTabSpec("Alle");
        spec.setContent(R.id.rl_student_all_tab);
        spec.setIndicator("Alle");
        host.addTab(spec);

        for(int i=0;i<host.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        }
    }


}
