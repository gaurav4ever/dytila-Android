package com.Dytila.gauravpc.dytilasp1.fragmentHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.Dytila.gauravpc.dytilasp1.AskDytila_allAnswers;
import com.Dytila.gauravpc.dytilasp1.Ask_dytila_m_ques_ans;
import com.Dytila.gauravpc.dytilasp1.R;
import com.Dytila.gauravpc.dytilasp1.models.AskDytila_mQuesModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaurav pc on 10-Jan-17.
 */
public class Fragment_ask_dytila_m_ques extends Fragment {

    private ListView mQuestion_list;
    private ProgressDialog pDialog;
    RelativeLayout notFound_layout,seeAnswersLayout,r,no_connection_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ask_dytila_m_ques, null);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        String userid_val=sharedPreferences.getString("user_id", "");

        mQuestion_list=(ListView)v.findViewById(R.id.mQuestionsList);
        mQuestion_list.setVisibility(View.GONE);
        r=(RelativeLayout)v.findViewById(R.id.loadingLayout);
        r.setVisibility(View.VISIBLE);
        no_connection_layout=(RelativeLayout)v.findViewById(R.id.no_connectionLayout);
        no_connection_layout.setVisibility(View.GONE);

        boolean check=isNetworkAvailable();
        if(check==true) {
            getQuestions(userid_val);
        }
        else{
            no_connection_layout.setVisibility(View.VISIBLE);
            r.setVisibility(View.GONE);
            Button retry_button=(Button)v.findViewById(R.id.retryButton);
            retry_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Ask_dytila_m_ques_ans.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }


        notFound_layout=(RelativeLayout)v.findViewById(R.id.notFoundLayout);
        notFound_layout.setVisibility(View.GONE);

        return v;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void getQuestions(String userid_val){
        String url="https://dytila.herokuapp.com/api/askDytila/myQues/"+userid_val;
        r.setVisibility(View.VISIBLE);
        RequestQueue requestQueue=new Volley().newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    List<AskDytila_mQuesModel> askDytila_mQuesModelList=new ArrayList<>();
                    JSONArray parentArray=response.getJSONArray("mQuestions");
                    if(parentArray.length()<1){
                        notFound_layout.setVisibility(View.VISIBLE);
                        mQuestion_list.setVisibility(View.GONE);
                    }
                    else{
                        for(int i=0;i<parentArray.length();i++){
                            JSONObject finalObject=parentArray.getJSONObject(i);
                            AskDytila_mQuesModel askDytila_mQuesModel=new AskDytila_mQuesModel();
                            askDytila_mQuesModel.setQue_id(finalObject.getString("que_id"));
                            askDytila_mQuesModel.setQue(finalObject.getString("que"));
                            askDytila_mQuesModel.setQue_date(finalObject.getString("que_date"));
                            askDytila_mQuesModelList.add(askDytila_mQuesModel);
                        }
                        postExecute(askDytila_mQuesModelList);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),"Some Thing Went Wrong! Please Try Again!",Toast.LENGTH_SHORT).show();
                }
                mQuestion_list.setVisibility(View.VISIBLE);
                r.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Some Thing Went Wrong! Please Try Again!",Toast.LENGTH_SHORT).show();
                mQuestion_list.setVisibility(View.VISIBLE);
                r.setVisibility(View.GONE);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void postExecute(List<AskDytila_mQuesModel> result){

        AskDytila_mQuesModelAdapter askDytila_mQuesModelAdapter=new AskDytila_mQuesModelAdapter(getActivity().getApplicationContext(),R.layout.row_mques,result);
        mQuestion_list.setAdapter(askDytila_mQuesModelAdapter);
    }
    class AskDytila_mQuesModelAdapter extends ArrayAdapter {
        public List<AskDytila_mQuesModel> askDytila_mQuesModelList;
        private int resource;

        private LayoutInflater inflater;

        public AskDytila_mQuesModelAdapter(Context context, int resource, List<AskDytila_mQuesModel> objects) {
            super(context, resource, objects);
            askDytila_mQuesModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_mques, null);
            }

            final TextView date,que_text;
            date=(TextView)convertView.findViewById(R.id.date);date.setText(askDytila_mQuesModelList.get(position).getQue_date());
            que_text=(TextView)convertView.findViewById(R.id.question);que_text.setText(makeFirstUpper(askDytila_mQuesModelList.get(position).getQue()));

            RelativeLayout seeAnswersLayout=(RelativeLayout)convertView.findViewById(R.id.seeAnswers);
            seeAnswersLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), AskDytila_allAnswers.class);
                    intent.putExtra("que_id",askDytila_mQuesModelList.get(position).getQue_id());
                    intent.putExtra("que",askDytila_mQuesModelList.get(position).getQue());
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }
    public String makeFirstUpper(String val){
        String final_answer="";
        final_answer+=String.valueOf(val.charAt(0)).toUpperCase();
        final_answer+=val.substring(1);
        return final_answer;
    }

}
