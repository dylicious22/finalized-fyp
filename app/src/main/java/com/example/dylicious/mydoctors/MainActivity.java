package com.example.dylicious.mydoctors;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import java.util.logging.Handler;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    SharedPreferences settings=getSharedPreferences("prefs",0);
                    boolean firstRun=settings.getBoolean("firstRun",false);
                    if(firstRun==false)//if running for first time
                    //Splash will load for first time
                    {
                        SharedPreferences.Editor editor=settings.edit();
                        editor.putBoolean("firstRun",true);
                        editor.commit();
                        Intent i=new Intent(MainActivity.this,OnBoardingGuide.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {

                        Intent a=new Intent(MainActivity.this,DoctorList.class);
                        startActivity(a);
                        finish();
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a paremnt activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        item.setVisible(false);

        return super.onOptionsItemSelected(item);
    }
}
