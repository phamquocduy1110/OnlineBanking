package tu.thesis.onlinebanking.pk;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Timer;

import tu.thesis.onlinebanking.pk.Fragment.Fragment_Main;
import tu.thesis.onlinebanking.pk.Fragment.Payment_history;
import tu.thesis.onlinebanking.pk.Model.UserModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentTransaction fTransaction;
 //   TextView last_login;
 private  String mydate;
    public static UserModel userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.USE_FINGERPRINT,
        Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE},00);



         mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());


        fTransaction = getSupportFragmentManager().beginTransaction();
        fTransaction.replace(R.id.frame_layout,new Fragment_Main()).commit();


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView lastlogin = (TextView) headerView.findViewById(R.id.last_login);
        TextView userName  = headerView.findViewById(R.id.userName);
      //  lastlogin.setText(mydate);


        SharedPreferences sp = getSharedPreferences("Last_Login", MODE_PRIVATE);

        String time = sp.getString("time","fist time open");

        lastlogin.setText("Last login at: "+time);



        navigationView.setNavigationItemSelectedListener(this);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            fTransaction = getSupportFragmentManager().beginTransaction();
            fTransaction.replace(R.id.frame_layout,new Fragment_Main()).commit();


            // Handle the camera action
        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(getApplicationContext(),Profile.class);
            startActivity(i);
           // Toast.makeText(getApplicationContext(),"This function is not available in this version, try later",Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_history) {
           // Toast.makeText(getApplicationContext(),"This function is not available in this version, try later",Toast.LENGTH_LONG).show();
            fTransaction = getSupportFragmentManager().beginTransaction();
            fTransaction.replace(R.id.frame_layout,new Payment_history()).commit();


        } else if (id == R.id.nav_about) {
            final AlertDialog.Builder custom = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.about,null);
            custom.setView(view);
            final AlertDialog dialog = custom.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);

        }else if (id == R.id.nav_contact){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Contact");
            builder.setMessage("If you have any troubleshoot using our system, you can contact to Customer Services via..");

            // add the buttons
            builder.setPositiveButton("Email", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "phyoeko.tutgo@gmail.com"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Services Report");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Add your inconvenience");


                    emailIntent.setType("message/rfc822");

                    try {
                        startActivity(Intent.createChooser(emailIntent,
                                "Send email using..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(),
                                "No email clients installed.",
                                Toast.LENGTH_SHORT).show();
                    }


                }
            });
            builder.setNeutralButton("Phone", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:09782561428" ));
                    startActivity(intent);


                }
            });
            builder.setNegativeButton("Message", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:09782561428")));
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();


        }
        else  if (id==R.id.nav_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            Toast.makeText(getApplicationContext()," You have been Logout",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }  if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {

            SharedPreferences.Editor editor = getSharedPreferences("Last_Login", MODE_PRIVATE).edit();
            editor.putString("time",mydate);
            editor.commit();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
            new CountDownTimer(180000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub

                    alert.dismiss();
                }
            }.start();
           // super.onBackPressed();
        }
    }
}
