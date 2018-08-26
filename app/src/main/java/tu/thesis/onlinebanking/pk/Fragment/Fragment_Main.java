package tu.thesis.onlinebanking.pk.Fragment;



import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tu.thesis.onlinebanking.pk.Model.UserData;
import tu.thesis.onlinebanking.pk.PhoneOperator;
import tu.thesis.onlinebanking.pk.R;
import tu.thesis.onlinebanking.pk.Transfer;


public class Fragment_Main extends Fragment {

    FragmentTransaction fTransaction;
    CardView create_card, my_card, his_card, tem_card;
    TextView tv,name;
    //String uid, mail, amount;
    private DatabaseReference dbRef;
    DatabaseReference databaseReference;
    FirebaseUser user;
    public static UserData userData;
    DatabaseReference ref;
    long balance=0,myBalance=0;
    int  bal;

    String myEmail="",myBal;
    String uid="";
    private ProgressDialog pDialog;
  static   boolean canTransfer=true;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment__main, container, false);
        create_card = view.findViewById(R.id.createcv);
        my_card = view.findViewById(R.id.mycv_card);
        his_card = view.findViewById(R.id.his_card);
        tem_card = view.findViewById(R.id.temp_card);
        tv = view.findViewById(R.id.current_amount);
        name = view.findViewById(R.id.name);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);


        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref=FirebaseDatabase.getInstance().getReference("/mbanking/users/").child(uid);
        pDialog.setMessage("Fetching your account data...");
        showDialog();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot p1) {
                if(p1.exists()){
                  name.setText("Welcome "+p1.child("name").getValue(String.class)+", your current balance is");
                    tv.setText((balance=p1.child("amount").getValue(Long.class))+"");
                    myEmail=p1.child("email").getValue(String.class);

                   // myBalance= p1.child("amount").getValue(Long.class)+"";
hideDialog();

                    userData = new UserData();
                userData.setUid(p1.child("uid").getValue() + "");
                userData.setName(p1.child("name").getValue() + "");
                userData.setBirthday(p1.child("birthday").getValue() + "");
                userData.setPhone(p1.child("phone").getValue() + "");
                userData.setEmail(p1.child("email").getValue() + "");

                userData.setAddress(p1.child("address").getValue() + "");
                userData.setNationalID(p1.child("nationalID").getValue() + "");
                // userData.setAccount(next.child("account").getValue()+"");

                    myBal = tv.getText().toString().trim();
                    bal = Integer.parseInt(myBal);

                    if (bal<=1000) {
                       showMsg("Warning!","Your main balance is lower than 1000 Ks, please top-up ");
                       canTransfer=false;

                    }

                }else{
                    showMsg("Error","Account does not exist.");
                    Toast.makeText(getActivity(), "Error Account does not exist.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError p1) {

            }
        });


    //








        create_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBal = tv.getText().toString().trim();
                bal = Integer.parseInt(myBal);

                if (bal<=1000) {
                    showMsg("Warning!","Sorry, your main balance is low , so you can't do any transcation process.");


                }  else {
                    Intent in=new Intent(getActivity(),PhoneOperator.class);
                    in.putExtra("balance",balance);
                    startActivity(in);
                  }

            }


        });

        my_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBal = tv.getText().toString().trim();
                bal = Integer.parseInt(myBal);

                if (bal<=1000) {
                    showMsg("Warning!","Sorry, your main balance is low , so you can't do any transcation process.");


                }  else {

                Intent intent=new Intent(getActivity(),Transfer.class);
                intent.putExtra("balance",balance);
                startActivity(intent);}

            }
        });

        his_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  MainActivity.selectedIndex = 3;
                fTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fTransaction.replace(R.id.frame_layout, new Forgine_Currency()).commit();
            }
        });

        tem_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fTransaction.replace(R.id.frame_layout, new Money_Calculator()).commit();



            }
        });


        return view;
    }
    public void showMsg(String title, String msg)
    {
        final AlertDialog ad=new AlertDialog.Builder(getActivity()).create();

        ad.setTitle(title);
        ad.setMessage(msg);

        ad.setButton(
                AlertDialog.BUTTON_POSITIVE,
                "Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface p1, int p2)
                    {
                        ad.dismiss();
                        //  startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
        ad.show();
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

