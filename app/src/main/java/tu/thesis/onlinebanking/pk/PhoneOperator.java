package tu.thesis.onlinebanking.pk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PhoneOperator extends AppCompatActivity {
    CardView card_mpt,card_telenor,card_ooredoo,card_mytel;
    long mybal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Phone Bill");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow);

        setSupportActionBar(mToolbar);
        card_mpt = findViewById(R.id.card_mpt);
        card_telenor = findViewById(R.id.card_telenor);
        card_ooredoo = findViewById(R.id.card_ooredoo);
        card_mytel = findViewById(R.id.card_mytel);
        mybal = getIntent().getLongExtra("balance", 0);

        card_mpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),Bill_Detail_act.class);

                i.putExtra("bal",mybal);
                i.putExtra("operator","MPT");
                startActivity(i);





            }
        });

        card_telenor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),Bill_Detail_act.class);

                i.putExtra("bal",mybal);
                i.putExtra("operator","Telenor");
                startActivity(i);


            }
        });

        card_ooredoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(),Bill_Detail_act.class);

                i.putExtra("bal",mybal);
                i.putExtra("operator","Ooredoo");
                startActivity(i);


            }
        });

        card_mytel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent i = new Intent(getApplicationContext(),Bill_Detail_act.class);

                i.putExtra("bal",mybal);
                i.putExtra("operator","MyTel");
                startActivity(i);




            }
        });


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
