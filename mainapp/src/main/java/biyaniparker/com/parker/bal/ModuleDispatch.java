package biyaniparker.com.parker.bal;

import android.content.Context;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import biyaniparker.com.parker.beans.DispatchDetailBean;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.beans.DispatchMasterBean;
import biyaniparker.com.parker.beans.GsonDispatchCombine;
import biyaniparker.com.parker.beans.GsonDispatchCombineArray;
import biyaniparker.com.parker.beans.OrderMasterBean;
import biyaniparker.com.parker.database.ItemDAODispatch;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.ParsingUtilities;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncStreamParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;
import biyaniparker.com.parker.view.homeadmin.orderdispatch.PartialDispatchListView;
import biyaniparker.com.parker.view.homeadmin.orderdispatch.RecentDispatchListView;

/**
 * Created by bt on 08/23/2016.
 */
public class ModuleDispatch implements DownloadUtility,ParsingUtilities
{

    Context context;
    public ArrayList<DispatchMasterAndDetails>partialDispatchList=new ArrayList<>();
    public ArrayList<DispatchMasterAndDetails> recentDispatchedList=new ArrayList<>();
    public ArrayList<DispatchMasterAndDetails> recentDispatchedUserList=new ArrayList<>();
    public GsonDispatchCombine gsonDispatch=new GsonDispatchCombine();
    public ArrayList<DispatchMasterAndDetails> customDispatchList =new ArrayList<>();

    public ModuleDispatch(Context context)
    {
        this.context=context;
    }




    //------------------------------------call web service ----------------------------

    public void dispatchOrder(DispatchMasterBean dispatchMasterBean) throws JSONException
    {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("OrderId",dispatchMasterBean.getOrderId());
        jsonObject.put("UserId",dispatchMasterBean.getUserId());
        jsonObject.put("TotolAmount",dispatchMasterBean.getTotolAmount());
        jsonObject.put("CustomerId",dispatchMasterBean.getCustomerId());

        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<dispatchMasterBean.dispatchDetails.size();i++)
        {
            JSONObject json=new JSONObject();
            DispatchDetailBean details=dispatchMasterBean.dispatchDetails.get(i);
            json.put("ActualQnty",details.getActualQnty());
            json.put("Quantity",details.getQuantity());
            json.put("SizeId",details.getSizeId());
            json.put("ProductId",details.getProductId());
            json.put("OrderDetailId",details.getOrderDetailId());
            jsonArray.put(json);
        }
        jsonObject.accumulate("details",jsonArray);

        String url= CommonUtilities.URL+"DispatchService.svc/insertDispatch";
            AsyncUtilities asyncUtilities=new AsyncUtilities(context,true,url,jsonObject.toString(),1,this);
            asyncUtilities.execute();
    }




    public void deletePartial(ArrayList<DispatchDetailBean> selected) throws JSONException
    {
            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<selected.size();i++)
            {
                DispatchDetailBean bean=selected.get(i);
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("DispatchId",bean.getDispatchId());
                jsonObject.put("DispatchDetailId",bean.getDispatchDetailId());
                jsonObject.put("UserId", UserUtilities.getUserId(context));
                jsonObject.put("ProductId",bean.getProductId());
                jsonObject.put("SizeId",bean.getSizeId());
                jsonObject.put("ClientId",UserUtilities.getClientId(context));
                try {
                    jsonObject.put("Quantity", bean.getOrderQnty() - bean.getQuantity());
                }
                catch (Exception e)
                {}
                jsonArray.put(jsonObject);
            }


        AsyncUtilities asyncUtilities=new AsyncUtilities(context,true,CommonUtilities.URL+"DispatchService.svc/DeletePartialDispatch",jsonArray.toString(),2,this);
        asyncUtilities.execute();

    }


    public void deletePartialNew(ArrayList<DispatchDetailBean> selected) throws JSONException
    {
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<selected.size();i++)
        {
            DispatchDetailBean bean=selected.get(i);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("DispatchId",bean.getDispatchId());
            jsonObject.put("DispatchDetailId",bean.getDispatchDetailId());
            jsonObject.put("UserId", UserUtilities.getUserId(context));
            jsonObject.put("ProductId",bean.getProductId());
            jsonObject.put("SizeId",bean.getSizeId());
            jsonObject.put("ClientId",UserUtilities.getClientId(context));
            try
            {
                jsonObject.put("Quantity", bean.getOrderQnty() - bean.getQuantity());
            }
            catch (Exception e)
            {}
            try
            {
                jsonObject.put("deleteStockQnt", bean.getOrderQnty() - bean.getQuantity());
            }
            catch (Exception e)
            {}
            try
            {
                jsonObject.put("moveStockQnt", bean.getOrderQnty() - bean.getQuantity());
            }
            catch (Exception e)
            {}
            jsonArray.put(jsonObject);
        }

        String url=CommonUtilities.URL+"DispatchService.svc/Delete_Maove_PartialDispatch";
        AsyncUtilities asyncUtilities=new AsyncUtilities(context,true,url,jsonArray.toString(),2,this);
        asyncUtilities.execute();

    }



    public void getRecentDispatch()
    {

        /*AsyncUtilities asyncUtilities=new AsyncUtilities(context, false, CommonUtilities.URL+"DispatchService.svc/GetAdminRecentDispatch?ClientId="+UserUtilities.getClientId(context),null,3,this);
        asyncUtilities.execute();*/
        AsyncStreamParsingUtilities asyncStreamParsingUtilities=new AsyncStreamParsingUtilities(context, false, CommonUtilities.URL+"DispatchService.svc/getAdminRecentDispatchByChangedDate?ClientId="+UserUtilities.getClientId(context)+"&date="+getMaxDate(),null,3,this,this);
        asyncStreamParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncStreamParsingUtilities.execute();
    }

// request dispached order of a user
    public void getRecentDispatchOfUser()
    {/*
            AsyncUtilities asyncUtilities=new AsyncUtilities(context,false,CommonUtilities.URL+"DispatchService.svc/GetCustomerRecentDispatch?ClientId="+UserUtilities.getClientId(context)+"&CustId="+UserUtilities.getUserId(context),null,3,this);
        asyncUtilities.execute();*/

        AsyncStreamParsingUtilities asyncStreamParsingUtilities=new AsyncStreamParsingUtilities(context,false,CommonUtilities.URL+"DispatchService.svc/GetCustomerRecentDispatch?ClientId="+UserUtilities.getClientId(context)+"&CustId="+UserUtilities.getUserId(context),null,3,this,this);
        asyncStreamParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncStreamParsingUtilities.execute();

    }

    //-------------------------------------- web service response handling------------------------------
    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
       //CommonUtilities.alert(context,str+"\n"+responseCode+"\n"+requestCode);

        DownloadUtility downloadUtility=(DownloadUtility)context;
        if(requestCode==1)
        {

            if (responseCode == 200)
            {
                try {
                    if(parseDispatch(str))
                    {
                        downloadUtility.onComplete("Success", requestCode, responseCode);

                    }
                    else
                    {
                        downloadUtility.onComplete("failed", requestCode, responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                downloadUtility.onComplete("Failed",requestCode,responseCode);
            }

        }

        else if(requestCode==2)
        {
            if(responseCode==200)
            {
                try {
                    if(parsePartialDispatch(str)) {
                        downloadUtility.onComplete("Success",requestCode,responseCode);

                    }
                    else
                    {
                        downloadUtility.onComplete("Failed",requestCode,responseCode);
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }


        else if(requestCode==3)
        {
            if(responseCode==200)
            {
               downloadUtility.onComplete("Success",requestCode,responseCode);
            }
            else
            {
                downloadUtility.onComplete("Failed",requestCode,responseCode);
            }
        }

    }


    private boolean parsePartialDispatch(String str) throws UnsupportedEncodingException
    {
        Gson gson=new Gson();
        InputStream in=new ByteArrayInputStream(str.getBytes("UTF-8"));
        JsonReader jsonReader=new JsonReader(new InputStreamReader(in, "UTF-8"));

        GsonDispatchCombine gsonDispatchCombine=gson.fromJson(jsonReader,GsonDispatchCombine.class);
        delete(gsonDispatchCombine.master.toDispatchMasterBean());
        save(gsonDispatchCombine.master.toDispatchMasterBean());
        for(int i=0;i<gsonDispatchCombine.list.size();i++)
        {
            delete(gsonDispatchCombine.list.get(i).toDispatchDetailBean());
            save(gsonDispatchCombine.list.get(i).toDispatchDetailBean());
        }

        return true;

    }

    private void delete(DispatchDetailBean dispatchDetailBean)
    {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        itemDAODispatch.deleteDispatchDetail(dispatchDetailBean);
    }

    private void delete(DispatchMasterBean dispatchMasterBean)
    {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        itemDAODispatch.deleteDispatchMaster(dispatchMasterBean);
    }

    private boolean parseDispatch(String str) throws UnsupportedEncodingException {
        Gson gson=new Gson();
        InputStream in=new ByteArrayInputStream(str.getBytes("UTF-8"));
        JsonReader jsonReader=new JsonReader(new InputStreamReader(in, "UTF-8"));

        GsonDispatchCombine gsonDispatchCombine=gson.fromJson(jsonReader,GsonDispatchCombine.class);

        if(gsonDispatchCombine.flag==0) {
            gsonDispatch = gsonDispatchCombine;
            save(gsonDispatchCombine.master.toDispatchMasterBean());
            for (int i = 0; i < gsonDispatchCombine.list.size(); i++) {
                save(gsonDispatchCombine.list.get(i).toDispatchDetailBean());
            }
            ModuleOrder moduleOrder = new ModuleOrder(context);
            moduleOrder.save(gsonDispatchCombine.order.toOrderMaster());
            return true;
        }
        else
        {
            return false;
        }
    }

    private void save(DispatchDetailBean dispatchDetailBean) {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        itemDAODispatch.insertDispatchDetail(dispatchDetailBean);
    }

    private void save(DispatchMasterBean dispatchMasterBean)
    {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        itemDAODispatch.insertDispatchMaster(dispatchMasterBean);
    }


    public void getPartialDispatchList()
    {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        partialDispatchList.clear();
        partialDispatchList.addAll(itemDAODispatch.getPartialList());
    }


    public void getRecenteDispatchList( )
    {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        recentDispatchedList.clear();
        recentDispatchedList.addAll(itemDAODispatch.getRecentList());

    }

    public void getRecentDispatchListOfUser(long userid)
    {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        recentDispatchedUserList.clear();
        recentDispatchedUserList.addAll(itemDAODispatch.getRecentListOfUser(userid));

    }


    public DispatchMasterAndDetails getDispatchByDispatchId(int dispathId)
    {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        return itemDAODispatch.getDispatchMasterAndDetailsBean(dispathId);
    }


    @Override
    public boolean parseStreamingData(InputStream stream)
    {
        try {
            Gson gson=new Gson();

            JsonReader jsonReader=new JsonReader(new InputStreamReader(stream,"UTF-8"));
            jsonReader.beginArray();
            while (jsonReader.hasNext())
            {
                GsonDispatchCombine gsonDispatchCombine=gson.fromJson(jsonReader,GsonDispatchCombine.class);
                delete(gsonDispatchCombine.master.toDispatchMasterBean());
                save(gsonDispatchCombine.master.toDispatchMasterBean());
                for(int i=0;i<gsonDispatchCombine.list.size();i++)
                {
                    delete(gsonDispatchCombine.list.get(i).toDispatchDetailBean());
                    save(gsonDispatchCombine.list.get(i).toDispatchDetailBean());
                }
            }
            jsonReader.endArray();
            jsonReader.close();

            getRecenteDispatchList();
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
        return false;
    }

    public  long getMaxDate()
    {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        return itemDAODispatch.getLatestChangedDate();
    }

    public void getCustomList(String custName, long fromDate, long toDate)
    {
        ItemDAODispatch itemDAODispatch=new ItemDAODispatch(context);
        customDispatchList.clear();
        customDispatchList.addAll(itemDAODispatch.getFilteredList(custName, fromDate, toDate));


    }
}
