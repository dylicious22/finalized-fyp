package com.example.dylicious.mydoctors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.sql.SQLException;

/**
 * Created by dylicious on 18/01/2016.
 */
public class SQLControllerDoctor
{
    private DatabaseHandler dbHandler;
    private Context docContext;
    private SQLiteDatabase sqlDB;

    public SQLControllerDoctor(Context context)
    {
        docContext = context;
    }

    public SQLControllerDoctor open() throws SQLiteException
    {
        dbHandler = new DatabaseHandler(docContext);
        sqlDB = dbHandler.getWritableDatabase();

        return this;
    }

    public void close()
    {
        dbHandler.close();
    }

    public void insertDoctor(String docname, String docadd, String docloc, String docspecialty,
                             String docnum, String doctime, String docendtime, String docremarks)
    {
        ContentValues values = new ContentValues();

        values.put(dbHandler.KEY_DOCNAME, docname);
        values.put(dbHandler.KEY_ADDRESS, docadd);
        values.put(dbHandler.KEY_LOCATION, docloc);
        values.put(dbHandler.KEY_SPECIALTY, docspecialty);
        values.put(dbHandler.KEY_PHONE, docnum);
        values.put(dbHandler.KEY_TIME, doctime);
        values.put(dbHandler.KEY_ENDTIME, docendtime);
        values.put(dbHandler.KEY_REMARKS, docremarks);

        sqlDB.insert(dbHandler.TABLE_DOCTOR, null, values);
    }

    public Cursor getAllDocData()
    {
        String[] allColumns = new String[]
                {
                        dbHandler.KEY_DOCID,
                        dbHandler.KEY_DOCNAME,
                        dbHandler.KEY_ADDRESS,
                        dbHandler.KEY_LOCATION,
                        dbHandler.KEY_SPECIALTY,
                        dbHandler.KEY_PHONE,
                        dbHandler.KEY_TIME,
                        dbHandler.KEY_ENDTIME,
                        dbHandler.KEY_REMARKS
                };

        Cursor c = sqlDB.query(dbHandler.TABLE_DOCTOR, allColumns, null, null, null, null,
                dbHandler.KEY_DOCNAME + " ASC");

        if (c != null)
        {
            c.moveToFirst();
        }

        return c;
    }

    public int updateDoctor(long doctorID, String docname, String docadd, String docloc,
                            String docspecialty, String docnum, String doctime, String docendtime,
                            String docremarks)
    {
        ContentValues updateValues = new ContentValues();

        updateValues.put(dbHandler.KEY_DOCNAME, docname);
        updateValues.put(dbHandler.KEY_ADDRESS, docadd);
        updateValues.put(dbHandler.KEY_LOCATION, docloc);
        updateValues.put(dbHandler.KEY_SPECIALTY, docspecialty);
        updateValues.put(dbHandler.KEY_PHONE, docnum);
        updateValues.put(dbHandler.KEY_TIME, doctime);
        updateValues.put(dbHandler.KEY_ENDTIME, docendtime);
        updateValues.put(dbHandler.KEY_REMARKS, docremarks);

        int i = sqlDB.update(dbHandler.TABLE_DOCTOR, updateValues, dbHandler.KEY_DOCID + " = " +
                doctorID, null);

        return i;
    }

    public void deleteDoctor(long doctorID)
    {
        sqlDB.delete(dbHandler.TABLE_DOCTOR, dbHandler.KEY_DOCID + " = " + doctorID, null);
    }


}
