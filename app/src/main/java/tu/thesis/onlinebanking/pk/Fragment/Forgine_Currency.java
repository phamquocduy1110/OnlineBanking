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
    TextView tv;
    Button btn_req;
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

        tv=(TextView) v.findViewById(R.id.tv);
        btn_req = v.findViewById(R.id.btn_req);
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
                            String rate=obj.get("MMK").toString();
                            String output="Date: "+date+"\n"+"Time: "+
                                    time+"\n"+
                                    "US 1$ = ျမန္မာ ေငြ "+rate+" Kyats";
                            tv.setText(output);
                            hideDialog();

                        }
                        catch (JSONException e)
                        {tv.setText(response);}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

// Error handling
                tv.setText(error.toString());

            }
        });


        Volley.newRequestQueue(getActivity()).add(stringRequest);

        btn_req.setOnClickListener(new View.OnClickListener() {
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
