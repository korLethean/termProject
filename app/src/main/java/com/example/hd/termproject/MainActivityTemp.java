/*
package com.example.hd.termproject;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SplashActivity finishActivity = SplashActivity.usedForFinish;
        finishActivity.finish();

        textYearMonth = (TextView)findViewById(R.id.textYearMonth);
        textYearMonth.setTextColor(ContextCompat.getColor(this, R.color.BLACK));
        textSelectedDay = (TextView)findViewById(R.id.textSelectedDay);
        textSelectedDay.setTextColor(ContextCompat.getColor(this, R.color.BLACK));
        buttonPrevious = (Button)findViewById(R.id.buttonPreviousMonth);
        buttonToday = (Button)findViewById(R.id.buttonToday);
        buttonNext = (Button)findViewById(R.id.buttonNextMonth);
        calendarFrame = (GridView)findViewById(R.id.calendarFrame);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

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
        CALENDAR_DATA.setTime(CALENDAR_CURRENTDATE);

        calendarAdapter = new CalendarAdapter(this, R.layout.calendar_item, CALENDAR_DATA);
        calendarFrame = (GridView)findViewById(R.id.calendarFrame);
        calendarFrame.setAdapter(calendarAdapter);

        setTextSelectedYearMonthDay();

        buttonPrevious.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH - 1, CALENDAR_DAY);
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
                CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH + 1, CALENDAR_DAY);
                calendarAdapter.notifyDataSetChanged();
                setTextSelectedYearMonthDay();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
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


public class MainActivity extends AppCompatActivity {
    CalendarView calendarView;
    TextView textSelectedDay;
    Button buttonToday;
    Date CALENDAR_CURRENTDATE = new Date();
    long CALENDAR_CURRENTTIME = CALENDAR_CURRENTDATE.getTime();
    int CALENDAR_YEAR;
    int CALENDAR_MONTH;
    int CALENDAR_DAY;
    String CALENDAR_DAYOFWEEK;
    Calendar CALENDAR_DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SplashActivity finishActivity = SplashActivity.usedForFinish;
        finishActivity.finish();

        calendarView = (CalendarView)findViewById(R.id.calendarView);
        textSelectedDay = (TextView)findViewById(R.id.textSelectedDay);
        buttonToday = (Button)findViewById(R.id.buttonToday);

        CALENDAR_DATA = Calendar.getInstance();
        CALENDAR_DATA.setTime(CALENDAR_CURRENTDATE);
        setTextSelectedDay();



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                CALENDAR_DATA.set(year, month, day);
                setTextSelectedDay();
            }
        });

        buttonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALENDAR_DATA.setTime(CALENDAR_CURRENTDATE);
                calendarView.setDate(CALENDAR_CURRENTTIME);
                setTextSelectedDay();
            }
        });
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.go_to_frag_a:
                switchFragment(0);
                return true;
            case R.id.go_to_frag_b:
                switchFragment(1);
                return true;
            case R.id.go_to_frag_c:
                switchFragment(2);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    protected void switchFragment(int id){
        final FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        if(id == 0)
            fragmentTransaction.replace(R.id.fragmentCaller, );
        else if(id == 1)
            fragmentTransaction.replace(R.id.fragmentCaller, );
        else if(id == 2)
            fragmentTransaction.replace(R.id.fragmentCaller, );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }/////////////////////////////

    public void setTextSelectedDay() {
        CALENDAR_YEAR = CALENDAR_DATA.get(Calendar.YEAR);
        CALENDAR_MONTH = CALENDAR_DATA.get(Calendar.MONTH);
        CALENDAR_DAY = CALENDAR_DATA.get(Calendar.DATE);

        switch(CALENDAR_DATA.get(Calendar.DAY_OF_WEEK)){
            case 1:
                CALENDAR_DAYOFWEEK = getString(R.string.SUN);
                break;
            case 2:
                CALENDAR_DAYOFWEEK = getString(R.string.MON);
                break;
            case 3:
                CALENDAR_DAYOFWEEK = getString(R.string.TUE);
                break;
            case 4:
                CALENDAR_DAYOFWEEK = getString(R.string.WED);
                break;
            case 5:
                CALENDAR_DAYOFWEEK = getString(R.string.THU);
                break;
            case 6:
                CALENDAR_DAYOFWEEK = getString(R.string.FRI);
                break;
            case 7:
                CALENDAR_DAYOFWEEK = getString(R.string.SAT);
                break;
            default:
                CALENDAR_DAYOFWEEK = getString(R.string.calendar_error);
                break;
        }

        textSelectedDay.setText(CALENDAR_YEAR + "/" + CALENDAR_MONTH + "/" + CALENDAR_DAY
                + " " + CALENDAR_DAYOFWEEK);
    }
}
*/