package biyaniparker.com.parker.bal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.ProductBeanWithQnty;
import biyaniparker.com.parker.beans.RowItem;
import biyaniparker.com.parker.beans.gson.GsonProductWithQnty;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.NotifyCallback;
import biyaniparker.com.parker.utilities.ParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncStreamParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt18 on 08/30/2016.
 */
public class ModuleUserHomeScreen implements DownloadUtility, ParsingUtilities
{
    Context context;
    AsyncStreamParsingUtilities asyncStreamParsingUtilities;
    public ArrayList<CategoryBean> parentList=new ArrayList<>();
    public  ArrayList<ProductBeanWithQnty> randomList=new ArrayList<>();
    NotifyCallback notifyCallback;
    List<GsonProductWithQnty> gsonProdWithQntyList;


    public ModuleUserHomeScreen(Context context)
    {
        this.context=context;
        ModuleCategory  moduleCategory=new ModuleCategory(context);
        moduleCategory.getParentCategoryList();
        parentList=moduleCategory.parentList;
    }

    public ArrayList<RowItem> getRowItems()
    {
        ArrayList<RowItem> list=new ArrayList<>();
        for(int i=0;i<parentList.size();i++)
        {
            RowItem item=new RowItem(parentList.get(i).getCategoryName(),0,parentList.get(i).getIcon());
            list.add(item);
        }

        return  list;
    }

    //-------------------------------------------------    Web api call   -------------------------------------------------------------------

    public void loadRandomProduct()
    {
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"StockService.svc/getRandomProduct?ClientId="+1,"",1,this);

        serverAsync.setAutoCancleable(false);
        serverAsync.execute();

    }

    public void loadRandomProductWithNotify()
    {
        asyncStreamParsingUtilities =new AsyncStreamParsingUtilities(context,false, CommonUtilities.URL+"StockService.svc/getRandomProduct?ClientId="+1,"",1,this,this);
        asyncStreamParsingUtilities.setAutoCancelable(false);
        asyncStreamParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncStreamParsingUtilities.setHidePdAfterConnection(true);
        asyncStreamParsingUtilities.execute();

    }

    //-----------------------------------------------  Response after calling web api ------------------------------------------------

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==1)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                AlertDialog.Builder al=new AlertDialog.Builder(context);
                al.setMessage(str);

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
        if(notifyCallback==null)
            notifyCallback=(NotifyCallback)  context;


       try
       {
           JSONArray jsonArray=new JSONArray(str);
           for(int i=0;i<jsonArray.length(); i++) {
               randomList.add(parseProductBenWithQnt(jsonArray.getJSONObject(i).toString()));
              if(i%10==0) {
                  Thread.sleep(500);
                  if(notifyCallback!=null)
                  {
                      notifyCallback.notifyToActivity();
                  }
              }
           }

       }
       catch (Exception e)
       {
           Toast.makeText(context,"Error :"+e.toString(),Toast.LENGTH_LONG).show();
       }

    }


    void parseProductsAndQuantityForDetailShopping(String str)
    {
        try
        {
            JSONArray jsonArray=new JSONArray(str);
            for(int i=0;i<jsonArray.length(); i++) {
                randomList.add(parseProductBenWithQnt(jsonArray.getJSONObject(i).toString()));
            }

        }
        catch (Exception e)
        {
            Toast.makeText(context,"Error :"+e.toString(),Toast.LENGTH_LONG).show();
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
            try {
                bean.setQnt(c.getInt("qnt"));
            }
            catch (Exception e){}
            //bean.setSequenceNo(c.getInt("SequenceNo"));
            try {
                bean.setCreatedBy(c.getLong("CreatedBy"));
            }catch (Exception e){}
            bean.setCreatedDate(CommonUtilities.parseDate(c.getString("CreatedDate")));
            try {
                bean.setChangedBy(c.getLong("ChangedBy"));
            }
            catch (Exception e){}
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
           String st=e.toString();
            st=st;
        }
        return null;
    }

    public void getOffLineRandomProducts()
    {


        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sh=context.getSharedPreferences("RandomProducts",context.MODE_PRIVATE);
                String s=sh.getString("str",null);
                parseProductsAndQuantity(s);
            }
        }).start();




    }

    @Override
    public boolean parseStreamingData(InputStream stream)
    {
        randomList.clear();
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
                randomList.add(gsonProductWithQnty.toProductBeanWithQnty());
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

    private void saveToSharePreferances(List<GsonProductWithQnty> gsonProdWithQntyList)
    {
        Gson gson=new Gson();
        String str=gson.toJson(gsonProdWithQntyList);
        SharedPreferences sh=context.getSharedPreferences("RandomProducts",context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sh.edit();
        ed.putString("str",str);
        ed.commit();
    }

    public void stopAsyncWork()
    {
        if(asyncStreamParsingUtilities!=null && gsonProdWithQntyList !=null)
        {
            saveToSharePreferances(gsonProdWithQntyList);
            asyncStreamParsingUtilities.closeConnection();
        }
    }
}
