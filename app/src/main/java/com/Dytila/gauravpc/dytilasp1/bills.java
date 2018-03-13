package com.Dytila.gauravpc.dytilasp1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Dytila.gauravpc.dytilasp1.models.billModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class bills extends AppCompatActivity {

    private ListView bills;
    ImageView cross_img;
    TextView countText,n1,n2,n3;
    RelativeLayout relativeLayout,r,no_connection_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        relativeLayout=(RelativeLayout)findViewById(R.id.notFoundLayout);
        relativeLayout.setVisibility(View.GONE);
        r=(RelativeLayout)findViewById(R.id.loadingLayout);
        r.setVisibility(View.GONE);
        no_connection_layout=(RelativeLayout)findViewById(R.id.no_connectionLayout);
        no_connection_layout.setVisibility(View.GONE);

        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String id_val=sharedPreferences.getString("user_id", "");
        String order_val=sharedPreferences.getString("order_id", "");

        Log.e("order Id","hello");
        Log.e("order Id",id_val);
        Log.e("order Id",order_val);

        bills = (ListView) findViewById(R.id.listbills);
        bills.setVisibility(View.GONE);

        if(order_val.equals("null")){
            relativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"No Bills Available",Toast.LENGTH_LONG).show();
        }
        else{
            boolean check=isNetworkAvailable();
            if(check==true) {
                new JSONTask().execute("https://dytila.herokuapp.com/api/bills/" + order_val);
            }
            else{
                no_connection_layout.setVisibility(View.VISIBLE);
                r.setVisibility(View.GONE);
                Button retry_button=(Button)findViewById(R.id.retryButton);
                retry_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(bills.this, bills.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            n1=(TextView)findViewById(R.id.num1);
            n2=(TextView)findViewById(R.id.num2);
            n3=(TextView)findViewById(R.id.billCount);
            String TmealFreq_val=sharedPreferences.getString("TmealFreq","");
            String mealFreq_val=sharedPreferences.getString("mealFreq", "");

            int number1=Integer.parseInt(TmealFreq_val);
            int number2=Integer.parseInt(mealFreq_val);
            int bill_Count=number1-number2;

            Handler handler1 = new Handler();
            for(int i=0;i<=number1;i++){
                final int finalI = i;
                handler1.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        n1.setText(finalI + "");
                    }
                }, 90 * i);
            }
            for(int i=0;i<=number2;i++){
                final int finalI = i;
                handler1.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        n2.setText(finalI + "");
                    }
                }, 90 * i);
            }
            for(int i=0;i<=bill_Count;i++){
                final int finalI = i;
                handler1.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        n3.setText(finalI + "");
                    }
                }, 90 * i);
            }
        }
//        new JSONTask().execute("https://dytila.herokuapp.com/api/bills/"+id_val);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class JSONTask extends AsyncTask<String,String ,List<billModel>> {

        private ProgressDialog dialog = new ProgressDialog(bills.this);

        @Override
        protected void onPreExecute() {

            r.setVisibility(View.VISIBLE);

            //timeout for Async Task
            final JSONTask asyncObject = this;
            new CountDownTimer(10000, 10000) {
                public void onTick(long millisUntilFinished) {
                    // monitor the progress
                }
                public void onFinish() {
                    // stop async task if not in progress
                    if (asyncObject.getStatus() == Status.RUNNING) {
                        asyncObject.cancel(false);
                        r.setVisibility(View.GONE);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(bills.this);
                        alertDialogBuilder.setMessage("Connection Is Unstable!");
                        alertDialogBuilder.setPositiveButton("Retry",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(bills.this, bills.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(bills.this, features.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
            }.start();

            super.onPreExecute();
        }

        @Override
        protected List<billModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson=buffer.toString();
                JSONObject parentObject=new JSONObject(finalJson);
                JSONArray parentArray=parentObject.getJSONArray("bills");

                if(parentArray.length()<1) {
                    return null;
                }
                else{
                    List<billModel> billModelList=new ArrayList<>();
                    for(int i=0;i<parentArray.length();i++){

                        JSONObject finalObject=parentArray.getJSONObject(i);
                        billModel billModel=new billModel();
                        billModel.setName(finalObject.getString("name"));
                        billModel.setBillID(finalObject.getString("billID"));
                        billModel.setDay(finalObject.getString("day"));
                        billModel.setDate(finalObject.getString("date"));
                        billModel.setMonth(finalObject.getString("month"));
                        billModel.setYear(finalObject.getString("year"));
                        billModel.setAdate(finalObject.getString("adate"));
                        billModelList.add(billModel);

                    }
                    //adding the final obejct in the list

                    return  billModelList;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<billModel> result) {
            super.onPostExecute(result);

            if(result==null){
                r.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                bills.setVisibility(View.GONE);
            }
            else{
                r.setVisibility(View.GONE);
                bills.setVisibility(View.VISIBLE);
                billAdapter billAdapter=new billAdapter(getApplicationContext(),R.layout.row_bill,result);
                bills.setAdapter(billAdapter);
            }
        }
    }

    public class billAdapter extends ArrayAdapter {

        public List<billModel> billModelList;
        private int resource;

        private LayoutInflater inflater;

        public billAdapter(Context context, int resource, List<billModel> objects) {
            super(context, resource, objects);
            billModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.row_bill,null);
            }


            TextView billID,Day,Date,Month,Year;
            Day=(TextView)convertView.findViewById(R.id.day);
            Date=(TextView)convertView.findViewById(R.id.date);
            Month=(TextView)convertView.findViewById(R.id.month);
//            Year=(TextView)convertView.findViewById(R.id.year);
            Day.setText(billModelList.get(position).getDay());
            Date.setText(billModelList.get(position).getAdate());
            Month.setText(billModelList.get(position).getMonth());
//            Year.setText(billModelList.get(position).getYear());

            billID=(TextView)convertView.findViewById(R.id.billID);
            billID.setText(billModelList.get(position).getBillID());
            billID.setTextSize(7 * getResources().getDisplayMetrics().density);

            return convertView;
        }
    }
}

