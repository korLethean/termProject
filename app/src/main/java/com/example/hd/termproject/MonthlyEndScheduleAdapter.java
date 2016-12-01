package com.example.hd.termproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MonthlyEndScheduleAdapter extends CursorAdapter {

    public MonthlyEndScheduleAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView endSchedules = (TextView)view.findViewById(R.id.scheduleItem);

        String hourString = String.valueOf(cursor.getInt(cursor.getColumnIndex("endHour")));
        String minString;
        if (cursor.getInt(cursor.getColumnIndex("endMin")) < 10)
            minString = String.valueOf("0" + cursor.getInt(cursor.getColumnIndex("endMin")));
        else
            minString = String.valueOf(cursor.getInt(cursor.getColumnIndex("endMin")));
        String subject = cursor.getString(cursor.getColumnIndex("subject"));
        String place = cursor.getString(cursor.getColumnIndex("place"));

        endSchedules.setText(context.getString(R.string.text_end)
                + hourString + " : " + minString + " " + subject + " / " + place);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.schedule_item, parent, false);
        return view;
    }
}
