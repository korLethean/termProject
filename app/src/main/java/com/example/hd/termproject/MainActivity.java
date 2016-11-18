package com.example.hd.termproject;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private Calendar CALENDAR_DATA;
    private int CALENDAR_YEAR;
    private int CALENDAR_MONTH;
    private int CALENDAR_DAY;

    private int FRAGMENT_MANAGER;
    final MonthFragment monthFragment = new MonthFragment();
//    final WeekFragment weekFragment = new WeekFragment();
//    final DayFragment dayFragment = new DayFragment();

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

        Bundle arguments = new Bundle();
        arguments.putInt("YEAR", CALENDAR_YEAR);
        arguments.putInt("MONTH", CALENDAR_MONTH);
        arguments.putInt("DAY", CALENDAR_DAY);

        FRAGMENT_MANAGER = -1;
        switchFragment(0);
        monthFragment.setArguments(arguments);
        //weekFragment.setArguments(arguments);
        //dayFragment.setArguments(arguments);

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

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}