package biyaniparker.com.parker.bal;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import biyaniparker.com.parker.beans.PriceBean;
import biyaniparker.com.parker.database.ItemDAOPrice;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt on 08/27/2016.
 */
public class ModulePrice implements DownloadUtility {
    Context context;
    public ArrayList<PriceBean> list=new ArrayList<PriceBean>();

    public ModulePrice(Context context)
    {
        this.context=context;
    }
    // --------------------------------------------------------------web service call------------------------------------------------------


    public void insertPrice(PriceBean priceBean) throws JSONException {
        JSONObject jsonObject=new JSONObject();
        //jsonObject.put("PriceId",priceBean.getPriceId())
        jsonObject.put("ConsumerPrice",priceBean.getConsumerPrice());
        jsonObject.put("DealerPrice",priceBean.getDealerPrice());
        jsonObject.put("ClientId",priceBean.getClientId());
        jsonObject.put("CreatedBy",priceBean.getCreatedBy());
        jsonObject.put("CreatedDate",priceBean.getCreatedDate());
        jsonObject.put("ChangedBy",priceBean.getChangedBy());
        jsonObject.put("ChangedDate",priceBean.getChangedDate());
        jsonObject.put("DeleteStatus",priceBean.getDeleteStatus());
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/InsertPriceMaster",jsonObject.toString(),2,this);
        serverAsync.execute();

    }


    public void updatePrice(PriceBean priceBean) throws JSONException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("PriceId",priceBean.getPriceId());
        jsonObject.put("ConsumerPrice",priceBean.getConsumerPrice());
        jsonObject.put("DealerPrice",priceBean.getDealerPrice());
        jsonObject.put("ClientId",priceBean.getClientId());
        jsonObject.put("CreatedBy",priceBean.getCreatedBy());
        jsonObject.put("CreatedDate",priceBean.getCreatedDate());
        jsonObject.put("ChangedBy",priceBean.getChangedBy());
        jsonObject.put("ChangedDate",priceBean.getChangedDate());
        jsonObject.put("DeleteStatus",priceBean.getDeleteStatus());
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/UpdatePriceMaster",jsonObject.toString(),3,this);
        serverAsync.execute();
    }

    public void deletePrice(PriceBean priceBean) throws JSONException
    {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("PriceId",priceBean.getPriceId());
        jsonObject.put("ConsumerPrice",priceBean.getConsumerPrice());
        jsonObject.put("DealerPrice",priceBean.getDealerPrice());
        jsonObject.put("ClientId",priceBean.getClientId());
        jsonObject.put("CreatedBy",priceBean.getCreatedBy());
        jsonObject.put("CreatedDate",priceBean.getCreatedDate());
        jsonObject.put("ChangedBy",priceBean.getChangedBy());
        jsonObject.put("ChangedDate",priceBean.getChangedDate());
        jsonObject.put("DeleteStatus",priceBean.getDeleteStatus());
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/DeletePriceMaster",jsonObject.toString(),4,this);
        serverAsync.execute();
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {

        // request code 1 for getting all users

        if(requestCode==1)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parsePrices(str)) {

                    downloadUtility.onComplete("Success", 1, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 1, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 1, responseCode);
            }
        }

        //   COde=2 is for Create Price response

        if(requestCode==2)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseInsertedPrice(str)) {

                    downloadUtility.onComplete("Success", 2, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 2, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 2, responseCode);
            }
        }



        //   COde=3 is for Update Price response

        if(requestCode==3)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseUpdatedPrice(str)) {

                    downloadUtility.onComplete("Success", 3, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 3, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 3, responseCode);
            }
        }


        //   COde=4 is for Update Price response

        if(requestCode==4)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                if(str.equals("0"))
                {
                    downloadUtility.onComplete("dependancy",requestCode,responseCode);
                }
                else
                {
                    if (parseUpdatedPrice(str)) {

                        downloadUtility.onComplete("Success", requestCode, responseCode);
                    } else {
                        downloadUtility.onComplete("Failed", requestCode, responseCode);
                    }
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", requestCode, responseCode);
            }
        }
    }

    private boolean parseUpdatedPrice(String str) {
        ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
        try {
            itemDAOPrice.update(ParsePrice(str));
            return  true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean parsePrices(String str) {
        ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
        try {
            JSONArray jsonArray=new JSONArray(str);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject json=jsonArray.getJSONObject(i);
                itemDAOPrice.insert(ParsePrice(json.toString()));
            }
            return  true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private boolean parseInsertedPrice(String str)
    {
        ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
        try {
            itemDAOPrice.insert(ParsePrice(str));
            return  true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    private PriceBean ParsePrice(String str) throws JSONException {
        PriceBean bean=new PriceBean();
        JSONObject json=new JSONObject(str);
        bean.setPriceId(json.getInt("PriceId"));
        bean.setConsumerPrice(json.getDouble("ConsumerPrice"));
        bean.setDealerPrice(json.getDouble("DealerPrice"));
        bean.setClientId(json.getLong("ClientId"));
        bean.setCreatedBy(json.getLong("CreatedBy"));
        bean.setCreatedDate(CommonUtilities.parseDate(json.getString("CreatedDate")));
        bean.setChangedBy(json.getLong("ChangedBy"));
        try
        {
            bean.setChangedDate(CommonUtilities.parseDate(json.getString("ChangedDate")));
        }
        catch (Exception e){}
        bean.setDeleteStatus(json.getString("DeleteStatus"));

        return bean;
    }

    public void syncPrice() {
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"ProductService.svc/GetAllPrices?ClientId="+1,"",1,this);
        serverAsync.execute();
    }

    public void getPrices() {
        ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
        list.clear();
        list.addAll(itemDAOPrice.getAllPrices(UserUtilities.getClientId(context)));
    }

    public PriceBean getPriceBean(int priceId) {
        ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);

        return  itemDAOPrice.getPriceBeanByPriceId(priceId);
    }


    public Double getPriceByPriceId(int priceId)
    {
        ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
        return itemDAOPrice.getPriceBeanByPriceId(priceId).getConsumerPrice();
    }


}
