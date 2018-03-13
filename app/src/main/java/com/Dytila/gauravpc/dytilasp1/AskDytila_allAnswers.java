package com.Dytila.gauravpc.dytilasp1;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.Dytila.gauravpc.dytilasp1.models.AskDytila_Answers;
import com.Dytila.gauravpc.dytilasp1.models.Likesmodel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AskDytila_allAnswers extends AppCompatActivity {
    
    private  ListView listView;
    TextView queText;
    int asyncTaskDone=0;
    ListView likesListView;
    RelativeLayout r,no_connection_layout,noLikesLayout,loadingLayout;
    String user_id,selectedPostAskDytilaMainActivity="0",liked="-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_dytila_all_answers);

        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String userid_val=sharedPreferences.getString("user_id", "");
        user_id=userid_val;

        Intent intent=getIntent();
        String que_id=intent.getStringExtra("que_id");
        String que=intent.getStringExtra("que");
        selectedPostAskDytilaMainActivity=intent.getStringExtra("selectedPos");

        queText=(TextView)findViewById(R.id.question);
        String finalqueText=que;
        if(finalqueText.length()>150){
            finalqueText=finalqueText.substring(0,140)+"...";
        }
        queText.setText(finalqueText);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        boolean check=isNetworkAvailable();
        if(check==true) {
            new JSONTask().execute("https://dytila.herokuapp.com/api/askDytila/"+que_id+"/answers/"+user_id);
            listView=(ListView)findViewById(R.id.askDytila_allAnswers);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public class JSONTask extends AsyncTask<String,String ,List<AskDytila_Answers>> {

        private ProgressDialog dialog = new ProgressDialog(AskDytila_allAnswers.this);

        @Override
        protected void onPreExecute() {

            this.dialog.setMessage("Please Wait ...");
            this.dialog.show();
            //timeout for Async Task
            final JSONTask asyncObject = this;
            new CountDownTimer(10000, 10000) {
                public void onTick(long millisUntilFinished) {
                    // monitor the progress
                    if(asyncTaskDone==1) {
                        Toast.makeText(getApplicationContext(), "Im done", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("Timer Status ","" + millisUntilFinished / 1000);
                }
                public void onFinish() {
                    // stop async task if not in progress
                    if (asyncObject.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncObject.cancel(false);
                        dialog.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AskDytila_allAnswers.this);
                        alertDialogBuilder.setMessage("Connection Is Unstable!");
                        alertDialogBuilder.setPositiveButton("Retry",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(AskDytila_allAnswers.this, askDytilaMain.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("Home",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(AskDytila_allAnswers.this, features.class);
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
        protected List<AskDytila_Answers> doInBackground(String... params) {
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
                JSONArray parentArray=parentObject.getJSONArray("answers");
                List<AskDytila_Answers> askDytila_AnswersList=new ArrayList<>();
                if(parentArray.length()<1){
                    return  null;
                }else{
                    for(int i=0;i<parentArray.length();i++){

                        JSONObject finalObject=parentArray.getJSONObject(i);
                        AskDytila_Answers askDytila_answers=new AskDytila_Answers();
                        askDytila_answers.setAns_id(finalObject.getString("ans_id"));
                        askDytila_answers.setUsername(finalObject.getString("by"));
                        askDytila_answers.setAns(finalObject.getString("ans"));
                        askDytila_answers.setDate(finalObject.getString("date"));
                        askDytila_answers.setAvatar(finalObject.getString("avatar"));
                        askDytila_answers.setLikesCount(finalObject.getString("likesCount"));
                        askDytila_answers.setLiked(finalObject.getString("liked"));
                        askDytila_AnswersList.add(askDytila_answers);

                    }
                    //adding the final obejct in the list

                    return  askDytila_AnswersList;
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
        protected void onPostExecute(List<AskDytila_Answers> askDytila_Answers) {
            super.onPostExecute(askDytila_Answers);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(askDytila_Answers==null){
                Toast.makeText(getApplicationContext(),"No Answers Found :(",Toast.LENGTH_SHORT).show();
            }else{
                AskDytila_AnswersAdapter askDytila_allAdapter=new AskDytila_AnswersAdapter(getApplicationContext(),R.layout.row_askdytila_answers,askDytila_Answers);
                listView.setAdapter(askDytila_allAdapter);
            }
        }
    }

    public class AskDytila_AnswersAdapter extends ArrayAdapter {

        public List<AskDytila_Answers> askDytila_AnswersModelList;
        private int resource;

        private LayoutInflater inflater;

        public AskDytila_AnswersAdapter(Context context, int resource, List<AskDytila_Answers> objects) {
            super(context, resource, objects);
            askDytila_AnswersModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.row_askdytila_answers,null);
            }

            TextView username_text,date_text,ans_text;
            ImageView img;
            username_text=(TextView)convertView.findViewById(R.id.usernameText);
            date_text=(TextView)convertView.findViewById(R.id.date);
            ans_text=(TextView)convertView.findViewById(R.id.answer);
            img=(ImageView)convertView.findViewById(R.id.img);

            String username_val=upperFirst(askDytila_AnswersModelList.get(position).getUsername());
            String ans=makeFirstUpper(askDytila_AnswersModelList.get(position).getAns());

            username_text.setText(username_val);
            ans_text.setText(ans);
            date_text.setText(askDytila_AnswersModelList.get(position).getDate());

            ImageLoader.getInstance().displayImage(askDytila_AnswersModelList.get(position).getAvatar(), img);

            //like and comment system
            final TextView likesCountText;
            likesCountText=(TextView)convertView.findViewById(R.id.likesCount);
            likesCountText.setText(askDytila_AnswersModelList.get(position).getLikesCount());

            RelativeLayout likeArea,commentArea,likeCountArea,commentCountArea;
            likeArea=(RelativeLayout)convertView.findViewById(R.id.likeArea);

            likeCountArea=(RelativeLayout)convertView.findViewById(R.id.likeCoundArea);


            likeCountArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(AskDytila_allAnswers.this);
                    dialog.setContentView(R.layout.ask_dytila_likes_layout);

                    TextView closeText=(TextView)dialog.findViewById(R.id.close);
                    noLikesLayout=(RelativeLayout)dialog.findViewById(R.id.noLikesLayout);
                    loadingLayout=(RelativeLayout)dialog.findViewById(R.id.loadingLayout);
                    likesListView=(ListView)dialog.findViewById(R.id.likesList);
                    noLikesLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.VISIBLE);
                    likesListView.setVisibility(View.GONE);

                    getLikes(askDytila_AnswersModelList.get(position).getAns_id());

                    closeText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

            final int flag=0;

            final ImageView imageView=(ImageView)convertView.findViewById(R.id.likeImg);

            if(askDytila_AnswersModelList.get(position).getLiked().equals("1"))
                imageView.setImageResource(R.drawable.like);
            else
                imageView.setImageResource(R.drawable.unlike);

            likeArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://dytila.herokuapp.com/api/askDytila/like";
                    RequestQueue requestQueue = new Volley().newRequestQueue(AskDytila_allAnswers.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        if (status.equals("dislike")) {
                                            if ((Integer.parseInt(likesCountText.getText().toString())) >= 0) {
                                                likesCountText.setText("" + ((Integer.parseInt(likesCountText.getText().toString())) - 1));
                                                imageView.setImageResource(R.drawable.unlike);
                                                askDytila_AnswersModelList.get(position).setLiked("0");
                                                askDytila_AnswersModelList.get(position).setLikesCount("" + (Integer.parseInt(likesCountText.getText().toString())));
                                                if(position==0){
                                                    liked="0";
                                                }
                                            }
                                        } else if (status.equals("like")) {
                                            likesCountText.setText("" + ((Integer.parseInt(likesCountText.getText().toString())) + 1));
                                            imageView.setImageResource(R.drawable.like);
                                            askDytila_AnswersModelList.get(position).setLiked("1");
                                            askDytila_AnswersModelList.get(position).setLikesCount("" + (Integer.parseInt(likesCountText.getText().toString())));
                                            if(position==0){
                                                liked="1";
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user_id", user_id);
                            params.put("ans_id", askDytila_AnswersModelList.get(position).getAns_id());
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            });

            return convertView;
        }
    }
    public void getLikes(final String ans_id){
        String url="https://dytila.herokuapp.com/api/askDytila/answer/likes";
        RequestQueue requestQueue=new Volley().newRequestQueue(AskDytila_allAnswers.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingLayout.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            List<Likesmodel> likesModelList=new ArrayList<>();
                            JSONArray parentArray=jsonObject.getJSONArray("likes");
                            if(parentArray.length()<1){
                                likesListView.setVisibility(View.GONE);
                                noLikesLayout.setVisibility(View.VISIBLE);
                            }
                            else{
                                likesListView.setVisibility(View.VISIBLE);
                                noLikesLayout.setVisibility(View.GONE);
                                for(int i=0;i<parentArray.length();i++){
                                    JSONObject finalObject=parentArray.getJSONObject(i);
                                    Likesmodel likesmodel=new Likesmodel();
                                    likesmodel.setUsername(finalObject.getString("username"));
                                    likesmodel.setAvatar(finalObject.getString("avatar"));
                                    likesModelList.add(likesmodel);
                                }
                                postExecuteLikes(likesModelList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something wrong happend :( Please Try Again!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("ans_id",ans_id);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void postExecuteLikes(List<Likesmodel> result){
        LikesModelAdapter likesmodelAdapter=new LikesModelAdapter(getApplicationContext().getApplicationContext(),R.layout.row_likes,result);
        likesListView.setAdapter(likesmodelAdapter);
    }
    class LikesModelAdapter extends ArrayAdapter {
        public List<Likesmodel> likesmodelList;
        private int resource;

        private LayoutInflater inflater;

        public LikesModelAdapter(Context context, int resource, List<Likesmodel> objects) {
            super(context, resource, objects);
            likesmodelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_likes, null);
            }

            TextView username;
            ImageView img;
            username=(TextView)convertView.findViewById(R.id.usernameText);
            username.setText(upperFirst(likesmodelList.get(position).getUsername()));

            img=(ImageView)convertView.findViewById(R.id.img);
            ImageLoader.getInstance().displayImage(likesmodelList.get(position).getAvatar(), img);

            return convertView;
        }

    }
    public String makeFirstUpper(String val){
        String final_answer="";
        final_answer+=String.valueOf(val.charAt(0)).toUpperCase();
        final_answer+=val.substring(1);
        return final_answer;
    }
    public String upperFirst(String val){
        String final_answer="";
        for(int i=0;i<val.length();i++){
            if(i==0)
                final_answer+=(String.valueOf(val.charAt(i))).toUpperCase();
            else if(String.valueOf(val.charAt(i-1)).equals(" "))
                final_answer+=(String.valueOf(val.charAt(i))).toUpperCase();
            else if(!String.valueOf(val.charAt(i)).equals(" "))
                final_answer+=(String.valueOf(val.charAt(i))).toLowerCase();
            else
                final_answer+=String.valueOf(val.charAt(i));
        }
        return final_answer;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("selected", selectedPostAskDytilaMainActivity);
        intent.putExtra("isLiked",liked);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

}


