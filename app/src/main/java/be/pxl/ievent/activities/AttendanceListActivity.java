package be.pxl.ievent.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.pxl.ievent.R;
import be.pxl.ievent.adapters.AttendanceAdapter;
import be.pxl.ievent.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceListActivity extends BaseActivity{
    @BindView(R.id.lv_attendance) ListView lvAttendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_list);
        ButterKnife.bind(this);

        android.widget.Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.tb_attendance);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Event mEvent = mRealm.where(Event.class).equalTo("id", getIntent().getIntExtra("eventId",0)).findFirst();

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < mEvent.getCurrentSubscriptionCount(); ++i) {
            list.add(mEvent.getSubscribers().get(i).getName());
        }
        lvAttendance.setAdapter(new AttendanceAdapter(this, list));
    }

}
