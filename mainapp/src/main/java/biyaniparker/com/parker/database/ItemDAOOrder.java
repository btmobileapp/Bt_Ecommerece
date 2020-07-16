package biyaniparker.com.parker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import biyaniparker.com.parker.beans.GsonOrderDetails;
import biyaniparker.com.parker.beans.OrderDetailBean;
import biyaniparker.com.parker.beans.OrderMasterBean;

/**
 * Created by bt on 09/13/2016.
 */
public class ItemDAOOrder
{
    Context context;

    public ItemDAOOrder(Context context)
    {
        this.context=context;
    }

    public long  saveOrderMaster(OrderMasterBean orderMasterBean)
    {
        deleteOrderMaster(orderMasterBean);
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        long l=db.insert("OrderMaster", null, convertToContent(orderMasterBean));
        db.close();
        return l;
    }

    private void deleteOrderMaster(OrderMasterBean orderMasterBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("OrderMaster", "OrderId=" + orderMasterBean.getOrderId(), null);
        db.close();
    }



    // method overloading

    private ContentValues convertToContent(OrderMasterBean orderMasterBean)
    {
        ContentValues values=new ContentValues();
        values.put("OrderId",orderMasterBean.getOrderId());
        values.put("OrderDate",orderMasterBean.getOrderDate());
        values.put("UserId",orderMasterBean.getUserId());
        values.put("OrderStatus",orderMasterBean.getOrderStatus());
        values.put("ChangeBy",orderMasterBean.getChangeBy());
        values.put("ChangedDate",orderMasterBean.getChangedDate());
        values.put("DeleteStatus",orderMasterBean.getDeleteStatus());
        values.put("TotolAmount",orderMasterBean.getTotolAmount());
        values.put("Address",orderMasterBean.getAddress());
        values.put("ShopName", orderMasterBean.getShopName());
        values.put("Name", orderMasterBean.getName());
        values.put("TotalQnty", orderMasterBean.getTotalQnty());

        return values;
    }

    public void saveOrderDetails(OrderDetailBean orderDetails)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        deleteOrderDetails(orderDetails);
        long l= db.insert("OrderDetails", null, convertToContent(orderDetails));
        db.close();
    }

    // method overloading

    private ContentValues convertToContent(OrderDetailBean orderDetails)
    {
        ContentValues values=new ContentValues();
        values.put("OrderDetailId",orderDetails.getOrderDetailId());
        values.put("OrderId",orderDetails.getOrderId());
        values.put("ProductId",orderDetails.getProductId());
        values.put("SizeId",orderDetails.getSizeId());
        values.put("PriceId",orderDetails.getPriceId());
        values.put("Quantity",orderDetails.getQuantity());
        values.put("DeleteStatus",orderDetails.getDeleteStatus());
        values.put("ProductName",orderDetails.getProductName());
        values.put("SizeName", orderDetails.getSizeName());
        values.put("ConsumerPrice", orderDetails.getConsumerPrice());
        values.put("DealerPrice",orderDetails.getDealerPrice());
        values.put("IconThumb",orderDetails.getIconThumb());

        return values;
    }

    private void deleteOrderDetails(OrderDetailBean orderDetails)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("OrderDetails", "OrderDetailId=" + orderDetails.getOrderDetailId(), null);
        db.close();
    }

    public ArrayList<OrderMasterBean> getOrders()
    {
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery(" Select * from OrderMaster where OrderStatus='inrequest' and DeleteStatus='false' order by OrderId desc ", null);
        ArrayList<OrderMasterBean> list=new ArrayList<OrderMasterBean>();
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                OrderMasterBean bean=new OrderMasterBean();
                bean.setOrderId(c.getInt(c.getColumnIndex("OrderId")));
                bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                bean.setAddress(c.getString(c.getColumnIndex("Address")));
                bean.setChangeBy(c.getLong(c.getColumnIndex("ChangeBy")));
                bean.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                bean.setName(c.getString(c.getColumnIndex("Name")));
                bean.setOrderDate(c.getLong(c.getColumnIndex("OrderDate")));
                bean.setOrderStatus(c.getString(c.getColumnIndex("OrderStatus")));
                bean.setShopName(c.getString(c.getColumnIndex("ShopName")));
                bean.setTotolAmount(c.getString(c.getColumnIndex("TotolAmount")));
                bean.setUserId(c.getLong(c.getColumnIndex("UserId")));
                bean.setTotalQnty(c.getInt(c.getColumnIndex("TotalQnty")));
                list.add(bean);
                c.moveToNext();
                i++;
            }
        }

        db.close();
        return list;
    }


    // getting one recorde of order Master by order id

    public OrderMasterBean getOrderBeanByOrderId(int orderId)
    {

        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery(" Select * from OrderMaster where OrderId="+orderId, null);
        OrderMasterBean bean=new OrderMasterBean();
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {

                bean.setOrderId(c.getInt(c.getColumnIndex("OrderId")));
                bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                bean.setAddress(c.getString(c.getColumnIndex("Address")));
                bean.setChangeBy(c.getLong(c.getColumnIndex("ChangeBy")));
                bean.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                bean.setName(c.getString(c.getColumnIndex("Name")));
                bean.setOrderDate(c.getLong(c.getColumnIndex("OrderDate")));
                bean.setOrderStatus(c.getString(c.getColumnIndex("OrderStatus")));
                bean.setShopName(c.getString(c.getColumnIndex("ShopName")));
                bean.setTotolAmount(c.getString(c.getColumnIndex("TotolAmount")));
                bean.setUserId(c.getLong(c.getColumnIndex("UserId")));
                i++;
            }
        }
        db.close();
        return bean;
    }

    public ArrayList<OrderDetailBean> getOrderDetailsByOrderId(int orderId)
    {
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery(" Select * from OrderDetails where OrderId="+orderId, null);
        ArrayList<OrderDetailBean> list=new ArrayList<OrderDetailBean>();

        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                OrderDetailBean bean=new OrderDetailBean();
                bean.setOrderId(c.getInt(c.getColumnIndex("OrderId")));
                bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                bean.setOrderDetailId(c.getInt(c.getColumnIndex("OrderDetailId")));
                bean.setProductId(c.getInt(c.getColumnIndex("ProductId")));
                bean.setSizeId(c.getInt(c.getColumnIndex("SizeId")));
                bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                bean.setQuantity(c.getInt(c.getColumnIndex("Quantity")));
                bean.setIconThumb(c.getString(c.getColumnIndex("IconThumb")));
                bean.setProductName(c.getString(c.getColumnIndex("ProductName")));
                bean.setSizeName(c.getString(c.getColumnIndex("SizeName")));
                bean.setConsumerPrice(c.getString(c.getColumnIndex("ConsumerPrice")));
                bean.setDealerPrice(c.getString(c.getColumnIndex("DealerPrice")));
                //bean.setUserId(c.getInt(c.getColumnIndex("UserId")));
                list.add(bean);
                c.moveToNext();
                i++;
            }
        }
        db.close();
        return list;
    }

    public ArrayList<OrderMasterBean> getUserOrders(long userId)
    {
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c = db.rawQuery(" Select * from OrderMaster where DeleteStatus='false' and UserId="+userId+" order by OrderId desc ", null);
        ArrayList<OrderMasterBean> list=new ArrayList<OrderMasterBean>();
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                OrderMasterBean bean=new OrderMasterBean();
                bean.setOrderId(c.getInt(c.getColumnIndex("OrderId")));
                bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                bean.setAddress(c.getString(c.getColumnIndex("Address")));
                bean.setChangeBy(c.getLong(c.getColumnIndex("ChangeBy")));
                bean.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                bean.setName(c.getString(c.getColumnIndex("Name")));
                bean.setOrderDate(c.getLong(c.getColumnIndex("OrderDate")));
                bean.setOrderStatus(c.getString(c.getColumnIndex("OrderStatus")));
                bean.setShopName(c.getString(c.getColumnIndex("ShopName")));
                bean.setTotolAmount(c.getString(c.getColumnIndex("TotolAmount")));
                bean.setTotalQnty(c.getInt(c.getColumnIndex("TotalQnty")));
                bean.setUserId(c.getLong(c.getColumnIndex("UserId")));
                list.add(bean);
                c.moveToNext();
                i++;
            }
        }
        db.close();
        return list;

    }

    public void updateOrderMaster(OrderMasterBean orderMasterBean)
    {
            SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
            db.update("OrderMaster",convertToContent(orderMasterBean),"OrderId="+orderMasterBean.getOrderId(),null);
        db.close();

    }

    public ArrayList<OrderMasterBean> getCustomOrders(String custName, long fromDate, long toDate)
    {

        String dateCondition= (fromDate==0 || toDate==0) ? "1==1": "OrderDate BETWEEN "+fromDate+"  and "+toDate ;
        String nameCondition= custName.equals("")? "1==1" : "Name='"+custName+"'" ;
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c = db.rawQuery(" Select * from OrderMaster where OrderStatus='inrequest' and DeleteStatus='false' and " + nameCondition + " and " + dateCondition + " order by OrderId desc ", null);
        //Cursor c=db.rawQuery(" Select * from OrderMaster where OrderStatus='inrequest' and DeleteStatus='false' and Name=" + custName + " and OrderDate BETWEEN "+fromDate+"  and "+toDate+" order by OrderId desc ", null);
        ArrayList<OrderMasterBean> list=new ArrayList<OrderMasterBean>();
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                OrderMasterBean bean=new OrderMasterBean();
                bean.setOrderId(c.getInt(c.getColumnIndex("OrderId")));
                bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                bean.setAddress(c.getString(c.getColumnIndex("Address")));
                bean.setChangeBy(c.getLong(c.getColumnIndex("ChangeBy")));
                bean.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                bean.setName(c.getString(c.getColumnIndex("Name")));
                bean.setOrderDate(c.getLong(c.getColumnIndex("OrderDate")));
                bean.setOrderStatus(c.getString(c.getColumnIndex("OrderStatus")));
                bean.setShopName(c.getString(c.getColumnIndex("ShopName")));
                bean.setTotolAmount(c.getString(c.getColumnIndex("TotolAmount")));
                bean.setUserId(c.getLong(c.getColumnIndex("UserId")));
                bean.setTotalQnty(c.getInt(c.getColumnIndex("TotalQnty")));
                list.add(bean);
                c.moveToNext();
                i++;
            }
        }

        db.close();
        return list;
    }



    public List<OrderMasterBean> getDeletedCustomOrders(String custName, long fromDate, long toDate) {
        String dateCondition= (fromDate==0 || toDate==0) ? "1==1": "OrderDate BETWEEN "+fromDate+"  and "+toDate ;
        String nameCondition= custName.equals("")? "1==1" : "Name='"+custName+"'" ;
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c = db.rawQuery(" Select * from OrderMaster where OrderStatus='delete' and DeleteStatus='true' and " + nameCondition + " and " + dateCondition + " order by OrderId desc ", null);
        //Cursor c=db.rawQuery(" Select * from OrderMaster where OrderStatus='inrequest' and DeleteStatus='false' and Name=" + custName + " and OrderDate BETWEEN "+fromDate+"  and "+toDate+" order by OrderId desc ", null);
        ArrayList<OrderMasterBean> list=new ArrayList<OrderMasterBean>();
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                OrderMasterBean bean=new OrderMasterBean();
                bean.setOrderId(c.getInt(c.getColumnIndex("OrderId")));
                bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                bean.setAddress(c.getString(c.getColumnIndex("Address")));
                bean.setChangeBy(c.getLong(c.getColumnIndex("ChangeBy")));
                bean.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                bean.setName(c.getString(c.getColumnIndex("Name")));
                bean.setOrderDate(c.getLong(c.getColumnIndex("OrderDate")));
                bean.setOrderStatus(c.getString(c.getColumnIndex("OrderStatus")));
                bean.setShopName(c.getString(c.getColumnIndex("ShopName")));
                bean.setTotolAmount(c.getString(c.getColumnIndex("TotolAmount")));
                bean.setUserId(c.getLong(c.getColumnIndex("UserId")));
                bean.setTotalQnty(c.getInt(c.getColumnIndex("TotalQnty")));
                list.add(bean);
                c.moveToNext();
                i++;
            }
        }

        db.close();
        return list;
    }



    public List<OrderMasterBean> getDeletedOrders()
    {
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c = db.rawQuery(" Select * from OrderMaster where OrderStatus='delete' and DeleteStatus='true'  order by OrderId desc ", null);
        //Cursor c=db.rawQuery(" Select * from OrderMaster where OrderStatus='inrequest' and DeleteStatus='false' and Name=" + custName + " and OrderDate BETWEEN "+fromDate+"  and "+toDate+" order by OrderId desc ", null);
        ArrayList<OrderMasterBean> list=new ArrayList<OrderMasterBean>();
        if(c!=null)
        {
            int i=0;
            c.moveToFirst();
            while(i<c.getCount())
            {
                OrderMasterBean bean=new OrderMasterBean();
                bean.setOrderId(c.getInt(c.getColumnIndex("OrderId")));
                bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                bean.setAddress(c.getString(c.getColumnIndex("Address")));
                bean.setChangeBy(c.getLong(c.getColumnIndex("ChangeBy")));
                bean.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                bean.setName(c.getString(c.getColumnIndex("Name")));
                bean.setOrderDate(c.getLong(c.getColumnIndex("OrderDate")));
                bean.setOrderStatus(c.getString(c.getColumnIndex("OrderStatus")));
                bean.setShopName(c.getString(c.getColumnIndex("ShopName")));
                bean.setTotolAmount(c.getString(c.getColumnIndex("TotolAmount")));
                bean.setUserId(c.getLong(c.getColumnIndex("UserId")));
                bean.setTotalQnty(c.getInt(c.getColumnIndex("TotalQnty")));
                list.add(bean);
                c.moveToNext();
                i++;
            }
        }

        db.close();
        return list;
    }


    public long getMaxChangedDate()
    {
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor cursor=db.rawQuery("Select ChangedDate from OrderMaster  where DeleteStatus='false' order by ChangedDate desc LIMIT 1 OFFSET 14  ", null);
        long maxCDate=0;
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                maxCDate = cursor.getLong(cursor.getColumnIndex("ChangedDate"));
            }
        }
        cursor.close();
        db.close();
        return maxCDate;

    }

    public void deleteAllOrder() {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("OrderDetails", null, null);
        db.delete("OrderMaster", null, null);
        db.close();
    }

}
