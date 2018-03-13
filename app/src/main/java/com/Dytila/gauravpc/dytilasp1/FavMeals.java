package com.Dytila.gauravpc.dytilasp1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.Dytila.gauravpc.dytilasp1.models.FavMealModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavMeals extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView favMealsListView;
    RelativeLayout no_found_layout;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_meals);

        pDialog = new ProgressDialog(FavMeals.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        final String userid_val=sharedPreferences.getString("user_id", "");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        no_found_layout=(RelativeLayout)findViewById(R.id.notFoundLayout);
        no_found_layout.setVisibility(View.GONE);

        favMealsListView=(ListView)findViewById(R.id.favMealsList);
        boolean check = isNetworkAvailable();
        if (check == true) {
            getData(userid_val);
        } else {
            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        ImageView imageView=(ImageView)findViewById(R.id.goBackImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void getData(String userid_val){
        String url="https://dytila.herokuapp.com/api/fav_meals/"+userid_val;
        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    List<FavMealModel> favMealModelList=new ArrayList<>();
                    JSONArray parentArray=response.getJSONArray("meals");
                    if(parentArray.length()<1){
                        no_found_layout.setVisibility(View.VISIBLE);
                        favMealsListView.setVisibility(View.GONE);
                    }
                    else{
                        for(int i=0;i<parentArray.length();i++){
                            JSONObject finalObject=parentArray.getJSONObject(i);
                            FavMealModel favMealModel=new FavMealModel();
                            favMealModel.setId(finalObject.getString("id"));
                            favMealModel.setName(finalObject.getString("name"));
                            favMealModel.setProgram(finalObject.getString("programs"));
                            favMealModel.setType(finalObject.getString("type"));
                            favMealModel.setImg(finalObject.getString("img"));
                            favMealModel.setProtein(finalObject.getString("protein"));
                            favMealModel.setCarbs(finalObject.getString("carbs"));
                            favMealModel.setFats(finalObject.getString("fats"));
                            favMealModel.setCalories(finalObject.getString("calories"));
                            favMealModel.setPrice(finalObject.getString("price"));
                            favMealModel.setDescription(finalObject.getString("description"));
                            favMealModel.setItems_included(finalObject.getString("items_included"));
                            favMealModel.setFi(finalObject.getString("fi"));
                            favMealModel.setStatus(finalObject.getString("status"));
                            favMealModel.setIsFav(finalObject.getString("isFav"));
                            favMealModelList.add(favMealModel);
                        }
                        postExecute(favMealModelList);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong :( Please try again!", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void postExecute(List<FavMealModel> result){

        FavMealModelAdapter favMealModelAdapter=new FavMealModelAdapter(getApplicationContext(),R.layout.row_fav_meals,result);
        favMealsListView.setAdapter(favMealModelAdapter);
    }
    class FavMealModelAdapter extends ArrayAdapter {
        public List<FavMealModel> favMealModelList;
        private int resource;

        private LayoutInflater inflater;

        public FavMealModelAdapter(Context context, int resource, List<FavMealModel> objects) {
            super(context, resource, objects);
            favMealModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_fav_meals, null);
            }

            TextView mealName,mealPrice,mealProgram;
            ImageView img;

            mealName=(TextView)convertView.findViewById(R.id.mealName);
            mealPrice=(TextView)convertView.findViewById(R.id.price);
            mealProgram=(TextView)convertView.findViewById(R.id.program);

            mealName.setText(upperFirst(favMealModelList.get(position).getName()));
            mealPrice.setText(favMealModelList.get(position).getPrice());
            mealProgram.setText((favMealModelList.get(position).getProgram()));

            img=(ImageView)convertView.findViewById(R.id.mealImg);
            ImageLoader.getInstance().displayImage(favMealModelList.get(position).getImg(), img);

            CardView cardView=(CardView)convertView.findViewById(R.id.view);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FavMeals.this, mealHandler.class);
                    intent.putExtra("position", "position : " + position);
                    intent.putExtra("id", favMealModelList.get(position).getId());
                    intent.putExtra("arrayVal", "vegMeals");
                    intent.putExtra("url", "https://dytila.herokuapp.com/api/dytilaMainMenu/nonveg");
                    intent.putExtra("name", favMealModelList.get(position).getName());
                    intent.putExtra("type", favMealModelList.get(position).getType());
                    intent.putExtra("program", favMealModelList.get(position).getProgram());
                    intent.putExtra("img", favMealModelList.get(position).getImg());
                    intent.putExtra("protein", favMealModelList.get(position).getProtein());
                    intent.putExtra("fats", favMealModelList.get(position).getFats());
                    intent.putExtra("carbs", favMealModelList.get(position).getCarbs());
                    intent.putExtra("calories", favMealModelList.get(position).getCalories());
                    intent.putExtra("price", favMealModelList.get(position).getPrice());
                    intent.putExtra("items_included", favMealModelList.get(position).getItems_included());
                    intent.putExtra("fi",favMealModelList.get(position).getFi());
                    intent.putExtra("description", favMealModelList.get(position).getDescription());
                    intent.putExtra("status", favMealModelList.get(position).getStatus());
                    intent.putExtra("status", favMealModelList.get(position).getStatus());
                    intent.putExtra("isFav", favMealModelList.get(position).getIsFav());
                    startActivity(intent);
                }
            });

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
