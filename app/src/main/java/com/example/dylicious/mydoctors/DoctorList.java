package com.example.dylicious.mydoctors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.orm.query.Select;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.List;

public class DoctorList extends Activity {

    ListView docListView;
    SQLControllerDoctor sqlControlDoc;
    TextView docIDP, docNameP, docAddP, docLocP, docSpecP, docNumP, docTimeP, docEndTimeP,
            docRemarksP;
    TextView eDocID, eDocName, eDocAdd, eDocLoc, eDocSpec, eDocNum, eDocTime, eDocEndTime,
            eDocRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        setTitle("List of Doctors");

        sqlControlDoc = new SQLControllerDoctor(this);
        sqlControlDoc.open();

        docListView = (ListView)findViewById(R.id.doclistView);

        Cursor cursor = sqlControlDoc.getAllDocData();

        String[] from = new String[]
                {
                        DatabaseHandler.KEY_DOCID,
                        DatabaseHandler.KEY_DOCNAME,
                        DatabaseHandler.KEY_ADDRESS,
                        DatabaseHandler.KEY_LOCATION,
                        DatabaseHandler.KEY_SPECIALTY,
                        DatabaseHandler.KEY_PHONE,
                        DatabaseHandler.KEY_TIME,
                        DatabaseHandler.KEY_ENDTIME,
                        DatabaseHandler.KEY_REMARKS
                };

        int[] to = new int[]
                {
                        R.id.docID,
                        R.id.docName,
                        R.id.docAdd,
                        R.id.docLoc,
                        R.id.docSpec,
                        R.id.docNum,
                        R.id.docConsultTime,
                        R.id.docEndTime,
                        R.id.docRemarks
                };

        SimpleCursorAdapter docAdapter = new SimpleCursorAdapter(DoctorList.this,
                R.layout.list_doctor_item, cursor, from, to, 0);

        docAdapter.notifyDataSetChanged();
        docListView.setAdapter(docAdapter);

        docListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                docIDP = (TextView) view.findViewById(R.id.docID);
                docNameP = (TextView) view.findViewById(R.id.docName);
                docAddP = (TextView) view.findViewById(R.id.docAdd);
                docLocP = (TextView) view.findViewById(R.id.docLoc);
                docSpecP = (TextView) view.findViewById(R.id.docSpec);
                docNumP = (TextView) view.findViewById(R.id.docNum);
                docTimeP = (TextView) view.findViewById(R.id.docConsultTime);
                docEndTimeP = (TextView) view.findViewById(R.id.docEndTime);
                docRemarksP = (TextView) view.findViewById(R.id.docRemarks);

                String docID_val = docIDP.getText().toString();
                String docName_val = docNameP.getText().toString();
                String docAdd_val = docAddP.getText().toString();
                String docLoc_val = docLocP.getText().toString();
                String docSpec_val = docSpecP.getText().toString();
                String docNum_val = docNumP.getText().toString();
                String docTime_val = docTimeP.getText().toString();
                String docEndTime_val = docEndTimeP.getText().toString();
                String docRemarks_val = docRemarksP.getText().toString();

                Intent editIntent = new Intent(getApplicationContext(), ViewDoctorProf.class);
                editIntent.putExtra("doc_ID", docID_val);
                editIntent.putExtra("doc_Name", docName_val);
                editIntent.putExtra("doc_Add", docAdd_val);
                editIntent.putExtra("doc_Loc", docLoc_val);
                editIntent.putExtra("doc_Spec", docSpec_val);
                editIntent.putExtra("doc_Num", docNum_val);
                editIntent.putExtra("doc_Time", docTime_val);
                editIntent.putExtra("doc_EndTime", docEndTime_val);
                editIntent.putExtra("doc_Remarks", docRemarks_val);
                startActivity(editIntent);
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
        btnAppViewFooter.setOnClickListener(new View.OnClickListener() {
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

        ImageButton btnViewProfileFooter = (ImageButton)findViewById(R.id.viewproffooter);
        btnViewProfileFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVPF = new Intent(getApplicationContext(), UserList.class);
                startActivity(intentVPF);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor_list, menu);
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
