package tu.thesis.onlinebanking.pk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tu.thesis.onlinebanking.pk.Model.TransactionModel;
import tu.thesis.onlinebanking.pk.R;

public class Bill_Detail_act extends AppCompatActivity {
    TransactionModel model1;
    DatabaseReference refUsers,ref1;
    String myUid="",ph_num,description;
    String amount;
    String am="";
    int checkAm = 0;
    TextView tvAm,contact_name,oName;
    long balance1;
    String operator;
    private ProgressDialog pDialog;
    Button topUp;
    private RadioGroup edu_group;
    EditText other_amt,phone_number,ph_des;
    ImageButton pick_content;
    static public final int RESULT_PICK_CONTACT = 0;
    Context context = this;
    Boolean otherAmount= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill__detail_act);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Phone Bill Top-UP ");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        contact_name=findViewById(R.id.contact_name);
        edu_group = findViewById(R.id.bill);
        other_amt = findViewById(R.id.other_amt);
        setSupportActionBar(mToolbar);
        phone_number = findViewById(R.id.phone_number);
        tvAm = findViewById(R.id.tvAm);
        oName = findViewById(R.id.oName);

        ph_des = findViewById(R.id.ph_des);
        pick_content = findViewById(R.id.pick_content);


        topUp=findViewById(R.id.topUp);
        balance1 = getIntent().getLongExtra("bal", 0);
        operator = getIntent().getStringExtra("operator");


        oName.setText(operator+ " Top-Up");


        refUsers = FirebaseDatabase.getInstance().getReference("/mbanking/users");
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        model1 = new TransactionModel();


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pick_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });

        edu_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.one:
                        // switch to fragment 1
                        amount = "1000";
                        Toast.makeText(getApplicationContext(),amount,Toast.LENGTH_LONG).show();
                        tvAm.setText(amount);
                        break;
                    case R.id.three:
                        amount = "3000";
                        Toast.makeText(getApplicationContext(),amount,Toast.LENGTH_LONG).show();
                        tvAm.setText(amount);
                        break;
                    case R.id.five:
                        // switch to fragment 1
                        amount = "5000";
                        Toast.makeText(getApplicationContext(),amount,Toast.LENGTH_LONG).show();
                        tvAm.setText(amount);
                        break;
                    case R.id.ten:

                        amount = "10000";
                        Toast.makeText(getApplicationContext(),amount,Toast.LENGTH_LONG).show();
                        tvAm.setText(amount);
                        break;
                    case R.id.other:
                            tvAm.setText("");
                        other_amt.setVisibility(View.VISIBLE);
                       otherAmount=true;
                        Toast.makeText(getApplicationContext(),amount,Toast.LENGTH_LONG).show();



                        break;



                }
            }
        });

        topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (otherAmount==true){
                    am=other_amt.getText().toString().trim();
                    checkAm = Integer.parseInt(am);
                    if (checkAm <500){
                        Toast.makeText(getApplicationContext(),"Sorry, minimum amount of Top-Up bill is 500 Ks ",Toast.LENGTH_LONG).show();
                        return;

                    }

                }else {
                    am = tvAm.getText().toString().trim();
                }

                ph_num = phone_number.getText().toString();
                description = ph_des.getText().toString();

                if (ph_num.length()==0){
                    Toast.makeText(getApplicationContext(),"Enter Phone number",Toast.LENGTH_LONG).show();
                    return;
                }


setTopUp();

    }


            public void setTopUp(){


        //public void onTopupClick();
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                //set title
                .setTitle("Confirm?")
                //set message
                .setMessage("Are you sure want to top up "+ am +" Ks  to "+ph_num)
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    model1.date = (System.currentTimeMillis());
                    model1.userId=myUid;
                        model1.amount = (Long.parseLong(am));

                        model1.email_or_ph=ph_num;

                        model1.description = ("Mobile Top-Up "+operator+" for "+description);
                        ref1 = FirebaseDatabase.getInstance().getReference("/mbanking/transactions").child(myUid);
                        //  ref2 = FirebaseDatabase.getInstance().getReference("/mbanking/transactions").child(otherUid);



                        ref1.push()
                                .setValue(model1);

//                        ref2.push()
//                                .setValue(model2);

                        balance1 -= model1.amount;
                        // balance2 += model1.amount;

                        if (model1.amount>balance1&&balance1<=1000)
                        {
                            showMsg("Error","No enough money to transfer. Please re-charge");
                            Toast.makeText(getApplicationContext(),model1.amount+"",Toast.LENGTH_LONG).show();
                            return;
                        }
                        pDialog.setMessage("Please wait...");
                        showDialog();

                        // transfering....
                        FirebaseDatabase.getInstance().getReference("/mbanking/users/" + myUid).child("amount").setValue(balance1);
                        // FirebaseDatabase.getInstance().getReference("/mbanking/users/" + otherUid).child("amount").setValue(balance2);

                        hideDialog();


String smsText =" You have successfully Top -up "+ am+ " from mobile financial services.";


                        showMsg("Success","You have successfully "+  model1.amount + " Ks TopUP to "+ph_num+" for "+description);
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phone_number.getText().toString().trim(),null,smsText,null,null);

                            Toast.makeText(getApplicationContext(),
                                            "Notification sent",Toast.LENGTH_LONG).show();
                        }catch (Exception e){

                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }


                    }
                })
                //set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();


    }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
           // Log.e("MainActivity", "Failed to pick contact");
        }
    }
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
           contact_name.setText("Name - "+name);
            phone_number.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showMsg(String title, String msg)
    {
        final AlertDialog ad=new AlertDialog.Builder(this).create();

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
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;

        }

        return true;
    }


}
