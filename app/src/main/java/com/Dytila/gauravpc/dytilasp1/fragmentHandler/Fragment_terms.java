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
public class Fragment_terms extends Fragment {

    TextView terms1,terms2,terms3,terms4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_terms, null);

        //Terms and condition
        terms1=(TextView)v.findViewById(R.id.tterms1);
        String terms_val="It shall be your sole responsibility to maintain the required computer equipment and internet connection that may be necessary to access, use and transact on the website. You are supposed to use this website for lawful and reasonable purposes only.";
        terms1.setText(terms_val);

        terms2=(TextView)v.findViewById(R.id.tterms2);
        String terms2_val="You agree to indemnify, defend and hold harmless Dytilanutrition.com, its owner, affiliate and employees, from and against any claims, demands, liabilities, damages, costs and attorney expenses asserted against or incurred by Dytilanutrition.com that arise out of, result from, or may be payable by virtue of, any breach or non-performance of any representation, warranty, covenant or agreement made or obligation to be performed by you pursuant to these Terms and conditions.";
        terms2.setText(terms2_val);

        terms3=(TextView)v.findViewById(R.id.tterms3);
        String terms3_val="In order to purchase products from our website, you need to register as a user. You will receive a mail at the registered email address upon completing the website’s registration process. You shall be solely responsible for maintaining the confidentiality your account and password and would be responsible for any activity that takes place through your account.";
        terms3.setText(terms3_val);

        terms4=(TextView)v.findViewById(R.id.tterms4);
        String terms4_val="The validity of an order is subject to the availability of the product in our stock. It may happen that the website shows a product to be available but the product may actually not be present in our stock due to any reason. In such cases we reserve the right to cancel the order. Our liability is restricted to refunding the amount collected against the order, if any. We are not responsible for any loss incurred, tangible or otherwise, due to incompletion of the order.";
        terms4.setText(terms4_val);

        return v;
    }
}
