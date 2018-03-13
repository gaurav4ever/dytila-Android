package com.Dytila.gauravpc.dytilasp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    String isLogin="0";
    char c;
    TextView proceed_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checking if session is set
        try {
            String msg;
            FileInputStream fileInputStream=openFileInput("Dytila Login");
            InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer=new StringBuffer();
            while((msg=bufferedReader.readLine())!=null){
                stringBuffer.append(msg);
            }

//            Log.e("session",stringBuffer.toString());
            String val=stringBuffer.toString();
            int len=val.length();
            Log.e("session",val+" "+len);
            c=val.charAt(len-1);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread timerThread=new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    if(c=='1'){
                        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("firstTime","0");
                        editor.commit();
                        Intent intent=new Intent(MainActivity.this,features.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, welcome_screens.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timerThread.start();

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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
}
