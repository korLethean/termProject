package com.example.hd.termproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {
    private TextView textPickedDay;
    private TextView textPickedTime;
    private TextView textSubject;
    private TextView textDescription;
    private EditText editSubject;
    private EditText editDescription;
    private Button buttonPickDay;
    private Button buttonPickTime;
    private Button buttonSave;
    private Button buttonClear;
    private Button buttonCancel;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private int CALENDAR_YEAR;
    private int CALENDAR_MONTH;
    private int CALENDAR_DAY;
    private ArrayList<String> CALENDAR_DAYOFWEEK;
    private int CALENDAR_HOUR;
    private int CALENDAR_MIN;
    private Calendar CALENDAR_DATA;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        CALENDAR_YEAR = MainActivity.getYear();
        CALENDAR_MONTH = MainActivity.getMonth();
        CALENDAR_DAY = MainActivity.getDay();

        CALENDAR_DATA = Calendar.getInstance();
        CALENDAR_HOUR = CALENDAR_DATA.get(Calendar.HOUR);
        CALENDAR_MIN = CALENDAR_DATA.get(Calendar.MINUTE);

        CALENDAR_DAYOFWEEK = new ArrayList<String>();
        CALENDAR_DAYOFWEEK.add(getString(R.string.calendar_error));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SUN));
        CALENDAR_DAYOFWEEK.add(getString(R.string.MON));
        CALENDAR_DAYOFWEEK.add(getString(R.string.TUE));
        CALENDAR_DAYOFWEEK.add(getString(R.string.WED));
        CALENDAR_DAYOFWEEK.add(getString(R.string.THU));
        CALENDAR_DAYOFWEEK.add(getString(R.string.FRI));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SAT));

        textPickedDay = (TextView)findViewById(R.id.textPickedDay);
        textPickedDay.setTextColor(ContextCompat.getColor(this, R.color.BLACK));
        textPickedTime = (TextView)findViewById(R.id.textPickedTime);
        textPickedTime.setTextColor(ContextCompat.getColor(this, R.color.BLACK));
        textSubject = (TextView)findViewById(R.id.textSubject);
        editSubject = (EditText)findViewById(R.id.editSubject);
        textDescription = (TextView)findViewById(R.id.textDescription);
        editDescription = (EditText)findViewById(R.id.editDescription);
        buttonPickDay = (Button)findViewById(R.id.buttonPickday);
        buttonPickTime = (Button)findViewById(R.id.buttonPickTime);
        buttonSave = (Button)findViewById(R.id.buttonSave);
        buttonClear = (Button)findViewById(R.id.buttonClear);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);
        datePickerDialog = new DatePickerDialog(AddScheduleActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                dateSetListener, CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
        timePickerDialog = new TimePickerDialog(AddScheduleActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                timeSetListener, CALENDAR_HOUR, CALENDAR_MIN, false);

        setTextSelectedDate();
        setTextSelectedTime();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        buttonPickDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        buttonPickTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MainActivity.setYear(CALENDAR_YEAR);
                MainActivity.setMonth(CALENDAR_MONTH);
                MainActivity.setDay(CALENDAR_DAY);
                MainActivity.setData();
                Toast.makeText(AddScheduleActivity.this, getString(R.string.message_saved), Toast.LENGTH_SHORT).show();
                onBackPressed();
                finish();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CALENDAR_YEAR = MainActivity.getYear();
                CALENDAR_MONTH = MainActivity.getMonth();
                CALENDAR_DAY = MainActivity.getDay();
                CALENDAR_HOUR = CALENDAR_DATA.get(Calendar.HOUR);
                CALENDAR_MIN = CALENDAR_DATA.get(Calendar.MINUTE);
                editSubject.setText(getString(R.string.text_subject_edit));
                editDescription.setText(getString(R.string.text_description_edit));
                setTextSelectedDate();
                setTextSelectedTime();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            CALENDAR_YEAR = year;
            CALENDAR_MONTH = month;
            CALENDAR_DAY = day;
            CALENDAR_DATA.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY);
            setTextSelectedDate();
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int min) {
            CALENDAR_HOUR = hour;
            CALENDAR_MIN = min;
            setTextSelectedTime();
        }
    };

    private void setTextSelectedDate() {
        int index = CALENDAR_DATA.get(Calendar.DAY_OF_WEEK);
        textPickedDay.setText(CALENDAR_YEAR + "/" + (CALENDAR_MONTH + 1) + "/" + CALENDAR_DAY
        + " " + CALENDAR_DAYOFWEEK.get(index));
    }

    private void setTextSelectedTime() {
        int hour;
        String min;
        String ampm;

        if(CALENDAR_HOUR < 12) {
            hour = CALENDAR_HOUR;
            ampm = getString(R.string.AM);
        }
        else {
            if(CALENDAR_HOUR == 12)
                hour = CALENDAR_HOUR;
            else
                hour = CALENDAR_HOUR - 12;
            ampm = getString(R.string.PM);
        }
        if(CALENDAR_MIN < 10)
            min = "0" + String.valueOf(CALENDAR_MIN);
        else
            min = String.valueOf(CALENDAR_MIN);
        textPickedTime.setText(ampm + " " + hour + ":" + min);
    }
}
