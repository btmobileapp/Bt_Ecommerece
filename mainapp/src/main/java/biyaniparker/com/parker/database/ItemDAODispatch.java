package biyaniparker.com.parker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import biyaniparker.com.parker.beans.DispatchDetailBean;
import biyaniparker.com.parker.beans.DispatchMasterAndDetails;
import biyaniparker.com.parker.beans.DispatchMasterBean;
import biyaniparker.com.parker.beans.GsonDispatchCombine;

/**
 * Created by bt on 08/23/2016.
 */
public class ItemDAODispatch {

    Context context;
    public ItemDAODispatch(Context context)
    {
        this.context=context;
    }

    public void insertDispatchMaster(DispatchMasterBean dispatchMasterBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
         long status=db.insert("DispatchMaster",null,convertToContent(dispatchMasterBean));
        db.close();

    }

    private ContentValues convertToContent(DispatchMasterBean bean) {
        ContentValues values=new ContentValues();
        values.put("DispatchId",bean.getDispatchId());
        values.put("DispatchNo",bean.getDispatchNo());
        values.put("ChallanNo",bean.getChallanNo());
        values.put("DispatchDate",bean.getDispatchDate());
        values.put("ChallanDate",bean.getChallanDate());
        values.put("DispatchBy",bean.getDispatchBy());
        values.put("OrderId",bean.getOrderId());
        values.put("CustomerId",bean.getCustomerId());
        values.put("Transport",bean.getTransport());
        values.put("Parcel",bean.getParcel());
        values.put("VatTinNo",bean.getVatTinNo());
        values.put("CstTinNo",bean.getCstTinNo());
        values.put("CheckedBy",bean.getCheckedBy());
        values.put("ReceivedBy",bean.getReceivedBy());
        //values.put("EnterBy",bean.getEnterD);
        values.put("EnterDate",bean.getEnterDate());
        values.put("ChangeBy",bean.getCheckedBy());
        values.put("Name",bean.getName());
        values.put("Address",bean.getAddress());
        values.put("ShopName",bean.getShopName());
        values.put("ChangedDate",bean.getChangedDate());
        values.put("DispatchStatus",bean.getDispatchStatus());
        values.put("TotolAmount",bean.getTotolAmount());
      values.put("OrderDate",bean.getOrderDate());
        return values;
    }

    public void insertDispatchDetail(DispatchDetailBean dispatchDetailBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase() ;
        ContentValues values=convertToContent(dispatchDetailBean);
        long l=db.insert("DispatchDetails", null, values);
        db.close();
    }

    private ContentValues convertToContent(DispatchDetailBean b)
    {
        ContentValues values=new ContentValues();


        values.put("DispatchDetailId",b.getDispatchDetailId());

        values.put("DispatchId",b.getDispatchId());
        values.put("OrderDetailId",b.getOrderDetailId());
        values.put("ProductId",b.getProductId());
        values.put("SizeId",b.getSizeId());
        values.put("Quantity",b.getQuantity());
        values.put("DeleteStatus",b.getDeleteStatus());
        values.put("DispatchStatus",b.getDispatchStatus());
        values.put("ProductName", b.getProductName());

        values.put("OrderQnty",b.getOrderQnty());
        values.put("ConsumerPrice",b.getConsumerPrice());
        values.put("DealerPrice", b.getDealerPrice());
        values.put("SizeName", b.getSizeName());

        return values;
    }


    public long getLatestChangedDate()
    {
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery(" Select ChangedDate  from  DispatchMaster order by ChangedDate desc LIMIT 1 OFFSET 14  ", null);
        c.moveToFirst();
        int i=0;
        long date=0;
        while(i<c.getCount()) {
            date = c.getLong(c.getColumnIndex("ChangedDate"));
            i++;
            c.moveToNext();
        }
        c.close();
        db.close();
        return  date;
    }



    // get details and master of dispatch
    public ArrayList<DispatchMasterAndDetails> getPartialList()
    {
        ArrayList<DispatchMasterAndDetails> list=new ArrayList<>();

        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();

        Cursor c=db.rawQuery(" Select * from DispatchMaster where DispatchStatus='partial'",null);
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                DispatchMasterAndDetails dispatchMasterAndDetails=new DispatchMasterAndDetails();
                DispatchMasterAndDetails bean=new DispatchMasterAndDetails();
                    bean.master=new DispatchMasterBean();
                bean.master.dispatchId=c.getInt(c.getColumnIndex("DispatchId"));
                bean.master.dispatchNo=c.getInt(c.getColumnIndex("DispatchNo"));
                bean.master.challanNo=c.getInt(c.getColumnIndex("ChallanNo"));
                bean.master.dispatchDate=c.getLong(c.getColumnIndex("DispatchDate"));
                bean.master.challanDate=c.getLong(c.getColumnIndex("ChallanDate"));
                bean.master.dispatchBy=c.getInt(c.getColumnIndex("DispatchBy"));
                bean.master.orderId=c.getInt(c.getColumnIndex("OrderId"));
                bean.master.customerId=c.getInt(c.getColumnIndex("CustomerId"));
                bean.master.transport=c.getString(c.getColumnIndex("Transport"));
                bean.master.parcel=c.getString(c.getColumnIndex("Parcel"));
                bean.master.vatTinNo=c.getString(c.getColumnIndex("VatTinNo"));
                bean.master.checkedBy=c.getInt(c.getColumnIndex("CheckedBy"));
                bean.master.receivedBy=c.getInt(c.getColumnIndex("ReceivedBy"));
                bean.master.enterDate=c.getLong(c.getColumnIndex("EnterDate"));
                bean.master.changeBy=c.getInt(c.getColumnIndex("ChangeBy"));
                bean.master.changedDate=c.getLong(c.getColumnIndex("ChangedDate"));
                bean.master.name=c.getString(c.getColumnIndex("Name"));
                bean.master.shopName=c.getString(c.getColumnIndex("ShopName"));
                bean.master.address=c.getString(c.getColumnIndex("Address"));
                bean.master.totolAmount=c.getDouble(c.getColumnIndex("TotolAmount"));
                bean.master.orderDate=c.getLong(c.getColumnIndex("OrderDate"));
               // bean.master.DispatchStatus=c.getString(c.getColumnIndex("DispatchStatus"))



                ArrayList<DispatchDetailBean> detailsList=new ArrayList<>();
                Cursor cursor=db.rawQuery("select * from DispatchDetails where DispatchId=" + bean.master.getDispatchId()+" and DispatchStatus='partial'", null);
                if(cursor!=null)
                {
                    cursor.moveToFirst();
                    DispatchDetailBean detail=null;//=new DispatchDetailBean();
                    int j=0;
                    while(j<cursor.getCount())
                    {
                        detail=new DispatchDetailBean();
                        detail.setDispatchDetailId(cursor.getInt(cursor.getColumnIndex("DispatchDetailId")));
                        detail.setDispatchId(cursor.getInt(cursor.getColumnIndex("DispatchId")));
                        detail.setOrderDetailId(cursor.getInt(cursor.getColumnIndex("OrderDetailId")));
                        detail.setProductId(cursor.getInt(cursor.getColumnIndex("ProductId")));
                        detail.setSizeId(cursor.getInt(cursor.getColumnIndex("SizeId")));
                        detail.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                        detail.setDeleteStatus(cursor.getString(cursor.getColumnIndex("DeleteStatus")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                        detail.setOrderQnty(cursor.getInt(cursor.getColumnIndex("OrderQnty")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setDealerPrice(cursor.getString(cursor.getColumnIndex("DealerPrice")));
                        detail.setSizeName(cursor.getString(cursor.getColumnIndex("SizeName")));
                        detail.setDispatchStatus(cursor.getString(cursor.getColumnIndex("DispatchStatus")));
                        detailsList.add(detail);
                        j++;
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                bean.details=new ArrayList<>();
                bean.details.addAll(detailsList);
                i++;
                c.moveToNext();
                list.add(bean);
            }
            c.close();
            db.close();
        }
        return list;
    }


    // delete DispatchMaster record
    public void deleteDispatchMaster(DispatchMasterBean dispatchMasterBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        long l=db.delete("DispatchMaster", "DispatchId=" + dispatchMasterBean.getDispatchId(), null);
        db.close();
    }

    public void deleteDispatchDetail(DispatchDetailBean dispatchDetailBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        long l=db.delete("DispatchDetails", "DispatchDetailId=" + dispatchDetailBean.getDispatchDetailId(), null);
        db.close();
    }


    // get recent dispatch list

    public ArrayList<DispatchMasterAndDetails> getRecentList()
    {
        ArrayList<DispatchMasterAndDetails> list=new ArrayList<>();

        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();

        Cursor c=db.rawQuery(" Select * from DispatchMaster ",null);
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                DispatchMasterAndDetails dispatchMasterAndDetails=new DispatchMasterAndDetails();
                DispatchMasterAndDetails bean=new DispatchMasterAndDetails();
                bean.master=new DispatchMasterBean();
                bean.master.dispatchId=c.getInt(c.getColumnIndex("DispatchId"));
                bean.master.dispatchNo=c.getInt(c.getColumnIndex("DispatchNo"));
                bean.master.challanNo=c.getInt(c.getColumnIndex("ChallanNo"));
                bean.master.dispatchDate=c.getLong(c.getColumnIndex("DispatchDate"));
                bean.master.challanDate=c.getLong(c.getColumnIndex("ChallanDate"));
                bean.master.dispatchBy=c.getInt(c.getColumnIndex("DispatchBy"));
                bean.master.orderId=c.getInt(c.getColumnIndex("OrderId"));
                bean.master.customerId=c.getInt(c.getColumnIndex("CustomerId"));
                bean.master.transport=c.getString(c.getColumnIndex("Transport"));
                bean.master.parcel=c.getString(c.getColumnIndex("Parcel"));
                bean.master.vatTinNo=c.getString(c.getColumnIndex("VatTinNo"));
                bean.master.checkedBy=c.getInt(c.getColumnIndex("CheckedBy"));
                bean.master.receivedBy=c.getInt(c.getColumnIndex("ReceivedBy"));
                bean.master.enterDate=c.getLong(c.getColumnIndex("EnterDate"));
                bean.master.changeBy=c.getInt(c.getColumnIndex("ChangeBy"));
                bean.master.changedDate=c.getLong(c.getColumnIndex("ChangedDate"));
                bean.master.name=c.getString(c.getColumnIndex("Name"));
                bean.master.shopName=c.getString(c.getColumnIndex("ShopName"));
                bean.master.address=c.getString(c.getColumnIndex("Address"));
                bean.master.totolAmount=c.getDouble(c.getColumnIndex("TotolAmount"));
                bean.master.orderDate=c.getLong(c.getColumnIndex("OrderDate"));
                // bean.master.DispatchStatus=c.getString(c.getColumnIndex("DispatchStatus"))



                ArrayList<DispatchDetailBean> detailsList=new ArrayList<>();
                Cursor cursor=db.rawQuery("select * from DispatchDetails where DispatchId=" + bean.master.getDispatchId(), null);
                if(cursor!=null)
                {
                    cursor.moveToFirst();
                    DispatchDetailBean detail=null;//=new DispatchDetailBean();
                    int j=0;
                    while(j<cursor.getCount())
                    {
                        detail=new DispatchDetailBean();
                        detail.setDispatchDetailId(cursor.getInt(cursor.getColumnIndex("DispatchDetailId")));
                        detail.setDispatchId(cursor.getInt(cursor.getColumnIndex("DispatchId")));
                        detail.setOrderDetailId(cursor.getInt(cursor.getColumnIndex("OrderDetailId")));
                        detail.setProductId(cursor.getInt(cursor.getColumnIndex("ProductId")));
                        detail.setSizeId(cursor.getInt(cursor.getColumnIndex("SizeId")));
                        detail.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                        detail.setDeleteStatus(cursor.getString(cursor.getColumnIndex("DeleteStatus")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                        detail.setOrderQnty(cursor.getInt(cursor.getColumnIndex("OrderQnty")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setDealerPrice(cursor.getString(cursor.getColumnIndex("DealerPrice")));
                        detail.setSizeName(cursor.getString(cursor.getColumnIndex("SizeName")));
                        detailsList.add(detail);
                        j++;
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                bean.details=new ArrayList<>();
                bean.details.addAll(detailsList);
                i++;
                c.moveToNext();
                list.add(bean);
            }
            c.close();
            db.close();
        }
        return list;
    }

    public ArrayList<DispatchMasterAndDetails> getRecentListOfUser(long userid)
    {
        ArrayList<DispatchMasterAndDetails> list=new ArrayList<>();

        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();

        Cursor c=db.rawQuery(" Select * from DispatchMaster where CustomerId="+userid,null);
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                DispatchMasterAndDetails dispatchMasterAndDetails=new DispatchMasterAndDetails();
                DispatchMasterAndDetails bean=new DispatchMasterAndDetails();
                bean.master=new DispatchMasterBean();
                bean.master.dispatchId=c.getInt(c.getColumnIndex("DispatchId"));
                bean.master.dispatchNo=c.getInt(c.getColumnIndex("DispatchNo"));
                bean.master.challanNo=c.getInt(c.getColumnIndex("ChallanNo"));
                bean.master.dispatchDate=c.getLong(c.getColumnIndex("DispatchDate"));
                bean.master.challanDate=c.getLong(c.getColumnIndex("ChallanDate"));
                bean.master.dispatchBy=c.getInt(c.getColumnIndex("DispatchBy"));
                bean.master.orderId=c.getInt(c.getColumnIndex("OrderId"));
                bean.master.customerId=c.getInt(c.getColumnIndex("CustomerId"));
                bean.master.transport=c.getString(c.getColumnIndex("Transport"));
                bean.master.parcel=c.getString(c.getColumnIndex("Parcel"));
                bean.master.vatTinNo=c.getString(c.getColumnIndex("VatTinNo"));
                bean.master.checkedBy=c.getInt(c.getColumnIndex("CheckedBy"));
                bean.master.receivedBy=c.getInt(c.getColumnIndex("ReceivedBy"));
                bean.master.enterDate=c.getLong(c.getColumnIndex("EnterDate"));
                bean.master.changeBy=c.getInt(c.getColumnIndex("ChangeBy"));
                bean.master.changedDate=c.getLong(c.getColumnIndex("ChangedDate"));
                bean.master.name=c.getString(c.getColumnIndex("Name"));
                bean.master.shopName=c.getString(c.getColumnIndex("ShopName"));
                bean.master.address=c.getString(c.getColumnIndex("Address"));
                bean.master.totolAmount=c.getDouble(c.getColumnIndex("TotolAmount"));
                bean.master.orderDate=c.getLong(c.getColumnIndex("OrderDate"));

                // bean.master.DispatchStatus=c.getString(c.getColumnIndex("DispatchStatus"))



                ArrayList<DispatchDetailBean> detailsList=new ArrayList<>();
                Cursor cursor=db.rawQuery("select * from DispatchDetails where DispatchId=" + bean.master.getDispatchId(), null);
                if(cursor!=null)
                {
                    cursor.moveToFirst();
                    DispatchDetailBean detail=null;//=new DispatchDetailBean();
                    int j=0;
                    while(j<cursor.getCount())
                    {
                        detail=new DispatchDetailBean();
                        detail.setDispatchDetailId(cursor.getInt(cursor.getColumnIndex("DispatchDetailId")));
                        detail.setDispatchId(cursor.getInt(cursor.getColumnIndex("DispatchId")));
                        detail.setOrderDetailId(cursor.getInt(cursor.getColumnIndex("OrderDetailId")));
                        detail.setProductId(cursor.getInt(cursor.getColumnIndex("ProductId")));
                        detail.setSizeId(cursor.getInt(cursor.getColumnIndex("SizeId")));
                        detail.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                        detail.setDeleteStatus(cursor.getString(cursor.getColumnIndex("DeleteStatus")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                        detail.setOrderQnty(cursor.getInt(cursor.getColumnIndex("OrderQnty")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setDealerPrice(cursor.getString(cursor.getColumnIndex("DealerPrice")));
                        detail.setSizeName(cursor.getString(cursor.getColumnIndex("SizeName")));
                        detailsList.add(detail);
                        j++;
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                bean.details=new ArrayList<>();
                bean.details.addAll(detailsList);
                i++;
                c.moveToNext();
                list.add(bean);
            }
            c.close();
            db.close();
        }
        return list;
    }

    public DispatchMasterAndDetails getDispatchMasterAndDetailsBean(int dispathId)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        DispatchMasterAndDetails bean=new DispatchMasterAndDetails();
        Cursor c=db.rawQuery(" Select * from DispatchMaster where DispatchId="+dispathId,null);
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {


                bean.master=new DispatchMasterBean();
                bean.master.dispatchId=c.getInt(c.getColumnIndex("DispatchId"));
                bean.master.dispatchNo=c.getInt(c.getColumnIndex("DispatchNo"));
                bean.master.challanNo=c.getInt(c.getColumnIndex("ChallanNo"));
                bean.master.dispatchDate=c.getLong(c.getColumnIndex("DispatchDate"));
                bean.master.challanDate=c.getLong(c.getColumnIndex("ChallanDate"));
                bean.master.dispatchBy=c.getInt(c.getColumnIndex("DispatchBy"));
                bean.master.orderId=c.getInt(c.getColumnIndex("OrderId"));
                bean.master.customerId=c.getInt(c.getColumnIndex("CustomerId"));
                bean.master.transport=c.getString(c.getColumnIndex("Transport"));
                bean.master.parcel=c.getString(c.getColumnIndex("Parcel"));
                bean.master.vatTinNo=c.getString(c.getColumnIndex("VatTinNo"));
                bean.master.checkedBy=c.getInt(c.getColumnIndex("CheckedBy"));
                bean.master.receivedBy=c.getInt(c.getColumnIndex("ReceivedBy"));
                bean.master.enterDate=c.getLong(c.getColumnIndex("EnterDate"));
                bean.master.changeBy=c.getInt(c.getColumnIndex("ChangeBy"));
                bean.master.changedDate=c.getLong(c.getColumnIndex("ChangedDate"));
                bean.master.name=c.getString(c.getColumnIndex("Name"));
                bean.master.shopName=c.getString(c.getColumnIndex("ShopName"));
                bean.master.address=c.getString(c.getColumnIndex("Address"));
                bean.master.totolAmount=c.getDouble(c.getColumnIndex("TotolAmount"));
                bean.master.orderDate=c.getLong(c.getColumnIndex("OrderDate"));

                // bean.master.DispatchStatus=c.getString(c.getColumnIndex("DispatchStatus"))



                ArrayList<DispatchDetailBean> detailsList=new ArrayList<>();
                Cursor cursor=db.rawQuery("select * from DispatchDetails where DispatchId=" + bean.master.getDispatchId(), null);
                if(cursor!=null)
                {
                    cursor.moveToFirst();
                    DispatchDetailBean detail=null;//=new DispatchDetailBean();
                    int j=0;
                    while(j<cursor.getCount())
                    {
                        detail=new DispatchDetailBean();
                        detail.setDispatchDetailId(cursor.getInt(cursor.getColumnIndex("DispatchDetailId")));
                        detail.setDispatchId(cursor.getInt(cursor.getColumnIndex("DispatchId")));
                        detail.setOrderDetailId(cursor.getInt(cursor.getColumnIndex("OrderDetailId")));
                        detail.setProductId(cursor.getInt(cursor.getColumnIndex("ProductId")));
                        detail.setSizeId(cursor.getInt(cursor.getColumnIndex("SizeId")));
                        detail.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                        detail.setDeleteStatus(cursor.getString(cursor.getColumnIndex("DeleteStatus")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                        detail.setOrderQnty(cursor.getInt(cursor.getColumnIndex("OrderQnty")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setDealerPrice(cursor.getString(cursor.getColumnIndex("DealerPrice")));
                        detail.setSizeName(cursor.getString(cursor.getColumnIndex("SizeName")));
                        detailsList.add(detail);
                        j++;
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                bean.details=new ArrayList<>();
                bean.details.addAll(detailsList);
                i++;
                c.moveToNext();

            }
            c.close();
            db.close();
        }
        return bean;
    }

    public List<DispatchMasterAndDetails> getFilteredList(String custName, long fromDate, long toDate) {


        String nameCondition= custName.equals("")?"1=1":"  Name = '"+custName+"'";
        String dateCondition= (fromDate==0 || toDate==0) ? "1=1": "DispatchDate BETWEEN "+fromDate+"  and "+toDate ;

        ArrayList<DispatchMasterAndDetails> list=new ArrayList<>();

        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();

        Cursor c=db.rawQuery(" Select * from DispatchMaster where "+nameCondition+" and "+ dateCondition +" ",null);
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                DispatchMasterAndDetails dispatchMasterAndDetails=new DispatchMasterAndDetails();
                DispatchMasterAndDetails bean=new DispatchMasterAndDetails();
                bean.master=new DispatchMasterBean();
                bean.master.dispatchId=c.getInt(c.getColumnIndex("DispatchId"));
                bean.master.dispatchNo=c.getInt(c.getColumnIndex("DispatchNo"));
                bean.master.challanNo=c.getInt(c.getColumnIndex("ChallanNo"));
                bean.master.dispatchDate=c.getLong(c.getColumnIndex("DispatchDate"));
                bean.master.challanDate=c.getLong(c.getColumnIndex("ChallanDate"));
                bean.master.dispatchBy=c.getInt(c.getColumnIndex("DispatchBy"));
                bean.master.orderId=c.getInt(c.getColumnIndex("OrderId"));
                bean.master.customerId=c.getInt(c.getColumnIndex("CustomerId"));
                bean.master.transport=c.getString(c.getColumnIndex("Transport"));
                bean.master.parcel=c.getString(c.getColumnIndex("Parcel"));
                bean.master.vatTinNo=c.getString(c.getColumnIndex("VatTinNo"));
                bean.master.checkedBy=c.getInt(c.getColumnIndex("CheckedBy"));
                bean.master.receivedBy=c.getInt(c.getColumnIndex("ReceivedBy"));
                bean.master.enterDate=c.getLong(c.getColumnIndex("EnterDate"));
                bean.master.changeBy=c.getInt(c.getColumnIndex("ChangeBy"));
                bean.master.changedDate=c.getLong(c.getColumnIndex("ChangedDate"));
                bean.master.name=c.getString(c.getColumnIndex("Name"));
                bean.master.shopName=c.getString(c.getColumnIndex("ShopName"));
                bean.master.address=c.getString(c.getColumnIndex("Address"));
                bean.master.totolAmount=c.getDouble(c.getColumnIndex("TotolAmount"));
                bean.master.orderDate=c.getLong(c.getColumnIndex("OrderDate"));

                // bean.master.DispatchStatus=c.getString(c.getColumnIndex("DispatchStatus"))



                ArrayList<DispatchDetailBean> detailsList=new ArrayList<>();
                Cursor cursor=db.rawQuery("select * from DispatchDetails where DispatchId=" + bean.master.getDispatchId(), null);
                if(cursor!=null)
                {
                    cursor.moveToFirst();
                    DispatchDetailBean detail=null;//=new DispatchDetailBean();
                    int j=0;
                    while(j<cursor.getCount())
                    {
                        detail=new DispatchDetailBean();
                        detail.setDispatchDetailId(cursor.getInt(cursor.getColumnIndex("DispatchDetailId")));
                        detail.setDispatchId(cursor.getInt(cursor.getColumnIndex("DispatchId")));
                        detail.setOrderDetailId(cursor.getInt(cursor.getColumnIndex("OrderDetailId")));
                        detail.setProductId(cursor.getInt(cursor.getColumnIndex("ProductId")));
                        detail.setSizeId(cursor.getInt(cursor.getColumnIndex("SizeId")));
                        detail.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                        detail.setDeleteStatus(cursor.getString(cursor.getColumnIndex("DeleteStatus")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                        detail.setOrderQnty(cursor.getInt(cursor.getColumnIndex("OrderQnty")));
                        detail.setConsumerPrice(cursor.getString(cursor.getColumnIndex("ConsumerPrice")));
                        detail.setDealerPrice(cursor.getString(cursor.getColumnIndex("DealerPrice")));
                        detail.setSizeName(cursor.getString(cursor.getColumnIndex("SizeName")));
                        detailsList.add(detail);
                        j++;
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                bean.details=new ArrayList<>();
                bean.details.addAll(detailsList);
                i++;
                c.moveToNext();
                list.add(bean);
            }
            c.close();
            db.close();
        }
        return list;
    }

    public void deleteDispatchDetail()
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        long l=db.delete("DispatchDetails", null, null);
        l=db.delete("DispatchMaster", null, null);

        db.close();
    }
}
