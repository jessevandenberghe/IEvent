package be.pxl.ievent.adapters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import be.pxl.ievent.App;
import be.pxl.ievent.R;
import be.pxl.ievent.activities.EventDetailActivity;
import be.pxl.ievent.models.Event;
import be.pxl.ievent.models.RealmString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by jessevandenberghe on 24/10/2017.
 */

public class EventAdapter extends RealmRecyclerViewAdapter<Event,RecyclerView.ViewHolder> {

    int filterType;

    public EventAdapter(@Nullable OrderedRealmCollection<Event> data, boolean autoUpdate, int filterType) {
        super(data, autoUpdate);
        this.filterType = filterType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Event event = getData().get(position);
        if(filterEvent(event, filterType))
            ((EventViewHolder)holder).setContent(event);
        else
            ((EventViewHolder)holder).ignore();
    }

    private boolean filterEvent(Event event, int filterType) {
        final int onlySubscribed = 1;
        final int onlyOpen = 2;
        final int all = 3;
        final int full = 4;

        boolean subscribed = false;

        if(filterType == all)
            return true;

        for (RealmString s: event.getSubscribers()) {
            if(s.getName().equals(App.getUserMail())){
                subscribed = true;
            }
        }

        if(filterType == onlyOpen){
            if(event.getSubscribers().size() != event.getMaxSubscriptions() && !subscribed)
                return true;
            else return false;
        }

        if(filterType == onlySubscribed){
            if(subscribed)
                return true;
            else return false;
        }

        if(filterType == full){
            if(event.getMaxSubscriptions() == event.getCurrentSubscriptionCount())
                return true;
            else return false;
        }
        return false;
    }


    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final View root;


        @BindView(R.id.event_title) TextView tvTitle;
        @BindView(R.id.event_category) TextView tvCategory;
        @BindView(R.id.event_organisator) TextView tvOrganisator;
        @BindView(R.id.event_date) TextView tvDate;
        @BindView(R.id.event_time) TextView tvTime;
        @BindView(R.id.event_location) TextView tvLocation;
        @BindView(R.id.event_subscribed) TextView tvSubcribed;

        private Event mEvent;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            root = itemView;
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(root.getContext(), EventDetailActivity.class);
            intent.putExtra("eventId", mEvent.getId());
            root.getContext().startActivity(intent);

        }

        public void setContent(Event event) {



            Date startDate = event.getStartDateTime();
            Date endDate = event.getEndDateTime();
            SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat simpleHour = new SimpleDateFormat("HH:mm");

            tvTitle.setText(event.getName());
            tvCategory.setText(event.getCategory());
            tvOrganisator.setText(event.getOrganisator());
            tvDate.setText(simpleDate.format(startDate));
            tvTime.setText(simpleHour.format(startDate) + " - " + simpleHour.format(endDate));
            tvLocation.setText(event.getLocationName());
            if(App.isTeacher()){
                createAmountSubscribedString(event, tvSubcribed);
            }
            else if(App.isStudent()){
                createSubscribedString(event,tvSubcribed);
            }

            mEvent = event;
        }

        void createAmountSubscribedString(Event event, TextView v){
            RealmList<RealmString> subscriberList = event.getSubscribers();

            v.setText(event.getCurrentSubscriptionCount() + "/" + event.getMaxSubscriptions());

            if(event.getCurrentSubscriptionCount() == event.getMaxSubscriptions()){
                v.setTextColor(root.getResources().getColor(R.color.colorWarning));
            }
            else{
                v.setTextColor(root.getResources().getColor(R.color.colorAccentDark));
            }
            return;
        }

        void createSubscribedString(Event event, TextView v){

            RealmList<RealmString> subscriberList = event.getSubscribers();

            for (RealmString s: subscriberList) {
                if(s.getName().equals(App.getUserMail())){
                    boolean attended = false;

                    for (RealmString attendence: event.getAttendSubscribers()) {
                        attended |= attendence.getName().equals(App.getUserMail());
                    }

                    if(attended){
                        v.setText("Bijgewoond");
                        v.setTextColor(root.getResources().getColor(R.color.colorPrimaryLight));
                    }
                    else{
                        v.setText("Ingeschreven");
                        v.setTextColor(root.getResources().getColor(R.color.colorAccentDark));
                        if(!AlreadyInSubsribeList(event)) {
                            App.addEvent(event);
                        }
                    }
                    return;
                }

            }
            if (subscriberList.size() == event.getMaxSubscriptions()){
                v.setText("Volzet");
                v.setTextColor(root.getResources().getColor(R.color.colorWarning));
                return;
            }

            v.setText("Niet ingeschreven");
            v.setTextColor(root.getResources().getColor(R.color.colorTextBlack));
        }

        private boolean AlreadyInSubsribeList(Event event) {

            boolean inList = false;

            for (Event e: App.getSubscribedEvents()) {
                inList |= e.getId() == event.getId();
            }

            return inList;
        }

        public void ignore() {
            root.setVisibility(View.GONE);
            root.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }
}
