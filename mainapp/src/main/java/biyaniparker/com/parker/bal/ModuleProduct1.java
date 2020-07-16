package biyaniparker.com.parker.bal;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.SizeDetailBean;
import biyaniparker.com.parker.beans.SizeMaster;
import biyaniparker.com.parker.beans.StockMasterBean;
import biyaniparker.com.parker.database.ItemDAOCategory;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.database.ItemDAOSizeMaster;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.MultifileUploadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncFileUploadUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncMultiFileUploadUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt on 08/13/2016.
 */
public class ModuleProduct1 implements DownloadUtility,MultifileUploadUtility
{

    Context context;
    public ArrayList<ProductBean> list=new ArrayList<ProductBean>();
    public ArrayList<SizeMaster>sizeMasters=new ArrayList<SizeMaster>();
    public ArrayList<CategoryBean>  sameParentCategoryList=new ArrayList<>();
    public ModuleProduct1(Context context)
    {
            this.context=context;
    }

    public void setSameParentCategoryList( int  cateId)
    {
          ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
          sameParentCategoryList.clear();
          CategoryBean categoryBean=  itemDAOCategory.getCategory(cateId);

          int parentCateId= categoryBean.getParentCategoryId();

           List<CategoryBean>  mlist= itemDAOCategory.getCategoriesParent(parentCateId);
           if(mlist!=null)
              sameParentCategoryList.addAll(mlist);

    }




    private  int refilledArrayLength;


    //------------------------------------------- web api call ---------------------------------------------------------------------------


    public void syncProduct()
    {
        // requset code ->   1
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"ProductService.svc/getAllProduct?ClientId="+1,"",1,this);
        serverAsync.execute();
    }

    public void loadFile(File bitmapFile,File bitmapFile1)
    {
        /*AsyncFileUploadUtilities asyncFileUploadUtilities=new AsyncFileUploadUtilities(context,10,
                CommonUtilities.URL+"ProductService.svc/UploadFile",this,bitmapFile);

        asyncFileUploadUtilities.execute();*/


        AsyncMultiFileUploadUtilities asyncMultiFileUploadUtilities=new AsyncMultiFileUploadUtilities
                (context,10,CommonUtilities.URL+"ProductService.svc/UploadFile",this,bitmapFile,bitmapFile1);

        asyncMultiFileUploadUtilities.execute();
    }

    public void createProduct(ProductBean productBean, ArrayList<StockMasterBean> stockList) throws JSONException {
        JSONObject contentValues=new JSONObject();
        contentValues.put("ProductId",productBean.getProductId());
        contentValues.put("ProductCode", productBean.getProductCode());
        contentValues.put("ProductName", productBean.getProductName());
        contentValues.put("StripCode", productBean.getStripCode());
        contentValues.put("Details", productBean.getDetails());
        contentValues.put("PriceId", productBean.getPriceId());
        contentValues.put("CategoryId", productBean.getCategoryId());
        contentValues.put("IconThumb", productBean.getIconThumb());
        contentValues.put("IconFull", productBean.getIconFull());
        contentValues.put("IconFull1", productBean.getIconFull1());
        contentValues.put("ClientId", productBean.getClientId());
        //contentValues.put("SequenceNo", productBean.getSequenceNo());
        contentValues.put("CreatedBy", productBean.getCreatedBy());
        contentValues.put("CreatedDate", productBean.getCreatedDate());
        contentValues.put("ChangedBy", productBean.getChangedBy());
        contentValues.put("ChangedDate", productBean.getChagedDate());
        contentValues.put("DeleteStatus", productBean.getDeleteStatus());
        contentValues.put("IsActive", productBean.getIsActive());


        JSONArray jsonArray=new JSONArray();

        for(int j=0;j<stockList.size();j++)
        {
            JSONObject jobjstock=new JSONObject();
            jobjstock.put("SizeId", stockList.get(j).getSizeId());
            jobjstock.put("InwardQty", stockList.get(j).getInwardQty());
            jsonArray.put(jobjstock);
        }
        contentValues.accumulate("StockList",jsonArray);

        contentValues.put("price", productBean.price);
        contentValues.put("IconFull2", productBean.IconFull2);
        contentValues.put("IconFull3", productBean.IconFull3);
        contentValues.put("IconFull4", productBean.IconFull4);
        contentValues.put("IconFull5", productBean.IconFull5);
        contentValues.put("UnitName", productBean.UnitName);

        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/InsertProduct",contentValues.toString(),2,this);
        serverAsync.execute();
    }

    public void updateProduct(ProductBean productBean) throws JSONException {
        JSONObject contentValues=new JSONObject();
        contentValues.put("ProductId",productBean.getProductId());
        contentValues.put("ProductCode", productBean.getProductCode());
        contentValues.put("ProductName", productBean.getProductName());
        contentValues.put("StripCode", productBean.getStripCode());
        contentValues.put("Details", productBean.getDetails());
        contentValues.put("PriceId", productBean.getPriceId());
        contentValues.put("CategoryId", productBean.getCategoryId());
        contentValues.put("IconThumb", productBean.getIconThumb());
        contentValues.put("IconFull", productBean.getIconFull());
        contentValues.put("IconFull1", productBean.getIconFull1());
        contentValues.put("ClientId", productBean.getClientId());
        //contentValues.put("SequenceNo", productBean.getSequenceNo());
        contentValues.put("CreatedBy", productBean.getCreatedBy());
        contentValues.put("CreatedDate", productBean.getCreatedDate());
        contentValues.put("ChangedBy", productBean.getChangedBy());
        contentValues.put("ChangedDate", productBean.getChagedDate());
        contentValues.put("DeleteStatus", productBean.getDeleteStatus());
        contentValues.put("IsActive", productBean.getIsActive());
         AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/UpdateProduct",contentValues.toString(),3,this);
        serverAsync.execute();
    }

    public void deleteProduct(ProductBean productBean) throws JSONException
    {
        JSONObject contentValues=new JSONObject();
        contentValues.put("ProductId",productBean.getProductId());
        contentValues.put("ProductCode", productBean.getProductCode());
        contentValues.put("ProductName", productBean.getProductName());
        contentValues.put("StripCode", productBean.getStripCode());
        contentValues.put("Details", productBean.getDetails());
        contentValues.put("PriceId", productBean.getPriceId());
        contentValues.put("CategoryId", productBean.getCategoryId());
        contentValues.put("IconThumb", productBean.getIconThumb());
        contentValues.put("IconFull", productBean.getIconFull());
        contentValues.put("IconFull1", productBean.getIconFull1());
        contentValues.put("ClientId", productBean.getClientId());
        //contentValues.put("SequenceNo", productBean.getSequenceNo());
        contentValues.put("CreatedBy", productBean.getCreatedBy());
        contentValues.put("CreatedDate", productBean.getCreatedDate());
        contentValues.put("ChangedBy", productBean.getChangedBy());
        contentValues.put("ChangedDate", productBean.getChagedDate());
        contentValues.put("DeleteStatus", productBean.getDeleteStatus());
        contentValues.put("IsActive", productBean.getIsActive());
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/DeleteProduct",contentValues.toString(),5,this);
        serverAsync.execute();
    }

    public void refillProduct(ProductBean productBean, ArrayList<StockMasterBean> stockList) throws JSONException {

        JSONArray jsonArray=new JSONArray();
        refilledArrayLength=stockList.size();
        for(int j=0;j<stockList.size();j++)
        {
            JSONObject jobjstock=new JSONObject();
            jobjstock.put("ProductId",productBean.getProductId());
            jobjstock.put("SizeId", stockList.get(j).getSizeId());
            jobjstock.put("InwardQty", stockList.get(j).getInwardQty());
            jobjstock.put("CreatedBy", productBean.getCreatedBy());
            jobjstock.put("ChangedBy", productBean.getChangedBy());
            jobjstock.put("DeleteStatus", productBean.getDeleteStatus());
            jobjstock.put("UserId",UserUtilities.getUserId(context));
            jobjstock.put("TransactionType","inward");
            jobjstock.put("ClientId",UserUtilities.getClientId(context));
            jsonArray.put(jobjstock);

        }

        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"StockService.svc/InsertStockMaster",jsonArray.toString(),4,this);
        serverAsync.execute();
    }


//-------------------------------------------------------  web api response ---------------------------------------------------------------


    //   removes previsous list and add new list from local database

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        if(requestCode==1)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseProducts(str)) {

                    downloadUtility.onComplete("Success", 1, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 1, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 1, responseCode);
            }
        }


        //  Response COde=2 is for Create Product response

        if(requestCode==2)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                if (parseInsertedProduct(str)) {

                    downloadUtility.onComplete("Success", 2, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 2, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 2, responseCode);
            }
        }


        //  Response COde=3 is for update Product response
        if(requestCode==3)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseUpdatedProduct(str)) {

                    downloadUtility.onComplete("Success", 3, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 3, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 3, responseCode);
            }
        }



        //  Response COde=5 is for deleted Product response
        if(requestCode==5)
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
                    if (parseUpdatedProduct(str)) {

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


        // to parse refilled product


        if(requestCode==4)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseRefilledProduct(str)) {

                    downloadUtility.onComplete("Success", 4, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 4, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 4, responseCode);
            }
        }

        //   Capture Photo Code 10

        if(requestCode==10)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if  (responseCode==200)  //(responseCode == 200)
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

            }
            else
            {
                downloadUtility.onComplete("Server Communication Failed", 10, 0);
            }
        }

    }

    private boolean parseRefilledProduct(String str)
    {
        JSONArray array= null;
        try {
            array = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(refilledArrayLength==array.length())
        {   return true;}

        else
        {return false;}
    }


    // ----------------------------------------------  Parse and reflecting to local db -------------------------------------------


    private boolean parseUpdatedProduct(String str)
    {
        try
        {
            ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
            itemDAOProduct.update(parseProductBean(str));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }



    private boolean parseInsertedProduct(String str)
    {
        try
        {
            ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
            itemDAOProduct.insert(parseProductBean(str));

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }





    public boolean parseProducts(String str)
    {
        try
        {
            JSONArray jsonArray =new JSONArray(str);
            if(jsonArray.length()==0){ Toast.makeText(context, " No record founds", Toast.LENGTH_LONG).show();}
            ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject jsonObject=jsonArray.getJSONObject(i);
                itemDAOProduct.insert(parseProductBean(jsonObject.toString()));

            }
            return  true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public void getProductList()
    {
        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        list.clear();
        list.addAll(itemDAOProduct.getAllProducts(UserUtilities.getClientId(context)));
    }




    // to parse product bean from jsonObject

    public ProductBean parseProductBean(String str)
    {
        try
        {
            ProductBean bean = new ProductBean();
            JSONObject c = new JSONObject(str);
            bean.setProductId(c.getInt("ProductId"));
            bean.setProductCode(c.getString("ProductCode"));
            bean.setProductName(c.getString("ProductName"));
            bean.setStripCode(c.getString("StripCode"));
            bean.setDetails(c.getString("Details"));
            bean.setPriceId(c.getInt("PriceId"));
            bean.setCategoryId(c.getInt("CategoryId"));
            bean.setIconThumb(c.getString("IconThumb"));
            bean.setIconFull(c.getString("IconFull"));
            bean.setIconFull1(c.getString("IconFull1"));
            bean.setClientId(c.getInt("ClientId"));
            //bean.setSequenceNo(c.getInt("SequenceNo"));
            try{
            bean.setCreatedBy(c.getLong("CreatedBy"));}catch (Exception e){}
            bean.setCreatedDate(CommonUtilities.parseDate(c.getString("CreatedDate")));
           try{   bean.setChangedBy(c.getLong("ChangedBy"));}catch (Exception e){}
            try
            {
                bean.setChagedDate(CommonUtilities.parseDate(c.getString("ChangedDate")));
            }
            catch (Exception e){}
            bean.setDeleteStatus(c.getString("DeleteStatus"));
            try
            {
                bean.setIsActive(c.getString("IsActive"));
            }
            catch (Exception e)
            {

            }
            return bean;
        }
        catch (Exception e)
        {

        }
        return null;
    }

    public void getSizeDetailList(CategoryBean bean)
    {
        ArrayList<SizeDetailBean> sizeDetailBeans=new ArrayList<SizeDetailBean>();

        int parentCatId=bean.getParentCategoryId(), catId=bean.getCategoryId();
        CategoryBean catBean=new CategoryBean();
        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
        while(parentCatId!=0)
        {
            catBean=itemDAOCategory.getCategory(parentCatId);
            catId=catBean.getCategoryId();
            parentCatId=catBean.getParentCategoryId();
        }

        sizeMasters.clear();
        sizeDetailBeans.addAll(itemDAOSizeMaster.getSizeDetailsByCategoryId(catId));


        for(int i=0;i<sizeDetailBeans.size();i++)
        {
            SizeMaster master=itemDAOSizeMaster.getSizeBean(sizeDetailBeans.get(i).getSizeId());
           if(master!=null)
           {

               sizeMasters.add(master);
           }
        }

        for(int j=0;j<sizeMasters.size();j++)
        {
            for(int k=j;k<sizeMasters.size();k++)
            {
                if(sizeMasters.get(j).getSizeId()==sizeMasters.get(k).getSizeId()&& k!=j)
                {
                    sizeMasters.remove(k);
                }
            }
        }

    }

    public ArrayList<ProductBean> getProductListByCatId(int catid)
    {
        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        return  itemDAOProduct.getProductsByCatId(catid);

    }

    public ProductBean getProductBeanByProductId(int productId) {

        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        return itemDAOProduct.getProduct(productId);
    }

    @Override
    public void onUploadComplete(String str1, String str2, int requestCode, int responseCode)
    {

      //  Toast.makeText(context.getApplicationContext(),"IN BBBB"+responseCode,Toast.LENGTH_LONG).show();
        MultifileUploadUtility utility= (MultifileUploadUtility) context;
        try
        {
            JSONObject jobj1 = new JSONObject(str1);
            JSONObject jobj2 = new JSONObject(str2);
            // Toast.makeText(context,str,Toast.LENGTH_LONG).show();
            if (200==responseCode)//jobj.getInt("Status")==1)
            {
               // downloadUtility.onComplete(jobj.getString("path"), 10, responseCode);
                utility.onUploadComplete(jobj1.getString("path"),jobj2.getString("path")
                ,10,responseCode);
            }
            else
            {
                utility.onUploadComplete(jobj1.getString("path"),jobj2.getString("path")
                        ,10,responseCode);
            }
        }
        catch (Exception e)
        {
           // Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
            utility.onUploadComplete("",""
                    ,10,0);
        }
    }


}
