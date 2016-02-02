package com.example.dylicious.mydoctors;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UserList extends Activity {

    ListView userList;
    SQLControllerUser sqlUser;
    DatabaseHandler dbHandler;
    TextView userIDViewList, usernameViewList, userMedViewList, userTreatViewList, userAllergyViewList;
    Button btnDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle("List of Family Members");

        userList = (ListView)findViewById(R.id.userlistView);

        sqlUser = new SQLControllerUser(this);
        sqlUser.open();

        Cursor userCursor = sqlUser.getAllUserData();
        String [] from = new String[]
                {
                        dbHandler.KEY_ID,
                        dbHandler.KEY_NAME,
                        dbHandler.KEY_MEDICATION,
                        dbHandler.KEY_TREATMENT,
                        dbHandler.KEY_ALLERGY
                };

        int[] to = new int[]
                {
                        R.id.userIDView,
                        R.id.userNameView,
                        R.id.userMedicationView,
                        R.id.userTreatmentView,
                        R.id.userAllergyView
                };

        SimpleCursorAdapter userAdapter = new SimpleCursorAdapter(UserList.this,
                R.layout.list_item_user, userCursor, from, to, 0);

        userAdapter.notifyDataSetChanged();
        userList.setAdapter(userAdapter);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userIDViewList = (TextView) view.findViewById(R.id.userIDView);
                usernameViewList = (TextView) view.findViewById(R.id.userNameView);
                userMedViewList = (TextView) view.findViewById(R.id.userMedicationView);
                userTreatViewList = (TextView) view.findViewById(R.id.userTreatmentView);
                userAllergyViewList = (TextView) view.findViewById(R.id.userAllergyView);

                String userID_val = userIDViewList.getText().toString();
                String userName_val = usernameViewList.getText().toString();
                String userMed_val = userMedViewList.getText().toString();
                String userTreat_val = userTreatViewList.getText().toString();
                String userAllergy_val = userAllergyViewList.getText().toString();

                Intent usereditIntent = new Intent(getApplicationContext(), ViewPatientProfile.class);
                usereditIntent.putExtra("user_ID", userID_val);
                usereditIntent.putExtra("user_name", userName_val);
                usereditIntent.putExtra("user_med", userMed_val);
                usereditIntent.putExtra("user_treat", userTreat_val);
                usereditIntent.putExtra("user_allergy", userAllergy_val);
                startActivity(usereditIntent);

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
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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
