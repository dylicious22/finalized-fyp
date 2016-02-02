package com.example.dylicious.mydoctors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewDoctorProf extends Activity {

    SQLControllerDoctor sqlDoc;
    TextView viewDocID, viewDocName, viewDocLoc, viewDocAdd, viewDocNum, viewDocSpecialty, viewDocTime,
            viewDocEndTime, viewDocRemarks;
    String docIDPVal, docNamePVal, docLocPVal, docAddPVal, docNumPVal, docSpecPVal, docTimePVal,
            docEndTimePVal, docRemarksPVal;
    ImageButton editDoctorBtn;
    Button deleteDoctorBtn;
    long _docID;
    AlertDialog.Builder deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor_prof);

        sqlDoc = new SQLControllerDoctor(this);
        sqlDoc.open();



        //declare textview
        viewDocID = (TextView)findViewById(R.id.docidview);
        viewDocName = (TextView)findViewById(R.id.docnameview);
        viewDocLoc = (TextView)findViewById(R.id.doclocview);
        viewDocAdd = (TextView)findViewById(R.id.docaddview);
        viewDocNum = (TextView)findViewById(R.id.docnumview);
        viewDocSpecialty = (TextView)findViewById(R.id.docspecialtyview);
        viewDocTime = (TextView)findViewById(R.id.docconsulttimeview);
        viewDocEndTime = (TextView)findViewById(R.id.docconsultendtimeview);
        viewDocRemarks = (TextView)findViewById(R.id.docremarksview);

        //retrieving data
        Intent intentViewDoc = getIntent();
        String docID = intentViewDoc.getStringExtra("doc_ID");
        String docname = intentViewDoc.getStringExtra("doc_Name");
        String docadd = intentViewDoc.getStringExtra("doc_Add");
        String docloc = intentViewDoc.getStringExtra("doc_Loc");
        String docnum = intentViewDoc.getStringExtra("doc_Num");
        String docspecialty = intentViewDoc.getStringExtra("doc_Spec");
        String doctime = intentViewDoc.getStringExtra("doc_Time");
        String docendtime = intentViewDoc.getStringExtra("doc_EndTime");
        String docremarks = intentViewDoc.getStringExtra("doc_Remarks");

        _docID = Long.parseLong(docID);

        viewDocID.setText(docID);
        viewDocName.setText(docname);
        viewDocSpecialty.setText(docspecialty);
        viewDocLoc.setText(docloc);
        viewDocNum.setText(docnum);
        viewDocAdd.setText(docadd);
        viewDocTime.setText(doctime);
        viewDocEndTime.setText(docendtime);
        viewDocRemarks.setText(docremarks);

        docIDPVal = docID;
        docNamePVal = docname;
        docAddPVal = docadd;
        docLocPVal = docloc;
        docNumPVal = docnum;
        docSpecPVal = docspecialty;
        docTimePVal = doctime;
        docEndTimePVal = docendtime;
        docRemarksPVal = docremarks;

        viewDocNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + docNumPVal));
                startActivity(callIntent);
            }
        });
        setTitle("Dr. " + docname + "'s Profile");

        viewDocAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Uri clinicUri = Uri.parse("geo:0,0?q=" + viewDocAdd.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, clinicUri);
                startActivity(mapIntent);
            }
        });

        editDoctorBtn = (ImageButton)findViewById(R.id.editDoc);
        editDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent editDocIntent = new Intent(getApplicationContext(), EditDoctor.class);
                editDocIntent.putExtra("doc_ID", docIDPVal);
                editDocIntent.putExtra("doc_Name", docNamePVal);
                editDocIntent.putExtra("doc_Add", docAddPVal);
                editDocIntent.putExtra("doc_Loc", docLocPVal);
                editDocIntent.putExtra("doc_Num", docNumPVal);
                editDocIntent.putExtra("doc_Spec", docSpecPVal);
                editDocIntent.putExtra("doc_Time", docTimePVal);
                editDocIntent.putExtra("doc_EndTime", docEndTimePVal);
                editDocIntent.putExtra("doc_Remarks", docRemarksPVal);
                startActivity(editDocIntent);
            }
        });

        deleteDoctorBtn = (Button)findViewById(R.id.deleteDoc);
        deleteDoctorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    deleteDialog = new AlertDialog.Builder(ViewDoctorProf.this);
                    deleteDialog.setTitle("DELETE CONFIRMATION");
                    deleteDialog.setMessage("Are you sure you want to delete?");
                    deleteDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sqlDoc.deleteDoctor(_docID);
                            Intent deleteDocIntent = new Intent(getApplicationContext(),
                                    DoctorList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


        ImageButton btnDocProfFooter = (ImageButton)findViewById(R.id.viewdocfooter);
        btnDocProfFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVDPF = new Intent(getApplicationContext(), DoctorList.class);
                startActivity(intentVDPF);
            }
        });

        ImageButton btnAppViewer = (ImageButton)findViewById(R.id.viewappfooter);
        btnAppViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        ImageButton btnUserProfileFooter = (ImageButton)findViewById(R.id.viewproffooter);
        btnUserProfileFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUPF = new Intent(getApplicationContext(), UserList.class);
                startActivity(intentUPF);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_doctor_prof, menu);
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
