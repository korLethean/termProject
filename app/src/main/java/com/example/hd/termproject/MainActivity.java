package com.example.hd.termproject;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static Calendar CALENDAR_DATA;
    private static int CALENDAR_YEAR;
    private static int CALENDAR_MONTH;
    private static int CALENDAR_DAY;

    private int FRAGMENT_MANAGER;
    private final MonthFragment monthFragment = new MonthFragment();
//    private final WeekFragment weekFragment = new WeekFragment();
//    private final DayFragment dayFragment = new DayFragment();
    private final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SplashActivity finishActivity = SplashActivity.usedForFinish;
        finishActivity.finish();

        CALENDAR_DATA = Calendar.getInstance();
        CALENDAR_YEAR = CALENDAR_DATA.get(Calendar.YEAR);
        CALENDAR_MONTH = CALENDAR_DATA.get(Calendar.MONTH);
        CALENDAR_DAY = CALENDAR_DATA.get(Calendar.DATE);

        FRAGMENT_MANAGER = -1;
        switchFragment(0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_months:
                switchFragment(0);
                return true;
            case R.id.menu_weeks:
                switchFragment(1);
                return true;
            case R.id.menu_days:
                switchFragment(2);
                return true;
            case R.id.menu_addSchedule:
                switchFragment(3);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    protected void switchFragment(int id){
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();

        if(id == 0 && FRAGMENT_MANAGER != 0) {
            FRAGMENT_MANAGER = 0;
            getSupportActionBar().setTitle(getString(R.string.title_month));
            fragmentTransaction.replace(R.id.activity_main, monthFragment);
        }
        else if(id == 1 && FRAGMENT_MANAGER != 1) {
            FRAGMENT_MANAGER = 1;
            getSupportActionBar().setTitle(getString(R.string.title_week));
            //fragmentTransaction.replace(R.id.activity_main, monthFragment);
        }
        else if(id == 2 && FRAGMENT_MANAGER != 2) {
            FRAGMENT_MANAGER = 2;
            getSupportActionBar().setTitle(getString(R.string.title_day));
            //fragmentTransaction.replace(R.id.activity_main, monthFragment);
        }
        else if(id == 3) {
            Intent intent = new Intent(getApplicationContext(), AddScheduleActivity.class);
            startActivity(intent);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static int getYear() {
        return CALENDAR_YEAR;
    }

    public static int getMonth() {
        return CALENDAR_MONTH;
    }

    public static int getDay() {
        return CALENDAR_DAY;
    }

    public static void setYear(int year) {
        CALENDAR_YEAR = year;
    }

    public static void setMonth(int month) {
        CALENDAR_MONTH = month;
    }

    public static void setDay(int day) {
        CALENDAR_DAY = day;
    }

    public static void setData() {
        CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
    }
}