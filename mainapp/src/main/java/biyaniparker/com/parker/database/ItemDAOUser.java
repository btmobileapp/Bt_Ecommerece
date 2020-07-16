package biyaniparker.com.parker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ShopMaster;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.beans.UserShopBean;
import biyaniparker.com.parker.utilities.UserUtilities;

/**
 * Created by bt on 08/22/2016.
 */
public class ItemDAOUser
{
    Context context;

    public ItemDAOUser(Context context)
    {
        this.context=context;
    }

      private ContentValues shopMasterToContentValues(ShopMaster shopdetails)
      {
        ContentValues values=new ContentValues();
        values.put("ShopId",shopdetails.getShopId());
        values.put("ShopName",shopdetails.getShopName());
        values.put("Address",shopdetails.getAddress());
        values.put("CreditLimit",shopdetails.getCreditLimit());
        values.put("ChangedBy",shopdetails.getChangedBy());
        values.put("ChangedDate",shopdetails.getChangedDate());
        values.put("CreatedBy", shopdetails.getCreatedBy());
        values.put("ChangedDate", shopdetails.getChangedDate());
        values.put("DeleteStatus", shopdetails.getDeleteStatus());
        return values;
    }

    public void deleteUserBean(UserBean userBean)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("User", "UserId=" + userBean.getUserId(), null);
        db.close();
    }

    public void deleteUser()
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();

        db.delete("User", null, null);
        db.close();
    }
    public void deleteShop()
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();

        db.delete("ShopMaster", null, null);
        db.close();
    }


    public void deleteShopMaster(ShopMaster shopMaster)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.delete("ShopMaster", "ShopId=" + shopMaster.getShopId(), null);
        db.close();
    }

    public ContentValues convertToContentValues(UserBean userBean)
    {

        ContentValues values=new ContentValues();
        values.put("UserId",userBean.getUserId());
        values.put("ShopId",userBean.getShopId());
        values.put("CreatedBy",userBean.getCreatedBy());
        values.put("AccessLavel",userBean.getAccessLavel());
        values.put("RoleName",userBean.getRoleName());
        values.put("UserType",userBean.getUserType());
        values.put("UserName",userBean.getUserName());
        values.put("Password",userBean.getPassword());
        values.put("OldPassword",userBean.getOldPassword());

        values.put("Name",userBean.getName());

        values.put("ContactNo",userBean.getContactNo());

        values.put("MobileNo",userBean.getMobileNo());
        values.put("IsActive",userBean.getIsActive());
        values.put("ClientId",userBean.getClientId());
        values.put("EmailId",userBean.getEmailId());
        values.put("OrgnisationIds",userBean.getOrgnisationIds());
        values.put("DeleteStatus",userBean.getDeleteStatus());
        values.put("ExpiryDate",userBean.getExpiryDate());
        values.put("DepartmentIds",userBean.getDepartmentIds());
        values.put("CanCreateUser",userBean.getCanCreateUser());
        values.put("CanAssignTask",userBean.getCanAssignTask());
        values.put("GCMID",userBean.getgCMID());
        values.put("DeviceID",userBean.getDeviceID());
        values.put("EnterBy",userBean.getEnterBy());
        values.put("EnterDate",userBean.getEnterDate());
        values.put("ChangedBy",userBean.getChangedBy());
        values.put("ChangedDate",userBean.getChangedDate());
        return values;
    }


    public ArrayList<UserShopBean> getAllUsers(int clientId) {
        ArrayList<UserShopBean> allUsersList=new ArrayList<UserShopBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery("Select * from User where ClientId="+clientId+" and DeleteStatus='false'",null);

        if(c!=null)
        {
            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount())
                {
                    UserBean userDetails=new UserBean();
                    ShopMaster shopMaster=new ShopMaster();

                    UserShopBean userBean=new UserShopBean();
                    try {
                        userDetails.setUserId(c.getInt(c.getColumnIndex("UserId")));
                    }catch ( Exception e){}
                    try {
                        userDetails.setShopId(c.getLong(c.getColumnIndex("ShopId")));
                    }catch ( Exception e){}

                    try {
                        Cursor query=db.rawQuery("Select * from ShopMaster where ShopId="+userDetails.getShopId(),null);
                        if(query!=null)
                        {


                            query.moveToFirst();
                            if (query.getCount() > 0)
                            {
                                try {
                                    shopMaster.setShopName(query.getString(query.getColumnIndex("ShopName")));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                shopMaster.setShopId(query.getLong(query.getColumnIndex("ShopId")));
                                shopMaster.setAddress(query.getString(query.getColumnIndex("Address")));
                                shopMaster.setCreditLimit(query.getDouble(query.getColumnIndex("CreditLimit")));
                                shopMaster.setDeleteStatus(query.getString(query.getColumnIndex("DeleteStatus")));
                                shopMaster.setChangedBy(query.getLong(query.getColumnIndex("ChangedBy")));
                                shopMaster.setChangedDate(query.getLong(query.getColumnIndex("ChangedDate")));
                                shopMaster.setCreatedBy(query.getLong(query.getColumnIndex("CreatedBy")));
                                shopMaster.setCreatedDate(query.getLong(query.getColumnIndex("CreatedDate")));
                            }
                        }
                        query.close();

                    }catch (Exception e)
                    {

                    }

                    userDetails.setCreatedBy(c.getInt(c.getColumnIndex("CreatedBy")));
                    userDetails.setAccessLavel(c.getInt(c.getColumnIndex("AccessLavel")));
                    userDetails.setRoleName(c.getString(c.getColumnIndex("RoleName")));
                    userDetails.setUserType(c.getString(c.getColumnIndex("UserType")));
                    userDetails.setUserName(c.getString(c.getColumnIndex("UserName")));
                    userDetails.setPassword(c.getString(c.getColumnIndex("Password")));
                    userDetails.setOldPassword(c.getString(c.getColumnIndex("OldPassword")));

                    userDetails.setName(c.getString(c.getColumnIndex("Name")));
                    userDetails.setEmailId(c.getString(c.getColumnIndex("EmailId")));
                    userDetails.setContactNo(c.getString(c.getColumnIndex("ContactNo")));
                    userDetails.setMobileNo(c.getString(c.getColumnIndex("MobileNo")));
                    userDetails.setIsActive(c.getString(c.getColumnIndex("IsActive")));
                    userDetails.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    userDetails.setOrgnisationIds(c.getString(c.getColumnIndex("OrgnisationIds")));
                    userDetails.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    userDetails.setExpiryDate(c.getLong(c.getColumnIndex("ExpiryDate")));
                    userDetails.setDepartmentIds(c.getString(c.getColumnIndex("DepartmentIds")));
                    userDetails.setCanCreateUser(c.getString(c.getColumnIndex("CanCreateUser")));
                    userDetails.setCanAssignTask(c.getString(c.getColumnIndex("CanAssignTask")));
                    userDetails.setgCMID(c.getString(c.getColumnIndex("GCMID")));
                    userDetails.setDeviceID(c.getString(c.getColumnIndex("DeviceID")));
                    userDetails.setEnterBy(c.getInt(c.getColumnIndex("EnterBy")));
                    userDetails.setEnterDate(c.getLong(c.getColumnIndex("EnterDate")));
                    userDetails.setChangedBy(c.getInt(c.getColumnIndex("ChangedBy")));
                    userDetails.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    c.moveToNext();
                    i++;
                    userBean.user=userDetails;
                    userBean.shopdetails=shopMaster;
                    allUsersList.add(userBean);

                }
            }
        }
        db.close();
        return allUsersList;

    }

    public UserShopBean getUserByUserId(int userId) {


        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        UserShopBean userBean=new UserShopBean();
        Cursor c=db.rawQuery("Select * from User where UserId="+userId+" and DeleteStatus='false'",null);

        if(c!=null) {
            if (c.moveToFirst()) {
                int i = 0;
                while (i < c.getCount()) {
                    UserBean userDetails = new UserBean();
                    ShopMaster shopMaster = new ShopMaster();

                    //UserShopBean userBean = new UserShopBean();
                    try {
                        userDetails.setUserId(c.getInt(c.getColumnIndex("UserId")));
                    } catch (Exception e) {
                    }
                    try {
                        userDetails.setShopId(c.getLong(c.getColumnIndex("ShopId")));
                    } catch (Exception e) {
                    }

                    try {
                        Cursor query = db.rawQuery("Select * from ShopMaster where ShopId=" + userDetails.getShopId(), null);
                        if (query != null) {


                            query.moveToFirst();
                            if (query.getCount() > 0) {
                                try {
                                    shopMaster.setShopName(query.getString(query.getColumnIndex("ShopName")));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                shopMaster.setShopId(query.getLong(query.getColumnIndex("ShopId")));
                                shopMaster.setAddress(query.getString(query.getColumnIndex("Address")));
                                shopMaster.setCreditLimit(query.getDouble(query.getColumnIndex("CreditLimit")));
                                shopMaster.setDeleteStatus(query.getString(query.getColumnIndex("DeleteStatus")));
                                shopMaster.setChangedBy(query.getLong(query.getColumnIndex("ChangedBy")));
                                shopMaster.setChangedDate(query.getLong(query.getColumnIndex("ChangedDate")));
                                shopMaster.setCreatedBy(query.getLong(query.getColumnIndex("CreatedBy")));
                                shopMaster.setCreatedDate(query.getLong(query.getColumnIndex("CreatedDate")));
                            }
                        }
                        query.close();

                    } catch (Exception e) {

                    }

                    userDetails.setCreatedBy(c.getInt(c.getColumnIndex("CreatedBy")));
                    userDetails.setAccessLavel(c.getInt(c.getColumnIndex("AccessLavel")));
                    userDetails.setRoleName(c.getString(c.getColumnIndex("RoleName")));
                    userDetails.setUserType(c.getString(c.getColumnIndex("UserType")));
                    userDetails.setUserName(c.getString(c.getColumnIndex("UserName")));
                    userDetails.setPassword(c.getString(c.getColumnIndex("Password")));
                    userDetails.setOldPassword(c.getString(c.getColumnIndex("OldPassword")));

                    userDetails.setName(c.getString(c.getColumnIndex("Name")));
                    userDetails.setEmailId(c.getString(c.getColumnIndex("EmailId")));
                    userDetails.setContactNo(c.getString(c.getColumnIndex("ContactNo")));
                    userDetails.setMobileNo(c.getString(c.getColumnIndex("MobileNo")));
                    userDetails.setIsActive(c.getString(c.getColumnIndex("IsActive")));
                    userDetails.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    userDetails.setOrgnisationIds(c.getString(c.getColumnIndex("OrgnisationIds")));
                    userDetails.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    userDetails.setExpiryDate(c.getLong(c.getColumnIndex("ExpiryDate")));
                    userDetails.setDepartmentIds(c.getString(c.getColumnIndex("DepartmentIds")));
                    userDetails.setCanCreateUser(c.getString(c.getColumnIndex("CanCreateUser")));
                    userDetails.setCanAssignTask(c.getString(c.getColumnIndex("CanAssignTask")));
                    userDetails.setgCMID(c.getString(c.getColumnIndex("GCMID")));
                    userDetails.setDeviceID(c.getString(c.getColumnIndex("DeviceID")));
                    userDetails.setEnterBy(c.getInt(c.getColumnIndex("EnterBy")));
                    userDetails.setEnterDate(c.getLong(c.getColumnIndex("EnterDate")));
                    userDetails.setChangedBy(c.getInt(c.getColumnIndex("ChangedBy")));
                    userDetails.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    c.moveToNext();
                    i++;
                    userBean.user=userDetails;
                    userBean.shopdetails=shopMaster;
                }
            }
        }
        db.close();
        return userBean;
    }

    public ShopMaster getShopDetailsById(long shopID) {
        ShopMaster bean=new ShopMaster();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();

        Cursor c=db.rawQuery("Select * from ShopMaster where ShopId="+shopID,null);
        if(c!=null)
        {
            bean.setShopId(c.getLong(c.getColumnIndex("ShopId")));
            bean.setShopName(c.getString(c.getColumnIndex("ShopName")));
            bean.setAddress(c.getString(c.getColumnIndex("Address")));
            bean.setCreditLimit(c.getDouble(c.getColumnIndex("CreditLimit")));
            bean.setChangedBy(c.getInt(c.getColumnIndex("ChangedBy")));
            bean.setCreatedBy(c.getInt(c.getColumnIndex("CreatedBy")));
            bean.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
            bean.setCreatedDate(c.getLong(c.getColumnIndex("CreatedDate")));
            bean.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
        }
        return bean;
    }

    public long getLatestChangedUser()
    {
        try
        {
            ShopMaster bean = new ShopMaster();
            SQLiteDatabase db = new DBHELPER(context).getReadableDatabase();
            Cursor c = db.rawQuery("Select * from User where", null);
            if (c != null)
            {
                return c.getLong(c.getColumnIndex("ChangedDate"));
            }
        }catch (Exception e){}
        return 0;
    }

    public long getLatestChangedShop()
    {
        try
        {
            ShopMaster bean = new ShopMaster();
            SQLiteDatabase db = new DBHELPER(context).getReadableDatabase();
            Cursor c = db.rawQuery("Select * from ShopMaster where", null);
            if (c != null)
            {
                return c.getLong(c.getColumnIndex("ChangedDate"));
            }
        }catch (Exception e){}
        return 0;
    }




    public void insertShopMaster(ShopMaster shop)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        deleteShopMaster(shop);
        long length=db.insert("ShopMaster", null, shopMasterToContentValues(shop));
        db.close();
    }

    public void insertUserBean(UserBean userBean)
    {
        deleteUserBean(userBean);
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.insert("User", null, convertToContentValues(userBean));
        db.close();

    }

    public void updateShopMaster(ShopMaster shopMaster)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.update("ShopMaster", shopMasterToContentValues(shopMaster), "ShopId=" + shopMaster.getShopId(), null);
        db.close();
    }

    public void updateUserBean(UserBean user)
    {
        SQLiteDatabase db=new DBHELPER(context).getWritableDatabase();
        db.update("User", convertToContentValues(user), "UserId=" + user.getUserId(), null);
        db.close();
    }





    //  return user list by name

    public ArrayList<UserShopBean> getUsersByName(String name)
    {
        int clientId=UserUtilities.getClientId(context);
        ArrayList<UserShopBean> allUsersList=new ArrayList<UserShopBean>();
        SQLiteDatabase db=new DBHELPER(context).getReadableDatabase();
        Cursor c=db.rawQuery("Select * from User where ClientId="+clientId+" and DeleteStatus='false' and Name like '%"+name+"%'",null);

        if(c!=null)
        {
            if(c.moveToFirst())
            {
                int i=0;
                while(i<c.getCount())
                {
                    UserBean userDetails=new UserBean();
                    ShopMaster shopMaster=new ShopMaster();

                    UserShopBean userBean=new UserShopBean();
                    try {
                        userDetails.setUserId(c.getInt(c.getColumnIndex("UserId")));
                    }catch ( Exception e){}
                    try {
                        userDetails.setShopId(c.getLong(c.getColumnIndex("ShopId")));
                    }catch ( Exception e){}

                    try {
                        Cursor query=db.rawQuery("Select * from ShopMaster where ShopId="+userDetails.getShopId(),null);
                        if(query!=null)
                        {


                            query.moveToFirst();
                            if (query.getCount() > 0)
                            {
                                try {
                                    shopMaster.setShopName(query.getString(query.getColumnIndex("ShopName")));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                shopMaster.setShopId(query.getLong(query.getColumnIndex("ShopId")));
                                shopMaster.setAddress(query.getString(query.getColumnIndex("Address")));
                                shopMaster.setCreditLimit(query.getDouble(query.getColumnIndex("CreditLimit")));
                                shopMaster.setDeleteStatus(query.getString(query.getColumnIndex("DeleteStatus")));
                                shopMaster.setChangedBy(query.getLong(query.getColumnIndex("ChangedBy")));
                                shopMaster.setChangedDate(query.getLong(query.getColumnIndex("ChangedDate")));
                                shopMaster.setCreatedBy(query.getLong(query.getColumnIndex("CreatedBy")));
                                shopMaster.setCreatedDate(query.getLong(query.getColumnIndex("CreatedDate")));
                            }
                        }
                        query.close();

                    }catch (Exception e)
                    {

                    }

                    userDetails.setCreatedBy(c.getInt(c.getColumnIndex("CreatedBy")));
                    userDetails.setAccessLavel(c.getInt(c.getColumnIndex("AccessLavel")));
                    userDetails.setRoleName(c.getString(c.getColumnIndex("RoleName")));
                    userDetails.setUserType(c.getString(c.getColumnIndex("UserType")));
                    userDetails.setUserName(c.getString(c.getColumnIndex("UserName")));
                    userDetails.setPassword(c.getString(c.getColumnIndex("Password")));
                    userDetails.setOldPassword(c.getString(c.getColumnIndex("OldPassword")));

                    userDetails.setName(c.getString(c.getColumnIndex("Name")));
                    userDetails.setEmailId(c.getString(c.getColumnIndex("EmailId")));
                    userDetails.setContactNo(c.getString(c.getColumnIndex("ContactNo")));
                    userDetails.setMobileNo(c.getString(c.getColumnIndex("MobileNo")));
                    userDetails.setIsActive(c.getString(c.getColumnIndex("IsActive")));
                    userDetails.setClientId(c.getInt(c.getColumnIndex("ClientId")));
                    userDetails.setOrgnisationIds(c.getString(c.getColumnIndex("OrgnisationIds")));
                    userDetails.setDeleteStatus(c.getString(c.getColumnIndex("DeleteStatus")));
                    userDetails.setExpiryDate(c.getLong(c.getColumnIndex("ExpiryDate")));
                    userDetails.setDepartmentIds(c.getString(c.getColumnIndex("DepartmentIds")));
                    userDetails.setCanCreateUser(c.getString(c.getColumnIndex("CanCreateUser")));
                    userDetails.setCanAssignTask(c.getString(c.getColumnIndex("CanAssignTask")));
                    userDetails.setgCMID(c.getString(c.getColumnIndex("GCMID")));
                    userDetails.setDeviceID(c.getString(c.getColumnIndex("DeviceID")));
                    userDetails.setEnterBy(c.getInt(c.getColumnIndex("EnterBy")));
                    userDetails.setEnterDate(c.getLong(c.getColumnIndex("EnterDate")));
                    userDetails.setChangedBy(c.getInt(c.getColumnIndex("ChangedBy")));
                    userDetails.setChangedDate(c.getLong(c.getColumnIndex("ChangedDate")));
                    c.moveToNext();
                    i++;
                    userBean.user=userDetails;
                    userBean.shopdetails=shopMaster;
                    allUsersList.add(userBean);

                }
            }
        }
        db.close();
        return allUsersList;
    }
}
