package biyaniparker.com.parker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import biyaniparker.com.parker.beans.SizeDetailBean;
import biyaniparker.com.parker.beans.SizeMaster;

/**
 * Created by bt on 08/19/2016.
 */
public class ItemDAOSizeMaster
{
    Context context;

    public ItemDAOSizeMaster(Context context)
    {
        this.context=context;
    }



    //  to insert sizemaster and size details
    public void insertSize(SizeMaster bean,ArrayList<SizeDetailBean> sizeDetailBeans)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        if(bean.getSizeId()!=0){delete(bean);}
        ContentValues values=new ContentValues();
        values.put("SizeId",bean.getSizeId());
        values.put("SizeName",bean.getSizeName());
        values.put("ClientId",bean.getClientId());
        values.put("SequenceNo",bean.getSequenceNo());
        values.put("DeleteStatus",bean.getDeleteStatus());
        values.put("CreatedBy",bean.getCreatedBy());
        values.put("CreatedDate",bean.getCreatedDate());
        values.put("ChangedBy",bean.getChangedBy());
        values.put("ChangedDate",bean.getChangedDate());
        long l=db.insert("SizeMaster",null,values);
        long status;
        for(int i=0;i< sizeDetailBeans.size();i++)
        {
            if(sizeDetailBeans.get(i).getSizeDetailId()!=0){deleteSizeDetail(sizeDetailBeans.get(i));}
            ContentValues v=new ContentValues();
            v.put("SizeDetailId", sizeDetailBeans.get(i).getSizeDetailId());
            v.put("SizeId", sizeDetailBeans.get(i).getSizeId());
            v.put("CategoryId", sizeDetailBeans.get(i).getCategoryId());
            v.put("DeleteStatus", sizeDetailBeans.get(i).getDeleteStatus());
            status=db.insert("SizeDetails",null,v);
        }

        db.close();


    }


    public ArrayList<SizeMaster> getAllSize(int clientId)
    {
        ArrayList<SizeMaster> sizeList=new ArrayList<SizeMaster>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery("Select * from SizeMaster where ClientId="+ clientId+" and DeleteStatus='false' order by SequenceNo",null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    SizeMaster bean = new SizeMaster();
                    try {
                        bean.sizeId = c.getInt(c.getColumnIndex("SizeId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.sizeName = c.getString(c.getColumnIndex("SizeName"));
                    try {
                        bean.sequenceNo = c.getInt(c.getColumnIndex("SequenceNo"));
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    bean.deleteStatus = c.getString(c.getColumnIndex("DeleteStatus"));

                    try {
                        bean.changedBy = c.getLong(c.getColumnIndex("ChangedBy"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        bean.changedDate = c.getLong(c.getColumnIndex("ChangedDate"));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try {
                        bean.createdBy = c.getLong(c.getColumnIndex("CreatedBy"));
                    }
                    catch (Exception e)
                    {

                    }
                    try{bean.createdDate=c.getLong(c.getColumnIndex("CreatedDate"));}catch (Exception e){}


                    try {
                        bean.clientId=c.getInt(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    sizeList.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return sizeList;
    }

    private void deleteSizeDetail(SizeDetailBean sizeDetailBean) {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("SizeDetails","SizeDetailId="+ sizeDetailBean.getSizeDetailId(),null);
        db.close();
    }

    private void delete(SizeMaster bean) {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("SizeMaster","SizeId="+bean.getSizeId(),null);
        db.close();
    }


    public void deleteAllSizes() {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("SizeMaster",null,null);
        db.close();
    }
    public void deleteAllDetailsSizes() {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("SizeDetails",null,null);
        db.close();
    }

    public SizeMaster getSizeBean(int sizeId)
    {

        SQLiteDatabase db = new DBHELPER(context).getReadableDatabase();
        Cursor c = db.rawQuery("Select * from SizeMaster where  SizeId=" + sizeId, null);
        SizeMaster bean = new SizeMaster();
        if (c != null)
        {
            if (c.moveToFirst()) {

                int i = 0;
                while (i < c.getCount()) {

                    try {
                        bean.sizeId = c.getInt(c.getColumnIndex("SizeId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.sizeName = c.getString(c.getColumnIndex("SizeName"));
                    try {
                        bean.sequenceNo = c.getInt(c.getColumnIndex("SequenceNo"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.deleteStatus = c.getString(c.getColumnIndex("DeleteStatus"));

                    try {
                        bean.changedBy = c.getLong(c.getColumnIndex("ChangedBy"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        bean.changedDate = c.getLong(c.getColumnIndex("ChangedDate"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        bean.createdBy = c.getLong(c.getColumnIndex("CreatedBy"));
                    } catch (Exception e) {

                    }
                    try {
                        bean.createdDate = c.getLong(c.getColumnIndex("CreatedDate"));
                    } catch (Exception e) {
                    }


                    try {
                        bean.clientId = c.getInt(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return  bean;
                  //  i++;
                }
            }
        }
        return null;
    }


    public ArrayList<SizeDetailBean> getSizeDetailsBySizeId(int sizeId) {

        ArrayList<SizeDetailBean> sizeList=new ArrayList<SizeDetailBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery("Select * from SizeDetails where SizeId="+ sizeId+" and DeleteStatus='false' ",null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    SizeDetailBean bean = new SizeDetailBean();
                    try {
                        bean.sizeDetailId = c.getInt(c.getColumnIndex("SizeDetailId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        bean.SizeId = c.getInt(c.getColumnIndex("SizeId"));
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    bean.DeleteStatus = c.getString(c.getColumnIndex("DeleteStatus"));


                    try {
                        bean.CategoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    sizeList.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }


        return sizeList;
    }

    public ArrayList<SizeDetailBean> getSizeDetailsByCategoryId(int catId) {



        ArrayList<SizeDetailBean> sizeList=new ArrayList<SizeDetailBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery("Select SizeDetails.*,SizeMaster.SequenceNo from SizeDetails,SizeMaster where SizeDetails.CategoryId="+ catId+" and SizeDetails.DeleteStatus='false' " +
                "and   SizeDetails.SizeId=SizeMaster.SizeId and SizeMaster.DeleteStatus='false' order by SizeMaster.SequenceNo",null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    SizeDetailBean bean = new SizeDetailBean();
                    try {
                        bean.sizeDetailId = c.getInt(c.getColumnIndex("SizeDetailId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        bean.SizeId = c.getInt(c.getColumnIndex("SizeId"));
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    bean.DeleteStatus = c.getString(c.getColumnIndex("DeleteStatus"));


                    try {
                        bean.CategoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    sizeList.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }


        return sizeList;

    }

    public void updateSizeMaster(SizeMaster sizeMaster)
    {
           SQLiteDatabase db= new DBHELPER(context).getWritableDatabase();
        //long status=db.update("")
        db.close();
    }

    public  HashMap<Integer,SizeMaster> getAllSizeForBuffer() {

        HashMap<Integer,SizeMaster> mapper=new HashMap<>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery("Select * from SizeMaster ",null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    SizeMaster bean = new SizeMaster();
                    try {
                        bean.sizeId = c.getInt(c.getColumnIndex("SizeId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.sizeName = c.getString(c.getColumnIndex("SizeName"));
                    try {
                        bean.sequenceNo = c.getInt(c.getColumnIndex("SequenceNo"));
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    bean.deleteStatus = c.getString(c.getColumnIndex("DeleteStatus"));

                    try {
                        bean.changedBy = c.getLong(c.getColumnIndex("ChangedBy"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        bean.changedDate = c.getLong(c.getColumnIndex("ChangedDate"));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    try {
                        bean.createdBy = c.getLong(c.getColumnIndex("CreatedBy"));
                    }
                    catch (Exception e)
                    {

                    }
                    try{bean.createdDate=c.getLong(c.getColumnIndex("CreatedDate"));}catch (Exception e){}


                    try {
                        bean.clientId=c.getInt(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    //sizeList.add(bean);
                    mapper.put(bean.sizeId,bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return mapper;
    }
}
