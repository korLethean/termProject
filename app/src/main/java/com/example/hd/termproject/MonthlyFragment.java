package com.example.hd.termproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

public class MonthlyFragment extends Fragment{
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
    private MonthlyStartScheduleAdapter monthlyStartScheduleAdapter;
    private ListView startScheduleFrame;
    private MonthlyEndScheduleAdapter monthlyEndScheduleAdapter;
    private ListView endScheduleFrame;

    private DBManager database;
    private Cursor startCursor;
    private Cursor endCursor;

    public MonthlyFragment() { }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                                Bundle savedInstanceState) {
        fragmentView = layoutInflater.inflate(R.layout.fragment_monthly, container, false);
        fragmentContext = container.getContext();
        database = new DBManager(fragmentContext, null);

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

        textYearMonth = (TextView)fragmentView.findViewById(R.id.textYearMonth);
        textSelectedDay = (TextView)fragmentView.findViewById(R.id.textSelectedDay);
        buttonLast = (Button)fragmentView.findViewById(R.id.buttonLastMonth);
        buttonToday = (Button)fragmentView.findViewById(R.id.buttonMonthlyToday);
        buttonPick = (Button)fragmentView.findViewById(R.id.buttonMonthlyPick);
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
                        setTextScheduleList();
                        changeStaticValues();
                    }
                }, CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);

        calendarAdapter = new CalendarAdapter(fragmentContext, R.layout.calendar_item, CALENDAR_DATA);
        calendarFrame = (GridView)fragmentView.findViewById(R.id.calendarFrame);
        calendarFrame.setAdapter(calendarAdapter);

        monthlyStartScheduleAdapter = new MonthlyStartScheduleAdapter(fragmentContext, null, 0);
        startScheduleFrame = (ListView)fragmentView.findViewById(R.id.scheduleMonthlyStartFrame);
        startScheduleFrame.setAdapter(monthlyStartScheduleAdapter);
        monthlyEndScheduleAdapter = new MonthlyEndScheduleAdapter(fragmentContext, null, 0);
        endScheduleFrame = (ListView)fragmentView.findViewById(R.id.scheduleMonthlyEndFrame);
        endScheduleFrame.setAdapter(monthlyEndScheduleAdapter);

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

                if(CALENDAR_MONTH == 0) {
                    CALENDAR_MONTH = 11;
                    CALENDAR_YEAR -= 1;
                }
                else
                    CALENDAR_MONTH -= 1;

                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                setTextSelectedYearMonthDay();
                setTextScheduleList();
                changeStaticValues();
                calendarAdapter.notifyDataSetChanged();
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

                if(CALENDAR_MONTH == 11) {
                    CALENDAR_MONTH = 0;
                    CALENDAR_YEAR += 1;
                }
                else
                    CALENDAR_MONTH += 1;

                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                setTextSelectedYearMonthDay();
                setTextScheduleList();
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
                    CALENDAR_DATA.set(calendarAdapter.getYear(), calendarAdapter.getMonth(), calendarAdapter.getDay(position));
                    setTextSelectedYearMonthDay();
                    setTextScheduleList();
                    changeStaticValues();
                    datePickerDialog.updateDate(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                }
            }
        });

        startScheduleFrame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schedulePopupWindow((Cursor)monthlyStartScheduleAdapter.getItem(position));
            }
        });

        endScheduleFrame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schedulePopupWindow((Cursor)monthlyEndScheduleAdapter.getItem(position));
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

        if(startCursor.getCount() > 0)
            monthlyStartScheduleAdapter.changeCursor(startCursor);
        else
            monthlyStartScheduleAdapter.changeCursor(null);

        if(endCursor.getCount() > 0)
            monthlyEndScheduleAdapter.changeCursor(endCursor);
        else
            monthlyEndScheduleAdapter.changeCursor(null);
    }

    private void schedulePopupWindow(Cursor cursor) {
        final LayoutInflater layoutInflater = (LayoutInflater) fragmentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.schedule_popup, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(fragmentContext);
        builder.setTitle(fragmentContext.getString(R.string.text_detail_title));
        builder.setView(popupView);
        final AlertDialog popupWindow = builder.create();
        popupWindow.setCanceledOnTouchOutside(false);

        final int databaseID = cursor.getInt(cursor.getColumnIndex("_id"));
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

        final TextView popupSubject = (TextView)popupView.findViewById(R.id.popupSubject);
        final TextView popupPlace = (TextView)popupView.findViewById(R.id.popupPlace);
        TextView popupStart = (TextView)popupView.findViewById(R.id.popupStart);
        TextView popupEnd = (TextView)popupView.findViewById(R.id.popupEnd);
        final TextView popupDescription = (TextView)popupView.findViewById(R.id.popupDescription);

        Button buttonClose = (Button)popupView.findViewById(R.id.buttonPopupClose);
        Button buttonDetail = (Button)popupView.findViewById(R.id.buttonPopupDetail);

        popupSubject.setText(cursor.getString(cursor.getColumnIndex("subject")));
        popupPlace.setText(cursor.getString(cursor.getColumnIndex("place")));
        popupStart.setText(start);
        popupEnd.setText(end);
        popupDescription.setText(cursor.getString(cursor.getColumnIndex("description")));

        popupWindow.show();

        buttonClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        buttonDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragmentContext.getApplicationContext(), DetailScheduleActivity.class);
                intent.putExtra("_id", databaseID);
                popupWindow.dismiss();
                startActivity(intent);
            }
        });
    }

    private void changeStaticValues() {
        MainActivity.setYear(CALENDAR_YEAR);
        MainActivity.setMonth(CALENDAR_MONTH);
        MainActivity.setDay(CALENDAR_DAY);
        MainActivity.setData();
    }
}
