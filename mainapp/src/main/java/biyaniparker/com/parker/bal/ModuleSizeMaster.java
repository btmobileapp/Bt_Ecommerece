package biyaniparker.com.parker.bal;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import biyaniparker.com.parker.beans.SizeDetailBean;
import biyaniparker.com.parker.beans.SizeMaster;
import biyaniparker.com.parker.database.ItemDAOSizeMaster;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt on 08/19/2016.
 */
public class ModuleSizeMaster implements DownloadUtility
{

    Context context;
    public ArrayList<SizeMaster> sizeList=new ArrayList<SizeMaster>();

    public ModuleSizeMaster(Context context)
    {
        this.context=context;
    }



    //---------------------------------------------------  web api call  ----------------------------------------------



    public void syncSize()
    {
        // requset code ->   1
        AsyncUtilities serverAsync=new AsyncUtilities(context,false, CommonUtilities.URL+"ProductService.svc/getAllSizes?ClientId="+UserUtilities.getClientId(context),"",1,this);
        serverAsync.execute();
    }


    public void insertSize(SizeMaster bean,ArrayList<Integer> arrayList) throws JSONException {

        // requset code ->   2
        JSONArray array=new JSONArray();
        for(int i=0;i<arrayList.size();i++)
        {
           JSONObject js= new JSONObject().put("CategoryId", arrayList.get(i));
            array.put(js);
        }

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("SizeName",bean.getSizeName());
        jsonObject.put("ClientId",bean.getClientId());
        jsonObject.put("SequenceNo",bean.getSequenceNo());

        jsonObject.put("DeleteStatus",bean.getDeleteStatus());
        jsonObject.put("CreatedBy",bean.getCreatedBy());
        jsonObject.put("CreatedDate",bean.getCreatedDate());
        jsonObject.put("ChangedBy",bean.getChangedBy());
        jsonObject.put("ChangedDate",bean.getChangedDate());
        jsonObject.put("details",array);
        //Toast.makeText(context,jsonObject.toString(),Toast.LENGTH_LONG).show();
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/InsertSizeMaster",jsonObject.toString(),2,this);
        serverAsync.execute();

    }


    public void updateSize(SizeMaster bean,ArrayList<Integer> arrayList) throws JSONException {

        // requset code ->   3

        JSONArray array=new JSONArray();
        for(int i=0;i<arrayList.size();i++)
        {
            JSONObject js= new JSONObject().put("CategoryId", arrayList.get(i));
            array.put(js);
        }

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("SizeName",bean.getSizeName());
        jsonObject.put("ClientId",bean.getClientId());
        jsonObject.put("SequenceNo",bean.getSequenceNo());

        jsonObject.put("DeleteStatus",bean.getDeleteStatus());
        jsonObject.put("CreatedBy",bean.getCreatedBy());
        jsonObject.put("CreatedDate",bean.getCreatedDate());
        jsonObject.put("ChangedBy",bean.getChangedBy());
        jsonObject.put("ChangedDate",bean.getChangedDate());
        jsonObject.put("details",array);
        jsonObject.put("SizeId",bean.getSizeId());
        JSONObject mainJson=new JSONObject();
        mainJson.put("sizeMaster",jsonObject);
        //Toast.makeText(context,jsonObject.toString(),Toast.LENGTH_LONG).show();
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/UpdateSizeMaster",mainJson.toString(),3,this);
        serverAsync.execute();

    }



    public void deleteSize(SizeMaster bean,ArrayList<Integer> arrayList) throws JSONException {

        // requset code ->   4

        JSONArray array=new JSONArray();
        for(int i=0;i<arrayList.size();i++)
        {
            JSONObject js= new JSONObject().put("CategoryId", arrayList.get(i));
            array.put(js);
        }

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("SizeName",bean.getSizeName());
        jsonObject.put("ClientId",bean.getClientId());
        jsonObject.put("SequenceNo",bean.getSequenceNo());

        jsonObject.put("DeleteStatus",bean.getDeleteStatus());
        jsonObject.put("CreatedBy",bean.getCreatedBy());
        jsonObject.put("CreatedDate",bean.getCreatedDate());
        jsonObject.put("ChangedBy",bean.getChangedBy());
        jsonObject.put("ChangedDate",bean.getChangedDate());
        jsonObject.put("details",array);
        jsonObject.put("SizeId",bean.getSizeId());
        JSONObject mainJson=new JSONObject();
        mainJson.put("sizeMaster",jsonObject);
        //Toast.makeText(context,jsonObject.toString(),Toast.LENGTH_LONG).show();
        AsyncUtilities serverAsync=new AsyncUtilities(context,true, CommonUtilities.URL+"ProductService.svc/DeleteSizeMaster",mainJson.toString(),4,this);
        serverAsync.execute();

    }

    // ----------------------------------------------   Handling web response ---------------------------------------------------------


    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        // request code 1 for sizelist

        if(requestCode==1)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {


                if (parseSizes(str)) {

                    downloadUtility.onComplete("Success", 1, responseCode);
                } else {
                    downloadUtility.onComplete("Failed", 1, responseCode);
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 1, responseCode);
            }
        }



        // request code 2 for insert size
        if(requestCode==2)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                try {
                    if (parseInsertedSize(str)) {

                        downloadUtility.onComplete("Success", 2, responseCode);
                    } else {
                        downloadUtility.onComplete("Failed", 2, responseCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 2, responseCode);
            }
        }



        // request code 3 for update

        if(requestCode==3)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                try {
                    if (parseInsertedSize(str)) {

                        downloadUtility.onComplete("Success", 3, responseCode);
                    } else {
                        downloadUtility.onComplete("Failed", 3, responseCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", 3, responseCode);
            }
        }

        // request code 4 for deleted size

        if(requestCode==4)
        {
            DownloadUtility downloadUtility = (DownloadUtility) context;
            if (responseCode == 200)
            {
                try {
                    if(str.equals("0"))
                    {
                        downloadUtility.onComplete("dependancy",requestCode,responseCode);
                    }
                    else
                    {
                        if (parseInsertedSize(str)) {

                            downloadUtility.onComplete("Success", requestCode, responseCode);
                        } else {
                            downloadUtility.onComplete("Failed", requestCode, responseCode);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
            {
                downloadUtility.onComplete("Server Communication Failed", requestCode, responseCode);
            }
        }
    }




//------------------------------------- Parsing and handling local database --------------------------------------


    private boolean parseInsertedSize(String str) throws JSONException {

        ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
        SizeMaster sizeMaster=new SizeMaster();
        ArrayList<SizeDetailBean> sizeDetail=new ArrayList<SizeDetailBean>();

        JSONObject jsonObject=new JSONObject(str);
        JSONArray array=jsonObject.getJSONArray("details");
        JSONObject size=jsonObject.getJSONObject("sizeMasters");
        sizeMaster.setSizeId(size.getInt("SizeId"));

        for(int i=0;i<array.length();i++)
        {
            SizeDetailBean s=new SizeDetailBean();
            s.setSizeId(sizeMaster.getSizeId());
            JSONObject j=array.getJSONObject(i);
            s.setCategoryId(j.getInt("CategoryId"));
           s.setDeleteStatus(j.getString("DeleteStatus"));
           s.setSizeDetailId(j.getInt("SizeDetailId"));
            sizeDetail.add(s);
        }


        sizeMaster.setSizeName(size.getString("SizeName"));
        sizeMaster.setDeleteStatus(size.getString("DeleteStatus"));

        try {
            sizeMaster.setSequenceNo(size.getInt("SequenceNo"));
            sizeMaster.setClientId(size.getInt("ClientId"));
        }
        catch(Exception e)
        {}

        sizeMaster.setCreatedBy(size.getLong("CreatedBy"));
        sizeMaster.setChangedBy(size.getLong("ChangedBy"));
        sizeMaster.setCreatedDate(CommonUtilities.parseDate(size.getString("CreatedDate")));
        sizeMaster.setChangedDate(CommonUtilities.parseDate(size.getString("ChangedDate")));


        itemDAOSizeMaster.insertSize(sizeMaster,sizeDetail);

        return true;
    }


    private boolean parseDeletedSize(String str) throws JSONException {

        SizeMaster sizeMaster=new SizeMaster();
        JSONObject size=new  JSONObject(str);
        sizeMaster.setSizeId(size.getInt("SizeId"));


        sizeMaster.setSizeName(size.getString("SizeName"));
        sizeMaster.setDeleteStatus(size.getString("DeleteStatus"));

        try {
            sizeMaster.setSequenceNo(size.getInt("SequenceNo"));
            sizeMaster.setClientId(size.getInt("ClientId"));
        }
        catch(Exception e)
        {}

        sizeMaster.setCreatedBy(size.getLong("CreatedBy"));
        sizeMaster.setChangedBy(size.getLong("ChangedBy"));
        sizeMaster.setCreatedDate(CommonUtilities.parseDate(size.getString("CreatedDate")));
        sizeMaster.setChangedDate(CommonUtilities.parseDate(size.getString("ChangedDate")));

        ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
        itemDAOSizeMaster.updateSizeMaster(sizeMaster);
        return false;
    }

    public  boolean parseSizes(String str) {

        try
        {
            JSONArray jsonArray =new JSONArray(str);
            if(jsonArray.length()==0){Toast.makeText(context," No record founds",Toast.LENGTH_LONG).show();}
            ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
            itemDAOSizeMaster.deleteAllSizes();
            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject jsonObject=jsonArray.getJSONObject(i);
                parseInsertedSize(jsonObject.toString());
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    // returns a list of SizeMaster bean

    public void getSizeList()
    {
        ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
        sizeList.clear();
        sizeList.addAll(itemDAOSizeMaster.getAllSize(UserUtilities.getClientId(context)));
    }


    // return a SizeMaster bean where sizedId Matches

    public SizeMaster getSizeMasterBean(int sizeId) {
        ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
        return itemDAOSizeMaster.getSizeBean(sizeId);

    }



    // return a list of SizeDatail Bean where Size Id mathces

    public ArrayList<SizeDetailBean> getSizeDetails(int sizeId) {
        ItemDAOSizeMaster itemDAOSizeMaster=new ItemDAOSizeMaster(context);
        return itemDAOSizeMaster.getSizeDetailsBySizeId(sizeId);

    }
}
