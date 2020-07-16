package biyaniparker.com.parker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import biyaniparker.com.parker.beans.CategoryBean;

/**
 * Created by bt on 08/11/2016.
 */
public class ItemDAOCategory
{
    Context context;

    public ItemDAOCategory(Context context)
    {
        this.context=context;
      //  SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
    }


    // get all categories


    public ArrayList<CategoryBean> getAllCategories(int clientId)
    {
        ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        Cursor c=   db.rawQuery("SELECT * FROM Category where clientId=" + clientId +" and DeleteStatus='false'", null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    CategoryBean bean = new CategoryBean();
                    try {
                        bean.categoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.categoryCode = c.getString(c.getColumnIndex("CategoryCode"));
                    bean.categoryName = c.getString(c.getColumnIndex("CategoryName"));
                    bean.icon = c.getString(c.getColumnIndex("Icon"));
                    bean.isLast = c.getString(c.getColumnIndex("IsLast"));
                    try {
                        bean.parentCategoryId = c.getInt(c.getColumnIndex("ParentCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    bean.deleteStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    bean.remark=c.getString(c.getColumnIndex("Remark"));
                    try {
                        bean.clientId=c.getLong(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return list;
    }


    // Parent Category list for Size Master


    public ArrayList<CategoryBean> getAllParentCategories(int clientId, int parentId)
    {
        ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        Cursor c=   db.rawQuery("SELECT * FROM Category where clientId=" + clientId+" and DeleteStatus='false' and ParentCategoryId="+parentId , null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    CategoryBean bean = new CategoryBean();
                    try {
                        bean.categoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.categoryCode = c.getString(c.getColumnIndex("CategoryCode"));
                    bean.categoryName = c.getString(c.getColumnIndex("CategoryName"));
                    bean.icon = c.getString(c.getColumnIndex("Icon"));
                    bean.isLast = c.getString(c.getColumnIndex("IsLast"));
                    try {
                        bean.parentCategoryId = c.getInt(c.getColumnIndex("ParentCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    bean.deleteStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    bean.remark=c.getString(c.getColumnIndex("Remark"));
                    try {
                        bean.clientId=c.getLong(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return list;
    }




    public CategoryBean getCategory(int CategoryId)
    {
      //  ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        Cursor c=   db.rawQuery("SELECT * FROM Category where CategoryId=" + CategoryId , null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    CategoryBean bean = new CategoryBean();
                    try {
                        bean.categoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.categoryCode = c.getString(c.getColumnIndex("CategoryCode"));
                    bean.categoryName = c.getString(c.getColumnIndex("CategoryName"));
                    bean.icon = c.getString(c.getColumnIndex("Icon"));
                    bean.isLast = c.getString(c.getColumnIndex("IsLast"));
                    try {
                        bean.parentCategoryId = c.getInt(c.getColumnIndex("ParentCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    bean.deleteStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    bean.remark=c.getString(c.getColumnIndex("Remark"));
                    try {
                        bean.clientId=c.getLong(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                   // list.add(bean);
                   // c.moveToNext();
                   // i++;

                    db.close();
                    return bean;
                }
            }
        }
        db.close();
        return null;
    }


    public void insert(CategoryBean categoryBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        if(categoryBean.getCategoryId()!=0)
        {
            delete(categoryBean);
        }
        ContentValues contentValues=new ContentValues();
        contentValues.put("CategoryId",categoryBean.getCategoryId());
        contentValues.put("CategoryCode",categoryBean.getCategoryCode());
        contentValues.put("CategoryName",categoryBean.getCategoryName());
        contentValues.put("ParentCategoryId",categoryBean.getParentCategoryId());
        contentValues.put("Icon",categoryBean.getIcon());
        contentValues.put("IsLast",categoryBean.getIsLast());
        contentValues.put("Remark",categoryBean.getRemark());
        contentValues.put("ClientId",categoryBean.getClientId());
        contentValues.put("CreatedBy",categoryBean.getCreatedBy());
        contentValues.put("CreatedDate",categoryBean.getCreatedDate());
        contentValues.put("ChangedBy",categoryBean.getChangedBy());
        contentValues.put("ChangedDate",categoryBean.getChangedDate());
        contentValues.put("DeleteStatus",categoryBean.getDeleteStatus());
        db.insert("Category", null, contentValues);
        db.close();
    }




    public void delete(CategoryBean categoryBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("Category","CategoryId="+categoryBean.categoryId,null);
        db.close();
    }




    public void update(CategoryBean categoryBean) {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        //contentValues.put("CategoryId",categoryBean.getCategoryId());
        contentValues.put("CategoryCode",categoryBean.getCategoryCode());
        contentValues.put("CategoryName",categoryBean.getCategoryName());
        contentValues.put("ParentCategoryId",categoryBean.getParentCategoryId());
        contentValues.put("Icon",categoryBean.getIcon());
        contentValues.put("IsLast",categoryBean.getIsLast());
        contentValues.put("Remark",categoryBean.getRemark());
        contentValues.put("ClientId",categoryBean.getClientId());
        contentValues.put("CreatedBy",categoryBean.getCreatedBy());
        contentValues.put("CreatedDate",categoryBean.getCreatedDate());
        contentValues.put("ChangedBy",categoryBean.getChangedBy());
        contentValues.put("ChangedDate",categoryBean.getChangedDate());
        contentValues.put("DeleteStatus",categoryBean.getDeleteStatus());
        db.update("Category",contentValues,"CategoryId="+categoryBean.getCategoryId(),null);
        db.close();
    }

    public void delete() {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("Category",null,null);
        db.close();
    }


    public ArrayList<CategoryBean> getLastCategories(int clientId, int j)
    {
        ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        Cursor c=   db.rawQuery("SELECT * FROM Category where isLast= 'true' and DeleteStatus='false'", null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    CategoryBean bean = new CategoryBean();
                    try {
                        bean.categoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.categoryCode = c.getString(c.getColumnIndex("CategoryCode"));
                    bean.categoryName = c.getString(c.getColumnIndex("CategoryName"));
                    bean.icon = c.getString(c.getColumnIndex("Icon"));
                    bean.isLast = c.getString(c.getColumnIndex("IsLast"));
                    try {
                        bean.parentCategoryId = c.getInt(c.getColumnIndex("ParentCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    bean.deleteStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    bean.remark=c.getString(c.getColumnIndex("Remark"));
                    try {
                        bean.clientId=c.getLong(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                     list.add(bean);
                    c.moveToNext();
                     i++;
                    //return bean;
                }
            }
        }
        db.close();
        return list;
    }
    public ArrayList<CategoryBean> getCategoriesParent(int ParentCategoryId)
    {
        ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        Cursor c=   db.rawQuery("SELECT * FROM Category where isLast= 'true' and DeleteStatus='false' and ParentCategoryId="+ParentCategoryId, null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    CategoryBean bean = new CategoryBean();
                    try {
                        bean.categoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.categoryCode = c.getString(c.getColumnIndex("CategoryCode"));
                    bean.categoryName = c.getString(c.getColumnIndex("CategoryName"));
                    bean.icon = c.getString(c.getColumnIndex("Icon"));
                    bean.isLast = c.getString(c.getColumnIndex("IsLast"));
                    try {
                        bean.parentCategoryId = c.getInt(c.getColumnIndex("ParentCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    bean.deleteStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    bean.remark=c.getString(c.getColumnIndex("Remark"));
                    try {
                        bean.clientId=c.getLong(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    list.add(bean);
                    c.moveToNext();
                    i++;
                    //return bean;
                }
            }
        }
        db.close();
        return list;
    }
    public HashMap<Integer,CategoryBean> getAllCategoriesForMapping()
    {
        HashMap<Integer,CategoryBean> maps=new HashMap<>();

        ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        Cursor c=   db.rawQuery("SELECT * FROM Category ", null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    CategoryBean bean = new CategoryBean();
                    try {
                        bean.categoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.categoryCode = c.getString(c.getColumnIndex("CategoryCode"));
                    bean.categoryName = c.getString(c.getColumnIndex("CategoryName"));
                    bean.icon = c.getString(c.getColumnIndex("Icon"));
                    bean.isLast = c.getString(c.getColumnIndex("IsLast"));
                    try {
                        bean.parentCategoryId = c.getInt(c.getColumnIndex("ParentCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    bean.deleteStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    bean.remark=c.getString(c.getColumnIndex("Remark"));
                    try {
                        bean.clientId=c.getLong(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    maps.put(bean.categoryId,bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();

        return  maps;
    }

    public ArrayList<CategoryBean> getAllCategoryNotLast(Context context)
    {
        ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        Cursor c=   db.rawQuery("SELECT * FROM Category where  DeleteStatus='false' and IsLast!='true'", null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    CategoryBean bean = new CategoryBean();
                    try {
                        bean.categoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.categoryCode = c.getString(c.getColumnIndex("CategoryCode"));
                    bean.categoryName = c.getString(c.getColumnIndex("CategoryName"));
                    bean.icon = c.getString(c.getColumnIndex("Icon"));
                    bean.isLast = c.getString(c.getColumnIndex("IsLast"));
                    try {
                        bean.parentCategoryId = c.getInt(c.getColumnIndex("ParentCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    bean.deleteStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    bean.remark=c.getString(c.getColumnIndex("Remark"));
                    try {
                        bean.clientId=c.getLong(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return list;
    }
    //freeda
    public ArrayList<CategoryBean> getAllCategoryWhereParentIsZero(Context context)
    {
        ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        Cursor c=   db.rawQuery("SELECT * FROM Category where  DeleteStatus='false' and parentCategoryId=0", null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    CategoryBean bean = new CategoryBean();
                    try {
                        bean.categoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.categoryCode = c.getString(c.getColumnIndex("CategoryCode"));
                    bean.categoryName = c.getString(c.getColumnIndex("CategoryName"));
                    bean.icon = c.getString(c.getColumnIndex("Icon"));
                    bean.isLast = c.getString(c.getColumnIndex("IsLast"));
                    try {
                        bean.parentCategoryId = c.getInt(c.getColumnIndex("ParentCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    bean.deleteStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    bean.remark=c.getString(c.getColumnIndex("Remark"));
                    try {
                        bean.clientId=c.getLong(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return list;
    }

    public Collection<? extends CategoryBean> getAllCategoryWhereNotLast(Context context)
    {
        ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        Cursor c=   db.rawQuery("SELECT * FROM Category where  DeleteStatus='false' and IsLast!= 'true'", null);
        if(c!=null)
        {
            if(c.moveToFirst())
            {

                int i=0;
                while(i<c.getCount()) {
                    CategoryBean bean = new CategoryBean();
                    try {
                        bean.categoryId = c.getInt(c.getColumnIndex("CategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bean.categoryCode = c.getString(c.getColumnIndex("CategoryCode"));
                    bean.categoryName = c.getString(c.getColumnIndex("CategoryName"));
                    bean.icon = c.getString(c.getColumnIndex("Icon"));
                    bean.isLast = c.getString(c.getColumnIndex("IsLast"));
                    try {
                        bean.parentCategoryId = c.getInt(c.getColumnIndex("ParentCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    try
                    {
                        bean.createdDate=c.getLong(c.getColumnIndex("CreatedDate"));
                    }
                    catch(Exception e)
                    {

                    }
                    bean.deleteStatus=c.getString(c.getColumnIndex("DeleteStatus"));
                    bean.remark=c.getString(c.getColumnIndex("Remark"));
                    try {
                        bean.clientId=c.getLong(c.getColumnIndex("ClientId"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    list.add(bean);
                    c.moveToNext();
                    i++;
                }
            }
        }
        db.close();
        return list;
    }
}
