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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import tu.thesis.onlinebanking.pk.Class.DateTime;
import tu.thesis.onlinebanking.pk.R;


public class Money_Calculator extends Fragment {
    TextView tv_rates,result,date;
    EditText et_cal;

    String rate,sub;
    String date_time;
    Button btnCalculate,btnOk;
    String url = "https://openexchangerates.org/api/latest.json?app_id=da124d8303794b239ac25e70ea308d1e";
    StringRequest stringRequest;
    private ProgressDialog pDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_money__calculator, container, false);

        tv_rates = v.findViewById(R.id.tv_rates);
        et_cal = v.findViewById(R.id.et_cal);
        btnCalculate = v.findViewById(R.id.btnCalculate);
        btnOk = v.findViewById(R.id.btnOK);
        result = v.findViewById(R.id.result);
        date = v.findViewById(R.id.date);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Please wait...");
        showDialog();
        //addListenerOnSpinnerItemSelection();
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
                             date_time= DateTime.Date(ticks);
                            String time= DateTime.Time(ticks);
                            obj=obj.getJSONObject("rates");
                             rate=obj.get("MMK").toString();
                           // rate.substring(0,7);

                         sub   =    rate.substring(0,7);
                          //  String output="Date: "+date+"\n"+"Time: "+
                            //        time+"\n"+
                              //      "US 1$ = ျမန္မာ ေငြ "+rate+" Kyats";
                            tv_rates.setText("The last exchange rate of US 1 $ is " + sub + " Kyats");
                            hideDialog();

                        }
                        catch (JSONException e)
                        {tv_rates.setText(response);
                            hideDialog();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

// Error handling
                tv_rates.setText(error.toString());
                hideDialog();

            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);

btnCalculate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      //  String cal = et_cal.getText().toString();
        float calculate = Float.valueOf(et_cal.getText().toString().trim());
        Float mutiply = Float.parseFloat(sub);

        float x = (float) (calculate * mutiply);

        result.setText(calculate+""+" USD = "+x+"" + " MMK");
        date.setText("Indicative Rate as on "+date_time);
        btnOk.setVisibility(View.VISIBLE);

      //  Toast.makeText(getActivity(),"work",Toast.LENGTH_SHORT).show();



    }
});
btnOk.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.frame_layout,new Fragment_Main()).commit();



    }
});


        return  v;
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
