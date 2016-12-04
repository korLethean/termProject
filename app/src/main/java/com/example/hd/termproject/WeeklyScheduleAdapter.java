package com.example.hd.termproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class WeeklyScheduleAdapter extends CursorAdapter {

    public WeeklyScheduleAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textStart = (TextView)view.findViewById(R.id.scheduleStart);
        TextView textEnd = (TextView)view.findViewById(R.id.scheduleEnd);
        TextView textSubject = (TextView)view.findViewById(R.id.scheduleSubject);

        String startYear = String.valueOf(cursor.getInt(cursor.getColumnIndex("startYear")));
        String startMonth = String.valueOf(cursor.getInt(cursor.getColumnIndex("startMonth")) + 1);
        String startDay = String.valueOf(cursor.getInt(cursor.getColumnIndex("startDay")));
        String startHour = String.valueOf(cursor.getInt(cursor.getColumnIndex("startHour")));
        String startMin;
        if (cursor.getInt(cursor.getColumnIndex("startMin")) < 10)
            startMin = String.valueOf("0" + cursor.getInt(cursor.getColumnIndex("startMin")));
        else
            startMin = String.valueOf(cursor.getInt(cursor.getColumnIndex("startMin")));

        String endYear = String.valueOf(cursor.getInt(cursor.getColumnIndex("endYear")));
        String endMonth = String.valueOf(cursor.getInt(cursor.getColumnIndex("endMonth")) + 1);
        String endDay = String.valueOf(cursor.getInt(cursor.getColumnIndex("endDay")));
        String endHour = String.valueOf(cursor.getInt(cursor.getColumnIndex("endHour")));
        String endMin;
        if (cursor.getInt(cursor.getColumnIndex("endMin")) < 10)
            endMin = String.valueOf("0" + cursor.getInt(cursor.getColumnIndex("endMin")));
        else
            endMin = String.valueOf(cursor.getInt(cursor.getColumnIndex("endMin")));

        String subject = cursor.getString(cursor.getColumnIndex("subject"));

        textStart.setText(startYear+ "/" + startMonth + "/" + startDay + " " + startHour
                        + ":" + startMin + " ~ ");

        textEnd.setText(endYear+ "/" + endMonth + "/" + endDay + " " + endHour + ":" + endMin);

        textSubject.setText(subject);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.schedule_weekly_item, parent, false);
        return view;
    }
}
