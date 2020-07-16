package biyaniparker.com.parker.view.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleUser;
import biyaniparker.com.parker.beans.ShopMaster;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;

public class UserCreateView extends AppCompatActivity implements View.OnClickListener, DownloadUtility {

    EditText edShopName, edPersonName, edContact, edAddress, edCreadit,edUserName, edPassward,edEmail,edRePass;
    Button buttonSave;
    UserBean bean; ShopMaster shopMasterbean;
    ModuleUser moduleUser;
    RadioButton rdAdmin, rdCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_create_user);
        init();
        getSupportActionBar().setTitle("Create User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        bean=new UserBean();
        shopMasterbean=new ShopMaster();
        moduleUser=new ModuleUser(this);
        CommonUtilities.hideatInItInputBoard(this);
    }

    private void init() {
        edShopName=(EditText)findViewById(R.id.edShopName);
        edPersonName=(EditText)findViewById(R.id.edPersonName);
        edContact=(EditText)findViewById(R.id.edContact);
        edAddress=(EditText)findViewById(R.id.edShopAddress);
        edCreadit=(EditText)findViewById(R.id.edCreditLimit);
        edUserName=(EditText)findViewById(R.id.edUserName);
        edPassward=(EditText)findViewById(R.id.edPassword);
        edEmail=(EditText)findViewById(R.id.edEmailAddress);
        rdAdmin=(RadioButton)findViewById(R.id.rdoAdmin);
        rdCustomer=(RadioButton)findViewById(R.id.rdoCustomer);
        rdCustomer.setChecked(true);
        edRePass=(EditText)findViewById(R.id.edRePass);
        buttonSave=(Button)findViewById(R.id.btnsave);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        CommonUtilities.hideSoftKeyBord(this);
        if (!new ConnectionDetector(this).isConnectingToInternet()) {
            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            if (validation()) {
                if (edPassward.getText().toString().equals(edRePass.getText().toString())) {
                    shopMasterbean.setShopName(edShopName.getText().toString());
                    bean.setName(edPersonName.getText().toString());
                    bean.setContactNo(edContact.getText().toString());
                    bean.setMobileNo(edContact.getText().toString());
                    shopMasterbean.setAddress(edAddress.getText().toString());
                    shopMasterbean.setCreditLimit(Double.parseDouble(edCreadit.getText().toString()));
                    bean.setUserName(edUserName.getText().toString());
                    bean.setPassword(edPassward.getText().toString());
                    bean.setCreatedBy(UserUtilities.getUserId(this));
                    shopMasterbean.setCreatedBy(UserUtilities.getUserId(this));
                    shopMasterbean.setCreatedDate(CommonUtilities.getCurrentTime());
                    bean.setEnterDate(CommonUtilities.getCurrentTime());
                    bean.setEmailId(edEmail.getText().toString());
                    bean.setRoleName(rdAdmin.isChecked() ? rdAdmin.getText().toString() : rdCustomer.getText().toString());
                    bean.setUserType(rdAdmin.isChecked() ? rdAdmin.getText().toString() : rdCustomer.getText().toString());
                    bean.setEnterBy(UserUtilities.getUserId(this));
                    bean.setIsActive("true");
                    bean.setDeleteStatus("false");
                    bean.setClientId(UserUtilities.getClientId(this));


                    try {
                        moduleUser.insertUser(bean,shopMasterbean);
                    } catch (Exception e) {

                    }

                } else {
                    Toast.makeText(getApplicationContext(), " Password and RePassword not matches .  ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), " Please Enter all fileds ...  ", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private boolean validation() {
        if(edShopName.getText().toString().equals("")||edPersonName.getText().toString().equals("")||edContact.getText().toString().equals("")
                ||edAddress.getText().toString().equals("")||edCreadit.getText().toString().equals("")||edUserName.getText().toString().equals("")
                ||edPassward.getText().toString().equals("")||edEmail.getText().toString().equals(""))
        {return false;}

        else
        {return true;}
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==2 && responseCode==200)
        {
            Toast.makeText(getApplicationContext()," Record inserted Successfully ..",Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("User Created Successfully");
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
        else if(requestCode==20 && responseCode==200)
        {
            Toast.makeText(getApplicationContext()," User Creation failed. Please Try again .. ",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }

    }
}
