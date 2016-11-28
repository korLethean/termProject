package com.example.hd.termproject;

import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by HD on 2016-11-28.
 */

public class DailyFragment extends Fragment {
    private View fragmentView;
    private Context fragmentContext;

    private TextView textToday;
    private TextView textDayOfWeek;
    private TextView textYear;
    private TextView textMonth;

    private TextView textSubject;
    private TextView textPlace;
    private TextView textStart;
    private TextView textEnd;
    private TextView textDescription;

    private Date CALENDAR_CURRENTDATE = new Date();
    private int CALENDAR_YEAR;
    private int CALENDAR_MONTH;
    private int CALENDAR_DAY;
    private boolean CALENDAR_LEAP;
    private ArrayList<String> CALENDAR_DAYOFWEEK;
    private Calendar CALENDAR_DATA;




}
