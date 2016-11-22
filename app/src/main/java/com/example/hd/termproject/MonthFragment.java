package com.example.hd.termproject;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
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

    public MonthFragment() { }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                                Bundle savedInstanceState) {
        fragmentView = layoutInflater.inflate(R.layout.fragment_month, container, false);
        fragmentContext = container.getContext();

        textYearMonth = (TextView)fragmentView.findViewById(R.id.textYearMonth);
        textYearMonth.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
        textSelectedDay = (TextView)fragmentView.findViewById(R.id.textSelectedDay);
        textSelectedDay.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
        buttonLast = (Button)fragmentView.findViewById(R.id.buttonLastMonth);
        buttonToday = (Button)fragmentView.findViewById(R.id.buttonToday);
        buttonPick = (Button)fragmentView.findViewById(R.id.buttonPick);
        buttonNext = (Button)fragmentView.findViewById(R.id.buttonNextMonth);
        calendarFrame = (GridView)fragmentView.findViewById(R.id.calendarFrame);
        datePickerDialog = new DatePickerDialog(fragmentContext, android.R.style.Theme_Holo_Dialog_MinWidth,
                dateSetListener, CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);

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
                calendarAdapter.notifyDataSetChanged();
                setTextSelectedYearMonthDay();
                changeStaticValues();
            }
        });

        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALENDAR_DATA.setTime(CALENDAR_CURRENTDATE);
                CALENDAR_DAY = CALENDAR_DATA.get(Calendar.DATE);
                calendarAdapter.notifyDataSetChanged();
                setTextSelectedYearMonthDay();
                changeStaticValues();
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
                CALENDAR_MONTH += 1;
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                calendarAdapter.notifyDataSetChanged();
                setTextSelectedYearMonthDay();
                changeStaticValues();
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
                    changeStaticValues();
                }
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
        calendarAdapter.notifyDataSetChanged();
        setTextSelectedYearMonthDay();
        super.onResume();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            CALENDAR_YEAR = year;
            CALENDAR_MONTH = month;
            CALENDAR_DAY = day;
            CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            calendarAdapter.notifyDataSetChanged();
            setTextSelectedYearMonthDay();
            changeStaticValues();
        }
    };

    private void setTextSelectedYearMonthDay() {
        int index = CALENDAR_DATA.get(Calendar.DAY_OF_WEEK);
        CALENDAR_YEAR = CALENDAR_DATA.get(Calendar.YEAR);
        CALENDAR_MONTH = CALENDAR_DATA.get(Calendar.MONTH);

        if((CALENDAR_YEAR % 4 == 0 && CALENDAR_YEAR % 100 != 0) || CALENDAR_YEAR % 400 == 0)
            CALENDAR_LEAP = true;
        else
            CALENDAR_LEAP = false;

        textYearMonth.setText(CALENDAR_YEAR + getString(R.string.calendar_year) + " " +
                + (CALENDAR_MONTH + 1) + getString(R.string.calendar_month));
        textSelectedDay.setText(CALENDAR_YEAR + "/" + (CALENDAR_MONTH + 1) + "/" + CALENDAR_DAY
               + " " + CALENDAR_DAYOFWEEK.get(index));
    }

    private void changeStaticValues() {
        MainActivity.setYear(CALENDAR_YEAR);
        MainActivity.setMonth(CALENDAR_MONTH);
        MainActivity.setDay(CALENDAR_DAY);
        MainActivity.setData();
    }
}
