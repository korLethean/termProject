package com.example.hd.termproject;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    Calendar CALENDAR_DATA;
    int CALENDAR_YEAR;
    int CALENDAR_MONTH;
    int CALENDAR_DAY;

    static int FRAGMENT_MANAGER;

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
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    final MonthFragment monthFragment = new MonthFragment();
//    final WeekFragment weekFragment = new WeekFragment();
 //   final DayFragment dayFragment = new DayFragment();

    protected void switchFragment(int id){
        Bundle arguments = new Bundle();
        arguments.putInt("YEAR", CALENDAR_YEAR);
        arguments.putInt("MONTH", CALENDAR_MONTH);
        arguments.putInt("DAY", CALENDAR_DAY);

        final FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        if(id == 0 && FRAGMENT_MANAGER != 0) {
            FRAGMENT_MANAGER = 0;
            monthFragment.setArguments(arguments);
            fragmentTransaction.replace(R.id.activity_main, monthFragment);
        }
        else if(id == 1 && FRAGMENT_MANAGER != 1) {
//            fragmentTransaction.replace(R.id.activity_main, monthFragment);
        }
        else if(id == 2 && FRAGMENT_MANAGER != 2) {
//            fragmentTransaction.replace(R.id.activity_main, monthFragment);
        }
        else if(id == FRAGMENT_MANAGER) {
            System.out.println("already active");
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}