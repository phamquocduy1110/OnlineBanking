package tu.thesis.onlinebanking.pk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tu.thesis.onlinebanking.pk.Model.UserData;
import tu.thesis.onlinebanking.pk.Model.UserModel;

public class Register extends AppCompatActivity {

//    UserData user;
//
//    FirebaseUser fbuser;

    private FirebaseAuth fbAuth;
    DatabaseReference ref;
    String uid="";

    TextView tv;
    private ProgressDialog pDialog;

   private EditText etName,etDOB,etPhone,etMail,etPass,etRePass,etAmount,etAddress,etNationalID,etAccount;

    @Override

    protected void onCreate(Bundle savedInstanceState)

    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        tv=(TextView)findViewById(R.id.tv);


        etName=(EditText)findViewById(R.id.etName);
        etDOB = findViewById(R.id.etDOB);
        etPhone = findViewById(R.id.etPhone);
        etMail=(EditText)findViewById(R.id.etMail);
        etPass=(EditText)findViewById(R.id.etPass);
        etRePass = findViewById(R.id.etRePass);
        etAmount = findViewById(R.id.etAmount);
        etAddress = findViewById(R.id.etAddress);
        etNationalID = findViewById(R.id.etNationalID);
//        etAccount = findViewById(R.id.etAccount);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        etPhone=(EditText)findViewById(R.id.etPhone);

        ref = FirebaseDatabase.getInstance().getReference("/mbanking/users/");

        fbAuth = FirebaseAuth.getInstance();
        if (fbAuth.getCurrentUser() != null)
        {
            return;
        }

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

    }

    @Override
    protected void onDestroy()
    {
        fbAuth.signOut();
        super.onDestroy();
    }



   public void onClick(View v){

      pDialog.setMessage("Registering please wait ...");
      showDialog();
       String email = etMail.getText().toString();
       String password = etPass.getText().toString();
       String Repass = etRePass.getText().toString();
       String nrc = etNationalID.getText().toString();
       String name = etName.getText().toString();
       String phone = etPhone.getText().toString();
       String dob = etDOB.getText().toString();

       if (name.length()==0){
           Toast.makeText(getApplicationContext(),"Enter your name",Toast.LENGTH_SHORT).show();
           hideDialog();
           return;
       }
       if (phone.length()==0){
           Toast.makeText(getApplicationContext(),"Enter phone number",Toast.LENGTH_SHORT).show();
           hideDialog();
           return;
       }
       if (dob.length()==0){

           Toast.makeText(getApplicationContext(),"Enter your birthday",Toast.LENGTH_SHORT).show();
           hideDialog();
           return;
       }

       if (email.length() == 0)
       {
           Toast.makeText(getApplicationContext(),"Enter an email address",Toast.LENGTH_SHORT).show();
           hideDialog();
           return;
       }

       if (password.length() < 6)
       {    hideDialog();
           Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();

           return;
       }
       if(!password.equals(Repass)){
           hideDialog();
           Toast.makeText(getApplicationContext(),"Password do not match",Toast.LENGTH_SHORT).show();

           return;
       }

       if (nrc.length()<10){
           hideDialog();
           Toast.makeText(getApplicationContext(),"Enter correct NRC number",Toast.LENGTH_SHORT).show();

           return;

       }

       fbAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(this,
                       new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(Task<AuthResult> task)
                           {

                               if (!task.isSuccessful())
                               {
                                   showMsg("Error", "Account creation failed");
                               }
                               else
                               {
                                   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                   user.sendEmailVerification()
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(Task<Void> task2)
                                               {
                                                   if (task2.isSuccessful())
                                                   {
                                                       UserModel model=new UserModel();
                                                       model.name = etName.getText().toString().trim();
                                                       model.birthday = etDOB.getText().toString().trim();
                                                       model.email = etMail.getText().toString();
                                                       model.amount = Long.parseLong((etAmount.getText().toString()));
                                                       model.password = etPass.getText().toString();
                                                       model.address = etAddress.getText().toString();
                                                       model.nationalID = etNationalID.getText().toString();
                                                       model.phone = etPhone.getText().toString();
                                                       model.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                       ref.child(model.uid).setValue(model);

                                                       showMsg("Sign Up Success", "Verification email sent. Please check your email to confirm.");
                                                   }
                                                   else
                                                   {
                                                       showMsg("Error", "Sign Up: Verification email error.");
                                                   }
                                               };

                                           });
                                   hideDialog();
                               }
                           }
                          // user = new UserData(etName.getText().toString(),etDOB.getText().toString(),
//                                etPhone.getText().toString(),
//                                etMail.getText().toString(),
//                                etAmount.getText().toString(),
//                                etAddress.getText().toString(),
//                                etNationalID.getText().toString(),
//                                etAccount.getText().toString(),
//                                firebaseUser.getUid());

                       });

   }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();


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
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });
    ad.show();
}


}
