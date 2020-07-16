package biyaniparker.com.parker.bal;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import biyaniparker.com.parker.beans.ShopMaster;
import biyaniparker.com.parker.beans.UserBean;
import biyaniparker.com.parker.beans.UserShopBean;
import biyaniparker.com.parker.database.ItemDAOUser;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt on 08/22/2016.
 */
public class ModuleUser implements  DownloadUtility{
    Context context;
   public ArrayList<UserShopBean> userList=new ArrayList<UserShopBean>();
    int flag=0;    // is for cheking wether recieved response is proper or  not
    public ModuleUser(Context context)
    {
        this.context=context;
    }



    //--------------------------------------------  web services call ---------------------------------------------------------

    // insert new user by calling web service
    // request code 2 for create user


    public void syncUser()
    {
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"UtilService.svc/getAllUsers?ClientId="+1,"",1,this);
        serverAsync.execute();
    }

    public void insertUser(UserBean userBean, ShopMaster shopMasterbean) throws JSONException
    {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("ShopName",shopMasterbean.getShopName());
        jsonObject.put("Name",userBean.getName());
        jsonObject.put("ContactNo",userBean.getContactNo());
        jsonObject.put("Address",shopMasterbean.getAddress());
        jsonObject.put("CreditLimit",shopMasterbean.getCreditLimit());
        jsonObject.put("Password",userBean.getPassword());
        jsonObject.put("CreatedBy",userBean.getCreatedBy());
        jsonObject.put("EnterBy",userBean.getEnterBy());
        jsonObject.put("EnterDate",userBean.getEnterDate());
        jsonObject.put("EmailId", userBean.getEmailId());
        jsonObject.put("RoleName",userBean.getRoleName());
        jsonObject.put("UserType",userBean.getUserType());
        jsonObject.put("CreatedBy",userBean.getCreatedBy());
        jsonObject.put("CreatedDate",shopMasterbean.getCreatedDate());
        jsonObject.put("DeleteStatus",userBean.getDeleteStatus());
        jsonObject.put("ClientId",userBean.getClientId());
        jsonObject.put("UserName",userBean.getUserName());
        jsonObject.put("IsActive",userBean.getIsActive());
        jsonObject.put("MobileNo",userBean.getMobileNo());

        AsyncUtilities asyncUtilities=new AsyncUtilities(context,true, CommonUtilities.URL+"UtilService.svc/CreateUser",jsonObject.toString(),2,this);
        asyncUtilities.execute();

    }

    // update user by calling web service

    public void updateUser(UserBean userBean, ShopMaster shopMasterbean) throws JSONException
    {

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("ShopName",shopMasterbean.getShopName());
        jsonObject.put("ShopId",userBean.getShopId());
        jsonObject.put("Name",userBean.getName());
        jsonObject.put("ContactNo",userBean.getContactNo());
        jsonObject.put("Address",shopMasterbean.getAddress());
        jsonObject.put("CreditLimit",shopMasterbean.getCreditLimit());
        jsonObject.put("Password",userBean.getPassword());
        jsonObject.put("ChangedBy",userBean.getEnterBy());
        jsonObject.put("ChangedDate",userBean.getEnterDate());
        jsonObject.put("EmailId", userBean.getEmailId());
        jsonObject.put("RoleName",userBean.getRoleName());
        jsonObject.put("UserType",userBean.getUserType());
        jsonObject.put("DeleteStatus",userBean.getDeleteStatus());
        jsonObject.put("ClientId",userBean.getClientId());
        jsonObject.put("UserName",userBean.getUserName());
        jsonObject.put("IsActive",userBean.getIsActive());
        jsonObject.put("MobileNo",userBean.getMobileNo());
        jsonObject.put("UserId",userBean.getUserId());
        jsonObject.put("CreatedBy",userBean.getCreatedBy());

        AsyncUtilities asyncUtilities=new AsyncUtilities(context,true, CommonUtilities.URL+"UtilService.svc/UpdateUser",jsonObject.toString(),3,this);
        asyncUtilities.execute();

    }


    public void updateUserPassword(UserBean bean) throws JSONException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("UserId",bean.getUserId());
        jsonObject.put("OldPassword",bean.getOldPassword());
        jsonObject.put("NewPassword", bean.getPassword());

        AsyncUtilities asyncUtilities=new AsyncUtilities(context,true, CommonUtilities.URL+"UtilService.svc/UpdateUserPassword",jsonObject.toString(),4,this);
        asyncUtilities.execute();
    }


    // ------------------------------------------------------ local database and parsing activities ------------------------------------------------


    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
            // request code 1 for getting all users
            while(1==1)
            {
                break;
            }
        if(requestCode==1)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseUsers(str)) {

                    downloadUtility.onComplete("Success", 1, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 1, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 1, responseCode);
            }
        }


        //   COde=2 is for Create Usre response

        if(requestCode==2)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                try
                {
                    if (parseInsertedUser(str))
                    {
                        if(flag==1)   // user not created due to query execution failed
                        {
                            downloadUtility.onComplete("failed ", 20, responseCode);
                            flag=0;
                        }
                        else
                        {
                            downloadUtility.onComplete("Success", 2, responseCode);
                        }
                    }
                    else
                    {
                        downloadUtility.onComplete("Failed", 2, responseCode);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                downloadUtility.onComplete("Server Communication Failed", 2, responseCode);
            }
        }

        // code 3 for update user response
        if(requestCode==3)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                try {
                    JSONArray jsonArray = new JSONArray(str);

                    if (parseUpdatedUser(jsonArray.getJSONObject(0).toString()))
                    {
                        if(flag==1)  // user not created due to query execution failed
                        {
                            downloadUtility.onComplete("failed ", 30, responseCode);
                            flag=0;
                        }
                        else
                        {
                            downloadUtility.onComplete("Record Saved", 3, responseCode);
                        }

                    } else {
                        downloadUtility.onComplete("Failed Save", 3, responseCode);
                    }
                }
                catch (Exception e)
                {
                    downloadUtility.onComplete("Failed Save", 3, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 3, responseCode);
            }
        }


        // code 4 for update user password response

        if(requestCode==4)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                try
                {
                   // JSONArray jsonArray=new JSONArray(str);
                    if (parseUserData(str))
                    {
                        downloadUtility.onComplete("Record Saved", 4, responseCode);
                    }
                    else
                    {
                        downloadUtility.onComplete("Failed Save", 4, responseCode);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                downloadUtility.onComplete("Server Communication Failed", 4, responseCode);
            }
        }
    }



    // ------------------------------------------------------Parsing and reflecting to local database ---------------------------------------------------------------



    private boolean parseUpdatedUser(String str) throws JSONException
    {
        ItemDAOUser itemDAOUser=new ItemDAOUser(context);
        UserBean user=new UserBean();
        JSONObject jsonObject=new JSONObject(str);
        JSONObject shopDetails=jsonObject.getJSONObject("ShopResult");
        JSONObject userDetails=jsonObject.getJSONObject("UserResult");
        itemDAOUser.updateShopMaster(parseShopMaster(shopDetails.toString()));
        user=parseUserBean(userDetails.toString());
        if(user.getUserId()==0)
        {
            flag=1;
        }
        else
        {
            itemDAOUser.updateUserBean(user);
        }
        return true;
    }





    //   parse list of shopmaster and user json string

    private boolean parseUsers(String str)
    {
        ItemDAOUser itemDAOUser=new ItemDAOUser(context);
        try {

            JSONObject jsonObject = new JSONObject(str);
            JSONArray shopArray = jsonObject.getJSONArray("ShopResult");
            JSONArray userArray = jsonObject.getJSONArray("UserResult");

            for (int i = 0; i < shopArray.length(); i++)
            {
                JSONObject shopJson=shopArray.getJSONObject(i);
                itemDAOUser.insertShopMaster(parseShopMaster(shopJson.toString()));
            }


            for(int j=0;j<userArray.length();j++)
            {
                JSONObject userJson=userArray.getJSONObject(j);
                itemDAOUser.insertUserBean(parseUserBean(userJson.toString()));
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }



//parse user and shopmaster details and saving to local database

    private boolean parseInsertedUser(String str) throws JSONException {
        ItemDAOUser itemDAOUser=new ItemDAOUser(context);
        UserBean user=new UserBean();
        JSONObject jsonObject=new JSONObject(str);
        JSONObject shopDetails=jsonObject.getJSONObject("ShopResult");
        JSONObject userDetails=jsonObject.getJSONObject("UserResult");
        itemDAOUser.insertShopMaster(parseShopMaster(shopDetails.toString()));
        user=parseUserBean(userDetails.toString());
        if(user.getUserId()==0)
        {
            flag=1;
        }
        else
        {
            itemDAOUser.insertUserBean(user);
        }
        return true;
    }



    // parse user json string and convert to User Bean

    public UserBean parseUserBean(String str) throws JSONException {
        JSONObject jsonObject=new JSONObject(str);
        UserBean userBean = new UserBean();
        userBean.setUserId(jsonObject.getInt("UserId"));
        userBean.setShopId(jsonObject.getLong("ShopId"));
        userBean.setCreatedBy(jsonObject.getInt("CreatedBy"));
        try {
            userBean.setAccessLavel(jsonObject.getInt("AccessLavel"));
        } catch (Exception e) {
        }
        userBean.setRoleName(jsonObject.getString("RoleName"));
        userBean.setUserType(jsonObject.getString("UserType"));
        userBean.setUserName(jsonObject.getString("UserName"));
        userBean.setPassword(jsonObject.getString("Password"));
        userBean.setOldPassword(jsonObject.getString("OldPassword"));

        userBean.setName(jsonObject.getString("Name"));
        userBean.setEmailId(jsonObject.getString("EmailId"));

        userBean.setContactNo(jsonObject.getString("ContactNo"));

        userBean.setMobileNo(jsonObject.getString("MobileNo"));
        userBean.setIsActive(jsonObject.getString("IsActive"));
        userBean.setClientId(jsonObject.getInt("ClientId"));
        userBean.setOrgnisationIds(jsonObject.getString("OrgnisationIds"));
        userBean.setDeleteStatus(jsonObject.getString("DeleteStatus"));
        try{userBean.setExpiryDate(jsonObject.getLong("ExpiryDate"));}catch (Exception e){}
        userBean.setDepartmentIds(jsonObject.getString("DepartmentIds"));
        userBean.setCanCreateUser(jsonObject.getString("CanCreateUser"));
        userBean.setCanAssignTask(jsonObject.getString("CanAssignTask"));
        userBean.setgCMID(jsonObject.getString("GCMID"));
        userBean.setDeviceID(jsonObject.getString("DeviceID"));
        try {
            userBean.setEnterBy(jsonObject.getInt("EnterBy"));
        }
        catch (Exception e)        {        }
        try {
            userBean.setChangedBy(jsonObject.getInt("ChangedBy"));
        }catch (Exception e){}


        try {
            userBean.setEnterDate(jsonObject.getLong("EnterDate"));
        }
        catch (Exception e){}

        try {
            userBean.setChangedDate(jsonObject.getLong("ChangedDate"));
        }
        catch (Exception e){}
        return userBean;
    }

// parse shopmaster json string and return shopmaster

    public ShopMaster parseShopMaster(String str) throws JSONException {
        ShopMaster shopMaster=new ShopMaster();
        JSONObject jsonShopDetails=new JSONObject(str);
        shopMaster.setShopId(jsonShopDetails.getLong("ShopId"));
        shopMaster.setShopName(jsonShopDetails.getString("ShopName"));
        try {
            shopMaster.setCreditLimit(jsonShopDetails.getDouble("CreditLimit"));
        }catch (Exception e){}
        shopMaster.setAddress(jsonShopDetails.getString("Address"));
        try {
            shopMaster.setCreatedBy(jsonShopDetails.getLong("CreatedBy"));
        }
        catch (Exception e)        {        }
        try {
            shopMaster.setChangedBy(jsonShopDetails.getInt("ChangedBy"));
        }catch (Exception e){}

        shopMaster.setDeleteStatus(jsonShopDetails.getString("DeleteStatus"));
        try {
            shopMaster.setCreatedDate(jsonShopDetails.getLong("CreatedDate"));
        }
        catch (Exception e){}

        try {
            shopMaster.setChangedDate(jsonShopDetails.getLong("ChangedDate"));
        }
        catch (Exception e){}
        return  shopMaster;
    }


//   return userbean and by user id

    public UserShopBean getUser(int userId)
    {
        ItemDAOUser itemDAOUser=new ItemDAOUser(context);
        return itemDAOUser.getUserByUserId(userId);
    }

// return shopmaster and return shopdetails

    public ShopMaster getShopDetailsByUserId(long shopID) {
        ItemDAOUser itemDAOUser=new ItemDAOUser(context);
        return itemDAOUser.getShopDetailsById(shopID);

    }


// return user list

    public void getUserList()
    {
        ItemDAOUser itemDAOUser=new ItemDAOUser(context);
        userList.clear();
        try
        {
          ArrayList<UserShopBean> list=itemDAOUser.getAllUsers(UserUtilities.getClientId(context));
            if(list!=null && list.size()!=0)
            userList.addAll(list);
        }
        catch (Exception e)
        {
                e.printStackTrace();
        }

    }



    private boolean parseUserData(String str) {

        try
        {
            JSONObject jsonObject=new JSONObject(str);
            if(jsonObject.getInt("UserId")!=0)
            {
                SharedPreferences sh=context.getSharedPreferences("UserBean",context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sh.edit();
                edit. putString("UserBean", str);
                edit.commit();
                return true;
            }


        }
        catch (Exception e)
        {

        }
        return false;
    }

    public void getUsersByCustomerName(String name)
    {
        ItemDAOUser itemDAOUser=new ItemDAOUser(context);
        userList.clear();
        userList.addAll(itemDAOUser.getUsersByName(name));
    }
}
