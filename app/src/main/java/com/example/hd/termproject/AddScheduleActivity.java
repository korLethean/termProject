package com.example.hd.termproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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
    private Button buttonReset;
    private Button buttonCancel;
    private Button buttonSave;
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

    private Button buttonImageSelect;
    private Button buttonImageDelete;
    private Button buttonVideoSelect;
    private Button buttonVideoDelete;

    private int HAS_IMAGE;
    private String IMAGE_FILE_PATH;
    private int HAS_VIDEO;
    private String VIDEO_FILE_PATH;

    private DBManager database;
    private int databaseID;

    private InputMethodManager inputMethodManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        database = new DBManager(getApplicationContext(), null);

        if(MainActivity.getMode()) {
            Intent intent = getIntent();
            databaseID = intent.getIntExtra("_id", 0);
        }

        START_CALENDAR_DATA = Calendar.getInstance();
        END_CALENDAR_DATA = Calendar.getInstance();

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
        buttonReset = (Button)findViewById(R.id.buttonAddReset);
        buttonCancel = (Button)findViewById(R.id.buttonAddCancel);
        buttonSave = (Button)findViewById(R.id.buttonAddSave);
        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        buttonImageSelect = (Button)findViewById(R.id.buttonImageSelect);
        buttonImageDelete = (Button)findViewById(R.id.buttonImageDelete);
        buttonVideoSelect = (Button)findViewById(R.id.buttonVideoSelect);
        buttonVideoDelete = (Button)findViewById(R.id.buttonVideoDelete);

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

        buttonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                initializeValues();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MainActivity.setMode(FALSE);
                onBackPressed();
                finish();
            }
        });

        buttonSave.setOnClickListener(saveListener);

        spinnerFastSetting.setOnItemSelectedListener(spinnerListener);

        buttonImageSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final FilePicker filePicker = new FilePicker(AddScheduleActivity.this).setFileListener(new FilePicker.FileSelectedListener() {
                    @Override
                    public void fileSelected(final File file) {
                        if(!file.getName().endsWith(".jpg") && !file.getName().endsWith(".png"))
                            Toast.makeText(AddScheduleActivity.this, getString(R.string.message_image_failed), Toast.LENGTH_SHORT).show();
                        else {
                            HAS_IMAGE = 1;
                            IMAGE_FILE_PATH = file.getPath();
                            buttonImageSelect.setClickable(FALSE);
                            buttonImageSelect.setText(getString(R.string.button_attached));
                            buttonImageDelete.setVisibility(View.VISIBLE);
                            Toast.makeText(AddScheduleActivity.this, getString(R.string.message_attached), Toast.LENGTH_SHORT).show();
                        }
                    };
                });
                filePicker.showDialog();
            }
        });

        buttonImageDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HAS_IMAGE = 0;
                IMAGE_FILE_PATH = null;
                buttonImageSelect.setClickable(TRUE);
                buttonImageSelect.setText(getString(R.string.button_select));
                buttonImageDelete.setVisibility(View.INVISIBLE);
                Toast.makeText(AddScheduleActivity.this, getString(R.string.message_attachment_deleted), Toast.LENGTH_SHORT).show();

            }
        });

        buttonVideoSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final FilePicker filePicker = new FilePicker(AddScheduleActivity.this).setFileListener(new FilePicker.FileSelectedListener() {
                    @Override
                    public void fileSelected(final File file) {
                        if(!file.getName().endsWith(".mp4"))
                            Toast.makeText(AddScheduleActivity.this, getString(R.string.message_video_failed), Toast.LENGTH_SHORT).show();
                        else {
                            HAS_VIDEO = 1;
                            VIDEO_FILE_PATH = file.getPath();
                            buttonVideoSelect.setClickable(FALSE);
                            buttonVideoSelect.setText(getString(R.string.button_attached));
                            buttonVideoDelete.setVisibility(View.VISIBLE);
                            Toast.makeText(AddScheduleActivity.this, getString(R.string.message_attached), Toast.LENGTH_SHORT).show();
                        }
                    };
                });
                filePicker.showDialog();
            }
        });

        buttonVideoDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HAS_VIDEO = 0;
                VIDEO_FILE_PATH = null;
                buttonVideoSelect.setClickable(TRUE);
                buttonVideoSelect.setText(getString(R.string.button_select));
                buttonVideoDelete.setVisibility(View.INVISIBLE);
                Toast.makeText(AddScheduleActivity.this, getString(R.string.message_attachment_deleted), Toast.LENGTH_SHORT).show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onResume() {
        initializeValues();
        super.onResume();
    }

    public void backgroundOnClick(View view) {
        hideKeyboard();
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                MainActivity.setMode(FALSE);
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

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
                if(MainActivity.getMode()) {
                    MainActivity.setMode(FALSE);
                    Toast.makeText(AddScheduleActivity.this, getString(R.string.message_edited), Toast.LENGTH_SHORT).show();
                    database.update(START_YEAR, START_MONTH, START_DAY, START_HOUR, START_MIN,
                            END_YEAR, END_MONTH, END_DAY, END_HOUR, END_MIN,
                            SCHEDULE_SUBJECT, SCHEDULE_PLACE, SCHEDULE_DESCRIPTION,
                            HAS_IMAGE, IMAGE_FILE_PATH, HAS_VIDEO, VIDEO_FILE_PATH, databaseID);
                }
                else {
                    Toast.makeText(AddScheduleActivity.this, getString(R.string.message_saved), Toast.LENGTH_SHORT).show();
                    database.insert(START_YEAR, START_MONTH, START_DAY, START_HOUR, START_MIN,
                            END_YEAR, END_MONTH, END_DAY, END_HOUR, END_MIN,
                            SCHEDULE_SUBJECT, SCHEDULE_PLACE, SCHEDULE_DESCRIPTION,
                            HAS_IMAGE, IMAGE_FILE_PATH, HAS_VIDEO, VIDEO_FILE_PATH);
                }
                onBackPressed();
                finish();
            }
        };

    private Spinner.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0) { // position == 0일때도 작동하기 때문에
            }                   // switch문 바깥에 있는 end_year = start_year 등의 구문은 작동함
            else {              // 이를 방지하기 위해 아무 기능을 하지 않는 if문 하나 작성
                END_YEAR = START_YEAR;
                END_MONTH = START_MONTH;
                END_DAY = START_DAY;
                END_HOUR = START_HOUR;
                END_MIN = START_MIN;
                switch (position) {
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
                if (position != 0) {
                    checkDateAndTimeError(SPINNER_CHECK);
                    setButtonTextSelectedDate(BUTTON_END);
                    endDatePickerDialog.updateDate(END_YEAR, END_MONTH, END_DAY);
                    setButtonTextSelectedTime(BUTTON_END);
                    endTimePickerDialog.updateTime(END_HOUR, END_MIN);
                }
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private void initializeValues() {
        if(MainActivity.getMode()) {
            String query = String.format("SELECT * FROM SCHEDULES WHERE _id='%d';", databaseID);
            SQLiteDatabase USE_FOR_QUERY = database.getReadableDatabase();
            Cursor cursor = USE_FOR_QUERY.rawQuery(query, null);
            cursor.moveToFirst();

            START_YEAR = cursor.getInt(cursor.getColumnIndex("startYear"));
            END_YEAR = cursor.getInt(cursor.getColumnIndex("endYear"));
            START_MONTH = cursor.getInt(cursor.getColumnIndex("startMonth"));
            END_MONTH = cursor.getInt(cursor.getColumnIndex("endMonth"));
            START_DAY = cursor.getInt(cursor.getColumnIndex("startDay"));
            END_DAY = cursor.getInt(cursor.getColumnIndex("endDay"));

            START_CALENDAR_DATA.set(START_YEAR, START_MONTH, START_DAY);
            END_CALENDAR_DATA.set(END_YEAR, END_MONTH, END_DAY);

            START_HOUR = cursor.getInt(cursor.getColumnIndex("startHour"));
            END_HOUR = cursor.getInt(cursor.getColumnIndex("endHour"));
            START_MIN = cursor.getInt(cursor.getColumnIndex("startMin"));
            END_MIN = cursor.getInt(cursor.getColumnIndex("endMin"));

            SCHEDULE_SUBJECT = cursor.getString(cursor.getColumnIndex("subject"));
            SCHEDULE_PLACE = cursor.getString(cursor.getColumnIndex("place"));
            SCHEDULE_DESCRIPTION = cursor.getString(cursor.getColumnIndex("description"));

            HAS_IMAGE =  cursor.getInt(cursor.getColumnIndex("hasImage"));
            IMAGE_FILE_PATH = cursor.getString(cursor.getColumnIndex("imagePath"));
            if(HAS_IMAGE == 1) {
                buttonImageSelect.setText(R.string.button_attached);
                buttonImageSelect.setClickable(FALSE);
                buttonImageDelete.setVisibility(View.VISIBLE);
            }
            else {
                buttonImageSelect.setClickable(TRUE);
                buttonImageDelete.setVisibility(View.INVISIBLE);
            }

            HAS_VIDEO =  cursor.getInt(cursor.getColumnIndex("hasVideo"));
            VIDEO_FILE_PATH = cursor.getString(cursor.getColumnIndex("videoPath"));
            if(HAS_VIDEO == 1) {
                buttonVideoSelect.setText(R.string.button_attached);
                buttonVideoSelect.setClickable(FALSE);
                buttonVideoDelete.setVisibility(View.VISIBLE);
            }
            else {
                buttonVideoSelect.setClickable(TRUE);
                buttonVideoDelete.setVisibility(View.INVISIBLE);
            }

            editSubject.setText(SCHEDULE_SUBJECT);
            editPlace.setText(SCHEDULE_PLACE);
            editDescription.setText(SCHEDULE_DESCRIPTION);
        }
        else {
            databaseID = -1;

            START_YEAR = MainActivity.getYear();
            END_YEAR = START_YEAR;
            START_MONTH = MainActivity.getMonth();
            END_MONTH = START_MONTH;
            START_DAY = MainActivity.getDay();
            END_DAY = START_DAY;

            START_CALENDAR_DATA.set(START_YEAR, START_MONTH, START_DAY);
            END_CALENDAR_DATA.set(END_YEAR, END_MONTH, END_DAY);

            START_HOUR = START_CALENDAR_DATA.get(Calendar.HOUR);
            END_HOUR = START_HOUR + 1;
            START_MIN = START_CALENDAR_DATA.get(Calendar.MINUTE);
            END_MIN = START_MIN;

            HAS_IMAGE = 0;
            IMAGE_FILE_PATH = null;
            HAS_VIDEO = 0;
            VIDEO_FILE_PATH = null;

            buttonImageSelect.setClickable(TRUE);
            buttonImageDelete.setVisibility(View.INVISIBLE);
            buttonVideoSelect.setClickable(TRUE);
            buttonVideoDelete.setVisibility(View.INVISIBLE);

            editSubject.setText("");
            editPlace.setText("");
            editDescription.setText("");
        }

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
                    // 시작일 = 종료일 이면서 시작시 > 종료시 또는 시작시 = 종료시 이면서 시작분 > 종료분
                    if(START_HOUR > END_HOUR || (START_HOUR == END_HOUR && START_MIN > END_MIN)) {
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
                }
                END_CALENDAR_DATA.set(END_YEAR, END_MONTH, END_DAY);
                break;
            default :
                break;
        }
    }

    private void hideKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(editSubject.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(editPlace.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(editDescription.getWindowToken(), 0);
    }
}
