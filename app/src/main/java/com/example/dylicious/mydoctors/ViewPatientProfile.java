package com.example.dylicious.mydoctors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewPatientProfile extends Activity {

    TextView viewPatName, viewPatMed, viewPatTreatment, viewPatAllergy;
    SQLControllerUser sqlUser;
    ImageButton editUserBtn;
    Button deleteUserBtn;
    long _userID;
    AlertDialog.Builder deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_profile);

        sqlUser = new SQLControllerUser(this);
        sqlUser.open();

        //declare TextView
        viewPatName = (TextView)findViewById(R.id.patnameview);
        viewPatMed = (TextView)findViewById(R.id.patmedview);
        viewPatTreatment = (TextView)findViewById(R.id.pattreatmentview);
        viewPatAllergy = (TextView)findViewById(R.id.patallergyview);

        Intent viewUserIntent = getIntent();
        final String pat_id = viewUserIntent.getStringExtra("user_ID");
        final String pat_name = viewUserIntent.getStringExtra("user_name");
        final String pat_med = viewUserIntent.getStringExtra("user_med");
        final String pat_treat = viewUserIntent.getStringExtra("user_treat");
        final String pat_allergy = viewUserIntent.getStringExtra("user_allergy");

        _userID = Long.parseLong(pat_id);

        viewPatName.setText(pat_name);
        viewPatMed.setText(pat_med);
        viewPatTreatment.setText(pat_treat);
        viewPatAllergy.setText(pat_allergy);

        setTitle(pat_name + "'s profile");
        editUserBtn = (ImageButton)findViewById(R.id.edituser);
        editUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editUserIntent = new Intent(getApplicationContext(), EditUser.class);
                editUserIntent.putExtra("user_ID", pat_id);
                editUserIntent.putExtra("user_name", pat_name);
                editUserIntent.putExtra("user_med", pat_med);
                editUserIntent.putExtra("user_treat", pat_treat);
                editUserIntent.putExtra("user_allergy", pat_allergy);
                startActivity(editUserIntent);
            }
        });

        deleteUserBtn = (Button)findViewById(R.id.deleteUser);
        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                deleteDialog = new AlertDialog.Builder(ViewPatientProfile.this);
                deleteDialog.setTitle("DELETE CONFIRMATION");
                deleteDialog.setMessage("Are you sure you want to delete?");
                deleteDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqlUser.deleteUser(_userID);
                        Intent deleteDocIntent = new Intent(getApplicationContext(),
                                UserList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(deleteDocIntent);
                    }
                });
                deleteDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                deleteDialog.show();
            }
        });

        ImageButton btnAddMenuFooter = (ImageButton)findViewById(R.id.addmenufooter);
        btnAddMenuFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAMF = new Intent(getApplicationContext(), AddMenu.class);
                startActivity(intentAMF);
            }
        });

        ImageButton btnViewDocFooter = (ImageButton)findViewById(R.id.viewdocfooter);
        btnViewDocFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVDF = new Intent (getApplicationContext(), DoctorList.class);
                startActivity(intentVDF);
            }
        });

        ImageButton btnAppViewFooter = (ImageButton)findViewById(R.id.viewappfooter);
        btnAppViewFooter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long startMillis = System.currentTimeMillis();
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, startMillis);
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                startActivity(intent);
            }
        });

        ImageButton btnSearchDocFooter = (ImageButton)findViewById(R.id.searchdocfooter);
        btnSearchDocFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri clinicUri = Uri.parse("geo:0,0?q=hospital");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, clinicUri);
                startActivity(mapIntent);
            }
        });

        ImageButton btnViewProfileFooter = (ImageButton)findViewById(R.id.viewproffooter);
        btnViewProfileFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVPF = new Intent (getApplicationContext(), UserList.class);
                startActivity(intentVPF);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_patient_profile, menu);
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
