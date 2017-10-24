package be.pxl.ievent.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.TabHost;
import android.widget.TextView;

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

public class StudentOverviewActivity extends BaseActivity {

    @BindView(R.id.th_student_tabcontainer) TabHost host;
    @BindView(R.id.rv_student_subscribed) RecyclerView rvSubscribed;
    @BindView(R.id.rv_student_open) RecyclerView rvOpen;
    @BindView(R.id.rv_student_all) RecyclerView rvAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_overview);
        ButterKnife.bind(this);

        setupDummyEvents();
        setupTabs();
        setupAdapters();
    }

    private void setupDummyEvents() {
        if(mRealm.where(Event.class).count() == 0){
            makeEvent("Kotlin", "AON", "JIDOKA", new Date(2017,10,11,9,00), new Date(2017,10,11,12,00), "Corda, IClassroom");
            makeEvent("Blockchain", "AON", "Appwise", new Date(2017,10,18,9,00), new Date(2017,10,18,12,00), "Corda, IClassroom");
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
        subscriberList.add(new RealmString("11501253@student.pxl.be"));

        if(nextID == 1)
            event.setSubscribers(subscriberList);

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(event);
            }
        });
    }

    private void setupAdapters() {
        RealmResults<Event> allEvents = mRealm.where(Event.class).findAll();
        /*RealmList<Event> subscribedEvents = new RealmList<Event>();
        RealmList<Event> unsubscribedOpenEvents = new RealmList<Event>();
        for (Event event: allEvents) {
            boolean subscribed = false;
            for (RealmString sub: event.getSubscribers()) {
                if(sub.getName().equals(App.getUserMail())) {
                    subscribed = true;
                }
            }
            if(subscribed){
                subscribedEvents.add(event);
            }
            else{
                if(event.getSubscribers().size() != event.getMaxSubscriptions()){
                    unsubscribedOpenEvents.add(event);
                }
            }
        }*/

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