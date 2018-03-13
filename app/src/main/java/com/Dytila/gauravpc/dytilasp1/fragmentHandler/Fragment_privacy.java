package com.Dytila.gauravpc.dytilasp1.fragmentHandler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Dytila.gauravpc.dytilasp1.R;

/**
 * Created by gaurav pc on 04-Jan-17.
 */
public class Fragment_privacy extends Fragment{

    TextView terms1,terms2,terms3,terms4;
    TextView pterms1_text,pterms2_text,pterms3_text,pterms4_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_privacy, null);

        //privacy Policy

        pterms1_text=(TextView)v.findViewById(R.id.pterms1);
        String pterms_val="We collect information from you when you register on our site, place an order, subscribe to a newsletter or enter information on our site.";
        pterms1_text.setText(pterms_val);

        pterms2_text=(TextView)v.findViewById(R.id.pterms2);
        String pterms2_val="We may use the information we collect from you when you register, make a purchase, sign up for our newsletter, respond to a survey or marketing communication, surf the website, or use certain other site features in the following ways:\n" +
                "\n\n" +
                "To personalize your experience and to allow us to deliver the type of content and product offerings in which you are most interested.\n" +
                "To improve our website in order to better serve you.\n\n" +
                "To improve our website in order to better serve you.\n\n" +
                "To allow us to better service you in responding to your customer service requests.\n\n" +
                "To administer a contest, promotion, survey or other site feature.\n\n" +
                "To ask for ratings and reviews of services or products\n\n" +
                "To follow up with them after correspondence (live chat, email or phone inquiries)\n\n";
        pterms2_text.setText(pterms2_val);

        pterms3_text=(TextView)v.findViewById(R.id.pterms3);
        String pterms3_val="Yes. Cookies are small files that a site or its service provider transfers to your computer's hard drive through your Web browser (if you allow) that enables the site's or service provider's systems to recognize your browser and capture and remember certain information. For instance, we use cookies to help us remember and process the items in your shopping cart. They are also used to help us understand your preferences based on previous or current site activity, which enables us to provide you with improved services. We also use cookies to help us compile aggregate data about site traffic and site interaction so that we can offer better site experiences and tools in the future. ";
        pterms3_text.setText(pterms3_val);

        pterms4_text=(TextView)v.findViewById(R.id.pterms4);
        String pterms4_val="Help remember and process the items in the shopping cart.\n\n" +
                "Understand and save user's preferences for future visits.\n\n" +
                "Keep track of advertisements.";
        pterms4_text.setText(pterms4_val);

        return v;
    }
}
