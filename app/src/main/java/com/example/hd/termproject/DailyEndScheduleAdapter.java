package com.example.hd.termproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DailyEndScheduleAdapter extends CursorAdapter {

    public DailyEndScheduleAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView endSchedules = (TextView)view.findViewById(R.id.scheduleItem);

        String subject = cursor.getString(cursor.getColumnIndex("subject"));

        endSchedules.setText(subject);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.schedule_item, parent, false);
        return view;
    }
}
