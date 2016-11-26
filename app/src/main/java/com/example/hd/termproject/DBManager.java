package com.example.hd.termproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper{
    private final static String DATABASE_NAME = "Schedules.db";
    private final static int DATABASE_VERSION = 1;

    public DBManager(Context context, CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE SCHEDULES(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "startYear INTEGER NULL," +
                "startMonth INTEGER NULL," +
                "startDay INTEGER NULL," +
                "startHour INTEGER NULL," +
                "startMin INTEGER NULL," +
                "endYear INTEGER NULL," +
                "endMonth INTEGER NULL," +
                "endDay INTEGER NULL," +
                "endHour INTEGER NULL," +
                "endMin INTEGER NULL," +
                "subject TEXT NULL," +
                "place TEXT NULL," +
                "description TEXT NULL" +
                ");");
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(int sy, int sm, int sd, int sHour, int sMin, int ey, int em, int ed, int eHour, int eMin,
                       String subject, String place, String description) {
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format (
                "INSERT INTO SCHEDULES (_id, startYear, startMonth, startDay, startHour, startMin, " +
                        "endYear, endMonth, endDay, endHour, endMin, subject, place, description)\n"+
                        "VALUES (NULL, '%d', '%d', '%d', '%d', '%d', " +
                        "'%d', '%d', '%d', '%d', '%d', '%s', '%s', '%s')",
                sy, sm, sd, sHour, sMin, ey, em, ed, eHour, eMin, subject, place, description);
        db.execSQL(query);
        db.close();
    }

    public void update(int sy, int sm, int sd, int sHour, int sMin, int ey, int em, int ed, int eHour, int eMin,
                       String subject, String place, String description) {
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("UPDATE SCHEDULES SET startYear='%d' SET startMonth='%d' SET startDay='%d'"+
                "SET startHour='%d' SET startMin='%d' SET endYear='%d' SET endMonth='%d' SET endDay='%d'" +
                "SET endHour='%d' SET endMin='%d' SET place='%s' SET description='%s'"+
                                "WHERE subject='%s'",
                sy, sm, sd, sHour, sMin, ey, em, ed, eHour, eMin, place, description, subject);
        db.execSQL(query);
        db.close();
    }

    public void delete(String query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public int getYear(int day) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT year FROM SCHEDULES WHERE startDay='%d';", day);
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getInt(0);
    }

    public int getDuration(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM SCHEDULES WHERE _id='%d';", id);
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getInt(0);
    }

    public String PrintStartData(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";
        String query = String.format("SELECT startHour, startMin, subject, place, description FROM SCHEDULES " +
                                    "WHERE startYear='%d' AND startMonth='%d' AND startDay='%d';",
                year, month, day);
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            String minutes = String.valueOf(cursor.getInt(2));
            if(cursor.getInt(2) < 10)
                minutes  = "0" + minutes;

            str += "[START] "
                    + cursor.getInt(0)
                    + " : " + minutes
                    + " " + cursor.getString(2)
                    + " at " + cursor.getString(3)
                    + " / " + cursor.getString(4)
                    + "\n";
        }
        return str;
    }

    public String PrintEndData(int year, int month, int day) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";
        String query = String.format("SELECT endHour, endMin, subject, place, description FROM SCHEDULES " +
                        "WHERE endYear='%d' AND endMonth='%d' AND endDay='%d';",
                year, month, day);
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
            String minutes = String.valueOf(cursor.getInt(2));
            if(cursor.getInt(2) < 10)
                minutes  = "0" + minutes;

            str += "[END] "
                    + cursor.getInt(0)
                    + " : " + minutes
                    + " " + cursor.getString(2)
                    + " at " + cursor.getString(3)
                    + " / " + cursor.getString(4)
                    + "\n";
        }
        return str;
    }
}
