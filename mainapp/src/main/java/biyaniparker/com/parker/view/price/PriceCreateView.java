package biyaniparker.com.parker.view.price;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModulePrice;
import biyaniparker.com.parker.beans.PriceBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;

public class PriceCreateView extends AppCompatActivity implements View.OnClickListener, DownloadUtility {

    EditText edDPrice, edCPrice;
    Button btnSave;
    ModulePrice modulePrice;
    PriceBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_create_price);




        getSupportActionBar().setTitle(" Create Price  ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        edCPrice=(EditText)findViewById(R.id.edCPrice);
        edDPrice=(EditText)findViewById(R.id.edDPrice);
        btnSave=(Button)findViewById(R.id.buttonsave);
        btnSave.setOnClickListener(this);


        bean=new PriceBean();
        modulePrice=new ModulePrice(this);

        CommonUtilities.hideatInItInputBoard(this);
    }

    //public void insertPrice()

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v)
    {
        CommonUtilities.hideSoftKeyBord(this);
        if (!new ConnectionDetector(this).isConnectingToInternet()) {
            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (validation())
            {

                    bean.setDealerPrice(Double.parseDouble(edDPrice.getText().toString()));
                    bean.setConsumerPrice(Double.parseDouble(edCPrice.getText().toString()));
                    bean.setCreatedBy(UserUtilities.getUserId(this));
                    bean.setCreatedDate(CommonUtilities.getCurrentTime());
                    bean.setDeleteStatus("false");
                    bean.setClientId(UserUtilities.getClientId(this));


                    try {
                        modulePrice.insertPrice(bean);
                    } catch (Exception e) {}


            }
            else
                {
                Toast.makeText(getApplicationContext(), " Please Enter all fileds ...  ", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validation() {
        if(edCPrice.getText().toString().equals("")||edDPrice.getText().toString().equals(""))
        {return  false;}
        else {
            return true;
        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==2 && responseCode==200)
        {
            Toast.makeText(getApplicationContext()," Record inserted Successfully ..",Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Record Saved");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            alertDialog.show();
        }
        else
        {
            Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }
    }
}
