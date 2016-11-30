package com.example.hd.termproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Boolean.TRUE;

public class DetailScheduleActivity extends AppCompatActivity {
    private TextView textDetailSubject;
    private TextView textDetailPlace;
    private TextView textDetailStartDate;
    private TextView textDetailEndDate;
    private TextView textDetailDescription;
    private Button buttonDetailEdit;
    private Button buttonDetailDelete;
    private Button buttonDetailClose;

    private Intent activityIntent;

    private int databaseID;
    private int START_YEAR;
    private int START_MONTH;
    private int START_DAY;
    private int START_HOUR;
    private int START_MIN;
    private String START_MIN_STRING;
    private int END_YEAR;
    private int END_MONTH;
    private int END_DAY;
    private int END_HOUR;
    private int END_MIN;
    private String END_MIN_STRING;
    private String SCHEDULE_SUBJECT;
    private String SCHEDULE_PLACE;
    private String SCHEDULE_DESCRIPTION;

    private DBManager database;
    SQLiteDatabase USE_FOR_QUERY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_schedule);
        database = new DBManager(getApplicationContext(), null);
        USE_FOR_QUERY = database.getReadableDatabase();

        activityIntent = getIntent();
        databaseID = activityIntent.getIntExtra("_id", 0);

        textDetailSubject = (TextView)findViewById(R.id.textDetailSubject);
        textDetailPlace = (TextView)findViewById(R.id.textDetailPlace);
        textDetailStartDate = (TextView)findViewById(R.id.textDetailStartDate);
        textDetailEndDate = (TextView)findViewById(R.id.textDetailEndDate);
        textDetailDescription = (TextView)findViewById(R.id.textDetailDescription);

        buttonDetailEdit = (Button)findViewById(R.id.buttonDetailEdit);
        buttonDetailDelete = (Button)findViewById(R.id.buttonDetailDelete);
        buttonDetailClose = (Button)findViewById(R.id.buttonDetailClose);

        buttonDetailEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddScheduleActivity.class);
                intent.putExtra("_id", databaseID);
                MainActivity.setMode(TRUE);
                startActivity(intent);
            }
        });

        buttonDetailDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailScheduleActivity.this);
                builder.setTitle(getString(R.string.alert_delete_title))
                        .setMessage(getString(R.string.alert_delete_description))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.alert_delete_confirm),
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int whichButton){
                                        USE_FOR_QUERY.delete("SCHEDULES", "_id=?", new String[]{String.valueOf(databaseID)});
                                        dialog.cancel();
                                        Toast.makeText(DetailScheduleActivity.this, getString(R.string.message_deleted), Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                })
                        .setNegativeButton(getString(R.string.alert_delete_cancel),
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int whichButton){
                                        dialog.cancel();
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonDetailClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
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

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    };

    private void initializeValues() {
        String query = String.format("SELECT * FROM SCHEDULES WHERE _id='%d';", databaseID);
        Cursor cursor = USE_FOR_QUERY.rawQuery(query, null);
        cursor.moveToFirst();

        START_YEAR = cursor.getInt(cursor.getColumnIndex("startYear"));
        START_MONTH = cursor.getInt(cursor.getColumnIndex("startMonth"));
        START_DAY = cursor.getInt(cursor.getColumnIndex("startDay"));
        START_HOUR = cursor.getInt(cursor.getColumnIndex("startHour"));
        START_MIN = cursor.getInt(cursor.getColumnIndex("startMin"));
        END_YEAR = cursor.getInt(cursor.getColumnIndex("endYear"));
        END_MONTH = cursor.getInt(cursor.getColumnIndex("endMonth"));
        END_DAY = cursor.getInt(cursor.getColumnIndex("endDay"));
        END_HOUR = cursor.getInt(cursor.getColumnIndex("endHour"));
        END_MIN = cursor.getInt(cursor.getColumnIndex("endMin"));
        SCHEDULE_SUBJECT = cursor.getString(cursor.getColumnIndex("subject"));
        SCHEDULE_PLACE = cursor.getString(cursor.getColumnIndex("place"));
        SCHEDULE_DESCRIPTION = cursor.getString(cursor.getColumnIndex("description"));

        if (START_MIN < 10)
            START_MIN_STRING = String.valueOf("0" + START_MIN);
        else
            START_MIN_STRING = String.valueOf(START_MIN);

        if (END_MIN < 10)
            END_MIN_STRING = String.valueOf("0" + END_MIN);
        else
            END_MIN_STRING = String.valueOf(END_MIN);

        String start = String.format("%d/%d/%d\t %d : %s", START_YEAR, START_MONTH + 1, START_DAY, START_HOUR, START_MIN_STRING);
        String end = String.format("%d/%d/%d\t %d : %s", END_YEAR, END_MONTH + 1, END_DAY, END_HOUR, END_MIN_STRING);

        textDetailSubject.setText(SCHEDULE_SUBJECT);
        textDetailPlace.setText(SCHEDULE_PLACE);
        textDetailStartDate.setText(start);
        textDetailEndDate.setText(end);
        textDetailDescription.setText(SCHEDULE_DESCRIPTION);
    }
}
