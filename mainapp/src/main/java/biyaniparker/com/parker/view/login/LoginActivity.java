package biyaniparker.com.parker.view.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import biyaniparker.com.parker.MainActivity;
import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleLogin;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectionDetector;
import biyaniparker.com.parker.view.category.CategoryListView;
import biyaniparker.com.parker.view.homeadmin.AdminProductMenu;
import biyaniparker.com.parker.view.homeuser.UserHomeScreen;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  ,DownloadUtility
{
//TextInputLayout
    Button btnLogin;
    EditText edUserId,edPassword;
    ModuleLogin objModuleLogin;
    TextView txtAddress,txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        objModuleLogin=new ModuleLogin(this);
        inItUi();

        CommonUtilities.hideatInItInputBoard(this);



        File myDir = new File(Environment.getExternalStorageDirectory().toString()+"/parker" );
        myDir.mkdirs();


        txtAddress=(TextView)findViewById(R.id.txtAddress);
        txt=(TextView)findViewById(R.id.txt);
        txtAddress.setText(CommonUtilities.AdminAdress);
        txt.setText(CommonUtilities.AdminContact);

    }

    private void inItUi()
    {
        btnLogin=(Button)findViewById(R.id.btnLogin);
        edUserId=(EditText)findViewById(R.id.edUserId);
        edPassword=(EditText)findViewById(R.id.edPassword);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        CommonUtilities.hideSoftKeyBord(this);
        String userid=edUserId.getText().toString();
        String pass=edPassword.getText().toString();
        if(userid.trim().equalsIgnoreCase("")||pass.trim().equalsIgnoreCase(""))
        {
                     Toast.makeText(this,"Please enter username and password ",Toast.LENGTH_LONG).show();
        }
       else
        {
            if(new ConnectionDetector(this).isConnectingToInternet())
            {
                objModuleLogin.performLogin(userid.trim(), pass.trim());
            }
            else
            {
                Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onComplete(String str, int requestCode,int responseCode)
    {
        if(responseCode==200) {
            if (requestCode == 1) {
                if (str.equalsIgnoreCase("LoginSuceess"))
                {
                    finish();
                    //if(UserUtilities.getUserType(this).equalsIgnoreCase("Admin"))
                    if(true)
                    {



                        SharedPreferences sh=getSharedPreferences("Sync",MODE_PRIVATE);
                        sh.edit().putBoolean("Sync",false).commit();

                        startActivity(new Intent(this, SyncActivity.class));
                        overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                    }
                    else
                    {

                    }
                } else if (str.equalsIgnoreCase("LoginFailed")) {
                    Toast.makeText(this, "Incorrect UserName or Password ", Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            Toast.makeText(this, "Server Communication Failed", Toast.LENGTH_LONG).show();
        }

    }
}
