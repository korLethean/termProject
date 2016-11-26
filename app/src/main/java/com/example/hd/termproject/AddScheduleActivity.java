package com.example.hd.termproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {
    private Button buttonPickStartDay;
    private DatePickerDialog startDatePickerDialog;
    private Button buttonPickStartTime;
    private TimePickerDialog startTimePickerDialog;
    private Button buttonPickEndDay;
    private DatePickerDialog endDatePickerDialog;
    private Button buttonPickEndTime;
    private TimePickerDialog endTimePickerDialog;
    private Spinner spinnerFastSetting;
    private EditText editSubject;
    private EditText editPlace;
    private EditText editDescription;
    private Button buttonSave;
    private Button buttonClear;
    private Button buttonCancel;
    private final int BUTTON_START = 0;
    private final int BUTTON_END = 1;
    private final int SPINNER_CHECK = 2;

    private int START_YEAR;
    private int START_MONTH;
    private int START_DAY;
    private int START_HOUR;
    private int START_MIN;
    private Calendar START_CALENDAR_DATA;
    private int END_YEAR;
    private int END_MONTH;
    private int END_DAY;
    private int END_HOUR;
    private int END_MIN;
    private Calendar END_CALENDAR_DATA;
    private ArrayList<String> CALENDAR_DAYOFWEEK;
    private String SCHEDULE_SUBJECT;
    private String SCHEDULE_PLACE;
    private String SCHEDULE_DESCRIPTION;

    private DBManager database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        database = new DBManager(getApplicationContext(), null);

        START_YEAR = MainActivity.getYear();
        END_YEAR = START_YEAR;
        START_MONTH = MainActivity.getMonth();
        END_MONTH = START_MONTH;
        START_DAY = MainActivity.getDay();
        END_DAY = START_DAY;

        START_CALENDAR_DATA = Calendar.getInstance();
        START_CALENDAR_DATA.set(START_YEAR, START_MONTH, START_DAY);
        END_CALENDAR_DATA = Calendar.getInstance();
        END_CALENDAR_DATA.set(END_YEAR, END_MONTH, END_DAY);

        START_HOUR = START_CALENDAR_DATA.get(Calendar.HOUR);
        END_HOUR = START_HOUR + 1;
        START_MIN = START_CALENDAR_DATA.get(Calendar.MINUTE);
        END_MIN = START_MIN;

        CALENDAR_DAYOFWEEK = new ArrayList<String>();
        CALENDAR_DAYOFWEEK.add(getString(R.string.calendar_error));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SUN));
        CALENDAR_DAYOFWEEK.add(getString(R.string.MON));
        CALENDAR_DAYOFWEEK.add(getString(R.string.TUE));
        CALENDAR_DAYOFWEEK.add(getString(R.string.WED));
        CALENDAR_DAYOFWEEK.add(getString(R.string.THU));
        CALENDAR_DAYOFWEEK.add(getString(R.string.FRI));
        CALENDAR_DAYOFWEEK.add(getString(R.string.SAT));

        spinnerFastSetting = (Spinner)findViewById(R.id.spinnerFastSetting);
        String[] spinnerResources = getResources().getStringArray(R.array.settings);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, spinnerResources);
        spinnerFastSetting.setAdapter(spinnerAdapter);

        buttonPickStartDay = (Button)findViewById(R.id.buttonPickStartDay);
        buttonPickStartTime = (Button)findViewById(R.id.buttonPickStartTime);
        setButtonTextSelectedDate(BUTTON_START);
        setButtonTextSelectedTime(BUTTON_START);
        startDatePickerDialog = new DatePickerDialog(AddScheduleActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        START_YEAR = year;
                        START_MONTH = month;
                        START_DAY = day;
                        START_CALENDAR_DATA.set(START_YEAR, START_MONTH, START_DAY);
                        checkDateAndTimeError(BUTTON_START);
                    }
                }, START_YEAR, START_MONTH, START_DAY);
        startTimePickerDialog = new TimePickerDialog(AddScheduleActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        START_HOUR = hour;
                        START_MIN = min;
                        checkDateAndTimeError(BUTTON_START);
                    }
                }, START_HOUR, START_MIN, false);
        buttonPickEndDay = (Button)findViewById(R.id.buttonPickEndDay);
        buttonPickEndTime = (Button)findViewById(R.id.buttonPickEndTime);
        setButtonTextSelectedDate(BUTTON_END);
        setButtonTextSelectedTime(BUTTON_END);
        endDatePickerDialog = new DatePickerDialog(AddScheduleActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        END_YEAR = year;
                        END_MONTH = month;
                        END_DAY = day;
                        END_CALENDAR_DATA.set(END_YEAR, END_MONTH, END_DAY);
                        checkDateAndTimeError(BUTTON_END);
                    }
                }, END_YEAR, END_MONTH, END_DAY);

        endTimePickerDialog = new TimePickerDialog(AddScheduleActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        END_HOUR = hour;
                        END_MIN = min;
                        checkDateAndTimeError(BUTTON_END);
                    }
                }, END_HOUR, END_MIN, false);
        editSubject = (EditText)findViewById(R.id.editSubject);
        editPlace = (EditText)findViewById(R.id.editPlace);
        editDescription = (EditText)findViewById(R.id.editDescription);
        buttonSave = (Button)findViewById(R.id.buttonSave);
        buttonClear = (Button)findViewById(R.id.buttonClear);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        buttonPickStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatePickerDialog.show();
                spinnerFastSetting.setSelection(0);
            }
        });

        buttonPickStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimePickerDialog.show();
                spinnerFastSetting.setSelection(0);
            }
        });

        buttonPickEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDatePickerDialog.show();
                spinnerFastSetting.setSelection(0);
            }
        });

        buttonPickEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimePickerDialog.show();
                spinnerFastSetting.setSelection(0);
            }
        });

        buttonSave.setOnClickListener(saveListener);

        buttonClear.setOnClickListener(clearListener);

        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        spinnerFastSetting.setOnItemSelectedListener(spinnerListener);
    }

    private Button.OnClickListener saveListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setYear(START_YEAR);
                MainActivity.setMonth(START_MONTH);
                MainActivity.setDay(START_DAY);
                MainActivity.setData();
                SCHEDULE_SUBJECT = editSubject.getText().toString();
                SCHEDULE_PLACE = editPlace.getText().toString();
                SCHEDULE_DESCRIPTION = editDescription.getText().toString();
                Toast.makeText(AddScheduleActivity.this, getString(R.string.message_saved), Toast.LENGTH_SHORT).show();
                database.insert(START_YEAR, START_MONTH, START_DAY, START_HOUR, START_MIN,
                        END_YEAR, END_MONTH, END_DAY, END_HOUR, END_MIN,
                        SCHEDULE_SUBJECT, SCHEDULE_PLACE, SCHEDULE_DESCRIPTION);
                onBackPressed();
                finish();
            }
        };

    private Button.OnClickListener clearListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            START_YEAR = MainActivity.getYear();
            END_YEAR = START_YEAR;
            START_MONTH = MainActivity.getMonth();
            END_MONTH = START_MONTH;
            START_DAY = MainActivity.getDay();
            END_DAY = START_DAY;
            START_HOUR = START_CALENDAR_DATA.get(Calendar.HOUR);
            END_HOUR = START_HOUR + 1;
            START_MIN = START_CALENDAR_DATA.get(Calendar.MINUTE);
            END_MIN = START_MIN;
            START_CALENDAR_DATA.set(START_YEAR, START_MONTH, START_DAY);
            END_CALENDAR_DATA.set(END_YEAR, END_MONTH, END_DAY);
            editSubject.setText(getString(R.string.text_subject_edit));
            editDescription.setText(getString(R.string.text_description_edit));
            setButtonTextSelectedDate(BUTTON_START);
            startDatePickerDialog.updateDate(START_YEAR, START_MONTH, START_DAY);
            setButtonTextSelectedDate(BUTTON_END);
            endDatePickerDialog.updateDate(END_YEAR, END_MONTH, END_DAY);
            setButtonTextSelectedTime(BUTTON_START);
            startTimePickerDialog.updateTime(START_HOUR, START_MIN);
            setButtonTextSelectedTime(BUTTON_END);
            endTimePickerDialog.updateTime(END_HOUR, END_MIN);
            spinnerFastSetting.setSelection(0);
        }
    };

    private Spinner.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            END_YEAR = START_YEAR;
            END_MONTH = START_MONTH;
            END_DAY = START_DAY;
            END_HOUR = START_HOUR;
            END_MIN = START_MIN;
            switch(position) {
                case 1:
                    END_MIN += 10;
                    break;
                case 2:
                    END_MIN += 30;
                    break;
                case 3:
                    END_HOUR += 1;
                    break;
                case 4:
                    END_HOUR += 2;
                    break;
                case 5:
                    END_HOUR += 3;
                    break;
                case 6:
                    END_HOUR += 4;
                    break;
                case 7:
                    END_HOUR += 5;
                    break;
                case 8:
                    END_HOUR += 8;
                    break;
                case 9:
                    END_HOUR += 12;
                    break;
                case 10:
                    START_HOUR = 0;
                    START_MIN = 0;
                    END_HOUR = 23;
                    END_MIN = 59;
                    setButtonTextSelectedTime(BUTTON_START);
                    startTimePickerDialog.updateTime(START_HOUR, START_MIN);
                    break;
                default:
                    break;
            }
            if(position != 0) {
                checkDateAndTimeError(SPINNER_CHECK);
                setButtonTextSelectedDate(BUTTON_END);
                endDatePickerDialog.updateDate(END_YEAR, END_MONTH, END_DAY);
                setButtonTextSelectedTime(BUTTON_END);
                endTimePickerDialog.updateTime(END_HOUR, END_MIN);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

    private void setButtonTextSelectedDate(int mode) {
        int index;
        switch(mode) {
            case BUTTON_START:
                index = START_CALENDAR_DATA.get(Calendar.DAY_OF_WEEK);
                buttonPickStartDay.setText(START_YEAR + "/" + (START_MONTH + 1) + "/"
                        + START_DAY + " " + CALENDAR_DAYOFWEEK.get(index));
                break;
            case BUTTON_END:
                index = END_CALENDAR_DATA.get(Calendar.DAY_OF_WEEK);
                buttonPickEndDay.setText(END_YEAR + "/" + (END_MONTH + 1) + "/"
                        + END_DAY + " " + CALENDAR_DAYOFWEEK.get(index));
                break;
            default :
                break;
        }
    }

    private void setButtonTextSelectedTime(int mode) {
        int hour;
        String min;
        String ampm;

        switch(mode) {
            case BUTTON_START:
                if(START_HOUR < 12) {
                    hour = START_HOUR;
                    ampm = getString(R.string.AM);
                }
                else {
                    if(START_HOUR == 12)
                        hour = START_HOUR;
                    else
                        hour = START_HOUR - 12;
                    ampm = getString(R.string.PM);
                }
                if(START_MIN < 10)
                    min = "0" + String.valueOf(START_MIN);
                else
                    min = String.valueOf(START_MIN);
                buttonPickStartTime.setText(ampm + " " + hour + ":" + min);
                break;
            case BUTTON_END:
                if(END_HOUR < 12) {
                    hour = END_HOUR;
                    ampm = getString(R.string.AM);
                }
                else {
                    if(END_HOUR == 12)
                        hour = END_HOUR;
                    else
                        hour = END_HOUR - 12;
                    ampm = getString(R.string.PM);
                }
                if(END_MIN < 10)
                    min = "0" + String.valueOf(END_MIN);
                else
                    min = String.valueOf(END_MIN);
                buttonPickEndTime.setText(ampm + " " + hour + ":" + min);
                break;
            default :
                break;
        }
    }

    private void checkDateAndTimeError(int mode) {
        switch(mode) {
            case BUTTON_START:
                if(START_CALENDAR_DATA.getTimeInMillis() > END_CALENDAR_DATA.getTimeInMillis()){
                    END_YEAR = START_YEAR;  // 시작일 > 종료일
                    END_MONTH = START_MONTH;
                    END_DAY = START_DAY;
                    END_HOUR = START_HOUR;
                    END_MIN = START_MIN;
                    END_CALENDAR_DATA.set(END_YEAR, END_MONTH, END_DAY);
                    setButtonTextSelectedDate(BUTTON_END);
                    endDatePickerDialog.updateDate(END_YEAR, END_MONTH, END_DAY);
                    setButtonTextSelectedTime(BUTTON_END);
                    endTimePickerDialog.updateTime(END_HOUR, END_MIN);
                }
                else if(START_CALENDAR_DATA.getTimeInMillis() == END_CALENDAR_DATA.getTimeInMillis()) {
                    if (START_HOUR > END_HOUR || (START_HOUR == END_HOUR && START_MIN > END_MIN)) {
                        END_HOUR = START_HOUR;  // 시작일 = 종료일 이면서 시작시 > 종료시 또는 시작시 = 종료시 이면서 시작분 > 종료분
                        END_MIN = START_MIN;
                        setButtonTextSelectedTime(BUTTON_END);
                        endTimePickerDialog.updateTime(END_HOUR, END_MIN);
                    }
                }
                setButtonTextSelectedDate(BUTTON_START);
                setButtonTextSelectedTime(BUTTON_START);
                break;
            case BUTTON_END:
                if(START_CALENDAR_DATA.getTimeInMillis() > END_CALENDAR_DATA.getTimeInMillis()) {
                    END_YEAR = START_YEAR;  // 시작일 > 종료일
                    END_MONTH = START_MONTH;
                    END_DAY = START_DAY;
                    END_HOUR = START_HOUR;
                    END_MIN = START_MIN;
                    END_CALENDAR_DATA.set(END_YEAR, END_MONTH, END_DAY);
                    Toast.makeText(AddScheduleActivity.this, getString(R.string.message_unable_set), Toast.LENGTH_SHORT).show();
                    endTimePickerDialog.updateTime(END_HOUR, END_MIN);
                    endDatePickerDialog.updateDate(END_YEAR, END_MONTH, END_DAY);
                }
                else if(START_CALENDAR_DATA.getTimeInMillis() == END_CALENDAR_DATA.getTimeInMillis()){
                    // 시작일 = 종료일 이면서 시작시 > 종료시 또는 시작시 = 종료시 이면서 시작분 >= 종료분
                    if(START_HOUR > END_HOUR || (START_HOUR == END_HOUR &&
                            (START_MIN > END_MIN || START_MIN == END_MIN))) {
                        END_HOUR = START_HOUR;
                        END_MIN = START_MIN;
                        Toast.makeText(AddScheduleActivity.this, getString(R.string.message_unable_set), Toast.LENGTH_SHORT).show();
                        endTimePickerDialog.updateTime(END_HOUR, END_MIN);
                    }
                }
                setButtonTextSelectedTime(BUTTON_END);
                setButtonTextSelectedDate(BUTTON_END);
                break;
            case SPINNER_CHECK:
                if(END_MIN > 60 || END_MIN == 60) {
                    END_MIN -= 60;
                    END_HOUR += 1;
                }
                if(END_HOUR > 24 || END_HOUR == 24) {
                    END_HOUR -= 24;
                    END_DAY += 1;
                    if(END_DAY > END_CALENDAR_DATA.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        END_DAY = 1;
                        END_MONTH += 1;
                        if(END_MONTH > 11) {
                            END_MONTH = 0;
                            END_YEAR += 1;
                        }
                    }
                    END_CALENDAR_DATA.set(END_YEAR, END_MONTH, END_DAY);
                }
                break;
            default :
                break;
        }
    }
}
