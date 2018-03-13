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
import com.Dytila.gauravpc.dytilasp1.R;
import com.Dytila.gauravpc.dytilasp1.features;
import com.Dytila.gauravpc.dytilasp1.models.Monthly_user_model;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaurav pc on 04-Jan-17.
 */
public class Fragment_monthly_orders extends Fragment {

    private ProgressDialog pDialog;
    private ListView ordersList;
    TextView t1Text;
    RelativeLayout no_found_layout;
    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_monthly_user_order, null);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        final String userid_val=sharedPreferences.getString("user_id", "");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        no_found_layout=(RelativeLayout)v.findViewById(R.id.notFoundLayout);
        no_found_layout.setVisibility(View.GONE);

        button=(Button)v.findViewById(R.id.orderButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),features.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ordersList=(ListView)v.findViewById(R.id.orderList);
        boolean check = isNetworkAvailable();
        if (check == true) {
            getData(userid_val);
        } else {
            Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return v;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void getData(String userid_val){
        String url="https://dytila.herokuapp.com/api/monthly_order/"+userid_val;
        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    List<Monthly_user_model> monthly_order_userModeList=new ArrayList<>();
                    JSONArray parentArray=response.getJSONArray("order");
                    if(parentArray.length()<1){
                        no_found_layout.setVisibility(View.VISIBLE);
                        ordersList.setVisibility(View.GONE);
                    }
                    else{
                        for(int i=0;i<parentArray.length();i++){
                            JSONObject finalObject=parentArray.getJSONObject(i);
                            Monthly_user_model monthly_user_model=new Monthly_user_model();
                            monthly_user_model.setOrder_id(finalObject.getString("order_id"));
                            monthly_user_model.setMealName(finalObject.getString("meal_name"));
                            monthly_user_model.setPrice(finalObject.getString("price"));
                            monthly_user_model.setPickUpAddress(finalObject.getString("location"));
                            monthly_user_model.setTmeal_freq(finalObject.getString("Tmeal_freq"));
                            monthly_user_model.setMeal_freq(finalObject.getString("meal_freq"));
                            monthly_user_model.setTime(finalObject.getString("time"));
                            monthly_user_model.setAddons(finalObject.getString("addons"));
                            monthly_user_model.setOver(finalObject.getString("over"));
                            monthly_order_userModeList.add(monthly_user_model);
                        }
                        postExecute(monthly_order_userModeList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Something went wrong :( Please try again!", Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Something went wrong :( Please try again!", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void postExecute(List<Monthly_user_model> result){

        Monthly_order_userAdapter monthly_order_userAdapter=new Monthly_order_userAdapter(getActivity(),R.layout.row_monthly_user_order,result);
        ordersList.setAdapter(monthly_order_userAdapter);
    }
    class Monthly_order_userAdapter extends ArrayAdapter {
        public List<Monthly_user_model> monthly_user_modelList;
        private int resource;

        private LayoutInflater inflater;

        public Monthly_order_userAdapter(Context context, int resource, List<Monthly_user_model> objects) {
            super(context, resource, objects);
            monthly_user_modelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_monthly_user_order, null);
            }

            TextView mealName,mealPrice,order_id,location,time,Tmeal_freqText,meal_freqText,overText,addonsText;

            mealName=(TextView)convertView.findViewById(R.id.mealName);
            mealPrice=(TextView)convertView.findViewById(R.id.price);
            order_id=(TextView)convertView.findViewById(R.id.id);
            location=(TextView)convertView.findViewById(R.id.pickupAddress);
            time=(TextView)convertView.findViewById(R.id.timing);
            Tmeal_freqText=(TextView)convertView.findViewById(R.id.Tmeal_freq);
            meal_freqText=(TextView)convertView.findViewById(R.id.meal_freq);
            overText=(TextView)convertView.findViewById(R.id.over);
            addonsText=(TextView)convertView.findViewById(R.id.addons);

            mealName.setText(upperFirst(monthly_user_modelList.get(position).getMealName()));
            mealPrice.setText("₹ "+monthly_user_modelList.get(position).getPrice());
            order_id.setText(monthly_user_modelList.get(position).getOrder_id());
            location.setText(monthly_user_modelList.get(position).getPickUpAddress());
            time.setText(monthly_user_modelList.get(position).getTime());
            Tmeal_freqText.setText(monthly_user_modelList.get(position).getTmeal_freq());
            meal_freqText.setText(monthly_user_modelList.get(position).getMeal_freq());
            overText.setText(monthly_user_modelList.get(position).getOver());
            if(monthly_user_modelList.get(position).getAddons().equals("No Specification"))
                addonsText.setText("No Addons");
            else
                addonsText.setText(monthly_user_modelList.get(position).getAddons());

            return convertView;
        }

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
}
