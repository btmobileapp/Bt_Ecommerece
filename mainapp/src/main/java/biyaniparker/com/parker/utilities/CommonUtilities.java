package biyaniparker.com.parker.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import biyaniparker.com.parker.database.ItemDAOCategory;
import biyaniparker.com.parker.database.ItemDAODispatch;
import biyaniparker.com.parker.database.ItemDAOOrder;
import biyaniparker.com.parker.database.ItemDAOPrice;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.database.ItemDAOSizeMaster;
import biyaniparker.com.parker.database.ItemDAOUser;

/**
 * Created by bt18 on 08/08/2016.
 */
public class CommonUtilities
{
    public final static String URL=

          //  "http://123.252.221.158:100/parkerservices/";
            //"http://192.168.73.100:100/parkerservices/";
          // "http://biyanitechnologies.com/parkerservices/";
          // "http://103.249.96.252/parkerservices/";
   // "http://btwebservices.biyanitechnologies.com/parkerservices/";
       //     "http://testing.biyanitechnologies.com/parkerservices/";
    // public final static String URL= "http://btwebservices.biyanitechnologies.com/sunanda/";
    "http://192.168.73.133/parker/";

    public static int Height=800;
    public static int Width=600;

    

/*
    public final static String RESPONCE_OK="Success";
    public static String GodName="|| Yashwant Dugdh Prakriya Ltd. ||";
    public static String AdminShop="Warana Dudh";
    public static String AdminAdress=" Yashwant Group of Industries" +
            "" +
            ",Shirala , Sangli";

              public static String Slogan="";
    public static String AdminContact="Ph No. 91 2345 270 100 ";

*/





    public final static String RESPONCE_OK="Success";
    public static String GodName="|| Shree Babaji Maharaj Namah ||";
    public static String AdminShop="MAHALAXMI APPARELS";
    public static String AdminAdress=" BLOCK NO.433/3C,GAT NO 24/3A, OPP.HOTELSAPTAGIRI,JAYSINGPUR-416101";
    public static String Slogan="";
    public static String AdminContact="Ph No. (02322)224744 ";

    public static long getCurrentTime()
    {
        Calendar calendar=Calendar.getInstance();
        return calendar.getTimeInMillis();
    }


    public static void setHeightWidth(int val)
    {
            Height= (int) (val*8.7);
            Width= (int) (val*6.5);
    }

    public static long parseDate(String strDate)
    {
        try {

            return Long.valueOf(strDate.replace("/Date(", "").replace(")/", ""));
        }
        catch (Exception e)
        {

        }
        return 0;
    }

    public static void hideatInItInputBoard(Activity context)
    {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void hideSoftKeyBord(Activity context) {
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = context.getCurrentFocus();
        if (v == null)
            return;


        inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException
    {
        if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try
            {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static void alert(Context context,String str)
    {
        AlertDialog.Builder al=new AlertDialog.Builder(context);
        al.setMessage(str);
        al.setPositiveButton("Ok ",null);
        al.show();
    }


    public static void printLog(String str)
    {
        Log.d("perror", str);
    }



    public static String longToDate(long orderDate)
    {
        Date date=new Date(orderDate);

        SimpleDateFormat f=new SimpleDateFormat("dd/MM/yyyy");
        return f.format(date);
    }


    public  static void  openPdf(Context context,String path)
    {
        try
        {
            File file = new File(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        }
        catch (Exception e)
        {

        }
    }

    public static  boolean emailWithAttachement(Context context, String emailaddress,
                                                String message,String subject,String filepath)
    {


        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        //Intent.Extra_e
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[]{ emailaddress});
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("application/image");
        //  emailIntent.setType(Intent.EXTRA_STREAM);

        Uri uri = Uri.parse("file://" + filepath);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(emailIntent);

        return true;
    }

    public static void deleteAll(Context context)
    {
        ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
        itemDAOPrice.delete();
        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        itemDAOCategory.delete();
        ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
        itemDAOSizeMaster.deleteAllSizes();
        itemDAOSizeMaster.deleteAllDetailsSizes();
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        itemDAODispatch.deleteDispatchDetail();

        ItemDAOUser itemDAOUser=new ItemDAOUser(context);
        itemDAOUser.deleteShop();
        itemDAOUser.deleteUser();;

        ItemDAOOrder itemDAOOrder=new ItemDAOOrder(context);
        itemDAOOrder.deleteAllOrder();

        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        itemDAOProduct.delete();


    }
}
