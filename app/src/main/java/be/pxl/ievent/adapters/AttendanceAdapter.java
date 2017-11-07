package be.pxl.ievent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import be.pxl.ievent.R;
import be.pxl.ievent.activities.BaseActivity;

/**
 * Created by robin on 7/11/2017.
 */

public class AttendanceAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> data;
    private static LayoutInflater inflater = null;

    public AttendanceAdapter(Context context, ArrayList<String> data) {
        // TODO Auto-generated constructor stub
        super(context, -1, data);
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.attendance_card, null);
        TextView text = (TextView) vi.findViewById(R.id.subscriber_name);
        TextView attendance = (TextView) vi.findViewById(R.id.subscriber_attendance);
        text.setText(data.get(position));
        attendance.setText("Aanwezig");
        return vi;
    }
}
