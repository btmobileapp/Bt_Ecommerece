package biyaniparker.com.parker.bal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.ProductBeanWithQnty;
import biyaniparker.com.parker.beans.gson.GsonProductWithQnty;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.NotifyCallback;
import biyaniparker.com.parker.utilities.ParsingUtilities;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncStreamParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt on 09/06/2016.
 */
public class ModuleUserProduct implements DownloadUtility, ParsingUtilities {
    Context context;
    AsyncStreamParsingUtilities asyncStreamParsingUtilities;
    NotifyCallback notifyCallback;
    public ArrayList<ProductBeanWithQnty> newProductList = new ArrayList<>();


    public ModuleUserProduct(Context context) {
        this.context = context;
    }


    //------------------------------ Web Api Call------------------------------

    public void getUserProducts(int catId) {
        String url = CommonUtilities.URL + "StockService.svc/getProductsFilter?Clientid=" + UserUtilities.getClientId(context) +
                "&CategoryId=" + catId + "&MinPrice=" + (-1 + "") + "&MaxPrice=" + (-1 + "") + "&SizeIds=" + "" + "&StripCode=" + "";
        AsyncUtilities serverAsync = new AsyncUtilities(context, false, url, "", 1, this);
        serverAsync.execute();

    }

    public void getUserProductsWithNotify(int catId) {
        String url = CommonUtilities.URL + "StockService.svc/getProductsFilter?Clientid=" + UserUtilities.getClientId(context) +
                "&CategoryId=" + catId + "&MinPrice=" + (-1 + "") + "&MaxPrice=" + (-1 + "") + "&SizeIds=" + "" + "&StripCode=" + "";
        asyncStreamParsingUtilities = new AsyncStreamParsingUtilities(context, false, url, "", 1, this, this);
        asyncStreamParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncStreamParsingUtilities.setHidePdAfterConnection(true);
        asyncStreamParsingUtilities.execute();

    }


    public void setFilter(final int catId, final long lowPrice, final long maxPrice, final String sizeIds, final String stripCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // CommonUtilities.alert();
                            String url = CommonUtilities.URL + "StockService.svc/getProductsFilter?Clientid=" + UserUtilities.getClientId(context) +
                                    "&CategoryId=" + catId + "&MinPrice=" + lowPrice + "&MaxPrice=" + maxPrice + "&SizeIds=" + sizeIds + "&StripCode=" + stripCode;
                            AsyncUtilities asyncUtilities = new AsyncUtilities(context, false, url, "", 2, ModuleUserProduct.this);
                            asyncUtilities.execute();
                            //   CommonUtilities.alert(context,url);

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void setFilterWithNotify(final int catId, final long lowPrice, final long maxPrice, final String sizeIds, final String stripCode)
    {
        // CommonUtilities.alert();
        String url = CommonUtilities.URL + "StockService.svc/getProductsFilter?Clientid=" + UserUtilities.getClientId(context) +
                "&CategoryId=" + catId + "&MinPrice=" + lowPrice + "&MaxPrice=" + maxPrice + "&SizeIds=" + sizeIds + "&StripCode=" + stripCode;
        asyncStreamParsingUtilities = new AsyncStreamParsingUtilities(context, false, url, "", 2, ModuleUserProduct.this, this);
        asyncStreamParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncStreamParsingUtilities.setHidePdAfterConnection(true);
        asyncStreamParsingUtilities.execute();
        //   CommonUtilities.alert(context,url);

    }





    //---------------------------Wep Api Response------------------------------

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==1)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
               // parseProductsAndQuantity(str);
                downloadUtility.onComplete("Success", 1, responseCode);
            }
            else
            {
                downloadUtility.onComplete("Server Communication Failed", 1, responseCode);
            }
        }

// response code for filtered result

        if(requestCode==2)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {

               // parseProductsAndQuantity(str);
                downloadUtility.onComplete("Success", 2, responseCode);
            }
            else
            {
                downloadUtility.onComplete("Server Communication Failed", 2, responseCode);
            }
        }
    }


    //--------------------------------------Parse ProductAnd Quantity------------------------------------


    void parseProductsAndQuantity(String str)
    {
        newProductList.clear();
        try
        {
            JSONArray jsonArray=new JSONArray(str);
            for(int i=0;i<jsonArray.length(); i++)
            {

                newProductList.add(parseProductBenWithQnt(jsonArray.getJSONObject(i).toString()));
            }
        }
        catch (Exception e)
        {
            String st12r=e.toString();
            AlertDialog.Builder al=new AlertDialog.Builder(context);
            al.setMessage(str);
           // al.show();
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
            catch (Exception e)
            {

            }
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
            AlertDialog.Builder al=new AlertDialog.Builder(context);
            al.setMessage(str);
            al.show();
        }
        return null;
    }

    public void loadFromDb(int catId)
    {
        try
        {
            ItemDAOProduct itemDAOProduct = new ItemDAOProduct(context);
            newProductList.clear();
            ArrayList<ProductBean> omiList = itemDAOProduct.getProductsByCatId(catId);
            for (int i = 0; i < omiList.size(); i++)
            {
               // newProductList.add((ProductBeanWithQnty) omiList.get(i));
                newProductList.add(omiList.get(i).toProductBeanWithQnty());
            }
            DownloadUtility utility= (DownloadUtility) context;
            utility.onComplete("",1,200);

        }
        catch (Exception e)
        {
            CommonUtilities.alert(context, e.toString());
        }
       // newProductList.addAll( );
    }

    @Override
    public boolean parseStreamingData(InputStream stream)
    {
        newProductList.clear();
        if(notifyCallback==null)
            notifyCallback=(NotifyCallback)  context;
        try
        {
            int count=1;
            ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);

            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.beginArray();
            int endFlag=0;

            boolean isNotify=false;
            int constK=5;
            while(reader.hasNext())
            {
                GsonProductWithQnty gsonProductWithQnty=gson.fromJson(reader, GsonProductWithQnty.class);
                final ProductBeanWithQnty productBeanWithQnty =gsonProductWithQnty.toProductBeanWithQnty();


                itemDAOProduct.insert((ProductBean) productBeanWithQnty);
                newProductList.add(productBeanWithQnty);
                if(count == 1 && asyncStreamParsingUtilities!=null)
                {
                    try{
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            asyncStreamParsingUtilities.hideProgress();
                        }
                    });}
                    catch (Exception e){}

                }

               /* if(count<12)
                    constK=10;
                else if(count>12)
                    constK=20;
                else   if(count>50)
                  constK=60;
                else   if(count>61)
                    constK=100;*/

                if(count % constK == 0   )
                {

                    if(notifyCallback!=null)
                    {
                        notifyCallback.notifyToActivity();
                        if(constK<150)
                          constK=constK*3;
                        else
                           constK=120;

                        if(!isNotify && asyncStreamParsingUtilities!=null)
                        {
                            try{
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        asyncStreamParsingUtilities.hideProgress();
                                    }
                                });}
                            catch (Exception e){}

                        }
                        isNotify=true;

                    }

                   // Thread.sleep(200);
                    endFlag++;
                }
                count++;
            }
            reader.endArray();

            notifyCallback.notifyToActivity();
            reader.close();



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public void stopAsyncWork()
    {
        //if(asyncStreamParsingUtilities.progressStatus)
        if(asyncStreamParsingUtilities!=null)
        asyncStreamParsingUtilities.closeConnection();
    }
}
