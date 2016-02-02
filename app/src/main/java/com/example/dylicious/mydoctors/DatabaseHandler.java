package com.example.dylicious.mydoctors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.dom.DOMLocator;


public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydoctors.db";
    public static final String TABLE_USER = "User";
    public static final String TABLE_DOCTOR = "Doctor";

    //Columns for Patients
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "pat_name";
    public static final String KEY_TREATMENT = "pat_treat";
    public static final String KEY_MEDICATION = "pat_med";
    public static final String KEY_ALLERGY = "pat_allergy";

    //Columns for Doctors
    public static final String KEY_DOCID = "_id";
    public static final String KEY_DOCNAME = "docName";
    public static final String KEY_ADDRESS = "docAdd";
    public static final String KEY_LOCATION = "docLoc";
    public static final String KEY_SPECIALTY = "docSpecialty";
    public static final String KEY_PHONE = "docPhone";
    public static final String KEY_TIME = "docTime";
    public static final String KEY_ENDTIME = "docEndTime";
    public static final String KEY_REMARKS = "docRemarks";

    public Context dbContext;
    public SQLiteDatabase sqlDB;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.e("DATABASE OPERATIONS", "DATABASE CREATED/OPENED...");
    }

    public DatabaseHandler open() throws SQLException {
        DatabaseHandler db = new DatabaseHandler(dbContext);
        sqlDB = db.getWritableDatabase();
        return this;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT NOT NULL, "
                + KEY_TREATMENT + " TEXT NOT NULL, "
                + KEY_MEDICATION + " TEXT NOT NULL, "
                + KEY_ALLERGY + " TEXT NOT NULL "
                + " );";

        db.execSQL(CREATE_USER_TABLE);

        String CREATE_DOCTOR_TABLE = "CREATE TABLE " + TABLE_DOCTOR +
                "("
                + KEY_DOCID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DOCNAME + " TEXT NOT NULL, "
                + KEY_ADDRESS + " TEXT NOT NULL, "
                + KEY_LOCATION + " TEXT NOT NULL, "
                + KEY_SPECIALTY + " TEXT NOT NULL, "
                + KEY_PHONE + " TEXT NOT NULL, "
                + KEY_TIME + " TEXT NOT NULL, "
                + KEY_ENDTIME + " TEXT NOT NULL, "
                + KEY_REMARKS + " TEXT NOT NULL "
                + " );";
        db.execSQL(CREATE_DOCTOR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR);
        onCreate(db);
    }
}