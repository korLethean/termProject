package com.example.hd.termproject;

import android.content.Context;
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
                "description TEXT NULL," +
                "hasImage INTEGER NULL," +
                "imagePath TEXT NULL," +
                "hasVideo INTEGER NULL," +
                "videoPath TEXT NULL" +
                ");");
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(int sy, int sm, int sd, int sHour, int sMin, int ey, int em, int ed, int eHour, int eMin,
                       String subject, String place, String description, int hi, String ip, int hv, String vp) {
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format (
                "INSERT INTO SCHEDULES (_id, startYear, startMonth, startDay, startHour, startMin, " +
                        "endYear, endMonth, endDay, endHour, endMin, subject, place, description, " +
                        "hasImage, imagePath, hasVideo, videoPath)\n"+
                        "VALUES (NULL, '%d', '%d', '%d', '%d', '%d', " +
                        "'%d', '%d', '%d', '%d', '%d', '%s', '%s', '%s', '%d', '%s', '%d', '%s')",
                sy, sm, sd, sHour, sMin, ey, em, ed, eHour, eMin, subject, place, description, hi, ip, hv, vp);
        db.execSQL(query);
        db.close();
    }

    public void update(int sy, int sm, int sd, int sHour, int sMin, int ey, int em, int ed, int eHour, int eMin,
                       String subject, String place, String description, int hi, String ip, int hv, String vp, int dbID) {
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("UPDATE SCHEDULES SET startYear='%d', startMonth='%d', startDay='%d', "+
                "startHour='%d', startMin='%d', endYear='%d', endMonth='%d', endDay='%d', " +
                "endHour='%d', endMin='%d', subject='%s', place='%s', description='%s', " +
                "hasImage='%d', imagePath='%s', hasVideo='%d', videoPath='%s' "+
                                "WHERE _id='%d'",
                sy, sm, sd, sHour, sMin, ey, em, ed, eHour, eMin, subject, place, description, hi, ip, hv, vp, dbID);
        db.execSQL(query);
        db.close();
    }

    public void delete(String query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
}
