package be.pxl.ievent.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import be.pxl.ievent.R;
import be.pxl.ievent.adapters.EventAdapter;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.models.RealmString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TeacherOverviewActivity extends BaseActivity {

    @BindView(R.id.th_teacher_tabcontainer) TabHost host;
    @BindView(R.id.rv_teacher_overview) RecyclerView rvTeacherOverview;

    // @BindView(R.id.rv_student_open) RecyclerView rvOpen;
    // @BindView(R.id.rv_student_all) RecyclerView rvAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher_overview);
        ButterKnife.bind(this);

        setupDummyEvents();
        setupTabs();
        setupAdapters();
        setupFAB();
    }

    private void setupDummyEvents() {
        if(mRealm.where(Event.class).count() == 0){
            makeEvent("Kotlin", "AON", "JIDOKA", new Date(2017,10,11,9,00), new Date(2017,10,11,12,00), "Corda, IClassroom");
            makeEvent("Blockchain", "AON", "Appwise", new Date(2017,10,18,9,00), new Date(2017,10,18,12,00), "Corda, IClassroom");
            makeEvent("OWASP", "SNB", "Fenego", new Date(2017,10,25,9,00), new Date(2017,10,25,12,00), "Corda Conference, Zaal 1");
        }
    }

    private void makeEvent(String title, String category, String organisator, Date startDate, Date endDate, String locationName) {
        int nextID = (int) ((mRealm.where(Event.class).max("id")==null? 0 : mRealm.where(Event.class).max("id").intValue()) + 1);

        final Event event = new Event();

        event.setId(nextID);
        event.setName(title);
        event.setCategory(category);
        event.setOrganisator(organisator);
        event.setStartDateTime(startDate.getTime());
        event.setEndDateTime(endDate.getTime());
        event.setLocationName(locationName);
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

    private void setupAdapters() {
        RealmResults<Event> allEvents = mRealm.where(Event.class).findAll();

        rvTeacherOverview.setLayoutManager(new LinearLayoutManager(this));
        rvTeacherOverview.setAdapter(new EventAdapter(allEvents, true, 3));
    }

    private void setupTabs() {
        host.setup();

        //Subscriber
        TabHost.TabSpec spec = host.newTabSpec("Alle events");
        spec.setContent(R.id.rl_teacher_event_overview);
        spec.setIndicator("Alle events");
        host.addTab(spec);

        for(int i=0;i<host.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        }
    }

    private void setupFAB(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_teacher_overview_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Toast.makeText(TeacherOverviewActivity.this, "Yeaaaaah", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
