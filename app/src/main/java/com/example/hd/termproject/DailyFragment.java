package com.example.hd.termproject;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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

    private TextView textStartNoSchedule;
    private TextView textEndNoSchedule;

    private DatePickerDialog datePickerDialog;
    private Button buttonLast;
    private Button buttonToday;
    private Button buttonPick;
    private Button buttonNext;
    private Button buttonDetail;

    private Date CALENDAR_CURRENTDATE = new Date();
    private int CALENDAR_YEAR;
    private int CALENDAR_MONTH;
    private int CALENDAR_DAY;
    private ArrayList<String> CALENDAR_DAYOFWEEK;
    private Calendar CALENDAR_DATA;

    private DailyStartScheduleAdapter dailyStartScheduleAdapter;
    private ListView startScheduleFrame;
    private DailyEndScheduleAdapter dailyEndScheduleAdapter;
    private ListView endScheduleFrame;

    private DBManager database;
    private Cursor startCursor;
    private Cursor endCursor;
    private int databaseID;

    private String SWITCH;

    public DailyFragment() { }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = layoutInflater.inflate(R.layout.fragment_daily, container, false);
        fragmentContext = container.getContext();
        database = new DBManager(fragmentContext, null);

        CALENDAR_DATA = Calendar.getInstance();
        CALENDAR_DAYOFWEEK = new ArrayList<String>();
        CALENDAR_DAYOFWEEK.add(getString(R.string.calendar_error));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SUN));
        CALENDAR_DAYOFWEEK.add(getString(R.string.MON));
        CALENDAR_DAYOFWEEK.add(getString(R.string.TUE));
        CALENDAR_DAYOFWEEK.add(getString(R.string.WED));
        CALENDAR_DAYOFWEEK.add(getString(R.string.THU));
        CALENDAR_DAYOFWEEK.add(getString(R.string.FRI));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SAT));

        textToday = (TextView)fragmentView.findViewById(R.id.textToday);
        textDayOfWeek = (TextView)fragmentView.findViewById(R.id.textDayOfWeek);
        textYear = (TextView)fragmentView.findViewById(R.id.textYear);
        textMonth = (TextView)fragmentView.findViewById(R.id.textMonth);

        textSubject = (TextView)fragmentView.findViewById(R.id.dailySubject);
        textPlace = (TextView)fragmentView.findViewById(R.id.dailyPlace);
        textStart = (TextView)fragmentView.findViewById(R.id.daliyStart);
        textEnd = (TextView)fragmentView.findViewById(R.id.dailyEnd);
        textDescription = (TextView)fragmentView.findViewById(R.id.dailyDescription);

        textStartNoSchedule = (TextView)fragmentView.findViewById(R.id.textDailyStartNoSchedule);
        textEndNoSchedule = (TextView)fragmentView.findViewById(R.id.textDailyEndNoSchedule);

        dailyStartScheduleAdapter = new DailyStartScheduleAdapter(fragmentContext, null, 0);
        startScheduleFrame = (ListView)fragmentView.findViewById(R.id.scheduleDailyStartFrame);
        startScheduleFrame.setAdapter(dailyStartScheduleAdapter);
        dailyEndScheduleAdapter = new DailyEndScheduleAdapter(fragmentContext, null, 0);
        endScheduleFrame = (ListView)fragmentView.findViewById(R.id.scheduleDailyEndFrame);
        endScheduleFrame.setAdapter(dailyEndScheduleAdapter);

        datePickerDialog = new DatePickerDialog(fragmentContext, android.R.style.Theme_Holo_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        CALENDAR_YEAR = year;
                        CALENDAR_MONTH = month;
                        CALENDAR_DAY = day;
                        CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                        setTextSelectedYearMonthDay();
                        setTextScheduleList();
                        initializeScheduleString();
                    }
                }, CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);

        buttonLast = (Button)fragmentView.findViewById(R.id.buttonLastDay);
        buttonToday = (Button)fragmentView.findViewById(R.id.buttonDailyToday);
        buttonPick = (Button)fragmentView.findViewById(R.id.buttonDailyPick);
        buttonNext = (Button)fragmentView.findViewById(R.id.buttonNextDay);
        buttonDetail = (Button)fragmentView.findViewById(R.id.buttonDailyDetail);

        setTextScheduleList();

        textStart.setOnClickListener(textListener);

        textEnd.setOnClickListener(textListener);

        buttonLast.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CALENDAR_DAY -= 1;
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                setTextSelectedYearMonthDay();
                setTextScheduleList();
                initializeScheduleString();
                datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            }
        });

        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALENDAR_DATA.setTime(CALENDAR_CURRENTDATE);
                CALENDAR_YEAR = CALENDAR_DATA.get(Calendar.YEAR);
                CALENDAR_MONTH = CALENDAR_DATA.get(Calendar.MONTH);
                CALENDAR_DAY = CALENDAR_DATA.get(Calendar.DATE);
                setTextSelectedYearMonthDay();
                setTextScheduleList();
                initializeScheduleString();
                datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            }
        });

        buttonPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CALENDAR_DAY += 1;
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                setTextSelectedYearMonthDay();
                setTextScheduleList();
                initializeScheduleString();
                datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            }
        });

        buttonDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragmentContext.getApplicationContext(), DetailScheduleActivity.class);
                intent.putExtra("_id", databaseID);
                startActivity(intent);
            }
        });

        startScheduleFrame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTextSelectedSchedule((Cursor)dailyStartScheduleAdapter.getItem(position));
            }
        });

        endScheduleFrame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTextSelectedSchedule((Cursor)dailyEndScheduleAdapter.getItem(position));
            }
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        CALENDAR_YEAR = MainActivity.getYear();
        CALENDAR_MONTH = MainActivity.getMonth();
        CALENDAR_DAY = MainActivity.getDay();
        CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
        datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
        setTextSelectedYearMonthDay();
        setTextScheduleList();
        initializeScheduleString();
        super.onResume();
    }

    private TextView.OnClickListener textListener = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            String[] text = new String[3];
            if ("START".equals(SWITCH)) {
                text = textStart.getText().toString().split("/");
                textStart.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
                textStart.setClickable(FALSE);
                textEnd.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLUE));
                textEnd.setClickable(TRUE);
                SWITCH = "END";
            } else if ("END".equals(SWITCH)) {
                text = textEnd.getText().toString().split("/");
                textStart.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLUE));
                textStart.setClickable(TRUE);
                textEnd.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
                textEnd.setClickable(FALSE);
                SWITCH = "START";
            }
            CALENDAR_YEAR = Integer.parseInt(text[0]);
            CALENDAR_MONTH = Integer.parseInt(text[1]) - 1;
            CALENDAR_DAY = Integer.parseInt(text[2].substring(0, 2).trim());
            CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            setTextSelectedYearMonthDay();
            setTextScheduleList();
        }
    };

    private void setTextScheduleList() {
        String startQuery = String.format("SELECT * FROM SCHEDULES WHERE startYear='%d' AND startMonth='%d' AND startDay='%d' " +
                        "ORDER BY startHour, startMin;",
                CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
        String endQuery = String.format("SELECT * FROM SCHEDULES WHERE endYear='%d' AND endMonth='%d' AND endDay='%d' " +
                        "ORDER BY endHour, endMin;",
                CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
        SQLiteDatabase USE_FOR_QUERY = database.getReadableDatabase();

        startCursor = USE_FOR_QUERY.rawQuery(startQuery, null);
        endCursor = USE_FOR_QUERY.rawQuery(endQuery, null);

        if(startCursor.getCount() > 0) {
            textStartNoSchedule.setVisibility(View.GONE);
            dailyStartScheduleAdapter.changeCursor(startCursor);
        }
        else {
            textStartNoSchedule.setVisibility(View.VISIBLE);
            dailyStartScheduleAdapter.changeCursor(null);
        }

        if(endCursor.getCount() > 0) {
            textEndNoSchedule.setVisibility(View.GONE);
            dailyEndScheduleAdapter.changeCursor(endCursor);
        }
        else {
            textEndNoSchedule.setVisibility(View.VISIBLE);
            dailyEndScheduleAdapter.changeCursor(null);
        }
    }

    private void setTextSelectedYearMonthDay() {
        int index = CALENDAR_DATA.get(Calendar.DAY_OF_WEEK);
        CALENDAR_YEAR = CALENDAR_DATA.get(Calendar.YEAR);
        CALENDAR_MONTH = CALENDAR_DATA.get(Calendar.MONTH);
        CALENDAR_DAY = CALENDAR_DATA.get(Calendar.DATE);

        textToday.setText(String.valueOf(CALENDAR_DAY));
        textDayOfWeek.setText(CALENDAR_DAYOFWEEK.get(index));
        textYear.setText(CALENDAR_YEAR + getString(R.string.calendar_year));
        textMonth.setText((CALENDAR_MONTH + 1) + getString(R.string.calendar_month));

        if(index == 1) {
            textToday.setTextColor(ContextCompat.getColor(fragmentContext, R.color.RED));
            textDayOfWeek.setTextColor(ContextCompat.getColor(fragmentContext, R.color.RED));
        }
        else if(index == 7) {
            textToday.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLUE));
            textDayOfWeek.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLUE));
        }
        else {
            textToday.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
            textDayOfWeek.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
        }
        changeStaticValues();
    }

    private void setTextSelectedSchedule(Cursor cursor) {

        databaseID = cursor.getInt(cursor.getColumnIndex("_id"));
        final int startYear = cursor.getInt(cursor.getColumnIndex("startYear"));
        final int startMonth = cursor.getInt(cursor.getColumnIndex("startMonth"));
        final int startDay = cursor.getInt(cursor.getColumnIndex("startDay"));
        final int startHour = cursor.getInt(cursor.getColumnIndex("startHour"));
        final int startMin = cursor.getInt(cursor.getColumnIndex("startMin"));
        String startMinString;
        if (startMin < 10)
            startMinString = String.valueOf("0" + startMin);
        else
            startMinString = String.valueOf(startMin);

        final int endYear = cursor.getInt(cursor.getColumnIndex("endYear"));
        final int endMonth = cursor.getInt(cursor.getColumnIndex("endMonth"));
        final int endDay = cursor.getInt(cursor.getColumnIndex("endDay"));
        final int endHour = cursor.getInt(cursor.getColumnIndex("endHour"));
        final int endMin = cursor.getInt(cursor.getColumnIndex("endMin"));
        String endMinString;
        if (endMin < 10)
            endMinString = String.valueOf("0" + endMin);
        else
            endMinString = String.valueOf(endMin);

        String start = String.format("%d/%d/%d\t %d : %s", startYear, startMonth + 1, startDay, startHour, startMinString);
        String end = String.format("%d/%d/%d\t %d : %s", endYear, endMonth + 1, endDay, endHour, endMinString);

        textSubject.setText(cursor.getString(cursor.getColumnIndex("subject")));
        textPlace.setText(cursor.getString(cursor.getColumnIndex("place")));
        textStart.setText(start);
        textEnd.setText(end);
        textDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
        buttonDetail.setVisibility(View.VISIBLE);

        if(endDay != CALENDAR_DAY && startDay != endDay) {
            textStart.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
            textStart.setClickable(FALSE);
            textEnd.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLUE));
            textEnd.setClickable(TRUE);
            SWITCH = "END";
        }
        else if(startDay != CALENDAR_DAY && startDay != endDay){
            textStart.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLUE));
            textStart.setClickable(TRUE);
            textEnd.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
            textEnd.setClickable(FALSE);
            SWITCH = "START";
        }
        else {
            textStart.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
            textStart.setClickable(FALSE);
            textEnd.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
            textEnd.setClickable(FALSE);
        }
    }

    private void initializeScheduleString() {
        textSubject.setText("");
        textPlace.setText("");
        textStart.setText("");
        textEnd.setText("");
        textDescription.setText("");
        buttonDetail.setVisibility(View.GONE);
    }

    private void changeStaticValues() {
        MainActivity.setYear(CALENDAR_YEAR);
        MainActivity.setMonth(CALENDAR_MONTH);
        MainActivity.setDay(CALENDAR_DAY);
        MainActivity.setData();
    }
}
