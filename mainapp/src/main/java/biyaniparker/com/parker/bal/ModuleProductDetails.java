package biyaniparker.com.parker.bal;

import android.content.Context;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.ProductBeanWithQnty;
import biyaniparker.com.parker.beans.ProductStockBean;
import biyaniparker.com.parker.beans.SizeMaster;
import biyaniparker.com.parker.beans.StockMasterBean;
import biyaniparker.com.parker.database.ItemDAOPrice;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.database.ItemDAOSizeMaster;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt on 09/03/2016.
 */
public class ModuleProductDetails implements DownloadUtility {
    Context context;
    int ProductId;
    public ModuleProductDetails(Context context)
    {
        this.context=context;
    }
    public void setProductId(int ProductId)
    {
        this.ProductId=ProductId;
    }
    public ArrayList<ProductStockBean>  availabaleStockList=new ArrayList<>();

    //-------------------------------------------------    Web api call   -------------------------------------------------------------------

    public void syncProduct()
    {
        // requset code ->   1
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"StockService.svc/getProductDetails?Clientid="+ UserUtilities.getClientId(context)+"&ProductId="+ProductId,"",1,this);
        serverAsync.execute();
    }

    public void syncAvailableProduct()
    {
        // requset code ->   3
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"StockService.svc/getProductDetails?Clientid="+ UserUtilities.getClientId(context)+"&ProductId="+ProductId,"",3,this);
        serverAsync.execute();

    }

    public void addToBag(ArrayList<StockMasterBean> stockList) throws JSONException
    {

        // request code => 2 is for calling web service of add to bag


        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<stockList.size();i++)
        {
            StockMasterBean stock=new StockMasterBean();
            stock=stockList.get(i);
            JSONObject json=new JSONObject();
            json.put("ProductId",stock.getProductId());
            json.put("SizeId",stock.getSizeId());
            json.put("InBagQty",stock.getInBagQty());
            json.put("ClientId",stock.getClientId());
            json.put("UserId",stock.getUserId());
            json.put("EnterBy",stock.getEnterBy());
            json.put("ChangedBY",stock.getChangedBY());
            json.put("TransactionType",stock.getTransactionType());
            jsonArray.put(json);
        }

        AsyncUtilities asyncUtilities=new AsyncUtilities(context,true,CommonUtilities.URL+"StockService.svc/InsertStockMasterBag",jsonArray.toString(),2,this);
        asyncUtilities.execute();
    }

    //------------------------------------------------------------------------------------------------------------------









    //---------------------------------------------------------------------------------------------------------------------

    @Override
    public void onComplete(String str, int requestCode,int responseCode)
    {

        if (requestCode == 1)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
             //   Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show();
                parseDetailsDataForShopping(str);

                downloadUtility.onComplete("Success",requestCode,responseCode);
            }
        }

        if (requestCode == 3)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                //   Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show();
                parseAvailbleStock(str);

                downloadUtility.onComplete("Success",requestCode,responseCode);
            }
        }

        // parsing bag added products

        if (requestCode == 2&&responseCode == 200)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (parseBagAddedData(str))
            {
                downloadUtility.onComplete("Success",requestCode,responseCode);
            }
            else
            {
                downloadUtility.onComplete("Failed",requestCode,responseCode);
            }
        }
    }

    private void parseAvailbleStock(String str)
    {
        try
        {
            JSONObject jsonObject=new JSONObject(str);
            JSONArray jsonArray=jsonObject.getJSONArray("stocks");
            try
            {
                for(int i=0;i<jsonArray.length();i++)
                {
                    ProductStockBean stockBean = new ProductStockBean();
                    stockBean.setSizeId(jsonArray.getJSONObject(i).getInt("SizeId"));
                    stockBean.setQnty(jsonArray.getJSONObject(i).getInt("qnty"));
                    availabaleStockList.add(stockBean);

                }
            }
            catch (Exception e)
            {}

            /*JSONArray jsonArray1=jsonObject.getJSONArray("stripCodeProducts");

            ModuleUserHomeScreen moduleUserHomeScreen=new ModuleUserHomeScreen(context);
            moduleUserHomeScreen.parseProductsAndQuantity(jsonArray1.toString());
            stripCodeProducts=moduleUserHomeScreen.randomList;
*/
        }
        catch (Exception e)
        {}

    }

    private boolean parseBagAddedData(String str)
    {
        try {
            JSONObject jsonObject=new JSONObject(str);
            int flag=jsonObject.getInt("flag");
            if(flag==0){return  true;}
            else if(flag==1){return false;}


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<ProductStockBean>  stockList=new ArrayList<>();
   public ArrayList<ProductBeanWithQnty> stripCodeProducts=new ArrayList<>();
    public void parseDetailsData(String str)
    {
        try
        {
            JSONObject jsonObject=new JSONObject(str);
            JSONArray jsonArray=jsonObject.getJSONArray("stocks");
            try
            {
                for(int i=0;i<jsonArray.length();i++)
                {
                    ProductStockBean stockBean = new ProductStockBean();
                    stockBean.setSizeId(jsonArray.getJSONObject(i).getInt("SizeId"));
                    stockBean.setQnty(jsonArray.getJSONObject(i).getInt("qnty"));
                    stockList.add(stockBean);

                }
            }
            catch (Exception e)
            {}

            JSONArray jsonArray1=jsonObject.getJSONArray("stripCodeProducts");

            ModuleUserHomeScreen moduleUserHomeScreen=new ModuleUserHomeScreen(context);
            moduleUserHomeScreen.parseProductsAndQuantityForDetailShopping(jsonArray1.toString());
            stripCodeProducts=moduleUserHomeScreen.randomList;

        }
        catch (Exception e)
        {}


    }

    public void parseDetailsDataForShopping(String str)
    {
        try
        {
            JSONObject jsonObject=new JSONObject(str);
            JSONArray jsonArray=jsonObject.getJSONArray("stocks");
            try
            {
                for(int i=0;i<jsonArray.length();i++)
                {
                    ProductStockBean stockBean = new ProductStockBean();
                    stockBean.setSizeId(jsonArray.getJSONObject(i).getInt("SizeId"));
                    stockBean.setQnty(jsonArray.getJSONObject(i).getInt("qnty"));
                    if(stockBean.getQnty()>0)
                      stockList.add(stockBean);
                }
            }
            catch (Exception e)
            {}

            JSONArray jsonArray1=jsonObject.getJSONArray("stripCodeProducts");

            ModuleUserHomeScreen moduleUserHomeScreen=new ModuleUserHomeScreen(context);
            moduleUserHomeScreen.parseProductsAndQuantityForDetailShopping(jsonArray1.toString());
            stripCodeProducts=moduleUserHomeScreen.randomList;

        }
        catch (Exception e)
        {}


    }

    public String getSizeNameBySizeId(int sizeId) {
        ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
        return    itemDAOSizeMaster.getSizeBean(sizeId).getSizeName();
    }

    public double getPriceFromPriceId(int priceId) {

        ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
        return    itemDAOPrice.getPriceBeanByPriceId(priceId).getConsumerPrice();

    }


    public void loadFromDb(String stripCode)
    {
        ItemDAOProduct itemDAOProduct =new ItemDAOProduct(context);
        stripCodeProducts.clear();
        ArrayList<ProductBean> tempProductList=itemDAOProduct.getCustomList(stripCode, 0, 0);
        for (int i=0;i<tempProductList.size();i++)
        {
            ProductBean prod=tempProductList.get(i);
            stripCodeProducts.add(prod.toProductBeanWithQnty());
        }

    }


}
