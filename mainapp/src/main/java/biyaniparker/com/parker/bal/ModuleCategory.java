package biyaniparker.com.parker.bal;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.database.ItemDAOCategory;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncFileUploadUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;
import biyaniparker.com.parker.utilities.serverutilities.FileUpload;

/**
 * Created by bt on 08/11/2016.
 */
public class ModuleCategory implements  DownloadUtility
{
    Context context;
    public ArrayList<CategoryBean> list=new ArrayList<CategoryBean>();
    public ArrayList<CategoryBean> lastCategoryList=new ArrayList<CategoryBean>();

    public ArrayList<CategoryBean> parentList=new ArrayList<>();
    public ArrayList<CategoryBean> notChildList=new ArrayList<>();

    public ModuleCategory(Context context)
    {
        this.context=context;
    }



    //-------------------------------------------------    Web api call   -------------------------------------------------------------------

    public void syncCategory()
    {
        // requset code ->   1
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"ProductService.svc/getAllCategory?ClientId="+1,"",1,this);
        serverAsync.execute();
    }

    // creates a new record of category and saves on server

    public void insertCategory(CategoryBean categoryBean) throws JSONException
    {

        // requset code ->   2
        JSONObject contentValues=new JSONObject();
        contentValues.put("CategoryId",categoryBean.getCategoryId());
        contentValues.put("CategoryCode", categoryBean.getCategoryCode());
        contentValues.put("CategoryName", categoryBean.getCategoryName());
        contentValues.put("ParentCategoryId", categoryBean.getParentCategoryId());
        contentValues.put("Icon", categoryBean.getIcon());
        contentValues.put("IsLast", categoryBean.getIsLast());
        contentValues.put("Remark", categoryBean.getRemark());
        contentValues.put("ClientId", categoryBean.getClientId());
        contentValues.put("CreatedBy", categoryBean.getCreatedBy());
        contentValues.put("CreatedDate",categoryBean.getCreatedDate());
        contentValues.put("ChangedBy", categoryBean.getChangedBy());
        contentValues.put("ChangedDate", categoryBean.getChangedDate());
        contentValues.put("DeleteStatus", categoryBean.getDeleteStatus());
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/InsertCategory",contentValues.toString(),2,this);
        serverAsync.execute();
    }


    // updates existing record of a category on server

    public void updateCategory(CategoryBean categoryBean) throws JSONException
    {

        // requset code ->   3
        JSONObject contentValues=new JSONObject();
        contentValues.put("CategoryId",categoryBean.getCategoryId());
        contentValues.put("CategoryCode",categoryBean.getCategoryCode());
        contentValues.put("CategoryName", categoryBean.getCategoryName());
        contentValues.put("ParentCategoryId", categoryBean.getParentCategoryId());
        contentValues.put("Icon", categoryBean.getIcon());
        contentValues.put("IsLast", categoryBean.getIsLast());
        contentValues.put("Remark", categoryBean.getRemark());
        contentValues.put("ClientId", categoryBean.getClientId());
        contentValues.put("CreatedBy", categoryBean.getCreatedBy());
        contentValues.put("CreatedDate", categoryBean.getCreatedDate());
        contentValues.put("ChangedBy", categoryBean.getChangedBy());
        contentValues.put("ChangedDate", categoryBean.getChangedDate());
        contentValues.put("DeleteStatus", categoryBean.getDeleteStatus());
        String val=contentValues.toString();
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/UpdateCategory",contentValues.toString(),3,this);
        serverAsync.execute();
    }



    public void deleteCategory(CategoryBean categoryBean) throws JSONException
    {

        // requset code ->   4
        JSONObject contentValues=new JSONObject();
        contentValues.put("CategoryId",categoryBean.getCategoryId());
        contentValues.put("CategoryCode",categoryBean.getCategoryCode());
        contentValues.put("CategoryName", categoryBean.getCategoryName());
        contentValues.put("ParentCategoryId", categoryBean.getParentCategoryId());
        contentValues.put("Icon", categoryBean.getIcon());
        contentValues.put("IsLast", categoryBean.getIsLast());
        contentValues.put("Remark", categoryBean.getRemark());
        contentValues.put("ClientId", categoryBean.getClientId());
        contentValues.put("CreatedBy", categoryBean.getCreatedBy());
        contentValues.put("CreatedDate", categoryBean.getCreatedDate());
        contentValues.put("ChangedBy", categoryBean.getChangedBy());
        contentValues.put("ChangedDate", categoryBean.getChangedDate());
        contentValues.put("DeleteStatus", categoryBean.getDeleteStatus());
        String val=contentValues.toString();
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/DeleteCategory",contentValues.toString(),4,this);
        serverAsync.execute();
    }

    //upload image file on server
    public void loadFile(File bitmapFile)
    {
        AsyncFileUploadUtilities asyncFileUploadUtilities=new AsyncFileUploadUtilities(context,10,
                CommonUtilities.URL+"ProductService.svc/UploadFile",this,bitmapFile);

        asyncFileUploadUtilities.execute();
    }




    //-----------------------------------------------  Response after calling web api ------------------------------------------------


    @Override
    public void onComplete(String str, int requestCode,int responseCode)
    {

        if(requestCode==1)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseCategories(str)) {

                    downloadUtility.onComplete("Success", 1, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 1, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 1, responseCode);
            }
        }

        //   COde=2 is for Create Category response

        if(requestCode==2)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseInsertedCategory(str)) {

                    downloadUtility.onComplete("Success", 2, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 2, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 2, responseCode);
            }
        }



        // code 3 for update category response
        if(requestCode==3)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseUpdatedCategory(str)) {

                    downloadUtility.onComplete("Record Saved", 3, responseCode);
                } else {
                    downloadUtility.onComplete("Failed Save", 3, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 3, responseCode);
            }
        }


        // code 4 for deleted category response
        if(requestCode==4)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {

                if(str.equals("0"))
                {
                    downloadUtility.onComplete("Dependancy",requestCode,responseCode);
                }
                else
                {
                    if (parseUpdatedCategory(str))
                    {
                        downloadUtility.onComplete("Record Deleted", requestCode, responseCode);
                    }
                    else
                    {
                        downloadUtility.onComplete("Failed to delete", requestCode, responseCode);
                    }
                }


            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 3, responseCode);
            }
        }



        //   Capture Photo Code 10

        if(requestCode==10)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if  (true)  //(responseCode == 200)
            {
                try
                {
                    JSONObject jobj = new JSONObject(str);
                    // Toast.makeText(context,str,Toast.LENGTH_LONG).show();
                    if (  true )//jobj.getInt("Status")==1)
                    {
                    downloadUtility.onComplete(jobj.getString("path"), 10, responseCode);
                    }
                    else
                    {

                    }
                }
                catch (Exception e)
                {
                    downloadUtility.onComplete("Server Communication Failed", 10, 0);
                }

            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 10, 0);
            }
        }

    }



    //------------------------------------------  parse response and reflecting in local db --------------------------------------------


    public  boolean parseUpdatedCategory(String str) {

        try
        {
            ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
            itemDAOCategory.update(parseCategoryBean(str));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


    //  method parseInsertedCategory is for parsing category which inserted into web-server database and saving to local db

    public  boolean parseInsertedCategory(String str) {
        try
        {
            ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
            itemDAOCategory.insert(parseCategoryBean(str));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }




    // parse categories coming from server and make a list of bean and reflecting to local db

    public  boolean parseCategories(String str) {

        try
        {
            JSONArray jsonArray =new JSONArray(str);
            if(jsonArray.length()==0){Toast.makeText(context," No record founds",Toast.LENGTH_LONG).show();}
            ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
            itemDAOCategory.delete();
            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject jsonObject=jsonArray.getJSONObject(i);
                itemDAOCategory.insert(parseCategoryBean(jsonObject.toString()));
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }




    //   removes previsous list and add new list from local database

    public void getCategoryList()
    {

        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        list.clear();

        list.addAll(itemDAOCategory.getAllCategories(UserUtilities.getClientId(context)));
    }



    //  get all parent categories

    public void getParentCategoryList()
    {
        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        parentList.clear();
        parentList.addAll(    itemDAOCategory.getAllCategoryWhereParentIsZero(context));
                //itemDAOCategory.getAllCategoryNotLast(context));
       // parentList.addAll(itemDAOCategory.getAllParentCategories(UserUtilities.getClientId(context),0));
        notChildList.clear();
        notChildList.addAll(    itemDAOCategory.getAllCategoryWhereNotLast(context));

    }
    public void getParentCategory0List()
    {
        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        parentList.clear();
        parentList.addAll(    itemDAOCategory.getAllCategoryWhereParentIsZero(context));
        // parentList.addAll(itemDAOCategory.getAllParentCategories(UserUtilities.getClientId(context),0));
    }
    public void getLastCategoryList()
    {
        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        lastCategoryList.clear();
        lastCategoryList.addAll(itemDAOCategory.getLastCategories(UserUtilities.getClientId(context),0));
    }


    //   to parse a Category bean from JsonObject

    public CategoryBean parseCategoryBean(String str) throws JSONException {
        JSONObject jsonObject=new JSONObject(str);
        CategoryBean bean=new CategoryBean();
        bean.setCategoryId(jsonObject.getInt("CategoryId"));
        bean.setCategoryCode(jsonObject.getString("CategoryCode"));
        bean.setCategoryName(jsonObject.getString("CategoryName"));
        try
        {
            bean.setParentCategoryId(jsonObject.getInt("ParentCategoryId"));
        }
        catch (Exception e)
        {}
         bean.setIcon(jsonObject.getString("Icon"));
        bean.setIsLast(jsonObject.getString("IsLast"));
        bean.setRemark(jsonObject.getString("Remark"));
        bean.setClientId(jsonObject.getLong("ClientId"));
        bean.setCreatedBy(jsonObject.getLong("CreatedBy"));
        //bean.setCreatedDate(jsonObject.getLong("CreatedDate"));
        //bean.setCreatedDate(1000);

        bean.setCreatedDate(CommonUtilities.parseDate(jsonObject.getString("CreatedDate")));
        bean.setChangedBy(jsonObject.getLong("ChangedBy"));
        //bean.setChangedDate(jsonObject.getLong("ChangedDate"));
        bean.setChangedDate(1000);
        //bean.setDeleteStatus(jsonObject.getString("DeleteStatus"));

        bean.setDeleteStatus(String.valueOf(jsonObject.getBoolean("DeleteStatus")));
        return bean;
    }


    // return list of categories by parentID

    public ArrayList<CategoryBean> getListByParentId(int parentId)
    {
        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        ArrayList <CategoryBean> listOfParents= new ArrayList<>();
        listOfParents.addAll(itemDAOCategory.getAllParentCategories(UserUtilities.getClientId(context),parentId));
        return  listOfParents;
    }





    public String getCategoryName(int parentCategoryId) {

        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
         CategoryBean bean= itemDAOCategory.getCategory(parentCategoryId);
        if(bean!=null)
            return   bean.getCategoryName();
        else return "";
    }

    public CategoryBean getCategoryBeanById(int categoryId) {
        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        return  itemDAOCategory.getCategory(categoryId);
    }


}
