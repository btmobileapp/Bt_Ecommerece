package biyaniparker.com.parker.view.user;

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
import androidx.appcompat.widget.Toolbar;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleUser;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;
import biyaniparker.com.parker.view.login.LoginActivity;

public class PasswordUpdateView extends AppCompatActivity implements View.OnClickListener, DownloadUtility{

    EditText edOldPass, edNewPass, edRePass, edUserName;
    Button btnSave;
    UserBean bean;
    ModuleUser moduleUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.o_activity_update_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        bean=new UserBean();
        moduleUser=new ModuleUser(this);
        edUserName = (EditText)findViewById(R.id.edUserName);
        edUserName.setText(UserUtilities.getUserName(this));
        edUserName.setEnabled(false);

        edOldPass=(EditText)findViewById(R.id.edOldPass);
        edNewPass=(EditText)findViewById(R.id.edNewPass);
        edRePass=(EditText)findViewById(R.id.edRePass);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_LONG).show();
        }
        else
        {
            if (validation())
            {
                if (edUserName.getText().toString().equals(UserUtilities.getUserName(this)))
                {

                    if(edNewPass.getText().toString().equals(edRePass.getText().toString()))
                    {
                        if(edOldPass.getText().toString().equals(UserUtilities.getUserPassword(this)))
                        {
                            bean.setPassword(edNewPass.getText().toString());
                            bean.setOldPassword(edOldPass.getText().toString());
                            bean.setUserId((int)UserUtilities.getUserId(this));

                            try {
                                moduleUser.updateUserPassword(bean);
                            } catch (Exception e) {
                            }
                        }
                        else
                        {
                            Toast.makeText(this," Incorrect password ...  ",Toast.LENGTH_LONG).show();
                            edOldPass.setText("");
                            edNewPass.setText("");
                            edRePass.setText("");
                        }


                    }
                    else
                    {
                        Toast.makeText(this,"password and repassword not matches .. ",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this," Please enter valid username ",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(this," Please enter all fields ",Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validation() {
        if(edOldPass.getText().toString().equals("")||edNewPass.getText().toString().equals("")||edRePass.getText().toString().equals(""))
        {return false;}
        else
        {return true;}
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==4 && responseCode==200)
        {
            Toast.makeText(getApplicationContext(), " Password updated Successfully ..", Toast.LENGTH_LONG).show();

            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.app_name));
            alertDialog.setMessage("Password Updated");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(PasswordUpdateView.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
