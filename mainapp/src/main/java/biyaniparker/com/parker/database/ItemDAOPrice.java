package biyaniparker.com.parker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import biyaniparker.com.parker.beans.PriceBean;
import biyaniparker.com.parker.utilities.CommonUtilities;

/**
 * Created by bt on 08/27/2016.
 */
public class ItemDAOPrice
{
    Context context;

    public ItemDAOPrice(Context context)
    {
       this.context=context;
    }

    public long insert(PriceBean priceBean)
    {
        delete(priceBean);
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("PriceId",priceBean.getPriceId());
        values.put("ConsumerPrice",priceBean.getConsumerPrice());
        values.put("DealerPrice",priceBean.getDealerPrice());
        values.put("ClientId",priceBean.getClientId());
        values.put("CreatedBy",priceBean.getCreatedBy());
        values.put("CreatedDate",priceBean.getCreatedDate());
        values.put("ChangedBy",priceBean.getChangedBy());
        values.put("ChangedDate",priceBean.getChangedDate());
        values.put("DeleteStatus",priceBean.getDeleteStatus());
        long ln= db.insert("PriceMaster",null,values);
        db.close();
        return ln;

    }

    public  void delete(PriceBean bean) {

        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("PriceMaster","PriceId="+bean.getPriceId(),null);
        db.close();
    }
    public  void delete() {

        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("PriceMaster",null,null);
        db.close();
    }

    public ArrayList<PriceBean> getAllPrices(int clientId) {
        ArrayList<PriceBean> arrayList=new ArrayList<PriceBean>();

        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery(" Select * from PriceMaster where ClientId=" + clientId+"  and DeleteStatus='false'", null);
        int i=0;
        if(c!=null)
        {
            if(c.moveToFirst())
            {
                while(i<c.getCount())
                {
                    PriceBean bean=new PriceBean();
                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setConsumerPrice(c.getDouble(c.getColumnIndex("ConsumerPrice")));
                    bean.setDealerPrice(c.getDouble(c.getColumnIndex("DealerPrice")));
                    bean.setClientId(c.getLong(c.getColumnIndex("ClientId")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(CommonUtilities.parseDate(c.getString(c.getColumnIndex("CreatedDate"))));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    try
                    {
                        bean.setChangedDate(CommonUtilities.parseDate(c.getString(c.getColumnIndex("ChangedDate"))));
                    }
                    catch (Exception e){}
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    arrayList.add(bean);
                    i++;
                    c.moveToNext();
                }
            }
        }

        db.close();
        return arrayList;
    }

    public PriceBean getPriceBeanByPriceId(int priceId) {
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();

        Cursor c=db.rawQuery("Select * from PriceMaster where PriceId=" + priceId, null);
        int i=0;
        PriceBean bean=new PriceBean();
        if(c!=null)
        {
            if(c.moveToFirst())
            {


                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setConsumerPrice(c.getDouble(c.getColumnIndex("ConsumerPrice")));
                    bean.setDealerPrice(c.getDouble(c.getColumnIndex("DealerPrice")));
                    bean.setClientId(c.getLong(c.getColumnIndex("ClientId")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(CommonUtilities.parseDate(c.getString(c.getColumnIndex("CreatedDate"))));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    try
                    {
                        bean.setChangedDate(CommonUtilities.parseDate(c.getString(c.getColumnIndex("ChangedDate"))));
                    }
                    catch (Exception e){}
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));

            }
        }
         //   c.close();

        db.close();

        return bean;

    }

    public void update(PriceBean priceBean) {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("PriceId",priceBean.getPriceId());
        values.put("ConsumerPrice",priceBean.getConsumerPrice());
        values.put("DealerPrice",priceBean.getDealerPrice());
        values.put("ClientId",priceBean.getClientId());
        values.put("CreatedBy",priceBean.getCreatedBy());
        values.put("CreatedDate",priceBean.getCreatedDate());
        values.put("ChangedBy",priceBean.getChangedBy());
        values.put("ChangedDate",priceBean.getChangedDate());
        values.put("DeleteStatus",priceBean.getDeleteStatus());
        db.update("PriceMaster",values,"PriceId="+priceBean.getPriceId(),null);
        db.close();
    }

    public HashMap<Integer,PriceBean> getPriceMapper() {

        HashMap<Integer,PriceBean> mapper=new HashMap<>();
//        HashMap<Integer,PriceBean> maps=new HashMap();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery("Select * from PriceMaster", null);

        int i=0;
        if(c!=null)
        {
            if(c.moveToFirst())
            {
                while(i<c.getCount())
                {
                    PriceBean bean=new PriceBean();
                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setConsumerPrice(c.getDouble(c.getColumnIndex("ConsumerPrice")));
                    bean.setDealerPrice(c.getDouble(c.getColumnIndex("DealerPrice")));
                    bean.setClientId(c.getLong(c.getColumnIndex("ClientId")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(CommonUtilities.parseDate(c.getString(c.getColumnIndex("CreatedDate"))));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    try
                    {
                        bean.setChangedDate(CommonUtilities.parseDate(c.getString(c.getColumnIndex("ChangedDate"))));
                    }
                    catch (Exception e){}
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                   // arrayList.add(bean);

                    mapper.put(bean.priceId,bean);
                    i++;
                    c.moveToNext();
                }
            }
        }

        db.close();

        return mapper;
    }
}
