package biyaniparker.com.parker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.UserUtilities;

/**
 * Created by bt on 08/13/2016.
 */
public class ItemDAOProduct
{
    Context context;

    public ItemDAOProduct(Context context)
    {
        this.context=context;
       // SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
    }


    public long getLatestChangedProduct()
    {
      try
      {
         SQLiteDatabase db = new DBHELPER(context).getReadableDatabase();
         Cursor c = db.rawQuery("SELECT Max(ChangedDate) as latest FROM Product", null);

         if (c != null) {

             if (c.moveToFirst()) {
                 int i = 0;
                 while (i < c.getCount())
                 {
                      long rt=  c.getLong(c.getColumnIndex("latest"));
                       i++;
                       db.close();
                     return  rt;
                 }
             }
         }

      }
      catch (Exception e)
      {}

      return 0;
    }

    public ArrayList<ProductBean> getAllProducts(int clientId)
    {
        ArrayList<ProductBean> list=new ArrayList<ProductBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM Product where clientId=" + clientId+" and DeleteStatus='false'", null);

       if(c!=null)
       {

           if(c.moveToFirst())
           {
               int i=0;
               while(i<c.getCount())
               {
                   ProductBean bean=new ProductBean();
                   bean.setProductId(c.getInt(c.getColumnIndex("ProductId")));
                   bean.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
                   bean.setProductName(c.getString(c.getColumnIndex("ProductName")));
                   bean.setStripCode(c.getString(c.getColumnIndex("StripCode")));
                   bean.setDetails(c.getString(c.getColumnIndex("Details")));
                   bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                   bean.setCategoryId(c.getInt(c.getColumnIndex("CategoryId")));
                   bean.setIconThumb(c.getString(c.getColumnIndex("IconThumb")));
                   bean.setIconFull(c.getString(c.getColumnIndex("IconFull")));
                   bean.setIconFull1(c.getString(c.getColumnIndex("IconFull1")));
                   bean.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                   bean.setSequenceNo(c.getInt(c.getColumnIndex("SequenceNo")));
                   bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                   bean.setCreatedDate(c.getLong(c.getColumnIndex("CreatedDate")));
                   bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                   bean.setChagedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                   bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                   bean.setIsActive(c.getString(c.getColumnIndex("IsActive")));
                   list.add(bean);
                   c.moveToNext();
                   i++;
               }
           }
       }
        db.close();
        return list;
    }

    public ArrayList<ProductBean> getAllProductsAboveDate(int clientId,long longDate)
    {
        ArrayList<ProductBean> list=new ArrayList<ProductBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        String query="SELECT * FROM Product where clientId=" + clientId+" and DeleteStatus='false' and CreatedDate>"+longDate;
        Cursor c= db.rawQuery(query, null);

        if(c!=null)
        {

            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount())
                {
                    ProductBean bean=new ProductBean();
                    bean.setProductId(c.getInt(c.getColumnIndex("ProductId")));
                    bean.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
                    bean.setProductName(c.getString(c.getColumnIndex("ProductName")));
                    bean.setStripCode(c.getString(c.getColumnIndex("StripCode")));
                    bean.setDetails(c.getString(c.getColumnIndex("Details")));
                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setCategoryId(c.getInt(c.getColumnIndex("CategoryId")));
                    bean.setIconThumb(c.getString(c.getColumnIndex("IconThumb")));
                    bean.setIconFull(c.getString(c.getColumnIndex("IconFull")));
                    bean.setIconFull1(c.getString(c.getColumnIndex("IconFull1")));
                    bean.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    bean.setSequenceNo(c.getInt(c.getColumnIndex("SequenceNo")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(c.getLong(c.getColumnIndex("CreatedDate")));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    bean.setChagedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    bean.setIsActive(c.getString(c.getColumnIndex("IsActive")));
                    long dbLong= c.getLong(c.getColumnIndex("CreatedDate"));
                    if(dbLong>  longDate)
                        list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return list;
    }

    public void insert(ProductBean productBean)
    {
        if(productBean.getProductId()!=0)
        {
            delete(productBean);
        }
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("ProductId",productBean.getProductId());
        contentValues.put("ProductCode",productBean.getProductCode());
        contentValues.put("ProductName",productBean.getProductName());
        contentValues.put("StripCode", productBean.getStripCode());
        contentValues.put("Details", productBean.getDetails());
        contentValues.put("PriceId", productBean.getPriceId());
        contentValues.put("CategoryId", productBean.getCategoryId());
        contentValues.put("IconThumb", productBean.getIconThumb());
        contentValues.put("IconFull", productBean.getIconFull());
        contentValues.put("IconFull1", productBean.getIconFull1());
        contentValues.put("ClientId", productBean.getClientId());
        contentValues.put("SequenceNo", productBean.getSequenceNo());
        contentValues.put("CreatedBy", productBean.getCreatedBy());
        contentValues.put("CreatedDate", productBean.getCreatedDate());
        contentValues.put("ChangedBy", productBean.getChangedBy());
        contentValues.put("ChangedDate", productBean.getChagedDate());
        contentValues.put("DeleteStatus", productBean.getDeleteStatus());
        contentValues.put("IsActive", productBean.getIsActive());
        long status=db.insert("Product",null,contentValues);
        db.close();
    }




    public void update(ProductBean productBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //contentValues.put("ProductId",productBean.getProductId());
        contentValues.put("ProductCode",productBean.getProductCode());
        contentValues.put("ProductName",productBean.getProductName());
        contentValues.put("StripCode", productBean.getStripCode());
        contentValues.put("Details", productBean.getDetails());
        contentValues.put("PriceId", productBean.getPriceId());
        contentValues.put("CategoryId", productBean.getCategoryId());
        contentValues.put("IconThumb", productBean.getIconThumb());
        contentValues.put("IconFull", productBean.getIconFull());
        contentValues.put("IconFull1", productBean.getIconFull1());
        contentValues.put("ClientId", productBean.getClientId());
        contentValues.put("SequenceNo", productBean.getSequenceNo());
        contentValues.put("CreatedBy", productBean.getCreatedBy());
        contentValues.put("CreatedDate", productBean.getCreatedDate());
        contentValues.put("ChangedBy", productBean.getChangedBy());
        contentValues.put("ChangedDate", productBean.getChagedDate());
        contentValues.put("DeleteStatus", productBean.getDeleteStatus());
        contentValues.put("IsActive", productBean.getIsActive());
        long status=db.update("Product", contentValues, "ProductId=" + productBean.getProductId(), null);
        db.close();
    }


    public void  delete(ProductBean productBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        long deleteStatus=db.delete("Product","ProductId="+productBean.getProductId(),null);
        db.close();
    }
    public void  delete()
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("Product",null,null);
        db.close();
    }

    public ArrayList<ProductBean> getProductsByCatId(int catid) {

        ArrayList<ProductBean> list=new ArrayList<ProductBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM Product where clientId=" + UserUtilities.getClientId(context)+" and DeleteStatus='false' and CategoryId="+catid, null);

        if(c!=null)
        {

            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount())
                {
                    ProductBean bean=new ProductBean();
                    bean.setProductId(c.getInt(c.getColumnIndex("ProductId")));
                    bean.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
                    bean.setProductName(c.getString(c.getColumnIndex("ProductName")));
                    bean.setStripCode(c.getString(c.getColumnIndex("StripCode")));
                    bean.setDetails(c.getString(c.getColumnIndex("Details")));
                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setCategoryId(c.getInt(c.getColumnIndex("CategoryId")));
                    bean.setIconThumb(c.getString(c.getColumnIndex("IconThumb")));
                    bean.setIconFull(c.getString(c.getColumnIndex("IconFull")));
                    bean.setIconFull1(c.getString(c.getColumnIndex("IconFull1")));
                    bean.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    bean.setSequenceNo(c.getInt(c.getColumnIndex("SequenceNo")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(c.getLong(c.getColumnIndex("CreatedDate")));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    bean.setChagedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    bean.setIsActive(c.getString(c.getColumnIndex("IsActive")));
                    list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        Collections.reverse(list);
        return list;
    }

    public ProductBean getProduct(int productId)
    {
        ProductBean bean=new ProductBean();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM Product where ProductId="+productId, null);

        if(c!=null)
        {

            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount())
                {

                    bean.setProductId(c.getInt(c.getColumnIndex("ProductId")));
                    bean.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
                    bean.setProductName(c.getString(c.getColumnIndex("ProductName")));
                    bean.setStripCode(c.getString(c.getColumnIndex("StripCode")));
                    bean.setDetails(c.getString(c.getColumnIndex("Details")));
                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setCategoryId(c.getInt(c.getColumnIndex("CategoryId")));
                    bean.setIconThumb(c.getString(c.getColumnIndex("IconThumb")));
                    bean.setIconFull(c.getString(c.getColumnIndex("IconFull")));
                    bean.setIconFull1(c.getString(c.getColumnIndex("IconFull1")));
                    bean.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    bean.setSequenceNo(c.getInt(c.getColumnIndex("SequenceNo")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(c.getLong(c.getColumnIndex("CreatedDate")));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    bean.setChagedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    bean.setIsActive(c.getString(c.getColumnIndex("IsActive")));

                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return bean;
    }




    ///  getting list for filter
    public ArrayList<ProductBean> getCustomList(String stripCode, int catId, int  price)
    {
            String stripcode=(stripCode.equals(""))?("1=1"):("StripCode='"+stripCode+"'");
            String catIdStr=(catId==0)?("1=1"):("CategoryId="+catId);
            String priceId=(price==0)?("1=1"):("PriceId="+price);

        int clientId=UserUtilities.getClientId(context);
        ArrayList<ProductBean> list=new ArrayList<ProductBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM Product where clientId=" + clientId+" and DeleteStatus='false' and "+stripcode+ " and "+catIdStr+" and "+priceId+"", null);

        if(c!=null)
        {

            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount())
                {
                    ProductBean bean=new ProductBean();
                    bean.setProductId(c.getInt(c.getColumnIndex("ProductId")));
                    bean.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
                    bean.setProductName(c.getString(c.getColumnIndex("ProductName")));
                    bean.setStripCode(c.getString(c.getColumnIndex("StripCode")));
                    bean.setDetails(c.getString(c.getColumnIndex("Details")));
                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setCategoryId(c.getInt(c.getColumnIndex("CategoryId")));
                    bean.setIconThumb(c.getString(c.getColumnIndex("IconThumb")));
                    bean.setIconFull(c.getString(c.getColumnIndex("IconFull")));
                    bean.setIconFull1(c.getString(c.getColumnIndex("IconFull1")));
                    bean.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    bean.setSequenceNo(c.getInt(c.getColumnIndex("SequenceNo")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(c.getLong(c.getColumnIndex("CreatedDate")));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    bean.setChagedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    bean.setIsActive(c.getString(c.getColumnIndex("IsActive")));
                    list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return list;

    }

    public ArrayList<ProductBean> getPriceFilteredList(int catID, long min, long max) {

        int clientId=UserUtilities.getClientId(context);
        ArrayList<ProductBean> list=new ArrayList<ProductBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        String query="SELECT Product.* FROM Product, PriceMaster where  Product.DeleteStatus='false' and Product.CategoryId="+catID+" and Cast( PriceMaster.ConsumerPrice as Decimal ) >="+min+" and Cast( PriceMaster.ConsumerPrice as Decimal ) <="+max+" and PriceMaster.PriceId= Product.PriceId";
        Cursor c= db.rawQuery(query, null);

        if(c!=null)
        {

            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount())
                {
                    ProductBean bean=new ProductBean();


                    bean.setProductId(c.getInt(c.getColumnIndex("ProductId")));
                    bean.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
                    bean.setProductName(c.getString(c.getColumnIndex("ProductName")));
                    bean.setStripCode(c.getString(c.getColumnIndex("StripCode")));
                    bean.setDetails(c.getString(c.getColumnIndex("Details")));
                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setCategoryId(c.getInt(c.getColumnIndex("CategoryId")));
                    bean.setIconThumb(c.getString(c.getColumnIndex("IconThumb")));
                    bean.setIconFull(c.getString(c.getColumnIndex("IconFull")));
                    bean.setIconFull1(c.getString(c.getColumnIndex("IconFull1")));
                    bean.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    bean.setSequenceNo(c.getInt(c.getColumnIndex("SequenceNo")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(c.getLong(c.getColumnIndex("CreatedDate")));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    bean.setChagedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    bean.setIsActive(c.getString(c.getColumnIndex("IsActive")));


                    list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return list;
    }

    public ArrayList<ProductBean> getFilteredList(int catID, long min, long max, String stripVal) {

        int clientId=UserUtilities.getClientId(context);
        String stripCondition= stripVal.equals("")? "1=1":" Product.StripCode='"+stripVal+"'";
        ArrayList<ProductBean> list=new ArrayList<ProductBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        String query="SELECT Product.* FROM Product, PriceMaster where  Product.DeleteStatus='false' and "+ stripCondition+" and Product.CategoryId="+catID+" and Cast( PriceMaster.ConsumerPrice as Decimal ) >="+min+" and Cast( PriceMaster.ConsumerPrice as Decimal ) <="+max+" and PriceMaster.PriceId= Product.PriceId";
        Cursor c= db.rawQuery(query, null);

        if(c!=null)
        {

            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount())
                {
                    ProductBean bean=new ProductBean();
                    bean.setProductId(c.getInt(c.getColumnIndex("ProductId")));
                    bean.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
                    bean.setProductName(c.getString(c.getColumnIndex("ProductName")));
                    bean.setStripCode(c.getString(c.getColumnIndex("StripCode")));
                    bean.setDetails(c.getString(c.getColumnIndex("Details")));
                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setCategoryId(c.getInt(c.getColumnIndex("CategoryId")));
                    bean.setIconThumb(c.getString(c.getColumnIndex("IconThumb")));
                    bean.setIconFull(c.getString(c.getColumnIndex("IconFull")));
                    bean.setIconFull1(c.getString(c.getColumnIndex("IconFull1")));
                    bean.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    bean.setSequenceNo(c.getInt(c.getColumnIndex("SequenceNo")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(c.getLong(c.getColumnIndex("CreatedDate")));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    bean.setChagedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    bean.setIsActive(c.getString(c.getColumnIndex("IsActive")));
                    list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return list;
    }

    public long getMaxChangedDate()
    {
        long maxDate=0;
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery("Select ChangedDate  from  Product order by ChangedDate desc LIMIT 1 OFFSET 30",null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount()) {
                    maxDate = c.getLong(c.getColumnIndex("ChangedDate"));
                    i++;
                    c.moveToNext();
                }
            }
        }
        db.close();
        return (maxDate-(86400000*3));
    }

    public HashMap<Integer,ProductBean> getProductsFromCommSepatredIds(String productsIds)
    {
        HashMap<Integer,ProductBean> maps=new HashMap<>();
     //   ArrayList<ProductBean> list=new ArrayList<ProductBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c= db.rawQuery("SELECT * FROM Product where ProductId in  ("+productsIds+")", null);

        if(c!=null)
        {

            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount())
                {
                    ProductBean bean=new ProductBean();
                    bean.setProductId(c.getInt(c.getColumnIndex("ProductId")));
                    bean.setProductCode(c.getString(c.getColumnIndex("ProductCode")));
                    bean.setProductName(c.getString(c.getColumnIndex("ProductName")));
                    bean.setStripCode(c.getString(c.getColumnIndex("StripCode")));
                    bean.setDetails(c.getString(c.getColumnIndex("Details")));
                    bean.setPriceId(c.getInt(c.getColumnIndex("PriceId")));
                    bean.setCategoryId(c.getInt(c.getColumnIndex("CategoryId")));
                    bean.setIconThumb(c.getString(c.getColumnIndex("IconThumb")));
                    bean.setIconFull(c.getString(c.getColumnIndex("IconFull")));
                    bean.setIconFull1(c.getString(c.getColumnIndex("IconFull1")));
                    bean.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    bean.setSequenceNo(c.getInt(c.getColumnIndex("SequenceNo")));
                    bean.setCreatedBy(c.getLong(c.getColumnIndex("CreatedBy")));
                    bean.setCreatedDate(c.getLong(c.getColumnIndex("CreatedDate")));
                    bean.setChangedBy(c.getLong(c.getColumnIndex("ChangedBy")));
                    bean.setChagedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    bean.setIsActive(c.getString(c.getColumnIndex("IsActive")));
                    maps.put(bean.getProductId(),bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return maps;

    }

    public boolean deleteInBulk(String pids)
    {
         SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        //Cursor c= db.rawQuery("SELECT * FROM Product where clientId=" + clientId+" and DeleteStatus='false'", null);
          db.execSQL("update Product set DeleteStatus='true' where ProductId in ("+pids+") ");
          db.close();
        return  true;
    }
}
