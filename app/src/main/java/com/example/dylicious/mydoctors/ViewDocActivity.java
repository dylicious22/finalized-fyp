package com.example.dylicious.mydoctors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.database.sqlite.*;
import android.widget.Toast;

public class ViewDocActivity extends Activity
{
    EditText inputDocName;
    EditText inputDocAddress;
    AutoCompleteTextView inputLoc, inputSpecialty;
    EditText inputPhoneNumber;
    EditText inputTime;
    EditText inputEndTime;
    EditText inputRemarks;
    Calendar myTime;
    SQLControllerDoctor sqlControlDoc;
    AlertDialog.Builder exitDialog;
    String [] docSpecList = {"Allergist", "Dermatologist", "Internal Medicine Physician",
            "Gynecologist", "General Physician", "Cardiologist", "Chemotherapist", "Neurologist",
            "Ophtalmologist", "Pathologist", "Psychatrist", "Urologist", "Rheumatologist",
            "Radiation Oncologist", "Pediatrician"};
    String [] docLocList = {"Terengganu", "Kelantan", "Perak", "Malacca", "Perlis", "Kedah",
            "Penang", "Sabah", "Sarawak", "Selangor", "Wilayah Persekutuan", "Negeri Sembilan",
            "Johor", "Pahang"};

    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doc);

        //Database Handler initiator
        sqlControlDoc = new SQLControllerDoctor(this);
        sqlControlDoc.open();

        //set format for date
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        //edittext assign id
        inputDocName = (EditText)findViewById(R.id.docname);
        inputDocAddress = (EditText)findViewById(R.id.docadd);
        inputLoc = (AutoCompleteTextView)findViewById(R.id.docloc);
        inputSpecialty = (AutoCompleteTextView)findViewById(R.id.docspecialty);
        inputPhoneNumber = (EditText)findViewById(R.id.docnum);
        inputTime = (EditText)findViewById(R.id.doctime);
        inputEndTime = (EditText)findViewById(R.id.docendtime);
        inputRemarks = (EditText)findViewById(R.id.docremarks);

        ArrayAdapter specAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                docSpecList);
        ArrayAdapter locAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                docLocList);
        inputLoc.setAdapter(locAdapter);
        inputLoc.setThreshold(1);

        inputSpecialty.setAdapter(specAdapter);
        inputSpecialty.setThreshold(1);



        //timepicker dialog
        myTime = Calendar.getInstance();
        inputTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int hour = myTime.get(Calendar.HOUR_OF_DAY);
                int min = myTime.get(Calendar.MINUTE);
                TimePickerDialog myTimePickerDialog = new TimePickerDialog(ViewDocActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        inputTime.setText(String.format("%02d:%02d", hourOfDay, minute));
//                        inputTimeString = inputTime.toString();
                    }
                }, hour, min, true);

                myTimePickerDialog.setTitle("Select Time");
                myTimePickerDialog.show();
            }
        });

        inputEndTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int hour = myTime.get(Calendar.HOUR_OF_DAY);
                int min = myTime.get(Calendar.MINUTE);
                TimePickerDialog myEndTimePickerDialog = new TimePickerDialog(ViewDocActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                            {
                                inputEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                try
                                {
                                    Date startTimeDate = format.parse(inputTime.getText().toString());
                                    Date endTimeDate = format.parse(inputEndTime.getText().toString());

                                    if (startTimeDate.compareTo(endTimeDate) >= 0)
                                    {
                                        Context timeContext = getApplicationContext();
                                        CharSequence text = "Please enter the correct end time";
                                        int duration = Toast.LENGTH_LONG;

                                        Toast toast = Toast.makeText(timeContext, text, duration);
                                        toast.show();
                                        inputEndTime.setText("");
                                    }
                                }
                                catch(ParseException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        }, hour, min, true);

                myEndTimePickerDialog.setTitle("Select Time");
                myEndTimePickerDialog.show();
            }
        });


        //save data
        Button saveDocDtl = (Button)findViewById(R.id.proceed2);
        saveDocDtl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                //convert to string
                String docname = inputDocName.getText().toString();
                String docadd = inputDocAddress.getText().toString();
                String docloc = inputLoc.getText().toString();
                String docspecialty = inputSpecialty.getText().toString();
                String docnum = inputPhoneNumber.getText().toString();
                String doctime = inputTime.getText().toString();
                String docendtime = inputEndTime.getText().toString();
                String docremarks = inputRemarks.getText().toString();

                if (!isValid(docname))
                {
                    inputDocName.setError("Invalid Name");

                    if (docname.matches("") || docadd.matches("") || docloc.matches("") ||
                            docspecialty.matches("") || docnum.matches("") || doctime.matches("") ||
                            docendtime.matches(""))
                    {
                        Context context = getApplicationContext();
                        CharSequence text = "Please fill up all the entry fields";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        return;
                    }
                    return;
                }
                else
                {
                    //perform insertion of data
                    sqlControlDoc.insertDoctor(docname, docadd, docloc, docspecialty, docnum,
                            doctime, docendtime, docremarks);

                    //intent to listview
                    Intent VDP = new Intent(ViewDocActivity.this,
                            DoctorList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(VDP);
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        exitDialog = new AlertDialog.Builder(ViewDocActivity.this);
        exitDialog.setTitle("EXIT CONFIRMATION");
        exitDialog.setMessage("Are you sure you want to exit and discard changes you had made?");
        exitDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        exitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        exitDialog.show();

    }

    public boolean isValid(String name)
    {
        String nameValid = "[a-zA-Z]";
        Pattern namePattern = Pattern.compile(nameValid);
        Matcher nameMatcher = namePattern.matcher(name);

        return nameMatcher.matches();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_doc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        item.setVisible(false);

        return super.onOptionsItemSelected(item);
    }

}
