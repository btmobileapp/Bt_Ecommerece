package biyaniparker.com.parker.bal;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.GsonCategoryBean;
import biyaniparker.com.parker.beans.GsonPriceBean;
import biyaniparker.com.parker.beans.GsonProductBean;
import biyaniparker.com.parker.beans.GsonShopMaster;
import biyaniparker.com.parker.beans.GsonSizeBean;
import biyaniparker.com.parker.beans.GsonUserBean;
import biyaniparker.com.parker.beans.PriceBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.ShopMaster;
import biyaniparker.com.parker.beans.SizeMaster;
import biyaniparker.com.parker.beans.TempBean;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.database.ItemDAOCategory;
import biyaniparker.com.parker.database.ItemDAOPrice;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.database.ItemDAOSizeMaster;
import biyaniparker.com.parker.database.ItemDAOUser;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt18 on 08/29/2016.
 */
public class ModuleSync implements DownloadUtility {

   Context context;
   public ModuleSync(Context context)
    {
            this.context=context;
            try
            {
                InputStream stream = new ByteArrayInputStream("".getBytes("UTF-8"));
            }
            catch (Exception e){}
    }
    public void sync()
    {
        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        long ln=itemDAOProduct.getLatestChangedProduct();
        long lastProductUpdate=DateAndOther.getForwordDay(ln, -15);

        ItemDAOUser itemDAOUser=new ItemDAOUser(context);

        long latestShop= DateAndOther.getForwordDay(itemDAOUser.getLatestChangedShop(),-15);
        long latestUser= DateAndOther.getForwordDay(itemDAOUser.getLatestChangedUser(), -15);





      //  Toast.makeText(context,DateAndOther.getStringDayfromMillisecond(ln),Toast.LENGTH_LONG).show();


        String urlString= CommonUtilities.URL+"SyncService.svc/getSyncAllUpdatedData?ClientId="+ UserUtilities.getClientId(context)
                +"&lastProductUpdate="+lastProductUpdate
                +"&latestShop="+latestShop
                +"&latestUser="+latestUser;

        AsyncParsingUtilities asyncUtilities=new AsyncParsingUtilities
                //(context,false,"http://192.168.73.104/parker/StockService.svc/gettemp","",1,this);//
                (context,false,urlString,"",2,this);
        //(context,false,"http://192.168.73.104/parker/ProductService.svc/getAllCategory?ClientId=1","",2,this);
        SharedPreferences sh=context.getSharedPreferences("Sync",context.MODE_PRIVATE);
        sh.edit().putBoolean("Sync",false).commit();
        asyncUtilities.setProgressDialoaugeVisibility(false);
        asyncUtilities.execute();
    }
    //
    public List<TempBean> readJsonStream(InputStream in) throws IOException
    {
        Gson gson=new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<TempBean> list = new ArrayList<TempBean>();
        reader.beginArray();
        while (reader.hasNext()) {
            TempBean bean = gson.fromJson(reader, TempBean.class);
            list.add(bean);
        }
        reader.endArray();
        reader.close();
        return list;
    }


    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if (requestCode == 1) {
            //  DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200) {
                try {
                    InputStream stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
                    List<TempBean> list = readJsonStream(stream);
                    String st = "";
                    for (int i = 0; i < list.size(); i++)
                        st = st + "\n" + list.get(i).toString();
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage(list.size() + "" + st);
                  //  alert.show();
                } catch (Exception e) {
                }
            }
        }
        if (requestCode == 2) {
             DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200) {
                try
                {
                     // parseSyncAll(str);
                   // InputStream stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
                   // parseStream(stream);
                    downloadUtility.onComplete("Success",requestCode,responseCode);

                } catch (Exception e) {
                    downloadUtility.onComplete("ParseFailed-"+e.toString(),requestCode,responseCode);
                }
            }
        }

    }

    //-------------------parse sync----------------

  public  void parseStream(InputStream str)
    {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(str, "UTF-8"));
            List<TempBean> list = new ArrayList<TempBean>();

            reader.beginObject();


          //  reader.beginArray();
            while (reader.hasNext())
            {
                String name = reader.nextName();

                if(name.equals("allPrice"))
                {
                    reader.beginArray();
                    ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
                    itemDAOPrice.delete();
                    while (reader.hasNext())
                    {
                        GsonPriceBean bean = gson.fromJson(reader, GsonPriceBean.class);
                        PriceBean priceBean = bean.toPriceBean();

                        long ln= itemDAOPrice.insert(priceBean);
                        ln=ln;
                        //String ss = "";
                    }

                    reader.endArray();
                }
                if(name.equals("allUser"))
                {
                    reader.beginArray();
                    ItemDAOUser itemDAOUser=new ItemDAOUser(context);
                    //itemDAOUser.deleteUser();
                    while (reader.hasNext())
                    {
                        GsonUserBean bean = gson.fromJson(reader, GsonUserBean.class);
                        UserBean userBean = bean.toUserBean();

                        itemDAOUser.insertUserBean(userBean);
                        //itemDAOPrice.insert(priceBean);
                        //String ss = "";
                    }

                    reader.endArray();
                }
                if(name.equals("allShops"))
                {
                    reader.beginArray();
                    ItemDAOUser itemDAOUser=new ItemDAOUser(context);
                   // itemDAOUser.deleteShop();
                    while (reader.hasNext())
                    {
                        GsonShopMaster bean = gson.fromJson(reader, GsonShopMaster.class);
                        ShopMaster userBean = bean.toShopMaster();

                        itemDAOUser.insertShopMaster(userBean);
                        //itemDAOPrice.insert(priceBean);
                        //String ss = "";
                    }

                    reader.endArray();
                }

                 if(name.equals("allCate"))
                {
                    reader.beginArray();
                    ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
                    itemDAOCategory.delete();
                    while (reader.hasNext())
                    {
                        GsonCategoryBean bean = gson.fromJson(reader, GsonCategoryBean.class);
                        CategoryBean categoryBean = bean.toCategoryBean();
                        itemDAOCategory.insert(categoryBean);

                        String ss = "";
                    }
                    reader.endArray();
                }
                if(name.equals("allProducts"))
                {
                    reader.beginArray();
                    ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
                  //  itemDAOProduct.delete();
                    while (reader.hasNext())
                    {
                        GsonProductBean bean = gson.fromJson(reader, GsonProductBean.class);

                        ProductBean productBean = bean.toProductBean();

                        itemDAOProduct.delete(productBean);
                        itemDAOProduct.insert(productBean);

                    }
                    reader.endArray();
                }
                if(name.equals("allSize"))
                {
                    reader.beginArray();
                    ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
                    itemDAOSizeMaster.deleteAllSizes();
                    itemDAOSizeMaster.deleteAllDetailsSizes();
                    while (reader.hasNext())
                    {

                        GsonSizeBean sizeBean = gson.fromJson(reader, GsonSizeBean.class);
                        SizeMaster sizeMaster= sizeBean.sizeMasters.toSizeMaster();

                        //sizeBean.details  =s


                        itemDAOSizeMaster.insertSize(sizeMaster,sizeBean.getDetailsList());




                              /*String nm = reader.nextName();
                              if(nm.equals("sizeMasters"))
                              {
                                     reader.beginArray();
                                      while (reader.hasNext())
                                     {

                                     }
                                     reader.endArray();
                               }
                             if(nm.equals("details"))
                             {
                                  reader.beginArray();
                                  while (reader.hasNext())
                                  {

                                  }
                                  reader.endArray();
                             }*/
                    }
                    reader.endArray();
                }

            }
            reader.endObject();
            SharedPreferences sh=context.getSharedPreferences("Sync", context.MODE_PRIVATE);
            sh.edit().putBoolean("Sync",true).commit();
            reader.close();
            str.close();
           // return list;
        }
        catch (Exception e)
        {


            String str1=e.toString();
            str1=str1;
            AlertDialog.Builder alert=new AlertDialog.Builder(context);
            alert.setMessage(str1);
            alert.show();
        }
      //  return null;
    }




    private void parseSyncAll(String str)
    {
        ModuleCategory  category=new ModuleCategory(context);
        ModuleProduct   product=new ModuleProduct(context);
        ModuleSizeMaster moduleSizeMaster=new ModuleSizeMaster(context);
        ModulePrice modulePrice=new ModulePrice(context);
        try {
            JSONObject jsonObject=new JSONObject(str);
            category.parseCategories(jsonObject.getJSONArray("allCate").toString());
        }
        catch (Exception e){}
        try {
            JSONObject jsonObject=new JSONObject(str);
            product.parseProducts(jsonObject.getJSONArray("allProducts").toString());
        }
        catch (Exception e){}
        try {
            JSONObject jsonObject=new JSONObject(str);
            moduleSizeMaster.parseSizes(jsonObject.getJSONArray("allPrice").toString());
        }
        catch (Exception e){}
        try {
            JSONObject jsonObject=new JSONObject(str);
           // modulePrice.p   (jsonObject.getJSONArray("allSize").toString());
        }
        catch (Exception e){}
    }


}
