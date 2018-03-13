package com.Dytila.gauravpc.dytilasp1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.Dytila.gauravpc.dytilasp1.mealsHandler.MapModel;
import com.Dytila.gauravpc.dytilasp1.models.FiModel;
import com.Dytila.gauravpc.dytilasp1.models.MyLocListener;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mealHandler extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView mealTypeHeadText,mealName,priceText,position,arrayVal,url,program,type,protein,carbs,fats,calories,items_includedText,descriptionText,bookThisMeal,infoText;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    ImageView goBack,mealPicImg;
    Button checkout_Button,monthlyplan_button;
    CardView macroContentCardView;
    private ProgressDialog pDialog;
    double myLat, myLang;
    private static DecimalFormat df2 = new DecimalFormat(".##");
    MyLocListener myLocListener;
    double minDistanceForDytila=5000;
    List<MapModel> mapModelList=new ArrayList<>();

    
    
    String id,price,pos_val,arrayValue_val,urlValue_val,img_val,name_val,type_val,program_val,protein_val,carbs_val,fats_val,calories_val,items_included_val,fi_val,description_val,status,isFav_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        final String userid_val=sharedPreferences.getString("user_id", "");

        SharedPreferences.Editor editor=sharedPreferences.edit();
        final String mKitchens=sharedPreferences.getString("mKitchen", "");

        mealPicImg=(ImageView)findViewById(R.id.mealPic);

        macroContentCardView=(CardView)findViewById(R.id.macroContentLayout);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        mealTypeHeadText=(TextView)findViewById(R.id.mealType);
        mealName=(TextView)findViewById(R.id.meal_name);
        priceText=(TextView)findViewById(R.id.price);
        program=(TextView)findViewById(R.id.program);
//        type=(TextView)findViewById(R.id.type);
        protein=(TextView)findViewById(R.id.protein);
        carbs=(TextView)findViewById(R.id.carbs);
        fats=(TextView)findViewById(R.id.fats);
        calories=(TextView)findViewById(R.id.calories);
        items_includedText=(TextView)findViewById(R.id.items_included);
        descriptionText=(TextView)findViewById(R.id.description);

        final Intent intent=getIntent();

        id=intent.getStringExtra("id");
        pos_val=intent.getStringExtra("position");
         arrayValue_val=intent.getStringExtra("arrayVal");
        urlValue_val=intent.getStringExtra("url");
         img_val=intent.getStringExtra("img");
         name_val=intent.getStringExtra("name");
         type_val=intent.getStringExtra("type");
         program_val=intent.getStringExtra("program");
        protein_val=intent.getStringExtra("protein");
        carbs_val=intent.getStringExtra("carbs");
        fats_val=intent.getStringExtra("fats");
        calories_val=intent.getStringExtra("calories");
        price=intent.getStringExtra("price");
        items_included_val=intent.getStringExtra("items_included");
        fi_val=intent.getStringExtra("fi");
        description_val=intent.getStringExtra("description");
         status=intent.getStringExtra("status");
        isFav_val=intent.getStringExtra("isFav");

        ImageLoader.getInstance().displayImage(img_val, mealPicImg);

        if(arrayValue_val.equals("juices")){
            macroContentCardView.setVisibility(View.GONE);
        }


//        Operation on featured list
        String[] fi_array=fi_val.split(",");
        List<FiModel> fiModelList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.row_fi, fi_array);

        for(int i=0;i<fi_array.length;i+=3){
            FiModel fiModel=new FiModel();
            fiModel.setName(fi_array[i]);
            fiModel.setDesc(fi_array[i + 1]);
            fiModel.setImg(fi_array[i + 2]);
            fiModelList.add(fiModel);
        }

        ExpandableHeightListView expandableListView = (ExpandableHeightListView) findViewById(R.id.fi_listView);

//        ListView listView = (ListView) findViewById(R.id.fi_listView);

        FiAdapter fiAdapter = new FiAdapter(getApplicationContext(), R.layout.row_fi, fiModelList);
        expandableListView.setAdapter(fiAdapter);
        // This actually does the magic
        expandableListView.setExpanded(true);
        expandableListView.setItemsCanFocus(false);

//        listView.setAdapter(mealsAdapter);
//        listView.set

        if(arrayValue_val.equals("vegMeals")){
            mealTypeHeadText.setText("Veg Meals");
        }else if(arrayValue_val.equals("nonVegMeals")){
            mealTypeHeadText.setText("Non-Veg Meals");
        }else if(arrayValue_val.equals("eggMeals")){
            mealTypeHeadText.setText("Non-Veg Meals");
        }else if(arrayValue_val.equals("shakes")){
            mealTypeHeadText.setText("Shakes");
        }else if(arrayValue_val.equals("juices")){
            mealTypeHeadText.setText("Juices");
        }

        mealName.setText(upperFirst(name_val));
//        mealName.setText(upperFirst(name_val));
        if(status.equals("coming soon")){
            priceText.setText("Coming Soon!");
        }else{
            priceText.setText(price);
        }
        program.setText(upperFirst("dytila " + program_val + " program"));
//        type.setText(upperFirst(type_val + " meal"));
        protein.setText(protein_val);
        carbs.setText(carbs_val);
        fats.setText(fats_val);
        calories.setText(calories_val);
        descriptionText.setText(description_val);
        items_includedText.setText(makeList(items_included_val));


            RelativeLayout proceedToCheckOutLayout=(RelativeLayout)findViewById(R.id.proceedToCheckOutLayout);
        proceedToCheckOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("coming soon")){
                    comingSoon();
                }
                else{
                    if(mKitchens.equals("null")){
                        final Dialog dialog = new Dialog(mealHandler.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.can_order_layout);
                        TextView t1Text=(TextView)dialog.findViewById(R.id.t1);
                        t1Text.setText("Oops! o_O");
                        TextView t2Text=(TextView)dialog.findViewById(R.id.t2);
                        t2Text.setText("Please let us find a kitchen near you!");
                        TextView t3Text=(TextView)dialog.findViewById(R.id.t3);
                        t3Text.setText("Go to home and click on 'My Dytila Kitchen' and then find again :)");
                        TextView close = (TextView) dialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else if(mKitchens.equals("unavailable")){
                        final Dialog dialog = new Dialog(mealHandler.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.can_order_layout);
                        TextView close = (TextView) dialog.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }else{
                        checkRes();
                    }
                }
            }
        });

        ImageView gobackImage=(ImageView)findViewById(R.id.gobackImage);
        gobackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayout layoutToFocus=(LinearLayout)findViewById(R.id.layoutToFocus);
        layoutToFocus.requestFocus();


//        Add Meal to Fav Operation
        final ImageView addToFavMealImage=(ImageView)findViewById(R.id.addToFavMeal);
        if(isFav_val.equals("yes"))
            addToFavMealImage.setImageResource(R.drawable.like);
        else
            addToFavMealImage.setImageResource(R.drawable.unlike);
        addToFavMealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFav(userid_val,id,arrayValue_val);
            }
        });

    }

    public void addToFav(final String user_id,final String meal_id,final String meal_type){

        String url="https://dytila.herokuapp.com/api/regular_orders/meal/add_to_fav";
        RequestQueue requestQueue=new Volley().newRequestQueue(mealHandler.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if(status.equals("added")){
                                ImageView imageView=(ImageView)findViewById(R.id.addToFavMeal);
                                imageView.setImageResource(R.drawable.like);
                            }
                            else if(status.equals("removed")){
                                ImageView imageView=(ImageView)findViewById(R.id.addToFavMeal);
                                imageView.setImageResource(R.drawable.unlike);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("meal_id",meal_id);
                params.put("meal_type",meal_type);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void checkRes(){
        String url="https://dytila.herokuapp.com/api/admin/res/check";

        pDialog.show();
        RequestQueue requestQueue=new Volley().newRequestQueue(mealHandler.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        String status =null;

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            status=jsonObject.getString("status");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status.equals("open")){

                            Intent intent1=new Intent(mealHandler.this,checkout.class);
                            intent1.putExtra("id",id);
                            intent1.putExtra("food_type",arrayValue_val);
                            intent1.putExtra("mealName",name_val);
                            intent1.putExtra("programName",program_val);
                            intent1.putExtra("type",type_val);
                            intent1.putExtra("food_img",img_val);
                            intent1.putExtra("price",price);
                            startActivity(intent1);

                        }else if(status.equals("close")){

                            final Dialog dialog = new Dialog(mealHandler.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.info_layout);
                            TextView t1Text,t2Text,close;
                            t1Text= (TextView) dialog.findViewById(R.id.t1);
                            t1Text.setText("Sorry, Dytila Kitchen is closed right now :(");
                            t2Text= (TextView) dialog.findViewById(R.id.t2);t2Text.setVisibility(View.GONE);
                            close= (TextView) dialog.findViewById(R.id.close);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something Went Wrong!",Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
        requestQueue.add(stringRequest);
    }
    public class FiAdapter extends ArrayAdapter<FiModel> {
        public List<FiModel> fiModelList;
        private int resource;
        private LayoutInflater inflater;

        public FiAdapter(Context context, int resource, List<FiModel> objects) {
            super(context, resource, objects);
            fiModelList=objects;
            this.resource=resource;
            inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(position%2==0){
                if(convertView==null) {
                    convertView = inflater.inflate(R.layout.row_fi, null);
                }
            }else{
                if(convertView==null) {
                    convertView = inflater.inflate(R.layout.row_fi2, null);
                }
            }
            ImageView fiImage=(ImageView)convertView.findViewById(R.id.fi_image);
            ImageLoader.getInstance().displayImage("https://dytila.herokuapp.com/static/img/app%20pics/fi/"+fiModelList.get(position).getImg(), fiImage);

            TextView fiName,fiDesc;
            fiName=(TextView)convertView.findViewById(R.id.fi_name);
            fiDesc=(TextView)convertView.findViewById(R.id.fi_desc);
            fiName.setText(fiModelList.get(position).getName());
            fiDesc.setText(fiModelList.get(position).getDesc());
            return convertView;
        }
    }


    public String makeList(String str){
        String[] new_str=str.split(",");
        String val="";
        for(int i=0;i<new_str.length;i++){
            val+="\n \u2022  "+new_str[i];
        }
        return upperFirst(val);
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
    public void comingSoon(){
        final Dialog dialog = new Dialog(mealHandler.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coming_soon_layout);
        TextView close = (TextView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}