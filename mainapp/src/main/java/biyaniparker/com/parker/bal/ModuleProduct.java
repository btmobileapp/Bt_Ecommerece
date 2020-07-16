package biyaniparker.com.parker.bal;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import biyaniparker.com.parker.beans.CategoryBean;
import biyaniparker.com.parker.beans.PriceBean;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.SizeDetailBean;
import biyaniparker.com.parker.beans.SizeMaster;
import biyaniparker.com.parker.beans.StockMasterBean;
import biyaniparker.com.parker.database.ItemDAOCategory;
import biyaniparker.com.parker.database.ItemDAOPrice;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.database.ItemDAOSizeMaster;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncFileUploadUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt on 08/13/2016.
 */
public class ModuleProduct implements DownloadUtility{

    Context context;
    public ArrayList<ProductBean> list=new ArrayList<ProductBean>();
    public ArrayList<SizeMaster>sizeMasters=new ArrayList<SizeMaster>();
    public ArrayList<ProductBean> filterList =new ArrayList<ProductBean>();
    public ModuleProduct(Context context)
    {
            this.context=context;
    }

    private  int refilledArrayLength;


    //------------------------------------------- web api call ---------------------------------------------------------------------------


    public void syncProduct()
    {
        // requset code ->   1
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"ProductService.svc/getAllProduct?ClientId="+UserUtilities.getClientId(context),"",1,this);
        serverAsync.execute();
    }

    public void loadFile(File bitmapFile)
    {
        AsyncFileUploadUtilities asyncFileUploadUtilities=new AsyncFileUploadUtilities(context,10,
                CommonUtilities.URL+"ProductService.svc/UploadFile",this,bitmapFile);

        asyncFileUploadUtilities.execute();
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


    public void productDeduction(ProductBean productBean, ArrayList<StockMasterBean> stockList) throws JSONException {

        JSONArray jsonArray=new JSONArray();
        refilledArrayLength=stockList.size();
        for(int j=0;j<stockList.size();j++)
        {
            JSONObject jobjstock=new JSONObject();
            jobjstock.put("ProductId",productBean.getProductId());
            jobjstock.put("SizeId", stockList.get(j).getSizeId());
            jobjstock.put("OutwardQty", stockList.get(j).getOutwardQty());
            jobjstock.put("CreatedBy", productBean.getCreatedBy());
            jobjstock.put("ChangedBy", productBean.getChangedBy());
            jobjstock.put("DeleteStatus", productBean.getDeleteStatus());
            jobjstock.put("UserId",UserUtilities.getUserId(context));

            jobjstock.put("TransactionType","deduct");
            jobjstock.put("ClientId",UserUtilities.getClientId(context));
            jsonArray.put(jobjstock);
        }

        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"StockService.svc/DeductStockMaster",jsonArray.toString(),5,this);
        serverAsync.execute();
    }


    public void manageProductStock(ProductBean productBean, ArrayList<StockMasterBean> stockList) throws JSONException {

        JSONArray jsonArray=new JSONArray();
        refilledArrayLength=stockList.size();
        for(int j=0;j<stockList.size();j++)
        {
            JSONObject jobjstock=new JSONObject();
            jobjstock.put("ProductId",productBean.getProductId());
            jobjstock.put("SizeId", stockList.get(j).getSizeId());
            if(stockList.get(j).getOutwardQty()!=0)
            {
                jobjstock.put("TransactionType","deduct");
                jobjstock.put("OutwardQty", stockList.get(j).getOutwardQty());
            }
            if(stockList.get(j).getInwardQty()!=0)
            {
                jobjstock.put("InwardQty", stockList.get(j).getInwardQty());
                jobjstock.put("TransactionType","inward");
            }
            jobjstock.put("CreatedBy", productBean.getCreatedBy());
            jobjstock.put("ChangedBy", productBean.getChangedBy());
            jobjstock.put("DeleteStatus", productBean.getDeleteStatus());
            jobjstock.put("UserId",UserUtilities.getUserId(context));


            jobjstock.put("ClientId",UserUtilities.getClientId(context));
            jsonArray.put(jobjstock);
        }

        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"StockService.svc/ManageStock",jsonArray.toString(),5,this);
        serverAsync.execute();
    }


    public void syncRecentProducts()
    {
        AsyncUtilities asyncUtilities=new AsyncUtilities(context,false,CommonUtilities.URL+"ProductService.svc/getRecentProducts?ClientId="+UserUtilities.getClientId(context)+"&Date="+getMaxChangedDate(),null,6,this);
        asyncUtilities.execute();
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

        //  request code 5 for deducted stock
        if( requestCode == 5 && responseCode == 200)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;

            if( parseDeductedStock(str))
            {
                downloadUtility.onComplete(CommonUtilities.RESPONCE_OK,requestCode, responseCode);
            }
            else
            {
                downloadUtility.onComplete("Failed",5, responseCode);
            }
        }



        if(requestCode ==6)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if(responseCode == 200) {
                if (parseProducts(str)) {

                    downloadUtility.onComplete("Success", requestCode, responseCode);
                }
                else {
                    downloadUtility.onComplete("Failed", requestCode, responseCode);
                }
            }
            else {
                downloadUtility.onComplete("Failed", requestCode, responseCode);
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

    private boolean parseDeductedStock(String str)
    {
        try {
            JSONObject jsonObject = new JSONObject(str);
            int flag = jsonObject.getInt("flag");
            if (flag == 0) {
                return true;
            } else if (flag == 1) {
                return false;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
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
            //if(jsonArray.length()==0){ Toast.makeText(context, " No record founds", Toast.LENGTH_LONG).show();}
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
        Collections.reverse(list);
    }

    public void getProductListAboveDate(long dateLong)
    {
        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        list.clear();
        list.addAll(itemDAOProduct.getAllProductsAboveDate(UserUtilities.getClientId(context),dateLong));
        Collections.reverse(list);
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

    public int getParentCategoryId(int cateId)
    {
        /*int parentCatId=-1;   //=bean.getParentCategoryId(), catId=bean.getCategoryId();
        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        CategoryBean catBean=itemDAOCategory.getCategory(CateId);

        parentCatId=CateId;

        while(parentCatId!=0)
        {
            catBean=itemDAOCategory.getCategory(parentCatId);
         //   parentCatId=catBean.getCategoryId();
            parentCatId=catBean.getParentCategoryId();
        }*/

        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        CategoryBean bean=itemDAOCategory.getCategory(cateId);
        int parentCatId=bean.getParentCategoryId(),
                catId=bean.getCategoryId();
        CategoryBean catBean=new CategoryBean();


        while(parentCatId!=0)
        {
            catBean=itemDAOCategory.getCategory(parentCatId);
            catId=catBean.getCategoryId();
            parentCatId=catBean.getParentCategoryId();
        }


        return  catId;
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

    public ArrayList<ProductBean> getCustomList(String stripCode, int catId, int price)
    {
            ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
              return itemDAOProduct.getCustomList(stripCode, catId, price);
    }

    public void getCustomListByPriceLimit(int catID, long min, long max)
    {
        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        filterList.clear();
         filterList.addAll(itemDAOProduct.getPriceFilteredList(catID,min,max));
    }

    public void getCustomListByFilter(int catID, long min, long max, String stripVal)
    {
        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        filterList.clear();
        filterList.addAll(itemDAOProduct.getFilteredList(catID, min, max, stripVal));
    }

    public long getMaxChangedDate()
    {
        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        return itemDAOProduct.getMaxChangedDate();
    }

    public    HashMap<Integer,ProductBean> productMapping=new HashMap<>();
    public void setProductBuffer(String productsIds) {
        //this.productBuffer = productBuffer;
        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(context);
        productMapping=  itemDAOProduct.getProductsFromCommSepatredIds(productsIds);

    }

    public    HashMap<Integer,CategoryBean> categoryMapping=new HashMap<>();
    public void setCategoryBuffer() {
        ItemDAOCategory itemDAOCategory=new ItemDAOCategory(context);
        categoryMapping=itemDAOCategory.getAllCategoriesForMapping();
    }


    public    HashMap<Integer,PriceBean> priceMapping=new HashMap<>();

    public void setPriceBuffer()
    {
                ItemDAOPrice itemDAOPrice=new ItemDAOPrice(context);
                 priceMapping=itemDAOPrice.getPriceMapper();
    }


    public    HashMap<Integer,SizeMaster> sizeMapping=new HashMap<>();
    public void setSizeBuffer()
    {
        ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
        sizeMapping= itemDAOSizeMaster.getAllSizeForBuffer();
    }
}
