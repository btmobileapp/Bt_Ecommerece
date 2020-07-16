package biyaniparker.com.parker.bal;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONObject;

import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;
import biyaniparker.com.parker.view.login.LoginActivity;

/**
 * Created by bt18 on 08/08/2016.
 */
public class ModuleLogin implements DownloadUtility
{
    Context context;
    public ModuleLogin(Context context)
    {
        this.context=context;
    }


    // Login function from server
    public void performLogin(String username, String pass)
    {

        // requset code ->   1
        String url=CommonUtilities.URL+"UtilService.svc/getUserDetail?username="+username+"&password="+pass+"&ClientId=1";
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, url ,"",1,this);
        serverAsync.execute();

    }

    @Override
    public void onComplete(String str, int requestCode,int responseCode)
    {
      //  Toast.makeText(context, ""+str, Toast.LENGTH_SHORT).show();
        if(requestCode==1)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                if (parseUserData(str))
                {
                    downloadUtility.onComplete("LoginSuceess", 1, responseCode);
                }
                else
                {
                    downloadUtility.onComplete("LoginFailed", 1, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 1, responseCode);
            }
        }

    }

    private boolean parseUserData(String str) {

        try
        {
            JSONObject jsonObject=new JSONObject(str);
            if(jsonObject.getInt("UserId")!=0)
            {
                SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sh.edit();
                edit. putString("UserBean", str);
                edit.commit();
                return true;
            }


        }
        catch (Exception e)
        {

        }
        return false;
    }
}
