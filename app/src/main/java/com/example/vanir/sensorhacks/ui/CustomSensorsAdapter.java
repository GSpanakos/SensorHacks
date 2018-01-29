package com.example.vanir.sensorhacks.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vanir.sensorhacks.R;
import com.example.vanir.sensorhacks.model.Sensor;

/**
 * Created by Γιώργος on 24/10/2017.
 */

public class CustomSensorsAdapter extends CursorAdapter implements View.OnClickListener {

    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private Cursor mCursor;
    Context mContext;

    //View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtStatus;
        TextView txtValue;
    }

    public CustomSensorsAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.mCursor = cursor;
        this.mContext = context;
    }

    //newView method is used to inflate a new view BUT NOT bind any data to it. Instead data is cached to a ViewHolder class
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View rowView;
        layoutInflater = LayoutInflater.from(context);
        rowView = layoutInflater.inflate(R.layout.content_sns_listview, parent, false);

        viewHolder = new ViewHolder();
        viewHolder.txtName = (TextView) rowView.findViewById(R.id.sns_listview_name);
        viewHolder.txtType = (TextView) rowView.findViewById(R.id.sns_listview_type);
        viewHolder.txtStatus = (TextView) rowView.findViewById(R.id.sns_listview_status);
        viewHolder.txtValue = (TextView) rowView.findViewById(R.id.sns_listview_value);
        rowView.setTag(viewHolder);

        return rowView;
    }

    //bindView glues data from cursor/database with inflated view
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        viewHolder = (ViewHolder) view.getTag();

        viewHolder.txtName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        viewHolder.txtType.setText(cursor.getString(cursor.getColumnIndexOrThrow("type")));
        viewHolder.txtStatus.setText(cursor.getString(cursor.getColumnIndexOrThrow("status")));
        viewHolder.txtValue.setText(cursor.getString(cursor.getColumnIndexOrThrow("value")));

    }


    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Sensor sensor = (Sensor) object;

        switch (v.getId()) {
            case R.id.sns_listview_name:
                Snackbar.make(v, "Sensor's Name" + sensor.getName(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
                break;
        }
    }

//    private int lastPosition = -1;
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        //Get data item for this position
//        Sensor sensor = getItem(position);
//
//        //view lookup cache stored in tag
//        ViewHolder viewHolder;
//
//        final View result;
//
//        // Check if an existing view is being reused, otherwise inflate the view
//        if (convertView == null) {
//
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.content_sns_listview, parent, false);
//            viewHolder.txtName = (TextView) convertView.findViewById(R.id.sns_listview_name);
//            viewHolder.txtType = (TextView) convertView.findViewById(R.id.sns_listview_type);
//            viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.sns_listview_status);
//            viewHolder.txtValue = (TextView) convertView.findViewById(R.id.sns_listview_value);
//
//            convertView.setTag(viewHolder);
//
//            result = convertView;
//
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//            result = convertView;
//        }
//
//        lastPosition = position;
//
//
//        viewHolder.txtName.setText(sensor.getName());
//        viewHolder.txtType.setText(sensor.getType());
//        viewHolder.txtStatus.setText(sensor.getStatus());
//        viewHolder.txtValue.setText(sensor.getValue());
//
//        return result;
//
//    }
}
