package com.example.dylicious.mydoctors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StorePatientProfile extends Activity {

    EditText inputUserName;
    AutoCompleteTextView inputUserMed, inputUserTreatment, inputUserAllergy;
    Button btnSaveUserProfile;
    SQLControllerUser sqlUser;
    AlertDialog.Builder exitDialog;
    String [] medications = {"None", "Ventolin", "Panadol", "Paracetamol", "Thyroxin", "Bactrim"};
    String [] allergy = {"None", "Seafood", "Panadol", "Bactrim", "Paracetamol"};
    String [] treatment = {"None", "Chemotherapy", "Blood Test", "Minor Surgery"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_patient_profile);
        setTitle("Personal Profile");

        sqlUser = new SQLControllerUser(this);
        sqlUser.open();

        //edittext for user info
        inputUserName = (EditText)findViewById(R.id.patname);
        inputUserMed = (AutoCompleteTextView)findViewById(R.id.patmed);
        inputUserTreatment = (AutoCompleteTextView)findViewById(R.id.pattreatment);
        inputUserAllergy = (AutoCompleteTextView)findViewById(R.id.patallergy);
        btnSaveUserProfile = (Button)findViewById(R.id.savepat);

        ArrayAdapter medAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                medications);
        ArrayAdapter treatAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                treatment);
        ArrayAdapter allergyAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                allergy);

        inputUserMed.setAdapter(medAdapter);
        inputUserMed.setThreshold(1);
        inputUserTreatment.setAdapter(treatAdapter);
        inputUserTreatment.setThreshold(1);
        inputUserAllergy.setAdapter(allergyAdapter);
        inputUserAllergy.setThreshold(1);

        btnSaveUserProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String username = inputUserName.getText().toString();
                    String usermed = inputUserMed.getText().toString();
                    String usertreatment = inputUserTreatment.getText().toString();
                    String userallergy = inputUserAllergy.getText().toString();

                    if (!isValidName(username))
                    {
                        inputUserName.setError("Invalid Input");
                        if (username.matches(""))
                        {
                            inputUserName.setError("Invalid Name");

                        }
                        return;
                    }
                    else
                    {
                        sqlUser.insertUser(username, usermed, usertreatment, userallergy);
                        Intent userView = new Intent(StorePatientProfile.this,
                                UserList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(userView);

                    }
                }
            });
        }

    @Override
    public void onBackPressed() {
        exitDialog = new AlertDialog.Builder(StorePatientProfile.this);
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

    public boolean isValidName(String name)
    {
        String nameValid = "[a-zA-Z]";
        Pattern namePattern = Pattern.compile(nameValid);
        Matcher nameMatcher = namePattern.matcher(name);

        return nameMatcher.matches();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_patient_profile, menu);
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
