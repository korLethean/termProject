package com.example.hd.termproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.DrmInitData;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.fragmentCloseEnterAnimation;
import static android.R.attr.scaleY;
import static android.R.attr.startY;
import static android.R.attr.width;
import static android.R.attr.height;

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
    StartScheduleAdapter startScheduleAdapter;
    private ListView startScheduleFrame;
    private ListView endScheduleFrame;
    EndScheduleAdapter endScheduleAdapter;

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

        startScheduleAdapter = new StartScheduleAdapter(fragmentContext, null, 0);
        startScheduleFrame = (ListView)fragmentView.findViewById(R.id.scheduleStartFrame);
        startScheduleFrame.setAdapter(startScheduleAdapter);

        endScheduleAdapter = new EndScheduleAdapter(fragmentContext, null, 0);
        endScheduleFrame = (ListView)fragmentView.findViewById(R.id.scheduleEndFrame);
        endScheduleFrame.setAdapter(endScheduleAdapter);

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
                CALENDAR_YEAR = CALENDAR_DATA.get(Calendar.YEAR);
                CALENDAR_MONTH = CALENDAR_DATA.get(Calendar.MONTH);
                CALENDAR_DAY = CALENDAR_DATA.get(Calendar.DATE);
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

                if(CALENDAR_MONTH == 11) {
                    CALENDAR_MONTH = 0;
                    CALENDAR_YEAR += 1;
                }
                else
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
                    CALENDAR_DATA.set(calendarAdapter.getYear(), calendarAdapter.getMonth(), calendarAdapter.getDay(position));
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
                schedulePopupWindow((Cursor) startScheduleAdapter.getItem(position));
            }
        });

        endScheduleFrame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schedulePopupWindow((Cursor) endScheduleAdapter.getItem(position));
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
        startQuery = String.format("SELECT * FROM SCHEDULES WHERE startYear='%d' AND startMonth='%d' AND startDay='%d' " +
                        "ORDER BY startHour, startMin;",
                CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
        endQuery = String.format("SELECT * FROM SCHEDULES WHERE endYear='%d' AND endMonth='%d' AND endDay='%d' " +
                        "ORDER BY endHour, endMin;",
                CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);

        startCursor = USE_FOR_QUERY.rawQuery(startQuery, null);
        endCursor = USE_FOR_QUERY.rawQuery(endQuery, null);

        if(startCursor.getCount() > 0)
            startScheduleAdapter.changeCursor(startCursor);
        else
            startScheduleAdapter.changeCursor(null);

        if(endCursor.getCount() > 0)
            endScheduleAdapter.changeCursor(endCursor);
        else
            endScheduleAdapter.changeCursor(null);

    }

    private void schedulePopupWindow(Cursor cursor) {
        LayoutInflater layoutInflater = (LayoutInflater) fragmentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.schedule_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final String databaseId = String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")));
        int startYear = cursor.getInt(cursor.getColumnIndex("startYear"));
        int startMonth = cursor.getInt(cursor.getColumnIndex("startMonth"));
        int startDay = cursor.getInt(cursor.getColumnIndex("startDay"));
        int startHour = cursor.getInt(cursor.getColumnIndex("startHour"));
        String startMinString;
        if (cursor.getInt(cursor.getColumnIndex("startMin")) < 10)
            startMinString = String.valueOf("0" + cursor.getInt(cursor.getColumnIndex("startMin")));
        else
            startMinString = String.valueOf(cursor.getInt(cursor.getColumnIndex("startMin")));

        int endYear = cursor.getInt(cursor.getColumnIndex("endYear"));
        int endMonth = cursor.getInt(cursor.getColumnIndex("endMonth"));
        int endDay = cursor.getInt(cursor.getColumnIndex("endDay"));
        int endHour = cursor.getInt(cursor.getColumnIndex("endHour"));
        String endMinString;
        if (cursor.getInt(cursor.getColumnIndex("endMin")) < 10)
            endMinString = String.valueOf("0" + cursor.getInt(cursor.getColumnIndex("endMin")));
        else
            endMinString = String.valueOf(cursor.getInt(cursor.getColumnIndex("endMin")));

        String start = String.format("%d/%d/%d\t %d : %s", startYear, startMonth, startDay, startHour, startMinString);
        String end = String.format("%d/%d/%d\t %d : %s", endYear, endMonth, endDay, endHour, endMinString);

        TextView popupSubject = (TextView)popupView.findViewById(R.id.popupSubject);
        TextView popupPlace = (TextView)popupView.findViewById(R.id.popupPlace);
        TextView popupStart = (TextView)popupView.findViewById(R.id.popupStart);
        TextView popupEnd = (TextView)popupView.findViewById(R.id.popupEnd);
        TextView popupDescription = (TextView)popupView.findViewById(R.id.popupDescription);

        Button buttonClose = (Button)popupView.findViewById(R.id.buttonClose);
        Button buttonEdit = (Button)popupView.findViewById(R.id.buttonEdit); // 미구현
        Button buttonDelete = (Button)popupView.findViewById(R.id.buttonDelete);

        popupSubject.setText(cursor.getString(cursor.getColumnIndex("subject")));
        popupPlace.setText(cursor.getString(cursor.getColumnIndex("place")));
        popupStart.setText(start);
        popupEnd.setText(end);
        popupDescription.setText(cursor.getString(cursor.getColumnIndex("description")));

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.update();

        buttonClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentContext);
                builder.setTitle(fragmentContext.getString(R.string.alert_delete_title))
                        .setMessage(fragmentContext.getString(R.string.alert_delete_description))
                        .setCancelable(false)
                        .setPositiveButton(fragmentContext.getString(R.string.alert_delete_confirm),
                                new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){
                                USE_FOR_QUERY.delete("SCHEDULES", "_id=?", new String[]{databaseId});
                                dialog.cancel();
                                popupWindow.dismiss();
                                setTextSchedule();
                                Toast.makeText(fragmentContext, getString(R.string.message_deleted), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(fragmentContext.getString(R.string.alert_delete_cancel),
                                new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
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
