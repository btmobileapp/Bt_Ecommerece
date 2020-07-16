package biyaniparker.com.parker.view.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleUser;
import biyaniparker.com.parker.beans.ShopMaster;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.beans.UserShopBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;

public class UserEditView extends AppCompatActivity implements View.OnClickListener, DownloadUtility, CompoundButton.OnCheckedChangeListener {

    EditText edShopName, edPersonName, edContact, edAddress, edCreadit,edUserName, edPassward,edEmail,edRePass;
    Button buttonSave, buttonDelete;
    UserShopBean bean;ShopMaster shopMasterbean;
    CheckBox chkShowPass;

    ModuleUser moduleUser;
    RadioButton rdAdmin, rdCustomer;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_edit_user);
        init();
        getSupportActionBar().setTitle("Edit User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        bean=new UserShopBean();
        shopMasterbean=new ShopMaster();
        moduleUser=new ModuleUser(this);
        renderView();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    private void renderView() {

        Intent intent = getIntent();
        int userId = intent.getIntExtra("UserId", 0);
        bean=moduleUser.getUser(userId);
        //shopMasterbean=moduleUser.getShopDetailsByUserId(bean.getShopId());

        edShopName.setText(bean.shopdetails.getShopName());
        edPersonName.setText(bean.user.getName());
        edAddress.setText(bean.shopdetails.getAddress());
        edContact.setText(bean.user.getContactNo());
        edCreadit.setText(String.valueOf(bean.shopdetails.getCreditLimit()));
        edEmail.setText(bean.user.getEmailId());
        edUserName.setText(bean.user.getUserName());
        chkShowPass=(CheckBox)findViewById(R.id.chkShowPass);
        chkShowPass.setOnCheckedChangeListener(this);
        if(bean.user.getUserType().equalsIgnoreCase("Customer"))
        {
            rdCustomer.setChecked(true);
        }
        else if( bean.user.getUserType().equalsIgnoreCase("null"))
        {
            rdCustomer.setChecked(false);
            rdAdmin.setChecked(false);
        }
        else
        {
            rdAdmin.setChecked(true);
        }
        edPassward.setText(bean.user.getPassword());
        edRePass.setText(bean.user.getPassword());
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
        edRePass=(EditText)findViewById(R.id.edRePass);
        buttonDelete=(Button)findViewById(R.id.btndelete);
        buttonDelete.setOnClickListener(this);
        buttonSave=(Button)findViewById(R.id.btnsave);
        buttonSave.setOnClickListener(this);
    }



    @Override
    public void onClick(View v)
    {
        CommonUtilities.hideSoftKeyBord(this);


        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_LONG).show();
        }
        else
        {
            if(buttonSave.isPressed()) {
                if (validation()) {
                    if (edPassward.getText().toString().equals(edRePass.getText().toString())) {

                        bean.user.setChangedBy(UserUtilities.getUserId(this));
                        bean.user.setChangedDate(CommonUtilities.getCurrentTime());
                        bean.shopdetails.setShopName(edShopName.getText().toString());
                        bean.user.setName(edPersonName.getText().toString());
                        bean.user.setContactNo(edContact.getText().toString());
                        bean.user.setMobileNo(edContact.getText().toString());
                        bean.shopdetails.setAddress(edAddress.getText().toString());

                        bean.shopdetails.setCreditLimit(Double.parseDouble(edCreadit.getText().toString()));
                        bean.user.setUserName(edUserName.getText().toString());
                        bean.user.setPassword(edPassward.getText().toString());
                        bean.user.setEmailId(edEmail.getText().toString());
                        bean.user.setRoleName(rdAdmin.isChecked() ? rdAdmin.getText().toString() : rdCustomer.getText().toString());
                        bean.user.setUserType(rdAdmin.isChecked() ? rdAdmin.getText().toString() : rdCustomer.getText().toString());
                        bean.user.setIsActive("true");
                        bean.user.setDeleteStatus("false");
                        bean.user.setClientId(UserUtilities.getClientId(this));


                        try {
                            moduleUser.updateUser(bean.user,bean.shopdetails);
                        } catch (Exception e) {

                        }
                    } else {

                        Toast.makeText(getApplicationContext(), " Password and RePassword not matches .  ", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), " Please Enter all fileds ...  ", Toast.LENGTH_LONG).show();
                }
            }
            else if(buttonDelete.getId()==v.getId())
            {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.app_name));
                alertDialog.setMessage("Are you sure to delete ? ");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        try {
                            flag = 1;
                            bean.user.setDeleteStatus("true");
                            moduleUser.updateUser(bean.user, bean.shopdetails);
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

                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //finish();
                    }
                });
                alertDialog.show();
            }
        }
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
        if(requestCode==3 && responseCode==200 && flag==0)
        {
            Toast.makeText(getApplicationContext()," Record updated Successfully ..",Toast.LENGTH_LONG).show();
            Intent intent=new Intent();
            intent.setAction("SyncUser");
            sendBroadcast(intent);
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
        else if(requestCode== 3 && responseCode ==200 && flag==1)
        {
            Toast.makeText(getApplicationContext()," Record Successfully deleted .. ",Toast.LENGTH_LONG).show();

            Intent intent=new Intent();
            intent.setAction("SyncUser");
            sendBroadcast(intent);

            finish();
    }
        else if(requestCode== 30 && responseCode ==200 )
        {
            Toast.makeText(getApplicationContext()," Record Updation failed .. Please Try again .. ",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext()," Bad request.....",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if(isChecked)
        {
            edRePass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            edPassward.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        else
        {

            edRePass.setInputType(129);
            edPassward.setInputType(129);
        }
    }
}
