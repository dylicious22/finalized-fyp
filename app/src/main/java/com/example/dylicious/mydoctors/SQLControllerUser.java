package com.example.dylicious.mydoctors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by dylicious on 20/01/2016.
 */
public class SQLControllerUser
{
    private DatabaseHandler dbHandler;
    private Context userContext;
    private SQLiteDatabase sqlDB;

    public SQLControllerUser(Context context)
    {
        userContext = context;
    }

    public SQLControllerUser open() throws SQLiteException
    {
        dbHandler = new DatabaseHandler(userContext);
        sqlDB = dbHandler.getWritableDatabase();

        return this;
    }

    public void close()
    {
        dbHandler.close();
    }

    public void insertUser(String username, String userMedication, String userTreatment,
                             String userAllergy)
    {
        ContentValues values = new ContentValues();

        values.put(dbHandler.KEY_NAME, username);
        values.put(dbHandler.KEY_MEDICATION, userMedication);
        values.put(dbHandler.KEY_TREATMENT, userTreatment);
        values.put(dbHandler.KEY_ALLERGY, userAllergy);

        sqlDB.insert(dbHandler.TABLE_USER, null, values);
    }

    public Cursor getAllUserData()
    {
        String[] allColumns = new String[]
                {
                        dbHandler.KEY_ID,
                        dbHandler.KEY_NAME,
                        dbHandler.KEY_MEDICATION,
                        dbHandler.KEY_TREATMENT,
                        dbHandler.KEY_ALLERGY
                };

        Cursor c = sqlDB.query(dbHandler.TABLE_USER, allColumns, null, null, null,
                null, dbHandler.KEY_NAME + " ASC");

        if (c != null)
        {
            c.moveToFirst();
        }

        return c;
    }

    public Cursor getSingleUser(String username)
    {
        String[] allUserColumns = new String[]
                {
                        dbHandler.KEY_ID,
                        dbHandler.KEY_NAME,
                        dbHandler.KEY_MEDICATION,
                        dbHandler.KEY_TREATMENT,
                        dbHandler.KEY_ALLERGY
                };

        String selection = dbHandler.KEY_NAME + " LIKE ? ";
        String[] selectionargs = {username};

        Cursor selectOneUser = sqlDB.query(dbHandler.TABLE_USER, allUserColumns, null, null, null,
                null, null);

        return selectOneUser;
    }

    public Cursor getUser(int id)
    {
        Cursor getUserCursor = sqlDB.rawQuery("SELECT * FROM User WHERE _id =" + id + "", null);
        return getUserCursor;
    }

    public int updateUser(long userID, String userName, String userTreatment, String userMedication,
                          String userAllergy)
    {
        ContentValues updateValues = new ContentValues();

        updateValues.put(dbHandler.KEY_NAME, userName);
        updateValues.put(dbHandler.KEY_MEDICATION, userMedication);
        updateValues.put(dbHandler.KEY_TREATMENT, userTreatment);
        updateValues.put(dbHandler.KEY_ALLERGY, userAllergy);

        int i = sqlDB.update(dbHandler.TABLE_USER, updateValues, dbHandler.KEY_ID + " = " +
                userID, null);

        return i;
    }

    public void deleteUser(long userID)
    {
        sqlDB.delete(dbHandler.TABLE_USER, dbHandler.KEY_ID + " = " + userID, null);
    }

}
