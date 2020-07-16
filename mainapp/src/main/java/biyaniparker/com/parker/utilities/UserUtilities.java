package biyaniparker.com.parker.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import biyaniparker.com.parker.view.homeuser.UserHomeScreen;

/**
 * Created by bt18 on 08/10/2016.
 */
public class UserUtilities
{

    public  static  long getUserId(Context context)
    {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
             JSONObject jsonObject=new JSONObject(sh.getString("UserBean","{}"));
             return  jsonObject.getLong("UserId");
        }
        catch (Exception e){}
        return   0;

    }
    public  static  void clearUser(Context context)
    {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
           sh.edit().clear().commit();
        }
        catch (Exception e){}

    }


    public  static  String getUserName(Context context)
    {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
            JSONObject jsonObject=new JSONObject(sh.getString("UserBean","{}"));
            return  jsonObject.getString("UserName");
        }
        catch (Exception e){}
        return   "";

    }

    public  static  String getUserPassword(Context context)
    {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
            JSONObject jsonObject=new JSONObject(sh.getString("UserBean","{}"));
            return  jsonObject.getString("Password");
        }
        catch (Exception e){}
        return   "";
    }

    public  static  String getUserType(Context context)
    {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
            JSONObject jsonObject=new JSONObject(sh.getString("UserBean","{}"));
            return  jsonObject.getString("UserType");
        }
        catch (Exception e){}
        return   "";

    }

    public  static  String getUserRole(Context context)
    {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
            JSONObject jsonObject=new JSONObject(sh.getString("UserBean","{}"));
            return  jsonObject.getString("RoleName");
        }
        catch (Exception e){}
        return   "";

    }

    public  static  int getClientId(Context context)
    {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
            JSONObject jsonObject=new JSONObject(sh.getString("UserBean","{}"));
            return  jsonObject.getInt("ClientId");
        }
        catch (Exception e){}
        return   0;

    }

    public  static  int getShopId(Context context)
    {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
            JSONObject jsonObject=new JSONObject(sh.getString("UserBean","{}"));
            return  jsonObject.getInt("ShopId");
        }
        catch (Exception e){}
        return   0;

    }

    public  static  String getShopName(Context context)
    {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
            JSONObject jsonObject=new JSONObject(sh.getString("UserBean","{}"));
            return  jsonObject.getString("ShopName");
        }
        catch (Exception e){}
        return   "";

    }


    public static String getName(Context context) {
        SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
        try
        {
            JSONObject jsonObject=new JSONObject(sh.getString("UserBean","{}"));
            return  jsonObject.getString("Name");
        }
        catch (Exception e){}
        return   "";
    }
}
