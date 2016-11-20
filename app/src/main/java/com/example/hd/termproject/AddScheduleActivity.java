package com.example.hd.termproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {
    TextView textPickedDay;
    TextView textPickedTime;
    TextView textSubject;
    TextView textDescription;
    EditText editSubject;
    EditText editDescription;
    Button buttonPickDay;
    Button buttonPickTime;
    Button buttonSave;
    Button buttonClear;
    Button buttonCancel;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private int CALENDAR_YEAR;
    private int CALENDAR_MONTH;
    private int CALENDAR_DAY;
    private int CALENDAR_HOUR;
    private int CALENDAR_MIN;
    private Calendar CALENDAR_DATA;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        final Intent intent = getIntent();

        CALENDAR_YEAR = intent.getIntExtra("YEAR", 0);
        CALENDAR_MONTH = intent.getIntExtra("MONTH", 0);
        CALENDAR_DAY = intent.getIntExtra("DAY", 0);

        CALENDAR_DATA = Calendar.getInstance();
        CALENDAR_HOUR = CALENDAR_DATA.get(Calendar.HOUR);
        CALENDAR_MIN = CALENDAR_DATA.get(Calendar.MINUTE);

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
                System.out.println("Save button");
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                CALENDAR_YEAR = intent.getIntExtra("YEAR", 0);
                CALENDAR_MONTH = intent.getIntExtra("MONTH", 0);
                CALENDAR_DAY = intent.getIntExtra("DAY", 0);
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
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            CALENDAR_YEAR = year;
            CALENDAR_MONTH = month;
            CALENDAR_DAY = day;

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

    public void setTextSelectedDate() {
        textPickedDay.setText(CALENDAR_YEAR + "/" + (CALENDAR_MONTH + 1) + "/" + CALENDAR_DAY);
    }

    public void setTextSelectedTime() {
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
