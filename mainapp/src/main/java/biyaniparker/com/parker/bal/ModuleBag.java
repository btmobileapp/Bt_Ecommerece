package biyaniparker.com.parker.bal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import biyaniparker.com.parker.R;
import biyaniparker.com.parker.beans.BagDetailsBean;
import biyaniparker.com.parker.beans.BagMasterBean;
import biyaniparker.com.parker.beans.CombineBagBean;
import biyaniparker.com.parker.beans.GsonOrderCombine;
import biyaniparker.com.parker.beans.GsonSelectedItem;
import biyaniparker.com.parker.beans.OrderDetailBean;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.database.ItemDAOOrder;
import biyaniparker.com.parker.database.ItemDAOPrice;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DateAndOther;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;
import biyaniparker.com.parker.view.homeadmin.orderdispatch.PartialDispatchListView;
import biyaniparker.com.parker.view.reports.PrintOrderSummary;

/**
 * Created by bt on 09/08/2016.
 */
public class ModuleBag implements DownloadUtility
{
    Context context;
    public ArrayList<BagMasterBean> bagMasterList=new ArrayList<>();

    public GsonOrderCombine orderCombine=new GsonOrderCombine();

    public ModuleBag(Context context)
    {
        this.context=context;
    }


    //---------------------------------------------------web service call------------------------------------------

    public void showBag()
    {


        String url=CommonUtilities.URL+"/StockService.svc/viewBag?ClientId="+ UserUtilities.getClientId(context)
                +"&UserId="+UserUtilities.getUserId(context);
        // request code 1 to show bag items webservice
        Log.d("VK",url);
        AsyncUtilities asyncUtilities=new AsyncUtilities(context,false,url ,"",1,this);
        asyncUtilities.execute();

    }

    public void removeItems(ArrayList<Integer> stockIds) throws JSONException
    {
        JSONObject jsonObject=new JSONObject();

        //jsonObject.put("stockids",stockIds);
        JSONArray jsonArray=new JSONArray();
        for(int j=0;j<stockIds.size();j++)
        {
            jsonArray.put(stockIds.get(j));
        }
        jsonObject.accumulate("stockids", jsonArray);
        jsonObject.put("ClientId", UserUtilities.getClientId(context));
        jsonObject.put("UserId", UserUtilities.getUserId(context));

        AsyncUtilities asyncUtilities=new AsyncUtilities(context,true,CommonUtilities.URL+"StockService.svc/RemoveFromBag",jsonObject.toString(),2,this);
        asyncUtilities.execute();
    }


    public void placeOrder(Double grandTotal, GsonSelectedItem gsonSelectedItem) throws JSONException {
        JSONObject jsonObject=new JSONObject();

        jsonObject.put("UserId",UserUtilities.getUserId(context));
        jsonObject.put("TotalAmount",grandTotal);
        jsonObject.put("ClientId", UserUtilities.getClientId(context));

        JSONArray jsonArray=new JSONArray();

        for(int c=0;c<gsonSelectedItem.masterBeans.size();c++)
        {
            BagMasterBean masterBean=gsonSelectedItem.masterBeans.get(c);

            for(int i=0;i<masterBean.bagDetails.size();i++)
            {
                JSONObject js=new JSONObject();
                BagDetailsBean details=masterBean.bagDetails.get(i);
                js.put("StockId",details.stockId);
                js.put("ProductId",details.productId);
                js.put("SizeId",details.sizeId);
                js.put("InBagQnty",details.inBagQnty);
                js.put("OrderQnty",details.inBagQnty);
                js.put("PriceId",masterBean.priceId);
                js.put("DeleteStatus","false");
                jsonArray.put(js);

            }
        }
        jsonObject.put("orederList",jsonArray);


        // request code 3 for place order

        AsyncUtilities asyncUtilities=new AsyncUtilities(context,true,CommonUtilities.URL+"OrderService.svc/InsertOrderNew",jsonObject.toString(),3,this);
        asyncUtilities.setAutoCancleable(false);
        asyncUtilities.execute();
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==1 && responseCode==200)
        {
            try
            {
                //CommonUtilities.alert(context,str);
                DownloadUtility downloadUtility = (DownloadUtility) context;
                if(parseBag(str))
                {
                        downloadUtility.onComplete("Success",requestCode,responseCode);
                }
                else
                {
                        downloadUtility.onComplete("Failed",requestCode,responseCode);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                CommonUtilities.alert(context, e.toString());
            }
        }


        if(requestCode==2 && responseCode==200)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;

            if(str.equals("0"))
            {
                downloadUtility.onComplete("Success",requestCode,responseCode);
            }
            else
            {
                downloadUtility.onComplete("Failed",requestCode,responseCode);
            }
        }



        if(requestCode==3 && responseCode==200)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;

           // CommonUtilities.alert(context,str);
            if(parseOrder(str))
            {
                downloadUtility.onComplete("Success",requestCode,responseCode);
            }
            else
            {
                downloadUtility.onComplete("Failed",requestCode,responseCode);
            }

        }

    }

    public GsonOrderCombine gsonMasterObject;
    private boolean parseOrder(String str)
    {
        gsonMasterObject=null;
        JSONObject json;
        try
        {
            Gson gson=new Gson();
            InputStream in=new ByteArrayInputStream(str.getBytes("UTF-8"));
            JsonReader jsonReader=new JsonReader(new InputStreamReader(in,"UTF-8"));
            GsonOrderCombine gsonOrderCombine=gson.fromJson(jsonReader,GsonOrderCombine.class);
            orderCombine=gsonOrderCombine;
            gsonMasterObject=gsonOrderCombine;
            try {
                ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);


                int totalQnty = 0;
                for (int i = 0; i < gsonOrderCombine.list.size(); i++) {
                    OrderDetailBean temp = gsonOrderCombine.list.get(i).toOrderDetail();
                    totalQnty += temp.getQuantity();
                    itemDAOOrder.saveOrderDetails(temp);
                }
                OrderMasterBean masterOrder = gsonOrderCombine.master.toOrderMaster();
                masterOrder.setTotalQnty(totalQnty);
                itemDAOOrder.saveOrderMaster(masterOrder);
            }
            catch (Exception exc)
            {}
            if(gsonMasterObject.warningMessage.contains(""))
            {
                return true;
            }


            return true;
        }
         catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (Exception Ex)
        {

        }

        return  false;
    }


    // -------------------------------------------- parsing -------------------------------------------------------------------

   public String lastOrder="";
    private boolean parseBag(String str) throws JSONException
    {
        try
        {
            ArrayList<BagMasterBean> mList = new ArrayList<>();
            int prodId = 0;
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("bag");
            //ArrayList<CombineBagBean> list=new ArrayList<CombineBagBean>();
            int flag = jsonObject.getInt("flag");
            if (flag != 0) {
                //    bag is empty
            }
            else
            {
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    BagMasterBean mBean = new BagMasterBean();
                    JSONObject json = jsonArray.getJSONObject(i);
                    mBean.userId = json.getInt("UserId");
                    mBean.clientId = json.getInt("ClientId");
                    mBean.transactionType = json.getString("TransactionType");
                    mBean.productId = json.getInt("ProductId");
                    mBean.priceId=json.getInt("PriceId");
                    BagDetailsBean dBean = new BagDetailsBean();

                    dBean.cPrice = json.getDouble("ConsumerPrice");
                    dBean.dPrice = json.getDouble("DealerPrice");
                    dBean.inBagQnty = json.getInt("InBagQty");
                    dBean.productId = json.getInt("ProductId");
                    dBean.productName = json.getString("ProductName");
                    dBean.selQnty = json.getInt("sqn");
                    dBean.sizeName = json.getString("SizeName");
                    dBean.stockId = json.getInt("StockId");
                    dBean.sizeId = json.getInt("SizeId");
                    dBean.iconThmub = json.getString("IconThumb");

                    try
                    {
                        if(i==0)
                        {
                            mBean.EnterDate = CommonUtilities.parseDate(jsonObject.getString("startTime"));
                        }
                    }
                    catch (Exception e)
                    {
                        //CommonUtilities.alert(context, "DATE   :"+e.toString());
                    }

                    //mBean.bagDetails.add(dBean);
                    int flg = 0, indicator = -1;

                    for (int j = 0; j < mList.size(); j++)
                    {
                        if (mBean.productId == mList.get(j).productId) {
                            flg = 1;
                            indicator = j;
                        }
                    }
                    if (flg == 0)
                    {
                        BagDetailsBean temp = dBean;
                        mBean.bagDetails.add(temp);
                        mList.add(mBean);
                    }
                    else if (flg == 1)
                    {
                        mList.get(indicator).bagDetails.add(dBean);
                    }
                }
            }
            bagMasterList.addAll(mList);

            try
            {
                JSONObject jr=  jsonObject.getJSONObject("recentOrder");
                lastOrder="Your last order on "+ DateAndOther.getStringDayfromMillisecond (CommonUtilities.parseDate(jr.getString("OrderDate")))+ ", Last Order Id is "+jr.getInt("OrderId");
            }
            catch (Exception ex)
            {}
        }
        catch (Exception e)
        {
            String qwe=e.toString();
            qwe=qwe;
            e.printStackTrace();

          //  CommonUtilities.alert(context,qwe);
        }
        return true;
    }



}
