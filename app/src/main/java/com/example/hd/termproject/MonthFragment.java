package com.example.hd.termproject;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthFragment extends Fragment{
    private View fragmentView;
    private Context fragmentContext;

    private TextView textYearMonth;
    private TextView textSelectedDay;
    private Button buttonLast;
    private Button buttonToday;
    private Button buttonPick;
    private Button buttonNext;
    private DatePickerDialog datePickerDialog;

    private Date CALENDAR_CURRENTDATE = new Date();
    private int CALENDAR_YEAR;
    private int CALENDAR_MONTH;
    private int CALENDAR_DAY;
    private boolean CALENDAR_LEAP;
    private ArrayList<String> CALENDAR_DAYOFWEEK;
    private Calendar CALENDAR_DATA;

    private CalendarAdapter calendarAdapter;
    private GridView calendarFrame;
    private ListView startScheduleFrame;
    private ListView endScheduleFrame;

    private Cursor startCursor;
    private Cursor endCursor;
    private SQLiteDatabase USE_FOR_QUERY;
    private String startQuery;
    private String endQuery;

    private DBManager database;

    public MonthFragment() { }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                                Bundle savedInstanceState) {
        fragmentView = layoutInflater.inflate(R.layout.fragment_month, container, false);
        fragmentContext = container.getContext();
        database = new DBManager(fragmentContext, null);
        USE_FOR_QUERY = database.getWritableDatabase();

        textYearMonth = (TextView)fragmentView.findViewById(R.id.textYearMonth);
        textSelectedDay = (TextView)fragmentView.findViewById(R.id.textSelectedDay);
        buttonLast = (Button)fragmentView.findViewById(R.id.buttonLastMonth);
        buttonToday = (Button)fragmentView.findViewById(R.id.buttonToday);
        buttonPick = (Button)fragmentView.findViewById(R.id.buttonPick);
        buttonNext = (Button)fragmentView.findViewById(R.id.buttonNextMonth);
        calendarFrame = (GridView)fragmentView.findViewById(R.id.calendarFrame);
        datePickerDialog = new DatePickerDialog(fragmentContext, android.R.style.Theme_Holo_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        CALENDAR_YEAR = year;
                        CALENDAR_MONTH = month;
                        CALENDAR_DAY = day;
                        CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                        calendarAdapter.notifyDataSetChanged();
                        setTextSelectedYearMonthDay();
                        setTextSchedule();
                        changeStaticValues();
                    }
                }, CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);

        CALENDAR_DATA = Calendar.getInstance();
        CALENDAR_DAYOFWEEK = new ArrayList<String>();
        CALENDAR_DAYOFWEEK.add(getString(R.string.calendar_error));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SUNDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.MONDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.TUESDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.WEDNESDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.THURSDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.FRIDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SATURDAY));

        calendarAdapter = new CalendarAdapter(fragmentContext, R.layout.calendar_item, CALENDAR_DATA);
        calendarFrame = (GridView)fragmentView.findViewById(R.id.calendarFrame);
        calendarFrame.setAdapter(calendarAdapter);

        startScheduleFrame = (ListView)fragmentView.findViewById(R.id.scheduleStartFrame);
        endScheduleFrame = (ListView)fragmentView.findViewById(R.id.scheduleEndFrame);

        buttonLast.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (CALENDAR_MONTH == 2) {
                    if(CALENDAR_LEAP == false && CALENDAR_DAY > 28)
                        CALENDAR_DAY = 28;
                    else if(CALENDAR_LEAP == true && CALENDAR_DAY > 29)
                        CALENDAR_DAY = 29;
                }
                else if(CALENDAR_DAY == 31) {
                    CALENDAR_DAY = 30;
                }
                CALENDAR_MONTH -= 1;
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                setTextSelectedYearMonthDay();
                setTextSchedule();
                changeStaticValues();
                calendarAdapter.notifyDataSetChanged();
                datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            }
        });

        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALENDAR_DATA.setTime(CALENDAR_CURRENTDATE);
                setTextSelectedYearMonthDay();
                setTextSchedule();
                changeStaticValues();
                calendarAdapter.notifyDataSetChanged();
                datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            }
        });

        buttonPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CALENDAR_MONTH == 0) {
                    if(CALENDAR_LEAP == false && CALENDAR_DAY > 28)
                        CALENDAR_DAY = 28;
                    else if(CALENDAR_LEAP == true && CALENDAR_DAY > 29)
                        CALENDAR_DAY = 29;
                }
                CALENDAR_MONTH += 1;
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                setTextSelectedYearMonthDay();
                setTextSchedule();
                changeStaticValues();
                calendarAdapter.notifyDataSetChanged();
                datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            }
        });

        calendarFrame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > (calendarAdapter.getStartPoint() - 1) &&
                        position - (calendarAdapter.getStartPoint()) < calendarAdapter.getEndPoint()) {
                    CALENDAR_YEAR = calendarAdapter.getYear();
                    CALENDAR_MONTH = calendarAdapter.getMonth();
                    CALENDAR_DAY = calendarAdapter.getDay(position);
                    CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                    setTextSelectedYearMonthDay();
                    setTextSchedule();
                    changeStaticValues();
                    datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                }
            }
        });

        startScheduleFrame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
            }
        });

        endScheduleFrame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        setTextSchedule();
        super.onResume();
    }

    private void setTextSelectedYearMonthDay() {
        int index = CALENDAR_DATA.get(Calendar.DAY_OF_WEEK);
        CALENDAR_YEAR = CALENDAR_DATA.get(Calendar.YEAR);
        CALENDAR_MONTH = CALENDAR_DATA.get(Calendar.MONTH);
        CALENDAR_DAY = CALENDAR_DATA.get(Calendar.DATE);

        if((CALENDAR_YEAR % 4 == 0 && CALENDAR_YEAR % 100 != 0) || CALENDAR_YEAR % 400 == 0)
            CALENDAR_LEAP = true;
        else
            CALENDAR_LEAP = false;

        textYearMonth.setText(CALENDAR_YEAR + getString(R.string.calendar_year) + " " +
                + (CALENDAR_MONTH + 1) + getString(R.string.calendar_month));
        textSelectedDay.setText(CALENDAR_YEAR + "/" + (CALENDAR_MONTH + 1) + "/" + CALENDAR_DAY
               + " " + CALENDAR_DAYOFWEEK.get(index));
    }

    private void setTextSchedule() {
        startQuery = String.format("SELECT _id, startHour, startMin, subject, place, description FROM SCHEDULES " +
                        "WHERE startYear='%d' AND startMonth='%d' AND startDay='%d' " +
                        "ORDER BY startHour, startMin;",
                CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
        endQuery = String.format("SELECT _id, endHour, endMin, subject, place, description FROM SCHEDULES " +
                        "WHERE endYear='%d' AND endMonth='%d' AND endDay='%d' " +
                        "ORDER BY endHour, endMin;",
                CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);

        startCursor = USE_FOR_QUERY.rawQuery(startQuery, null);
        endCursor = USE_FOR_QUERY.rawQuery(endQuery, null);

        StartScheduleAdapter startScheduleAdapter = new StartScheduleAdapter(fragmentContext, startCursor, 0);
        startScheduleFrame.setAdapter(startScheduleAdapter);

        EndScheduleAdapter endScheduleAdapter = new EndScheduleAdapter(fragmentContext, endCursor, 0);
        endScheduleFrame.setAdapter(endScheduleAdapter);
    }

    private void changeStaticValues() {
        MainActivity.setYear(CALENDAR_YEAR);
        MainActivity.setMonth(CALENDAR_MONTH);
        MainActivity.setDay(CALENDAR_DAY);
        MainActivity.setData();
    }
}
