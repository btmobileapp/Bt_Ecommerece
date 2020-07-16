package biyaniparker.com.parker.services;

import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import biyaniparker.com.parker.ProgressNoticeBar;
import biyaniparker.com.parker.beans.ProductBean;
import biyaniparker.com.parker.beans.ProductBeanWithQnty;
import biyaniparker.com.parker.beans.gson.GsonProductWithQnty;
import biyaniparker.com.parker.database.ItemDAOProduct;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.UserUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;
import biyaniparker.com.parker.utilities.serverutilities.ConnectServer;
import biyaniparker.com.parker.utilities.serverutilities.ResponseBody;
import biyaniparker.com.parker.view.homeadmin.AdminHomeScreen;

public class ProductSyncService extends Service {
    public ProductSyncService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //android.os.Debug.waitForDebugger();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        long currentTime = System.currentTimeMillis();
        String today = CommonUtilities.longToDate(currentTime);
        SharedPreferences sh = ProductSyncService.this.getSharedPreferences("SyncDate", MODE_PRIVATE);
        String lastSyncDay = sh.getString("SyncDate", "");


        if (lastSyncDay.equals(today))
        {
            //startActivity(new Intent(ProductSyncService.this, AdminHomeScreen.class));
            //Log.e("Info","Product sync already done");
        }
        else
        {
            // call sync product
            AsyncCall asyncCall=new AsyncCall();
            asyncCall.execute();
            stopSelf();
        }


        return super.onStartCommand(intent, flags, startId);

    }

    public long getMaxChangedDate()
    {
        ItemDAOProduct itemDAOProduct=new ItemDAOProduct(this);
        return itemDAOProduct.getMaxChangedDate();
    }


    private void updateSyncTime()
    {
        long currentTime = System.currentTimeMillis();
        String today = CommonUtilities.longToDate(currentTime);
        SharedPreferences sh = this.getSharedPreferences("SyncDate", MODE_PRIVATE);
        SharedPreferences.Editor editor=sh.edit();
        editor.putString("SyncDate", today);
        editor.commit();
       // Toast.makeText(ProductSyncService.this, "Successfully synced", Toast.LENGTH_SHORT).show();
    }

    private class AsyncCall extends AsyncTask
    {


        ResponseBody responseBody;
        InputStream inputStream;
        ConnectServer connectServer;
        String url;
        int totalSize;

        boolean parsing;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            connectServer=new ConnectServer();
            //Log.e("Info","Product sync started");
            url=CommonUtilities.URL+"ProductService.svc/getRecentProducts?ClientId="+UserUtilities.getClientId(getApplicationContext())+"&Date="+(getMaxChangedDate()-(864000000));
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                inputStream =connectServer.getConnectionInputStream(url);
                //Log.e("Info","Product sync getting data");
              //  totalSize=  inputStream.available();
                //inputStream.
            } catch (IOException e) {
                e.printStackTrace();
            }

            try
            {
            int calculateLength=0;
            int count=1;
            Gson gson = new Gson();
            JsonReader reader = null;

            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));


            reader.beginArray();
            int endFlag=0;



            while(reader.hasNext())
            {
                GsonProductWithQnty gsonProductWithQnty=gson.fromJson(reader, GsonProductWithQnty.class);
                String strObject=gson.toJson(gsonProductWithQnty);
                calculateLength=calculateLength+strObject.getBytes().length;
                //Log.e("Info","Product sync parsing");

               /* if(connectServer.contentLength>0)
                {
                    int inc = (calculateLength / connectServer.contentLength) * 100;
                    ProgressNoticeBar.notify(ProductSyncService.this, "Loading Products", 1, inc);
                }*/
                ItemDAOProduct    itemDAOProduct=new ItemDAOProduct(getApplicationContext());
                itemDAOProduct.insert(gsonProductWithQnty.toProductBeanWithQnty());
                count++;
            }
            parsing=true;
            reader.endArray();
            //inputStream.close();
            reader.close();


            }
            catch (Exception  e)
            {
                parsing=false;
                e.printStackTrace();
                //Log.e("ERROR", e.toString());
                Log.e("Info","Error Sync"+e.toString());
                //Toast.makeText(ProductSyncService.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            if(parsing)
            {
                    updateSyncTime();
            }
            else
            {
                //Toast.makeText(getApplicationContext(),"Product Sync failed ..",Toast.LENGTH_SHORT).show();
            }
            stopSelf();

        }

    }


    public boolean parseProducts(String str)
    {
        try
        {
            JSONArray jsonArray =new JSONArray(str);
            if(jsonArray.length()==0){ Toast.makeText(getApplicationContext(), " No record founds", Toast.LENGTH_LONG).show();}
            ItemDAOProduct itemDAOProduct=new ItemDAOProduct(getApplicationContext());
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



    private void saveProductWithQnty(ProductBeanWithQnty productBeanWithQnty)
    {

    }



}
