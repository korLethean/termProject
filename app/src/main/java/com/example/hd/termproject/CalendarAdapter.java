package com.example.hd.termproject;

import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;
    private int mResource;
    private LayoutInflater layoutInflater;

    private Calendar CALENDAR_DATA;
    private ArrayList<String> CALENDAR_DAYOFWEEK;
    private ArrayList<String> CALENDAR_DATENUMBER;

    private final int DAYOFWEEK_EXCLUDE = 7;
    private final int NULL_INCLUDE = 6;
    private int DATE_START_POINT;
    private int DATE_END_POINT;

    private int YEAR;
    private int MONTH;
    private int[] DAY = new int[31];
    private final String DATE_NULL = " ";

    public CalendarAdapter(Context context, int resource, Calendar calendar) {
        mContext = context;
        mResource = resource;
        CALENDAR_DATA = calendar;
        CALENDAR_DAYOFWEEK = new ArrayList<String>();
        CALENDAR_DAYOFWEEK.add(mContext.getResources().getString(R.string.SUN));
        CALENDAR_DAYOFWEEK.add(mContext.getResources().getString(R.string.MON));
        CALENDAR_DAYOFWEEK.add(mContext.getResources().getString(R.string.TUE));
        CALENDAR_DAYOFWEEK.add(mContext.getResources().getString(R.string.WED));
        CALENDAR_DAYOFWEEK.add(mContext.getResources().getString(R.string.THU));
        CALENDAR_DAYOFWEEK.add(mContext.getResources().getString(R.string.FRI));
        CALENDAR_DAYOFWEEK.add(mContext.getResources().getString(R.string.SAT));

        CALENDAR_DATENUMBER = new ArrayList<String>();
        for (int i = 0; i < 31; i++) {
            CALENDAR_DATENUMBER.add(String.valueOf(i + 1));
        }
    }

    @Override
    public int getCount() {
        return CALENDAR_DATA.getActualMaximum(Calendar.DAY_OF_MONTH) + DAYOFWEEK_EXCLUDE + NULL_INCLUDE;
    }

    @Override
    public Object getItem(int position) {
        return CALENDAR_DATA.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getYear() {
        return YEAR;
    }

    public int getMonth() {
        return MONTH;
    }

    public int getDay(int position) {
        return DAY[position - DATE_START_POINT];
    }

    public int getStartPoint() {
        return DATE_START_POINT;
    }

    public int getEndPoint() {
        return DATE_END_POINT;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Calendar FOR_CALCULATE_POINT = Calendar.getInstance();
        FOR_CALCULATE_POINT.set(CALENDAR_DATA.get(Calendar.YEAR), CALENDAR_DATA.get(Calendar.MONTH), 1);
        DATE_START_POINT = FOR_CALCULATE_POINT.get(Calendar.DAY_OF_WEEK) + DAYOFWEEK_EXCLUDE - 1;
        DATE_END_POINT = FOR_CALCULATE_POINT.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (convertView == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(mResource, parent, false);
        }

        TextView calendarText = (TextView) convertView.findViewById(R.id.calendarItem);
        if (getItemId(position) < DAYOFWEEK_EXCLUDE) {
            calendarText.setText(CALENDAR_DAYOFWEEK.get(position));
        } else {
            if (getItemId(position) < DATE_START_POINT) {
                calendarText.setText(DATE_NULL);
            } else {
                if ((position - DATE_START_POINT) < DATE_END_POINT) {
                    calendarText.setText(CALENDAR_DATENUMBER.get(position - DATE_START_POINT));
                    DAY[position - DATE_START_POINT] = position - DATE_START_POINT + 1;
                } else
                    calendarText.setText(DATE_NULL);
            }
        }

        if (position % 7 == 0)
            calendarText.setTextColor(ContextCompat.getColor(mContext, R.color.RED));
        else if (position % 7 == 6)
            calendarText.setTextColor(ContextCompat.getColor(mContext, R.color.BLUE));
        else
            calendarText.setTextColor(ContextCompat.getColor(mContext, R.color.BLACK));

        YEAR = CALENDAR_DATA.get(Calendar.YEAR);
        MONTH = CALENDAR_DATA.get(Calendar.MONTH);

        return convertView;
    }
}