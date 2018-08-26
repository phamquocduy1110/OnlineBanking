package tu.thesis.onlinebanking.pk.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tu.thesis.onlinebanking.pk.Model.TransactionModel;
import tu.thesis.onlinebanking.pk.Model.UserData;
import tu.thesis.onlinebanking.pk.R;


public class Transfer extends Fragment {

    EditText etEmail,etAmount,etDesc;
    DatabaseReference refUsers,ref1,ref2;
    TextView tvName,tvPhone;
    Button btOKTransfer,check;
    TransactionModel model1,model2;
    String myUid="",otherUid="",b1="100";
    Long balance1,balance2;
    public static UserData userData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transfer, container, false);
        userData= new UserData();
       // Toast.makeText(getActivity(),b1,Toast.LENGTH_LONG).show();
        balance1 = Fragment_Main.userData.getAmount();
     //  balance1 = Long.parseLong(b1);
        //addListenerOnSpinnerItemSelection();
        etEmail = (EditText)v.findViewById(R.id.etEmailTransfer);
        etAmount = (EditText)v.findViewById(R.id.etAmountTransfer);
        etDesc = (EditText)v.findViewById(R.id.etDescriptionTransfer);

        tvName = (TextView)v.findViewById(R.id.tvNameTransfer);
        tvPhone = (TextView)v.findViewById(R.id.tvPhoneTransfer);
        btOKTransfer = (Button)v.findViewById(R.id.btOKTransfer);
        check = v.findViewById(R.id.check);

        etEmail.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
            {

            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
            {
                tvName.setText("");
                tvPhone.setText("");
                btOKTransfer.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable p1)
            {

            }

        });
        refUsers = FirebaseDatabase.getInstance().getReference("/mbanking/users");
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

check.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        checkClick();
    }
});

btOKTransfer.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        okClick();
    }
});


        return  v;
    }

    public void checkClick(){
refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot p1) {
        {
            try
            {
                for (DataSnapshot dss:p1.getChildren())
                {
                    if (dss.child("email").getValue(String.class).equals(etEmail.getText().toString()))
                    {
        etAmount.setEnabled(true);
        btOKTransfer.setEnabled(true);
        model1 = new TransactionModel();
        model2 = new TransactionModel();
        balance2 = dss.child("amount").getValue(Long.class);

        tvName.setText(dss.child("name").getValue().toString());
        tvPhone.setText(dss.child("phone").getValue().toString());
        btOKTransfer.setEnabled(true);

        model1.date = (model2.date = System.currentTimeMillis());
        model1.type = 0;
        model2.type = 1;
        model1.userId = (otherUid = dss.child("uid").getValue().toString());

        model2.userId = myUid;

       // hideProgressDialog();
        break;
    }
                                        }
        //hideProgressDialog();
    }
catch (Exception e)
    {
      //  hideProgressDialog();
     //   showMsg("Error", e.toString());
        Toast.makeText(getActivity(),"error "+ e.toString(),Toast.LENGTH_LONG).show();
    }
    }


}

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

});}

    @Override
    public void onDetach() {
        FragmentTransaction fTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.frame_layout,new Fragment_Main()).commit();
        super.onDetach();

    }

    public void okClick(){


      //  Toast.makeText(getActivity(),"error ",Toast.LENGTH_LONG).show();
        model1.amount = (model2.amount = Long.parseLong("0" + etAmount.getText().toString()));
        model1.email_or_ph = etEmail.getText().toString();
        model2.email_or_ph=FirebaseAuth.getInstance().getCurrentUser().getEmail();

        model1.description = (model2.description = etDesc.getText().toString());
        ref1 = FirebaseDatabase.getInstance().getReference("/mbanking/transactions").child(myUid);
        ref2 = FirebaseDatabase.getInstance().getReference("/mbanking/transactions").child(otherUid);

        ref1.push()
                .setValue(model1);

        ref2.push()
                .setValue(model2);

        balance1 -= model1.amount;
        balance2 += model1.amount;

        if (balance1 < model1.amount)
        {
            showMsg("Error","No enough money to transfer.");
            return;
        }

       // showProgressDialog("Please wait. Transferring money...");
        FirebaseDatabase.getInstance().getReference("/mbanking/users/" + myUid).child("balance").setValue(balance1);
        FirebaseDatabase.getInstance().getReference("/mbanking/users/" + otherUid).child("balance").setValue(balance2);

       // hideProgressDialog();

//**************
//Send SMS
//**************

        showMsg("Success", model1.amount + " transferred to " + model1.email_or_ph + ".");



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
                        //finish();
                    }
                });
        ad.show();
    }
}
