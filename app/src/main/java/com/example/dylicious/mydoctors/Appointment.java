package com.example.dylicious.mydoctors;

import com.orm.SugarRecord;

/**
 * Created by dylicious on 10/20/15.
 */
public class Appointment extends SugarRecord<Appointment> {
    int _id;
    String _appname;
    String _apptime;
    String _appdate;
    String _appdesc;

    //default constructor
    public Appointment() {
    }

    //without time and date
    public Appointment(int id, String appname, String appdesc) {
        this._id = id;
        this._appname = appname;
        this._appdesc = appdesc;
    }

    //with time and date
    public Appointment(int id, String appname, String apptime, String appdate, String appdesc) {
        this._id = id;
        this._appname = appname;
        this._apptime = apptime;
        this._appdate = appdate;
        this._appdesc = appdesc;
    }

    public int getID()
    {
        return this._id;
    }

    public void setID(int id)
    {
        this._id = id;
    }

    public String getAppName()
    {
        return this._appname;
    }

    public void setAppName(String appname)
    {
        this._appname = appname;
    }

    public String getAppTime() {
        return this._apptime;
    }

    public void setAppTime(String apptime) {
        this._apptime = apptime;
    }

    public String getAppDate() {
        return this._appdate;
    }

    public void setAppDate(String appdate) {
        this._appdate = appdate;
    }

    public String getAppDesc()
    {
        return this._appdesc;
    }

    public void setAppDesc(String appdesc)
    {
        this._appdesc = appdesc;
    }
}
