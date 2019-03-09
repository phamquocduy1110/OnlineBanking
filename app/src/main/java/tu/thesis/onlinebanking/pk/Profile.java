package tu.thesis.onlinebanking.pk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import tu.thesis.onlinebanking.pk.Fragment.Fragment_Main;
import tu.thesis.onlinebanking.pk.Model.UserData;
import tu.thesis.onlinebanking.pk.Model.UserModel;

public class Profile extends AppCompatActivity {


    ArrayList<String> listItems=new ArrayList<String>();
  //  DatabaseReference databaseReference;
    FirebaseUser user;
    String name;
    String address;
    Long amount;
    String birthday;
    String email;
    String account;
    String nationalID;
    String phone;

    public static UserModel userData;

    TextView pro_name,pro_email,pro_phone,pro_nationalID,pro_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Profile");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        setSupportActionBar(mToolbar);
        pro_name = findViewById(R.id.pro_name);
        pro_email = findViewById(R.id.pro_email);
        pro_phone = findViewById(R.id.pro_phone);
        pro_nationalID = findViewById(R.id.pro_nationalID);

        pro_address = findViewById(R.id.pro_address);





         userData= new UserModel();


        name = Fragment_Main.userData.getName();
        address =Fragment_Main.userData.getAddress();
        phone = Fragment_Main.userData.getPhone();
        email = Fragment_Main.userData.getEmail();
        birthday = Fragment_Main.userData.getBirthday();
      //  amount = (Fragment_Main.userData.getAmount());
        account = Fragment_Main.userData.getAccount();
        nationalID = Fragment_Main.userData.getNationalID();

       pro_name.setText(name);
       pro_email.setText(email);

       pro_nationalID.setText(nationalID);
      // pro_amount.setText(String.valueOf(amount));
       pro_phone.setText(phone);
       pro_address.setText(address);



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
