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

public class WeeklyFragment extends Fragment {
    private View fragmentView;
    private Context fragmentContext;

    private TextView textWeeklyYearMonth;
    private TextView[] textDate;
    int[] day = new int[7];

    private WeeklyScheduleAdapter[] weeklyScheduleAdapter;
    private ListView[] scheduleFrame;

    private DatePickerDialog datePickerDialog;
    private Button buttonLast;
    private Button buttonToday;
    private Button buttonPick;
    private Button buttonNext;

    private Date CALENDAR_CURRENTDATE = new Date();
    private int CALENDAR_YEAR;
    private int CALENDAR_MONTH;
    private int CALENDAR_DAY;
    private ArrayList<String> CALENDAR_DAYOFWEEK;
    private Calendar CALENDAR_DATA;

    private DBManager database;
    private Cursor[] cursor;
    private int databaseID;

    public WeeklyFragment() { }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = layoutInflater.inflate(R.layout.fragment_weekly, container, false);
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

        textWeeklyYearMonth = (TextView)fragmentView.findViewById(R.id.textWeeklyYearMonth);
        cursor = new Cursor[7];
        textDate = new TextView[7];
        textDate[0] = (TextView)fragmentView.findViewById(R.id.textSunday);
        textDate[1] = (TextView)fragmentView.findViewById(R.id.textMonday);
        textDate[2] = (TextView)fragmentView.findViewById(R.id.textTuesday);
        textDate[3] = (TextView)fragmentView.findViewById(R.id.textWednesday);
        textDate[4] = (TextView)fragmentView.findViewById(R.id.textThursday);
        textDate[5] = (TextView)fragmentView.findViewById(R.id.textFriday);
        textDate[6] = (TextView)fragmentView.findViewById(R.id.textSaturday);

        weeklyScheduleAdapter = new WeeklyScheduleAdapter[7];
        scheduleFrame = new ListView[7];
        scheduleFrame[0] = (ListView)fragmentView.findViewById(R.id.scheduleSundayFrame);
        scheduleFrame[1] = (ListView)fragmentView.findViewById(R.id.scheduleMondayFrame);
        scheduleFrame[2] = (ListView)fragmentView.findViewById(R.id.scheduleTuesdayFrame);
        scheduleFrame[3] = (ListView)fragmentView.findViewById(R.id.scheduleWednesdayFrame);
        scheduleFrame[4] = (ListView)fragmentView.findViewById(R.id.scheduleThursdayFrame);
        scheduleFrame[5] = (ListView)fragmentView.findViewById(R.id.scheduleFridayFrame);
        scheduleFrame[6] = (ListView)fragmentView.findViewById(R.id.scheduleSaturdayFrame);

        for(int i = 0 ; i < 7 ; i++) {
            final int temp = i;
            textDate[i].setOnClickListener(textListender);
            weeklyScheduleAdapter[i] = new WeeklyScheduleAdapter(fragmentContext, null, 0);
            scheduleFrame[i].setAdapter(weeklyScheduleAdapter[i]);
            scheduleFrame[i].setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor cursor = (Cursor)weeklyScheduleAdapter[temp].getItem(position);
                    databaseID = cursor.getInt(cursor.getColumnIndex("_id"));

                    Intent intent = new Intent(fragmentContext.getApplicationContext(), DetailScheduleActivity.class);
                    intent.putExtra("_id", databaseID);
                    startActivity(intent);
                }
            });
        }

        datePickerDialog = new DatePickerDialog(fragmentContext, android.R.style.Theme_Holo_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        CALENDAR_YEAR = year;
                        CALENDAR_MONTH = month;
                        CALENDAR_DAY = day;
                        CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                        setTextWeeklyDate();
                        setTextScheduleList();
                        changeStaticValues();
                    }
                }, CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);


        buttonLast = (Button)fragmentView.findViewById(R.id.buttonLastWeek);
        buttonToday = (Button)fragmentView.findViewById(R.id.buttonWeeklyToday);
        buttonPick = (Button)fragmentView.findViewById(R.id.buttonWeeklyPick);
        buttonNext = (Button)fragmentView.findViewById(R.id.buttonNextWeek);

        buttonLast.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CALENDAR_DAY -= 7;
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                setTextWeeklyDate();
                setTextScheduleList();
                changeStaticValues();
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
                setTextWeeklyDate();
                setTextScheduleList();
                changeStaticValues();
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
                CALENDAR_DAY += 7;
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                setTextWeeklyDate();
                setTextScheduleList();
                changeStaticValues();
                datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
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
        setTextWeeklyDate();
        setTextScheduleList();
        super.onResume();
    }

    private TextView.OnClickListener textListender = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView tempTextView = (TextView)fragmentView.findViewById(view.getId());
            String text = tempTextView.getText().toString();
            CALENDAR_DAY = Integer.parseInt(text.substring(3, 5).trim());
            CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            setTextWeeklyDate();
            changeStaticValues();
        }
    };

    private void setTextWeeklyDate() {
        int index = 1 - CALENDAR_DATA.get(Calendar.DAY_OF_WEEK);
        CALENDAR_YEAR = CALENDAR_DATA.get(Calendar.YEAR);
        CALENDAR_MONTH = CALENDAR_DATA.get(Calendar.MONTH);
        CALENDAR_DAY = CALENDAR_DATA.get(Calendar.DATE);

        textWeeklyYearMonth.setText(CALENDAR_YEAR + fragmentContext.getString(R.string.calendar_year) + " "
                                + (CALENDAR_MONTH + 1) + fragmentContext.getString(R.string.calendar_month) + " "
                                + CALENDAR_DAY + fragmentContext.getString(R.string.calendar_day));

        for(int i = 0 ; i < 7 ; i++) {
            day[i] = CALENDAR_DAY + i + index;
            if(i == 0)
                textDate[i].setTextColor(ContextCompat.getColor(fragmentContext, R.color.RED));
            else if(i == 6)
                textDate[i].setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLUE));
            else
                textDate[i].setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));

            if(day[i] < 1) {
                textDate[i].setText("Last Month");
                textDate[i].setTextColor(ContextCompat.getColor(fragmentContext, R.color.GRAY));
            }
            else if(day[i] > CALENDAR_DATA.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                textDate[i].setText("Next Month");
                textDate[i].setTextColor(ContextCompat.getColor(fragmentContext, R.color.GRAY));
            }
            else
                textDate[i].setText((CALENDAR_MONTH + 1) + "/" + day[i] + " " + CALENDAR_DAYOFWEEK.get(i + 1));
        }
    }

    private void setTextScheduleList() {
        SQLiteDatabase USE_FOR_QUERY = database.getReadableDatabase();
        for(int i = 0 ; i < 7 ; i++) {
            String query = String.format("SELECT * FROM SCHEDULES WHERE startYear='%d' AND startMonth='%d' AND startDay='%d' " +
                            "OR endYear='%d' AND endMonth='%d' AND endDay='%d' " +
                            "ORDER BY startHour, startMin;",
                    CALENDAR_YEAR, CALENDAR_MONTH, day[i], CALENDAR_YEAR, CALENDAR_MONTH, day[i]);
            cursor[i] = USE_FOR_QUERY.rawQuery(query, null);

            if (cursor[i].getCount() > 0) {
                weeklyScheduleAdapter[i].changeCursor(cursor[i]);
                resetViewSize(i);
            }
            else {
                weeklyScheduleAdapter[i].changeCursor(null);
                resetViewSize(i);
            }
        }
    }

    private void changeStaticValues() {
        MainActivity.setYear(CALENDAR_YEAR);
        MainActivity.setMonth(CALENDAR_MONTH);
        MainActivity.setDay(CALENDAR_DAY);
        MainActivity.setData();
    }

    private void resetViewSize(int index) {
        ViewGroup.LayoutParams params = scheduleFrame[index].getLayoutParams();
        params.height = 120 * scheduleFrame[index].getCount();
        scheduleFrame[index].setLayoutParams(params);
        scheduleFrame[index].requestLayout();
    }
}