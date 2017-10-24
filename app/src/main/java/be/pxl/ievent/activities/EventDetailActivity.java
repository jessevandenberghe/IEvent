package be.pxl.ievent.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.pxl.ievent.App;
import be.pxl.ievent.R;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.models.RealmString;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import io.realm.RealmList;

public class EventDetailActivity extends BaseActivity {

    @BindView(R.id.tb_detail_header) Toolbar tbHeader;
    @BindView(R.id.tv_event_detail_title) TextView tvTitle;
    @BindView(R.id.tv_event_detail_organisator) TextView tvOrganisator;
    @BindView(R.id.tv_event_detail_date) TextView tvDate;
    @BindView(R.id.tv_event_detail_time) TextView tvTime;
    @BindView(R.id.tv_event_detail_location) TextView tvLocation;
    @BindView(R.id.tv_event_detail_subscribed) TextView tvSubscribed;
    @BindView(R.id.tv_event_detail_description) TextView tvDescription;
    @BindView(R.id.tc_event_detail_category) TagContainerLayout tcCategory;
    @BindView(R.id.iv_event_detail_subscribe) ImageView ivSubscribe;

    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        setup();
    }

    private void setup() {
        mEvent = mRealm.where(Event.class).equalTo("id", getIntent().getIntExtra("eventId",0)).findFirst();

        Date startDate = new Date(mEvent.getStartDateTime());
        Date endDate = new Date(mEvent.getEndDateTime());
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleHour = new SimpleDateFormat("HH:mm");

        tbHeader.setTitle(mEvent.getName());
        tbHeader.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvTitle.setText(mEvent.getName());
        tvOrganisator.setText(mEvent.getOrganisator());
        tvDate.setText(simpleDate.format(startDate));
        tvTime.setText(simpleHour.format(startDate) + " - " + simpleHour.format(endDate));
        tvLocation.setText(mEvent.getLocationName());
        createSubscribedString(mEvent, tvSubscribed);
        tvDescription.setText(mEvent.getDescription());
        tcCategory.addTag(mEvent.getCategory());

    }

    void createSubscribedString(Event event, TextView v){

        RealmList<RealmString> subscriberList = event.getSubscribers();

        for (RealmString s: subscriberList) {
            if(s.getName().equals(App.getUserMail())){
                v.setText("Ingeschreven");
                v.setTextColor(getResources().getColor(R.color.colorAccentDark));
                ivSubscribe.setImageResource(R.drawable.ic_subscribe_accent);
                return;
            }

        }
        if (subscriberList.size() == event.getMaxSubscriptions()){
            v.setText("Volzet");
            v.setTextColor(getResources().getColor(R.color.colorWarning));
            ivSubscribe.setImageResource(R.drawable.ic_subscribe_red);
            return;
        }

        v.setText("Niet ingeschreven");
        v.setTextColor(getResources().getColor(R.color.colorTextBlack));
        ivSubscribe.setImageResource(R.drawable.ic_subscribe_black);
    }
}
