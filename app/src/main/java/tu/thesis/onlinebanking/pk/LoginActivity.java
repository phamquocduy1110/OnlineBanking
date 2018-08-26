package tu.thesis.onlinebanking.pk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import com.google.firebase.auth.*;
import com.google.android.gms.tasks.*;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity  {


private  EditText name,pass;
    private FirebaseAuth auth;
    private String username,password;
    TextView register;
    private IntentIntegrator qrScan;
    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener authListener;



    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        register = findViewById(R.id.register);
        qrScan = new IntentIntegrator(this);
        // Set up the login form.
       // name = username.getText().toString().trim();
        //pwd = password.getText().toString().trim();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        fbAuth = FirebaseAuth.getInstance();


        View v = findViewById(android.R.id.content);
        checkConnection(v);
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null&&user.isEmailVerified())
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        };

        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this,

                        Register.class);

                startActivity(intent);

            }
        });

    }
//    @Override
//    public void onStart()
//    {
//        super.onStart();
//        fbAuth.addAuthStateListener(authListener);
//    }
//
//    @Override
//    public void onStop()
//    {
//        super.onStop();
//        if (authListener != null)
//        {
//            fbAuth.removeAuthStateListener(authListener);
//        }
//    }

    public void onClick(View v){
        if (v.getId()==R.id.sign_in) {
             username = name.getText().toString();
             password = pass.getText().toString();
            if (username.length() == 0)
            {
                name.setError("Enter an email address");
                return;
            }

            if (password.length() < 6)
            {
                pass.setError("Password must be at least 6 characters");
                return;
            }
            hideKeyboard(v);
            pDialog.setMessage("Loading ...");
           showDialog();

            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.signInWithEmailAndPassword(username,

                   password)

                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override

                        public void onComplete( Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                FirebaseUser user =

                                        FirebaseAuth.getInstance().getCurrentUser();

                                if(user.isEmailVerified()){


                                    Toast.makeText(getApplicationContext(),"Verified",Toast.LENGTH_LONG).show();
                                    //tv.setText("verified");
                                    hideDialog();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);

                                }else{

                                  //  Toast.makeText(getApplicationContext(),"Verification email not confirmed yet.",Toast.LENGTH_LONG).show();
                                    hideDialog();
                                    showMsg("Error","Verification email not confirmed yet.");
                                  //  tv.setText("Verification email not confirmed yet.");

                                }

                            }else{

                                Intent intent=new Intent(LoginActivity.this,

                                        Register.class);
                                hideDialog();
                                startActivity(intent);

                            }

                        }

                    });

        }


//

        else {
            qrScan.initiateScan();

//

        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection(View view){
        if(isOnline()){
            Snackbar.make(view, "Welcome", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();
        }else{
            Snackbar.make(view, "You are offline, please check your internet connection", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", null)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent)).show();

        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
              //  TastyToast.makeText(getApplicationContext(), "Result not found, try again!", 3000, TastyToast.CONFUSING);

            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    name.setText(obj.getString("email"));
                    pass.setText(obj.getString("password"));
                   // etNick.setText(obj.getString("name"));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {


            super.onActivityResult(requestCode, resultCode, data);
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
                    }
                });
        ad.show();
    }

    @Override
    public void onBackPressed() {
        LoginActivity.this.finishAffinity();
    }
}

