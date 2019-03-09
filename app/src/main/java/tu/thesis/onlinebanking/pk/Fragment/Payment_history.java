package tu.thesis.onlinebanking.pk.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tu.thesis.onlinebanking.pk.Model.TransactionModel;
import tu.thesis.onlinebanking.pk.R;


public class Payment_history extends Fragment {

    ListView lv;
    MyAdapter adapter;
    List<TransactionModel> data;
    String myId="";
    DatabaseReference ref;

    String format="dd/MM/yy hh:mm a";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment_history, container, false);
        lv=(ListView)v.findViewById(R.id.lvTransactions);
        myId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (myId.equals("nqWhqaQZxUWuWxhuKPR4Pdqizdp1")){
           // Toast
            ref = FirebaseDatabase.getInstance().getReference("/mbanking/admin").child(myId);
        }
        else {
            ref = FirebaseDatabase.getInstance().getReference("/mbanking/transactions").child(myId);
        }
        if(ref!=null) {
            //  showProgressDialog("Fetching transaction data...");
ref.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot p1) {
        data=new ArrayList<>();
        for(DataSnapshot dss:p1.getChildren()){
            TransactionModel model=dss.getValue(TransactionModel.class);
            data.add(model);
        }
        Collections.reverse(data);
        adapter=new MyAdapter();
        lv.setAdapter(adapter);
       // hideProgressDialog();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
        }
        return v;
    }



    class MyAdapter extends ArrayAdapter<TransactionModel> {

        public MyAdapter(){

            super(getActivity().getBaseContext(),R.layout.item_layout,data);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.item_layout,parent,false);
            }
            TextView tvDate=(TextView)convertView.findViewById(R.id.tvDateTime);
            TextView tvDescription=(TextView)convertView.findViewById(R.id.tvDescription);
            TextView tvAmount=(TextView)convertView.findViewById(R.id.tvAmount);
            TextView tvTex = convertView.findViewById(R.id.tvTex);
            TransactionModel model=data.get(position);

            tvDate.setText(DateFormat.format(format,model.date));


            tvTex.setText("Tex "+model.taxx);
            String pre="- ";
            if(model.type==0){
                tvAmount.setTextColor(Color.RED);
                tvDescription.setText(model.description+"\n"+" to "+model.email_or_ph);
                tvTex.setTextColor(Color.RED);
            }else if (model.type==1){
                tvAmount.setTextColor(Color.GREEN);
                tvTex.setVisibility(View.INVISIBLE);
                tvDescription.setText(model.description+"\n"+" from "+model.email_or_ph);
                pre="+ ";
            }
            else {
                tvAmount.setTextColor(Color.GREEN);
                tvDescription.setText("Transcation charges"+"\n"+" from "+model.email_or_ph);
                tvTex.setVisibility(View.INVISIBLE);
                pre="+ ";
            }
            tvAmount.setText(pre+model.amount+"");

            return convertView;
        }


    }



}
