package tu.thesis.onlinebanking.pk.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import tu.thesis.onlinebanking.pk.Class.DateTime;
import tu.thesis.onlinebanking.pk.R;


public class Forgine_Currency extends Fragment {

    String url = "https://openexchangerates.org/api/latest.json?app_id=da124d8303794b239ac25e70ea308d1e";
    StringRequest stringRequest;
    String rateMMK,rateEUR,rateJPY,rateCNY,rateINR, rateTHB,rateSGD;
    TextView tvUSD,tvEUR,tvJPY,tvCNY,tvINR,tvTHB,tvSGD,tvtime;
    Button btn_req,ok;
    float mmk=00.0f,eur=0.00f,result=0.00f;
  //  FragmentTransaction fTransaction;

    private ProgressDialog pDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forgine__currency, container, false);

        //addListenerOnSpinnerItemSelection();

        tvUSD=(TextView) v.findViewById(R.id.tvUSD);
        tvtime = v.findViewById(R.id.time);
        tvEUR = v.findViewById(R.id.tvEUR);
        tvJPY = v.findViewById(R.id.tvJPY);
        tvCNY = v.findViewById(R.id.tvCNY);
        tvINR = v.findViewById(R.id.tvINR);
        tvTHB = v.findViewById(R.id.tvTHB);
        tvSGD = v.findViewById(R.id.tvSGD);
        btn_req = v.findViewById(R.id.btn_req);
        ok = v.findViewById(R.id.ok);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait...");
        showDialog();

        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

// Result handling
                        try
                        {
                            JSONObject obj=new JSONObject(response);
                            String timestamp=obj.get("timestamp").toString();
                            long ticks=Long.parseLong(timestamp)*1000;
                            String date=DateTime.Date(ticks);
                            String time= DateTime.Time(ticks);
                            obj=obj.getJSONObject("rates");
                             rateMMK=obj.get("MMK").toString();
                             rateEUR = obj.get("EUR").toString();
                             rateJPY = obj.get("JPY").toString();
                             rateCNY = obj.get("CNY").toString();
                             rateINR = obj.get("INR").toString();
                             rateTHB = obj.get("THB").toString();
                             rateSGD = obj.get("SGD").toString();


                            String sTime="Date: "+date+"\n"+"Time: "+
                                    time;
                           // String mmk = "US 1$ = ျမန္မာ ေငြ "+rateMMK+" Kyats";
                            tvtime.setText(sTime);
                            tvUSD.setText( "USD 1$ = ျမန္မာ ေငြ "+rateMMK+" Kyats");
                            hideDialog();

                        }
                        catch (JSONException e)
                        {tvtime.setText(response);}

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

// Error handling
                tvtime.setText(error.toString());

            }
        });


        Volley.newRequestQueue(getActivity()).add(stringRequest);





        btn_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
float jpy=0.00f,cny=0.00f,inr=0.00f,thb=0.00f,sgd=0.00f,jpyResult=0.00f,cnyResult=0.00f,inrResult=0.00f,thbResult=0.00f,sgdResult=0.00f;

                mmk = Float.parseFloat(rateMMK);
                eur = Float.parseFloat(rateEUR);
                jpy = Float.parseFloat(rateJPY);
                cny = Float.parseFloat(rateCNY);
                inr = Float.parseFloat(rateINR);
                thb = Float.parseFloat(rateTHB);
                sgd = Float.parseFloat(rateSGD);

                result = mmk/eur;
                tvEUR.setVisibility(View.VISIBLE);

                jpyResult = mmk/jpy;
                cnyResult = mmk/cny;
                inrResult = mmk/inr;
                thbResult = mmk/thb;
                sgdResult = mmk/sgd;
//
                tvEUR.setText("1 EUR (EURO) = "+result+"  Kyats");
                tvEUR.setVisibility(View.VISIBLE);

                tvJPY.setText("1 Yen (Japan) = " +jpyResult+" Kyats");
                tvJPY.setVisibility(View.VISIBLE);

                tvCNY.setText("1 Renminbi (China) = "+cnyResult+" Kyats");
                tvCNY.setVisibility(View.VISIBLE);

                tvINR.setText("1 Rupees (India) = " +inrResult+" Kyats");
                tvINR.setVisibility(View.VISIBLE);

                tvTHB.setText("1 Baht (Thailand) = "+thbResult+" Kyats");
                tvTHB.setVisibility(View.VISIBLE);

                tvSGD.setText("1 Dollar (Singapore) = "+ sgdResult+" Kyats");
                tvSGD.setVisibility(View.VISIBLE);


            }
        });

ok.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fTransaction.replace(R.id.frame_layout,new Fragment_Main()).commit();
    }
});






        return  v;
    }


//    public void onClick(View v){
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
//    }
@Override
public void onDetach() {
    FragmentTransaction fTransaction = getActivity().getSupportFragmentManager().beginTransaction();
    fTransaction.replace(R.id.frame_layout,new Fragment_Main()).commit();
    super.onDetach();

}

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
