package biyaniparker.com.parker.view.price;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModulePrice;
import biyaniparker.com.parker.beans.PriceBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;

public class PriceEditView extends AppCompatActivity implements View.OnClickListener, DownloadUtility {

    EditText edDPrice, edCPrice;
    Button btnSave,btnDelete;
    ModulePrice modulePrice;
    PriceBean bean;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_edit_price);


        getSupportActionBar().setTitle(" Edit Price  ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        edCPrice=(EditText)findViewById(R.id.edCPrice);
        edDPrice=(EditText)findViewById(R.id.edDPrice);
        btnSave=(Button)findViewById(R.id.buttonUpdate);
        btnDelete=(Button)findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(this);
        btnSave.setOnClickListener(this);


        bean=new PriceBean();
        modulePrice=new ModulePrice(this);

        renderView();
        CommonUtilities.hideatInItInputBoard(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    private void renderView()
    {
        Intent intent=getIntent();
        int priceId=intent.getIntExtra("PriceId",0);
        bean=modulePrice.getPriceBean(priceId);
        edCPrice.setText((int) (double) bean.getConsumerPrice() + "");
        edDPrice.setText((int) (double) bean.getDealerPrice()+"");

    }

    @Override
    public void onClick(View v)
    {

        CommonUtilities.hideSoftKeyBord(this);
        if (!new ConnectionDetector(this).isConnectingToInternet()) {
            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_LONG).show();
        }
        else {
            if (btnSave.isPressed())
            {
                if (validation()) {

                    bean.setDealerPrice((Double.parseDouble(edDPrice.getText().toString())));
                    bean.setConsumerPrice(Double.parseDouble(edCPrice.getText().toString()));
                    bean.setChangedBy(UserUtilities.getUserId(this));
                    bean.setChangedDate(CommonUtilities.getCurrentTime());
                    bean.setDeleteStatus("false");
                    bean.setClientId(UserUtilities.getClientId(this));


                    try
                    {
                        modulePrice.updatePrice(bean);
                    } catch (Exception e) {
                    }


                }
                else {
                    Toast.makeText(getApplicationContext(), " Please Enter all fileds ...  ", Toast.LENGTH_LONG).show();
                }
            }
            else if (btnDelete.isPressed()) {


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.app_name));
                alertDialog.setMessage("Are you sure to delete ? ");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            bean.setDeleteStatus("true");
                            modulePrice.deletePrice(bean);
                            flag = 1;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.setCancelable(true);
                    }
                });
                alertDialog.show();
            }
        }
    }

    private boolean validation()
    {
        if(edCPrice.getText().toString().equals("")||edDPrice.getText().toString().equals(""))
        {return  false;}
        else {
            return true;
        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(requestCode==3 && responseCode==200 && flag==0)
        {
            Toast.makeText(getApplicationContext()," Record updated Successfully ..",Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Record Updated");
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
        else if(requestCode== 4 && responseCode ==200 && flag==1)
        {
            if(str.equals("dependancy"))
            {
                Toast.makeText(getApplicationContext()," Sorry Price "+bean.getConsumerPrice()+" cannot be deleted, it may in current use .. ",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext()," Record Successfully deleted .. ",Toast.LENGTH_LONG).show();
            }

            finish();
        }
//        else if(requestCode== 30 && responseCode ==200 )
//        {
//            Toast.makeText(getApplicationContext()," Record Updation failed .. Please Try again .. ",Toast.LENGTH_SHORT).show();
//        }
        else
        {
            Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }
    }
}
