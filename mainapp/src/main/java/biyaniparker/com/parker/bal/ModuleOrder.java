package biyaniparker.com.parker.bal;

import android.content.Context;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import biyaniparker.com.parker.beans.GsonOrder;
import biyaniparker.com.parker.beans.GsonOrderDetails;
import biyaniparker.com.parker.beans.GsonOrderMaster;
import biyaniparker.com.parker.beans.GsonSelectedItem;
import biyaniparker.com.parker.beans.OrderDetailBean;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.database.ItemDAOCategory;
import biyaniparker.com.parker.database.ItemDAODispatch;
import biyaniparker.com.parker.database.ItemDAOOrder;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.ParsingUtilities;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncStreamParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt on 09/13/2016.
 */
public class ModuleOrder implements DownloadUtility,ParsingUtilities {
    Context context;
    public ArrayList<OrderMasterBean> orderList = new ArrayList<>();
    public ArrayList<OrderMasterBean> userOrderList = new ArrayList<>();
    public ArrayList<OrderMasterBean> customOrderList = new ArrayList<>();
    public ArrayList<OrderMasterBean> deletedOrderList = new ArrayList<>();

    long dispatcgid = 0;

    public ModuleOrder(Context context) {
        this.context = context;
    }

    public void getRecentOrders(long maxChanedDate) {
       /* AsyncUtilities asyncUtilities = new AsyncUtilities(context, false, CommonUtilities.URL + "OrderService.svc/getAdminRecentOrder?ClientId=" + UserUtilities.getClientId(context), "", 1, this);
        asyncUtilities.setAutoCancleable(false);
        asyncUtilities.execute();*/

        AsyncStreamParsingUtilities asyncParsingUtilities=new AsyncStreamParsingUtilities(context, false, CommonUtilities.URL + "OrderService.svc/getAdminRecentOrder?ClientId=" + UserUtilities.getClientId(context)+"&ChangedDate="+maxChanedDate, "", 1, this,this);
        asyncParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncParsingUtilities.setAutoCancelable(false);
        asyncParsingUtilities.execute();


    }

    //  get user recent orders

    public void getRecentOrdersOfUser() {
       /* AsyncUtilities asyncUtilities = new AsyncUtilities(context, false, CommonUtilities.URL + "OrderService.svc/getUserRecentOrder?ClientId=" + UserUtilities.getClientId(context) + "&UserId=" + UserUtilities.getUserId(context), "", 1, this);
        asyncUtilities.execute();*/


        AsyncStreamParsingUtilities asyncParsingUtilities=new AsyncStreamParsingUtilities(context, false, CommonUtilities.URL + "OrderService.svc/getUserRecentOrder?ClientId=" + UserUtilities.getClientId(context) + "&UserId=" + UserUtilities.getUserId(context), "", 1, this,this);
        asyncParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncParsingUtilities.execute();

    }

    // delete order

    public void deleteOrder(OrderMasterBean bean) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("OrderId", bean.getOrderId());
        jsonObject.put("UserId", bean.getUserId());
        jsonObject.put("ChangeBy", UserUtilities.getUserId(context));
        jsonObject.put("ClientId", UserUtilities.getClientId(context));

        AsyncUtilities asyncUtilities = new AsyncUtilities(context, true, CommonUtilities.URL + "OrderService.svc/deleteOrder", jsonObject.toString(), 2, this);
        asyncUtilities.execute();

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        // CommonUtilities.alert(context,str);
        DownloadUtility downloadUtility = (DownloadUtility) context;
        if (requestCode == 1)
        {
            if (responseCode == 200)
            {
                downloadUtility.onComplete("Success", requestCode, responseCode);
            }
            else
            {
                downloadUtility.onComplete("Failed", requestCode, responseCode);
            }
        } else if (requestCode == 2) {
            if (responseCode == 200) {
                try {
                    JSONObject j = new JSONObject(str);
                    if (j.getInt("OrderId") != 0) {

                        Gson gson = new Gson();
                        InputStream stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
                        JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
                        GsonOrderMaster orderMaster = gson.fromJson(reader, GsonOrderMaster.class);
                        update(orderMaster.toOrderMaster());

                        downloadUtility.onComplete("Success", requestCode, responseCode);
                    } else {
                        downloadUtility.onComplete("Failed", requestCode, responseCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }
        }

    }


    private void update(OrderMasterBean orderMasterBean) {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        itemDAOOrder.updateOrderMaster(orderMasterBean);
    }

    public void save(OrderDetailBean orderDetails) {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        itemDAOOrder.saveOrderDetails(orderDetails);
    }

    public void save(OrderMasterBean orderMasterBean) {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        dispatcgid = itemDAOOrder.saveOrderMaster(orderMasterBean);
    }


    //-----------------------------------------------------web service call------------------------------------


    public void getOrderList()
    {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        orderList.clear();
        orderList.addAll(itemDAOOrder.getOrders());
        //  CommonUtilities.alert(context,orderList.size()+"");
    }


    //------------------------------------------------------ local db manupations --------------------------------------------------

    public long getMaxChanedDate()
    {
        ItemDAOOrder itemDAOOrder=new ItemDAOOrder(context);
        return itemDAOOrder.getMaxChangedDate();
    }


    public OrderMasterBean getOrderBeanById(int orderId) {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        return itemDAOOrder.getOrderBeanByOrderId(orderId);
    }


    public ArrayList<OrderDetailBean> getOrderDetailsById(int orderId) {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        return itemDAOOrder.getOrderDetailsByOrderId(orderId);
    }


    public void getUserOrderList() {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        userOrderList.clear();
        userOrderList.addAll(itemDAOOrder.getUserOrders(UserUtilities.getUserId(context)));
    }

    public void getDeletedOrderList()
    {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        deletedOrderList.clear();
        deletedOrderList.addAll(itemDAOOrder.getDeletedOrders());

    }


    public void getCustomList(String custName, long fromDate, long toDate) {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        customOrderList.clear();
        customOrderList.addAll(itemDAOOrder.getCustomOrders(custName, fromDate, toDate));
        Collections.reverse(customOrderList);
    }

    public void getDeleteCustomList(String custName, long fromDate, long toDate)
    {
        ItemDAOOrder itemDAOOrder = new ItemDAOOrder(context);
        deletedOrderList.clear();
        deletedOrderList.addAll(itemDAOOrder.getDeletedCustomOrders(custName, fromDate, toDate));

    }

    @Override
    public boolean parseStreamingData(InputStream stream)
    {
        try
        {
            // InputStream stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));

            reader.beginArray();
            int ocnt=0;

            while (reader.hasNext())
            {

                int totalqnt = 0;
                GsonOrder gsonOrder = gson.fromJson(reader, GsonOrder.class);

                for (int c = 0; c < gsonOrder.od.size(); c++)
                {
                    OrderDetailBean detailBean = gsonOrder.od.get(c).toOrderDetail();
                    totalqnt = totalqnt + detailBean.getQuantity();
                    save(detailBean);
                }


                OrderMasterBean masterBean = gsonOrder.om.toOrderMaster();
                masterBean.setTotalQnty(totalqnt);
                save(masterBean);
                ocnt++;

            }
            reader.endArray();

            Log.d("Dispatch Count", ocnt + "");


            if(UserUtilities.getUserType(context).equalsIgnoreCase("Admin"))
            {
                getOrderList();
            }
            else
            {
                getUserOrderList();
            }

            Log.d("Order DB Count", orderList.size() + "");

            Log.d("Ok","OK");

            return  true;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        catch (Exception e)
        {
        }

        return false;
    }



}
