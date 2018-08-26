package tu.thesis.onlinebanking.pk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        pd=new ProgressDialog(this);
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

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(String msg){
        pd.setMessage(msg);
        pd.show();
    }

    public void hideProgressDialog(){
        pd.hide();
    }

}
