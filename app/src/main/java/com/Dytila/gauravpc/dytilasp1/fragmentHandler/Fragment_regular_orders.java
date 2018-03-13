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
import android.widget.ImageView;
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
import com.Dytila.gauravpc.dytilasp1.models.Regular_order_userModel;
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
public class Fragment_regular_orders extends Fragment {

    private ProgressDialog pDialog;
    private ListView ordersList;
    TextView t1Text;
    RelativeLayout no_found_layout;
    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_regular_orders, null);

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
        String url="https://dytila.herokuapp.com/api/regular_orders/"+userid_val;
        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    List<Regular_order_userModel> regular_order_userModeList=new ArrayList<>();
                    JSONArray parentArray=response.getJSONArray("order");
                    if(parentArray.length()<1){
                        no_found_layout.setVisibility(View.VISIBLE);
                        ordersList.setVisibility(View.GONE);
                    }
                    else{
                        for(int i=0;i<parentArray.length();i++){
                            JSONObject finalObject=parentArray.getJSONObject(i);
                            Regular_order_userModel regular_order_userModel=new Regular_order_userModel();
                            regular_order_userModel.setOrder_id(finalObject.getString("order_id"));
                            regular_order_userModel.setMealName(finalObject.getString("meal_name"));
                            regular_order_userModel.setPrice(finalObject.getString("price"));
                            regular_order_userModel.setItemCount(finalObject.getString("item_count"));
                            regular_order_userModel.setPickUpAddress(finalObject.getString("pickUpAddress"));
                            regular_order_userModel.setTime(finalObject.getString("time"));
                            regular_order_userModel.setAddons(finalObject.getString("addons"));
                            regular_order_userModel.setImg(finalObject.getString("img"));
                            regular_order_userModel.setDel_status(finalObject.getString("delivery"));
                            regular_order_userModeList.add(regular_order_userModel);
                        }
                        postExecute(regular_order_userModeList);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
    public void postExecute(List<Regular_order_userModel> result){

        Regular_order_userAdapter regular_order_userAdapter=new Regular_order_userAdapter(getActivity(),R.layout.row_regular_orders_user,result);
        ordersList.setAdapter(regular_order_userAdapter);
    }
    class Regular_order_userAdapter extends ArrayAdapter {
        public List<Regular_order_userModel> regular_order_userModelList;
        private int resource;

        private LayoutInflater inflater;

        public Regular_order_userAdapter(Context context, int resource, List<Regular_order_userModel> objects) {
            super(context, resource, objects);
            regular_order_userModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_regular_orders_user, null);
            }

            TextView mealName,mealPrice,count,order_id,location,time,delStatusText;
            ImageView img;

            mealName=(TextView)convertView.findViewById(R.id.mealName);
            mealPrice=(TextView)convertView.findViewById(R.id.price);
            count=(TextView)convertView.findViewById(R.id.itemCount);
            order_id=(TextView)convertView.findViewById(R.id.id);
            location=(TextView)convertView.findViewById(R.id.pickupAddress);
            time=(TextView)convertView.findViewById(R.id.timing);
            delStatusText=(TextView)convertView.findViewById(R.id.delStatus);

            mealName.setText(upperFirst(regular_order_userModelList.get(position).getMealName()));
            mealPrice.setText("₹ "+regular_order_userModelList.get(position).getPrice());
            count.setText(regular_order_userModelList.get(position).getItemCount());
            order_id.setText(regular_order_userModelList.get(position).getOrder_id());
            location.setText(regular_order_userModelList.get(position).getPickUpAddress());
            time.setText(regular_order_userModelList.get(position).getTime());
            delStatusText.setText(regular_order_userModelList.get(position).getDel_status());

            img=(ImageView)convertView.findViewById(R.id.mealImg);
            ImageLoader.getInstance().displayImage(regular_order_userModelList.get(position).getImg(), img);

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
