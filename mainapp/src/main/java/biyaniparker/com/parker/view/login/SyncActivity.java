package biyaniparker.com.parker.view.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import biyaniparker.com.parker.LaunchActivity;
import biyaniparker.com.parker.R;
import biyaniparker.com.parker.bal.ModuleSync;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.view.homeadmin.AdminHomeScreen;
import biyaniparker.com.parker.view.homeuser.UserHomeScreen;

public class SyncActivity extends AppCompatActivity implements DownloadUtility
{

    ModuleSync moduleSync;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        moduleSync=new ModuleSync(this);
        moduleSync.sync();

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
       // Toast.makeText(this,str,Toast.LENGTH_LONG).show();
        if(responseCode==200)
        {
            if(requestCode==2)
            {

                SharedPreferences sh=getSharedPreferences("Sync",MODE_PRIVATE);
                if(  sh.getBoolean("Sync",false))
              {


                  finish();
                  if (UserUtilities.getUserType(this).equalsIgnoreCase("Admin")) {
                      startActivity(new Intent(this, AdminHomeScreen.class));
                  } else {
                      startActivity(new Intent(this, UserHomeScreen.class));
                      overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
                  }
              }
              else
              {
                  AlertDialog.Builder app=new AlertDialog.Builder(this);
                  app.setTitle("Parker");
                  app.setMessage("Sync Not Completed");
                  app.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                               moduleSync.sync();
                      }
                  });
                  app.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {

                      }
                  });
                  app.setOnCancelListener(new DialogInterface.OnCancelListener() {
                      @Override
                      public void onCancel(DialogInterface dialog) {
                          finish();
                      }
                  });
                  app.show();
              }

            }
        }
        else
        {
            startActivity(new Intent(this, LaunchActivity.class));
            overridePendingTransition(R.animator.pull_in_right, R.animator.push_out_left);
        }

    }
}
