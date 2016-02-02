package com.example.dylicious.mydoctors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditDoctor extends Activity {

    EditText editDocName, editDocAdd, editDocNum, editDocTime, editDocEndTime, editDocRemarks;
    AutoCompleteTextView editDocLoc, editDocSpec;
    Button editBtn;
    long _docID;
    SQLControllerDoctor sqlDoc;
    Calendar myTime;
    AlertDialog.Builder exitDialog;
    String [] docSpecList = {"Allergist", "Dermatologist", "Internal Medicine Physician",
            "Gynecologist", "General Physician", "Cardiologist", "Chemotherapist", "Neurologist",
            "Ophtalmologist", "Pathologist", "Psychatrist", "Urologist", "Rheumatologist",
            "Radiation Oncologist", "Pediatrician"};
    String [] docLocList = {"Terengganu", "Kelantan", "Perak", "Malacca", "Perlis", "Kedah",
            "Penang", "Sabah", "Sarawak", "Selangor", "Wilayah Persekutuan", "Negeri Sembilan",
            "Johor", "Pahang"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor);

        sqlDoc = new SQLControllerDoctor(this);
        sqlDoc.open();

        editDocName = (EditText)findViewById(R.id.edocname);
        editDocAdd = (EditText)findViewById(R.id.edocadd);
        editDocLoc = (AutoCompleteTextView)findViewById(R.id.edocloc);
        editDocSpec = (AutoCompleteTextView)findViewById(R.id.edocspecialty);
        editDocNum = (EditText)findViewById(R.id.edocnum);
        editDocTime = (EditText)findViewById(R.id.edoctime);
        editDocEndTime = (EditText)findViewById(R.id.edocendtime);
        editDocRemarks = (EditText)findViewById(R.id.edocremarks);


        Intent editDoc = getIntent();
        String doc_ID = editDoc.getStringExtra("doc_ID");
        String doc_Name = editDoc.getStringExtra("doc_Name");
        String doc_Add = editDoc.getStringExtra("doc_Add");
        String doc_Loc = editDoc.getStringExtra("doc_Loc");
        String doc_Spec = editDoc.getStringExtra("doc_Spec");
        String doc_Num = editDoc.getStringExtra("doc_Num");
        String doc_Time = editDoc.getStringExtra("doc_Time");
        String doc_EndTime = editDoc.getStringExtra("doc_EndTime");
        String doc_Remarks = editDoc.getStringExtra("doc_Remarks");

        _docID = Long.parseLong(doc_ID);

        editDocName.setText(doc_Name);
        editDocAdd.setText(doc_Add);
        editDocLoc.setText(doc_Loc);
        editDocSpec.setText(doc_Spec);
        editDocNum.setText(doc_Num);
        editDocTime.setText(doc_Time);
        editDocEndTime.setText(doc_EndTime);
        editDocRemarks.setText(doc_Remarks);

        ArrayAdapter specAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                docSpecList);
        ArrayAdapter locAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                docLocList);
        editDocLoc.setAdapter(locAdapter);
        editDocLoc.setThreshold(1);

        editDocSpec.setAdapter(specAdapter);
        editDocSpec.setThreshold(1);

        myTime = Calendar.getInstance();
        editDocTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = myTime.get(Calendar.HOUR_OF_DAY);
                int min = myTime.get(Calendar.MINUTE);
                TimePickerDialog myTimePickerDialog = new TimePickerDialog(EditDoctor.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                editDocTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, hour, min, true);

                myTimePickerDialog.setTitle("Select Time");
                myTimePickerDialog.show();
            }
        });

        editDocEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int hour = myTime.get(Calendar.HOUR_OF_DAY);
                int min = myTime.get(Calendar.MINUTE);
                TimePickerDialog myTimePickerDialog = new TimePickerDialog(EditDoctor.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                editDocEndTime.setText(String.format("%02d:%02d", hourOfDay,
                                        minute));
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                try
                                {
                                    Date startTimeDate =
                                            format.parse(editDocTime.getText().toString());
                                    Date endTimeDate =
                                            format.parse(editDocEndTime.getText().toString());

                                    if (startTimeDate.compareTo(endTimeDate) >= 0)
                                    {
                                        Context timeContext = getApplicationContext();
                                        CharSequence text = "Please enter the correct end time";
                                        int duration = Toast.LENGTH_LONG;

                                        Toast toast = Toast.makeText(timeContext, text, duration);
                                        toast.show();
                                    }
                                }
                                catch(ParseException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }, hour, min, true);

                myTimePickerDialog.setTitle("Select Time");
                myTimePickerDialog.show();
            }
        });


        editBtn = (Button)findViewById(R.id.editbtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String update_docname = editDocName.getText().toString();
                String update_docadd = editDocAdd.getText().toString();
                String update_docloc = editDocLoc.getText().toString();
                String update_docspec = editDocSpec.getText().toString();
                String update_docnum = editDocNum.getText().toString();
                String update_doctime = editDocTime.getText().toString();
                String update_docendtime = editDocEndTime.getText().toString();
                String update_docremarks = editDocRemarks.getText().toString();

                if (!isValid(update_docname))
                {
                    editDocName.setError("Invalid Name");

                    if (update_docname.matches("") || update_docadd.matches("") || update_docloc.matches("") ||
                            update_docspec.matches("") || update_docnum.matches("") || update_doctime.matches("") ||
                            update_docendtime.matches(""))
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
                    sqlDoc.updateDoctor(_docID, update_docname, update_docadd, update_docloc,
                            update_docspec, update_docnum, update_doctime, update_docendtime,
                            update_docremarks);

                    Intent backIntent = new Intent(getApplicationContext(),
                            DoctorList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(backIntent);
                }

            }
        });




    }


    @Override
    public void onBackPressed() {
        exitDialog = new AlertDialog.Builder(EditDoctor.this);
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
        getMenuInflater().inflate(R.menu.menu_edit_doctor, menu);
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
