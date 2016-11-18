package com.example.hd.termproject;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthFragment extends Fragment{
    View fragmentView;
    Context fragmentContext;
    Bundle fragmentBundle;

    TextView textYearMonth;
    TextView textSelectedDay;
    Button buttonPrevious;
    Button buttonToday;
    Button buttonNext;

    Date CALENDAR_CURRENTDATE = new Date();
    int CALENDAR_YEAR;
    int CALENDAR_MONTH;
    int CALENDAR_DAY;
    ArrayList<String> CALENDAR_DAYOFWEEK;
    Calendar CALENDAR_DATA;

    private CalendarAdapter calendarAdapter;
    private GridView calendarFrame;

    public MonthFragment() { }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                                Bundle savedInstanceState) {
        fragmentView = layoutInflater.inflate(R.layout.fragment_month, container, false);
        fragmentContext = container.getContext();
        fragmentBundle = new Bundle();
        fragmentBundle = getArguments();

        textYearMonth = (TextView)fragmentView.findViewById(R.id.textYearMonth);
        textYearMonth.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
        textSelectedDay = (TextView)fragmentView.findViewById(R.id.textSelectedDay);
        textSelectedDay.setTextColor(ContextCompat.getColor(fragmentContext, R.color.BLACK));
        buttonPrevious = (Button)fragmentView.findViewById(R.id.buttonPreviousMonth);
        buttonToday = (Button)fragmentView.findViewById(R.id.buttonToday);
        buttonNext = (Button)fragmentView.findViewById(R.id.buttonNextMonth);
        calendarFrame = (GridView)fragmentView.findViewById(R.id.calendarFrame);

        CALENDAR_YEAR = fragmentBundle.getInt("YEAR");
        CALENDAR_MONTH = fragmentBundle.getInt("MONTH");
        CALENDAR_DAY = fragmentBundle.getInt("DAY");

        CALENDAR_DAYOFWEEK = new ArrayList<String>();
        CALENDAR_DAYOFWEEK.add(getString(R.string.calendar_error));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SUNDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.MONDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.TUESDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.WEDNESDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.THURSDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.FRIDAY));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SATURDAY));

        CALENDAR_DATA = Calendar.getInstance();
        CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);

        calendarAdapter = new CalendarAdapter(fragmentContext, R.layout.calendar_item, CALENDAR_DATA);
        calendarFrame = (GridView)fragmentView.findViewById(R.id.calendarFrame);
        calendarFrame.setAdapter(calendarAdapter);

        setTextSelectedYearMonthDay();

        buttonPrevious.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CALENDAR_MONTH -= 1;
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                calendarAdapter.notifyDataSetChanged();
                setTextSelectedYearMonthDay();
            }
        });

        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALENDAR_DATA.setTime(CALENDAR_CURRENTDATE);
                calendarAdapter.notifyDataSetChanged();
                setTextSelectedYearMonthDay();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALENDAR_MONTH += 1;
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                calendarAdapter.notifyDataSetChanged();
                setTextSelectedYearMonthDay();
            }
        });

        calendarFrame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > (calendarAdapter.getDATE_START_POINT() - 1) &&
                        position - (calendarAdapter.getDATE_START_POINT()) < calendarAdapter.getDATE_END_POINT()) {
                    CALENDAR_YEAR = calendarAdapter.getYear();
                    CALENDAR_MONTH = calendarAdapter.getMonth();
                    CALENDAR_DAY = calendarAdapter.getDay(position);

                    CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
                    setTextSelectedYearMonthDay();
                }
            }
        });

        return fragmentView;
    }

    public void setTextSelectedYearMonthDay() {
        int index = CALENDAR_DATA.get(Calendar.DAY_OF_WEEK);
        CALENDAR_YEAR = CALENDAR_DATA.get(Calendar.YEAR);
        CALENDAR_MONTH = CALENDAR_DATA.get(Calendar.MONTH);
        CALENDAR_DAY = CALENDAR_DATA.get(Calendar.DATE);

        textYearMonth.setText(CALENDAR_YEAR + getString(R.string.calendar_year) + " " +
                + (CALENDAR_MONTH + 1) + getString(R.string.calendar_month));
        textSelectedDay.setText(CALENDAR_YEAR + "/" + (CALENDAR_MONTH + 1) + "/" + CALENDAR_DAY
               + " " + CALENDAR_DAYOFWEEK.get(index));
    }
}
