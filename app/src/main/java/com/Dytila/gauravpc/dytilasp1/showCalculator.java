package com.Dytila.gauravpc.dytilasp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class showCalculator extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    TextView user_name,user_email;
    Spinner ageSpinner,weightSpinner,heightSpinner,exerciseLevelSinner;
    CardView card1,card2,card3,resultCard;
    ImageView userAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get shared Preferences
        SharedPreferences sharedPreferences=getSharedPreferences("userInfo", Context.MODE_APPEND);
        String username_val=sharedPreferences.getString("username", "");
        String userid_val=sharedPreferences.getString("id","");
        String mobile_val=sharedPreferences.getString("mobile","");
        String email_val=sharedPreferences.getString("email","");
        String order_id=sharedPreferences.getString("order_id","");
        String avatar_val=sharedPreferences.getString("avatar","");
        //Shared Preferences Ends

        //Navigation Operation Begins

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        RelativeLayout navHeaderLayout=(RelativeLayout)findViewById(R.id.navHeader);
        navHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(showCalculator.this,account.class);
                startActivity(intent);
            }
        });
        user_name=(TextView)findViewById(R.id.username);
        user_email=(TextView)findViewById(R.id.user_email);
        user_name.setText(username_val);
        user_email.setText(email_val);
        userAvatar=(ImageView)findViewById(R.id.userImg);
        ImageLoader.getInstance().displayImage(avatar_val, userAvatar);
        LinearLayout homeLayout,accountLayout,myOrdersLayout,billsLayout,renewLayout,logoutLayout,aboutUsLayout,favMealsLayout;
        homeLayout=(LinearLayout)findViewById(R.id.home);
        accountLayout=(LinearLayout)findViewById(R.id.account);
        myOrdersLayout=(LinearLayout)findViewById(R.id.my_orders);
        favMealsLayout=(LinearLayout)findViewById(R.id.fav_meals);
        billsLayout=(LinearLayout)findViewById(R.id.bills);
        renewLayout=(LinearLayout)findViewById(R.id.renew);
        logoutLayout=(LinearLayout)findViewById(R.id.logout);
        aboutUsLayout=(LinearLayout)findViewById(R.id.aboutus);

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(showCalculator.this,features.class);
                startActivity(intent);
                finish();
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(showCalculator.this,account.class);
                startActivity(intent);
                finish();
            }
        });
        myOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(showCalculator.this,Order_details.class);
                startActivity(intent);
                finish();
            }
        });
        favMealsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(showCalculator.this, FavMeals.class);
                startActivity(intent);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(showCalculator.this,bills.class);
                startActivity(intent);
                finish();
            }
        });
        renewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(showCalculator.this,renew.class);
                startActivity(intent);
                finish();
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences=getSharedPreferences("userInfo",Context.MODE_APPEND);
                sharedPreferences.edit().clear().commit();
                String loginMsg="0";
                try {
                    FileOutputStream fileOutputStream=openFileOutput("Dytila Login",MODE_APPEND);
                    fileOutputStream.write(loginMsg.getBytes());
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"Logout Successfully",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(showCalculator.this,activity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        aboutUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(showCalculator.this,about_us.class);
                startActivity(intent);
            }
        });
        //Navigation Operation Ends

        String calType=getIntent().getExtras().getString("calType","");

//


        resultCard=(CardView)findViewById(R.id.result);resultCard.setVisibility(View.GONE);
        card1=(CardView)findViewById(R.id.cc);card1.setVisibility(View.GONE);
//        card1=(CardView)findViewById(R.id.result);card2.setVisibility(View.GONE);
        card3=(CardView)findViewById(R.id.bmiCal);card3.setVisibility(View.GONE);

        if(calType.equals("bodyFatCal")){

        }
        else if(calType.equals("calorieCal")){

            card1.setVisibility(View.VISIBLE);

            ageSpinner=(Spinner)findViewById(R.id.age);
            weightSpinner=(Spinner)findViewById(R.id.weight);
            heightSpinner=(Spinner)findViewById(R.id.height);
            exerciseLevelSinner=(Spinner)findViewById(R.id.exercise_level);

            try {
                Field popup=Spinner.class.getDeclaredField("mPopup");
                popup.setAccessible(true);
                android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(heightSpinner);
                popupWindow.setHeight(1000);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            List<String>list1=new ArrayList<String>();
            List<String>list2=new ArrayList<String>();
            List<String>list3=new ArrayList<String>();

            list1.add("Select Age");
            for(int i=1;i<=90;i++){
                list1.add(String.valueOf(i));
            }
            list2.add("Select Weight(KGs)");
            for(int i=20;i<=150;i++){
                list2.add(String.valueOf(i));
            }
            list3.add("Select Height(foot & inches)");
            for(int i=4;i<=7;i++){
                for(int j=0;j<=11;j++){
                    list3.add(i+"'"+j+"''");
                }
            }

            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list3);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            ageSpinner.setAdapter(dataAdapter1);
            weightSpinner.setAdapter(dataAdapter2);
            heightSpinner.setAdapter(dataAdapter3);

            ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.exerciselevels,android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            exerciseLevelSinner.setAdapter(adapter);

            ageSpinner.setOnItemSelectedListener(this);
            weightSpinner.setOnItemSelectedListener(this);
            heightSpinner.setOnItemSelectedListener(this);
            exerciseLevelSinner.setOnItemSelectedListener(this);

            Button submit=(Button)findViewById(R.id.ccsubmit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RadioGroup radioGroup=(RadioGroup)findViewById(R.id.sexSelector);
                    int id=radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton=(RadioButton)findViewById(id);
                    if (radioGroup.getCheckedRadioButtonId() == -1)
                    {
                        Toast.makeText(getApplicationContext(),"Please select your sex",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String sex = radioButton.getText().toString();
                        String age=ageSpinner.getSelectedItem().toString();
                        String weight=weightSpinner.getSelectedItem().toString();
                        String h=heightSpinner.getSelectedItem().toString();
                        String exerciseLevel=exerciseLevelSinner.getSelectedItem().toString();

                        if(age.equals("Select Age")){
                            Toast.makeText(getApplicationContext(),"Please specify your age",Toast.LENGTH_SHORT).show();
                        }else{
                            if(weight.equals("Select Weight(KGs)")){
                                Toast.makeText(getApplicationContext(),"Please specify your weight",Toast.LENGTH_SHORT).show();
                            }else{
                                if(h.equals("Select Height(foot & inches)")){
                                    Toast.makeText(getApplicationContext(),"Please specify your height",Toast.LENGTH_SHORT).show();
                                }else{

                                    int foot,inches;
                                    String i;
                                    foot=Integer.parseInt(String.valueOf(h.charAt(0)));
                                    if(String.valueOf(h.charAt(3)).equals("'")){
                                        i=String.valueOf(h.charAt(2));
                                    }
                                    else{
                                        i=String.valueOf(h.charAt(2))+String.valueOf(h.charAt(3));
                                    }
                                    inches = Integer.parseInt(i);
                                    float height_in_centimeters;
                                    height_in_centimeters= ((float) (foot*0.3048) + (float) (inches*0.0254))*100;

                                    int a=Integer.parseInt(age);
                                    int w=Integer.parseInt(weight);

                                    float BMR;
                                    BMR= (float) ((10*w)+(6.25*height_in_centimeters)-(5*a));

                                    String BMRresult = String.format("%.2f", BMR);

                                    double calorieIntake = 0;
                                    String calorieIntakeResult = null;
                                    String finalAnswer;
                                    if(exerciseLevel.equals("No Exercise")){
                                        calorieIntake=BMR * 1.2;
                                        calorieIntakeResult=String.format("%.2f",calorieIntake);
                                    }
                                    else if(exerciseLevel.equals("Light Exercise(1-3 days a week)")){
                                        calorieIntake=BMR * 1.375;
                                        calorieIntakeResult=String.format("%.2f",calorieIntake);
                                    }
                                    else if(exerciseLevel.equals("Moderate Exercise(3-5 days a week)")){
                                        calorieIntake=BMR * 1.55;
                                        calorieIntakeResult=String.format("%.2f",calorieIntake);
                                    }
                                    else if(exerciseLevel.equals("Heavy Exercise(6-7 days a week)")){
                                        calorieIntake=BMR * 1.725;
                                        calorieIntakeResult=String.format("%.2f",calorieIntake);
                                    }
                                    else if(exerciseLevel.equals("Very Heavy Exercise (twice a day)")){
                                        calorieIntake=BMR * 1.9;
                                        calorieIntakeResult=String.format("%.2f",calorieIntake);
                                    }
                                    finalAnswer="Your BMR is "+BMRresult+"\n\n Your Daily Calories Need :  "+calorieIntakeResult+"Kcal/day";

                                    resultCard.setVisibility(View.VISIBLE);
                                    TextView resultText;
                                    resultText=(TextView)findViewById(R.id.result_text);
                                    resultText.setText(finalAnswer);
                                    resultText.setTextSize(7 * getResources().getDisplayMetrics().density);
                                }
                            }
                        }
                    }



                }
            });

        }
        else if(calType.equals("bmiCal")){

            card3.setVisibility(View.VISIBLE);

            final Spinner weightSpinner,heightSpinner;


            weightSpinner=(Spinner)findViewById(R.id.bmiweight);
            heightSpinner=(Spinner)findViewById(R.id.bmiheight);
            List<String>list2=new ArrayList<String>();
            List<String>list3=new ArrayList<String>();

            list2.add("Select Weight(KGs)");
            for(int i=20;i<=150;i++){
                list2.add(String.valueOf(i));
            }
            list3.add("Select Height(foot & inches)");
            for(int i=4;i<=7;i++){
                for(int j=0;j<=11;j++){
                    list3.add(i+"'"+j+"''");
                }
            }

            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list3);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            weightSpinner.setAdapter(dataAdapter2);
            heightSpinner.setAdapter(dataAdapter3);

            weightSpinner.setOnItemSelectedListener(this);
            heightSpinner.setOnItemSelectedListener(this);

            Button submitButton;
            submitButton=(Button)findViewById(R.id.bmiSubmitButton);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String w=weightSpinner.getSelectedItem().toString();
                    String h=heightSpinner.getSelectedItem().toString();

                    if(w.equals("Select Weight(KGs)")){
                        Toast.makeText(getApplicationContext(),"Please specify you weight",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (h.equals("Select Height(foot & inches)")) {
                            Toast.makeText(getApplicationContext(), "Please specify you weight", Toast.LENGTH_SHORT).show();
                        } else {
                            int weight,foot,inches;

                            weight=Integer.parseInt(w);

                            String i;
                            foot=Integer.parseInt(String.valueOf(h.charAt(0)));
                            if(String.valueOf(h.charAt(3)).equals("'")){
                                i=String.valueOf(h.charAt(2));
                            }
                            else{
                                i=String.valueOf(h.charAt(2))+String.valueOf(h.charAt(3));
                            }
                            inches = Integer.parseInt(i);

                            Float bmi;
                            //convert foot and inches into meters
                            float height_in_meters;
                            height_in_meters= ((float) (foot*0.3048) + (float) (inches*0.0254));
                            bmi=weight/(height_in_meters*height_in_meters);
                            String result = String.format("%.2f", bmi);

                            //Showing Result
                            resultCard.setVisibility(View.VISIBLE);
                            TextView resultText;
                            resultText=(TextView)findViewById(R.id.result_text);

                            if(bmi<18.5){
                                resultText.setText("BMI = "+result+" Underweight");
                                resultText.setTextColor(Color.parseColor("#d30707"));
                            }
                            else if(bmi>=18.5 && bmi<25){
                                resultText.setText("BMI = "+result+" Normal");
                                resultText.setTextColor(Color.parseColor("#308921"));
                            }
                            else if(bmi>=25 && bmi<30){
                                resultText.setText("BMI = "+result+" Overweight");
                                resultText.setTextColor(Color.parseColor("#d30707"));
                            }
                            else if(bmi>=30 && bmi<40){
                                resultText.setText("BMI = "+result+" Obese");
                                resultText.setTextColor(Color.parseColor("#ab0303"));
                            }
                            else if(bmi>=30){
                                resultText.setText("BMI = "+result+" Morbid Obesity");
                                resultText.setTextColor(Color.parseColor("#720909"));
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spin = (Spinner)parent;

        if(spin.getId()==R.id.bmiweight){

        }
        else if(spin.getId()==R.id.bmiheight){

        }

//        if(spin.getId()==R.id.age){
//
//        }
//        else if(spin.getId()==R.id.weight){
//
//        }
//        else if(spin.getId()==R.id.height){
//
//        }
//        else if(spin.getId()==R.id.exercise_level){
//
//        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("menu option", "Menu Made");
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

//    onNavigationItemSelected

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("menu option", "Menu item now");
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


}

