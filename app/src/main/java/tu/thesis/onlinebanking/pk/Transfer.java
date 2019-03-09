package tu.thesis.onlinebanking.pk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import tu.thesis.onlinebanking.pk.Fragment.Fragment_Main;
import tu.thesis.onlinebanking.pk.Model.TransactionModel;
import tu.thesis.onlinebanking.pk.Model.UserData;
import tu.thesis.onlinebanking.pk.Model.UserModel;

public class Transfer extends AppCompatActivity {

    EditText etEmail,etAmount,etDesc;
    DatabaseReference refUsers,ref1,ref2,refAdmin;
    TextView tvName,tvPhone;
    Button btOk,btnCancle;
    TransactionModel model1,model2,modelAdmin;
    String myUid="",otherUid="",myemail,refid,myName,adminUid="nqWhqaQZxUWuWxhuKPR4Pdqizdp1";
    long balance1,balance2,adminBalance,to_send;
    long tax;
    private ProgressDialog pDialog;
    public static UserModel userData;
    String s = "";

    int range = 9;  // to generate a single number with this range, by default its 0..9
    int length = 6;
    int otp_result;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Transfer");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);

        setSupportActionBar(mToolbar);

        userData= new UserModel();
        generateRandomNumber();


        myemail = Fragment_Main.userData.getEmail();
        myName = Fragment_Main.userData.getName();

        refid = Fragment_Main.userData.getUid();

        balance1 = getIntent().getLongExtra("balance", 0);
        to_send = getIntent().getLongExtra("balance", 0);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        etEmail = (EditText)findViewById(R.id.etEmailTransfer);
        etAmount = (EditText)findViewById(R.id.etAmountTransfer);
        etDesc = (EditText)findViewById(R.id.etDescriptionTransfer);

        tvName = (TextView)findViewById(R.id.tvNameTransfer);
        tvPhone = (TextView)findViewById(R.id.tvPhoneTransfer);
        btOk = (Button)findViewById(R.id.btOKTransfer);
        btnCancle = findViewById(R.id.btnCancle);
        btOk.setEnabled(false);




        refUsers = FirebaseDatabase.getInstance().getReference("/mbanking/users");
        refAdmin = FirebaseDatabase.getInstance().getReference("/mbanking/users");

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transfer.super.onBackPressed();
            }
        });

    }

    public void checkClick(View v)
    {  pDialog.setMessage("Checking user....");
        showDialog();
        //showProgressDialog("Checking user...");
        refUsers.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot p1)
            {
                try
                {
                    for (DataSnapshot dss:p1.getChildren())
                    {
                        if (dss.child("email").getValue(String.class).equals(etEmail.getText().toString().trim()))
                        {
                            etAmount.setEnabled(true);
                            btOk.setEnabled(true);
                            btOk.setBackground(getResources().getDrawable(R.drawable.button_background));
                            model1 = new TransactionModel();
                            model2 = new TransactionModel();
                          //  modelAdmin =new TransactionModel();
                            balance2 = dss.child("amount").getValue(Long.class);

                            tvName.setText(dss.child("name").getValue().toString());
                            tvPhone.setText(dss.child("phone").getValue().toString());
                            btOk.setEnabled(true);

                            model1.date = (model2.date = System.currentTimeMillis());
                            model1.type = 0;
                            model2.type = 1;
                            model1.userId = (otherUid = dss.child("uid").getValue().toString());

                            model2.userId = myUid;

                          hideDialog();
                            break;
                        }
                        else {

                        }
                    }
                   hideDialog();
                }
                catch (Exception e)
                {
                    hideDialog();
                    showMsg("Error", e.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError p1)
            {
                hideDialog();
            }
        });

    }

    public void okClick(View v)
    {

        String am = etAmount.getText().toString();
        int checkAm = Integer.parseInt(am);
if (etAmount.getText().equals("")){
    showMsg("Sorry","Enter amount");
    return;

}
if (checkAm<=50000){
    tax = 300;

}
if (checkAm <=100000 && checkAm> 50000){

    tax= 600;
}

        if (checkAm > 100000){
            showMsg("Sorry","Limited amount! The maximum amount of transaction is 100000 Ks. Try again");
           // Toast.makeText(getApplicationContext(),"Limited amount! The maximum amount of transaction is 100000 Ks",Toast.LENGTH_LONG).show();

            return;
        }
        if (checkAm < 1000){
            showMsg("Sorry","The minimum amount of transaction is 1000 Ks. Try again");
            // Toast.makeText(getApplicationContext(),"Limited amount! The maximum amount of transaction is 100000 Ks",Toast.LENGTH_LONG).show();

            return;
        }
        refAdmin.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot p1)
            {
                try
                {
                    for (DataSnapshot dss:p1.getChildren())
                    {
                        if (dss.child("email").getValue(String.class).equals("finalyearthesis8@gmail.com"))
                        {

                            modelAdmin =new TransactionModel();
                            adminBalance = dss.child("amount").getValue(Long.class);


                            btOk.setEnabled(true);

                            modelAdmin.date =  System.currentTimeMillis();
                            modelAdmin.type = 3;
                            modelAdmin.taxx=0;
                            modelAdmin.amount=tax;
                            modelAdmin.email_or_ph= myemail;
                            //   modelAdmin.description = model1.email_or_ph + " to " + model2.email_or_ph;
                            //  modelAdmin.amount =

                            break;
                        }
                        else {

                        }
                    }
                    hideDialog();
                }
                catch (Exception e)
                {
                    hideDialog();
                    showMsg("Error", e.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError p1)
            {
                hideDialog();
            }
        });





        final String mailTitle = "One Time Password !";

        final String mailBody = "Hello "+ myName + ",\nWe are generated One Time Password to continue your transcation." +
                "Please enter OTP in your transcation activity.\nThe code is : "+s+"\nIt will expire in 3 mins.\n\n\n Thanks you.\n\nDisclaimer: Please do not reply to this email as it is auto-generated. For any queries, please call on 09782561428";



        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("https://tuthesis.000webhostapp.com/email_server.php");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("StringOne", myemail+","+model1.email_or_ph));
                    nameValuePairs.add(new BasicNameValuePair("StringTwo", mailBody));
                    nameValuePairs.add(new BasicNameValuePair("StringThree",mailTitle));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);



                } catch (ClientProtocolException e) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                }
            }
        });

        thread.start();
        Toast.makeText(getApplicationContext(),"OTP successfully sent to "+ myemail,Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplicationContext(),otp_result+"",Toast.LENGTH_LONG).show();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_alerdialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.otp);

        edt.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(6) });


        dialogBuilder.setTitle("OTP Confirm?");

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String textOTP = edt.getText().toString();
                 int OTP =  Integer.parseInt(textOTP);
                if (OTP== otp_result) {
                    Toast.makeText(getApplicationContext(),tax+"",Toast.LENGTH_LONG).show();
                    model1.amount = (model2.amount = Long.parseLong("0" + etAmount.getText().toString().trim()));
                    model1.email_or_ph = etEmail.getText().toString().trim();
                    model1.taxx = tax;
                    model2.email_or_ph=FirebaseAuth.getInstance().getCurrentUser().getEmail();

                    model1.description = (model2.description = etDesc.getText().toString());
                    ref1 = FirebaseDatabase.getInstance().getReference("/mbanking/transactions").child(myUid);
                    ref2 = FirebaseDatabase.getInstance().getReference("/mbanking/transactions").child(otherUid);
                  //  refAdmin =
                    refAdmin = FirebaseDatabase.getInstance().getReference("/mbanking/admin").child(adminUid);


                    ref1.push()
                            .setValue(model1);

                    ref2.push()
                            .setValue(model2);
                    refAdmin.push()
                            .setValue(modelAdmin);

                    balance1 -= model1.amount+tax;
                    balance2 += model1.amount;
                    adminBalance +=tax;

                    if (balance1 < model1.amount)
                    {
                        showMsg("Error","No enough money to transfer.");
                        return;
                    }
                    pDialog.setMessage("Please wait. Transferring money....");
                    showDialog();

                    // transfering....
                    FirebaseDatabase.getInstance().getReference("/mbanking/users/" + myUid).child("amount").setValue(balance1);
                    FirebaseDatabase.getInstance().getReference("/mbanking/users/" + otherUid).child("amount").setValue(balance2);

                    FirebaseDatabase.getInstance().getReference("/mbanking/users/" + adminUid).child("amount").setValue(adminBalance);
                    hideDialog();
                    String dec = etDesc.getText().toString();



                    String smsText = "You have received "+ model1.amount+" Ks from "
                            +myemail+" for "+ dec+" OTP for this transaction is"+ otp_result+"";


//                    **************
//                    Send SMS
//                    **************
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(tvPhone.getText().toString().trim(),null,smsText,null,null);

                            Toast.makeText(getApplicationContext(),
                                    "" +
                                            "Notification sent to "+tvName.getText().toString(),Toast.LENGTH_LONG).show();
                        }catch (Exception e){

                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    String  nowDate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                    Intent in = new Intent(getApplicationContext(),Transfer_Confimed.class);
                    in.putExtra("name",tvName.getText().toString());
                    in.putExtra("from",model2.email_or_ph);
                    in.putExtra("to",model1.email_or_ph);
                    in.putExtra("amount",etAmount.getText().toString());
                    in.putExtra("date",nowDate);
                    in.putExtra("description",dec);
                    in.putExtra("charges",tax);
                    in.putExtra("balance",balance1);
                    in.putExtra("mainbalance",to_send);


                    final String mailT = "New Value Added";
                    final String mailB = "Dear value Customer,\nCongratulation, you have received "+etAmount.getText().toString()+" Ks from "+ model2.email_or_ph+" for "+
                            dec+"\n";
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                HttpClient httpclient = new DefaultHttpClient();
                                HttpPost httppost = new HttpPost("https://tuthesis.000webhostapp.com/email_server.php");
                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                                nameValuePairs.add(new BasicNameValuePair("StringOne", model1.email_or_ph));
                                nameValuePairs.add(new BasicNameValuePair("StringTwo", "Dear value Customer,\nCongratulation, you have received "+etAmount.getText().toString()+" Ks from "+ model2.email_or_ph+ "\n"));
                                nameValuePairs.add(new BasicNameValuePair("StringThree",mailT));
                                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                                HttpResponse response = httpclient.execute(httppost);



                            } catch (ClientProtocolException e) {
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                                // TODO Auto-generated catch block
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                                // TODO Auto-generated catch block
                            }
                        }
                    });

                    thread.start();

                    startActivity(in);

                    Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_LONG).show();

                }  else {

                    Toast.makeText(getApplicationContext(),"Incorrect OTP, try again",Toast.LENGTH_LONG).show();
                }
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                //pass
            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.show();
        new CountDownTimer(180000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                b.dismiss();
                Toast.makeText(getApplicationContext(),"OTP request time out, try again later",Toast.LENGTH_LONG).show();
            }
        }.start();






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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public int generateRandomNumber() {
        int randomNumber;

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }

        randomNumber = Integer.parseInt(s);
        otp_result = randomNumber;

        return randomNumber;
    }


}
