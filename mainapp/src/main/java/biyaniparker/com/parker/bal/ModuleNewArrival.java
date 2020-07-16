package biyaniparker.com.parker.bal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.ProductBeanWithQnty;
import biyaniparker.com.parker.beans.gson.GsonProductWithQnty;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.NotifyCallback;
import biyaniparker.com.parker.utilities.ParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncMultiFileUploadUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncStreamParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt18 on 08/31/2016.
 */
public class ModuleNewArrival implements DownloadUtility, ParsingUtilities
{
    Context context;

    public ArrayList<ProductBeanWithQnty> newProductList=new ArrayList<>();
    AsyncStreamParsingUtilities asyncStreamParsingUtilities;
    List<GsonProductWithQnty>gsonProdWithQntyList;

    public ModuleNewArrival(Context context)
    {
        this.context=context;
    }
    //------------------------------ Web Api Call------------------------------
    public void loadNewArrivalProduct()
    {
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"StockService.svc/getNewArrivalProduct?ClientId="+1,"",1,this);
        serverAsync.execute();

    }


    public void loadNewArrivalProductWithNotify()
    {
        asyncStreamParsingUtilities=new AsyncStreamParsingUtilities(context,false, CommonUtilities.URL+"StockService.svc/getNewArrivalProduct?ClientId="+1,"",1,this,this);
        asyncStreamParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncStreamParsingUtilities.setHidePdAfterConnection(true);
        asyncStreamParsingUtilities.execute();

    }




    //---------------------------Wep Api Response------------------------------

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==1)
        {

            SharedPreferences sh=context.getSharedPreferences("NewArrival",context.MODE_PRIVATE);
            SharedPreferences.Editor ed=sh.edit();
            ed.putString("str",str);
            ed.commit();

            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
              /*  if (parseCategories(str)) {

                    downloadUtility.onComplete("Success", 1, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 1, responseCode);
                }*/
               // AlertDialog.Builder al=new AlertDialog.Builder(context);
               // al.setMessage(str);
              //  al.show();

               // parseProductsAndQuantity(str);
                downloadUtility.onComplete("Success", 1, responseCode);

            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 1, responseCode);
            }
        }
    }


    //--------------------------------------Parse ProductAnd Quantity------------------------------------


    void parseProductsAndQuantity(String str)
    {
        try
        {
            JSONArray jsonArray=new JSONArray(str);
            for(int i=0;i<jsonArray.length(); i++) {
                newProductList.add(parseProductBenWithQnt(jsonArray.getJSONObject(i).toString()));
            }

        }
        catch (Exception e)
        {
        String st12r=e.toString();
        }

    }


    public ProductBeanWithQnty parseProductBenWithQnt(String str)
    {
        try
        {
            final ProductBeanWithQnty bean = new ProductBeanWithQnty();
            JSONObject c = new JSONObject(str);
            bean.setProductId(c.getInt("ProductId"));
            bean.setProductCode(c.getString("ProductCode"));
            bean.setProductName(c.getString("ProductName"));
            bean.setStripCode(c.getString("StripCode"));
            bean.setDetails(c.getString("Details"));
            bean.setPriceId(c.getInt("PriceId"));
            bean.setCategoryId(c.getInt("CategoryId"));
            bean.setIconThumb(c.getString("IconThumb"));
            bean.setIconFull(c.getString("IconFull"));
            bean.setIconFull1(c.getString("IconFull1"));
            bean.setClientId(c.getInt("ClientId"));
            bean.setQnt(c.getInt("qnt"));
            //bean.setSequenceNo(c.getInt("SequenceNo"));
           try{ bean.setCreatedBy(c.getLong("CreatedBy"));}catch (Exception e){}
            bean.setCreatedDate(CommonUtilities.parseDate(c.getString("CreatedDate")));
            try{bean.setChangedBy(c.getLong("ChangedBy"));}catch (Exception e){}
            try
            {
                bean.setChagedDate(CommonUtilities.parseDate(c.getString("ChangedDate")));
            }
            catch (Exception e){}
            bean.setDeleteStatus(c.getString("DeleteStatus"));
            try
            {
                bean.setIsActive(c.getString("IsActive"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
                        itemDAOProduct.insert((ProductBean)bean);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }).start();
            return bean;
        }
        catch (Exception e)
        {
                e.printStackTrace();
        }
        return null;
    }

    public void getOffline()
    {
        SharedPreferences sh=context.getSharedPreferences("NewArrival",context.MODE_PRIVATE);
        String s=sh.getString("str","[]");
        parseProductsAndQuantity(s);
    }
    NotifyCallback notifyCallback;


    @Override
    public boolean parseStreamingData(InputStream stream)
    {

            newProductList.clear();
        gsonProdWithQntyList =new ArrayList<GsonProductWithQnty>();

         if(notifyCallback==null)
           notifyCallback=(NotifyCallback)  context;
        try
        {
            int count=1;
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.beginArray();
            int endFlag=0;

            while(reader.hasNext())
            {
                GsonProductWithQnty gsonProductWithQnty=gson.fromJson(reader,GsonProductWithQnty.class);
                gsonProdWithQntyList.add(gsonProductWithQnty);
                newProductList.add(gsonProductWithQnty.toProductBeanWithQnty());
                asyncStreamParsingUtilities.setHidePdAfterConnection(true);
                if(count % 10 == 0   )
                {

                    if(notifyCallback!=null)
                        notifyCallback.notifyToActivity();

                  endFlag++;
                }
                count++;
            }
            reader.endArray();
            reader.close();

                notifyCallback.notifyToActivity();
                saveToSharePreferances(gsonProdWithQntyList);



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private void saveToSharePreferances(List<GsonProductWithQnty> newProductList)
    {
        Gson gson=new Gson();
        String listStr=gson.toJson(newProductList);
        SharedPreferences sh=context.getSharedPreferences("NewArrival",context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sh.edit();
        ed.putString("str",listStr);
        ed.commit();
    }


    public void stopAsyncWork()
    {
        //if(asyncStreamParsingUtilities.progressStatus)
        if(asyncStreamParsingUtilities!=null && gsonProdWithQntyList !=null)
        {
            saveToSharePreferances(gsonProdWithQntyList);
            asyncStreamParsingUtilities.closeConnection();
        }
    }
}
