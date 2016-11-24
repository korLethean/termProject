package com.example.hd.termproject;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static Calendar DATA;
    private static int YEAR;
    private static int MONTH;
    private static int DAY;
    private static int HOUR;
    private static int MIN;
    private static int DURATION;
    private static String SUBJECT;
    private static String DESCRIPTION;

    private int FRAGMENT_MANAGER;
    private final MonthFragment monthFragment = new MonthFragment();
//    private final WeekFragment weekFragment = new WeekFragment();
//    private final DayFragment dayFragment = new DayFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SplashActivity finishActivity = SplashActivity.usedForFinish;
        finishActivity.finish();

        DATA = Calendar.getInstance();
        YEAR = DATA.get(Calendar.YEAR);
        MONTH = DATA.get(Calendar.MONTH);
        DAY = DATA.get(Calendar.DATE);

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
        return YEAR;
    }

    public static int getMonth() {
        return MONTH;
    }

    public static int getDay() {
        return DAY;
    }

    public static void setYear(int year) {
        YEAR = year;
    }

    public static void setMonth(int month) {
        MONTH = month;
    }

    public static void setDay(int day) { DAY = day; }

    public static void setData() {
        DATA.set(YEAR, MONTH, DAY);
    }
}