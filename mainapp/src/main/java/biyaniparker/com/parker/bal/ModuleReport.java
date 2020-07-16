package biyaniparker.com.parker.bal;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import biyaniparker.com.parker.beans.ReportProductStockBean;
import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.NotifyCallback;
import biyaniparker.com.parker.utilities.ParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncStreamParsingUtilities;
import biyaniparker.com.parker.utilities.serverutilities.AsyncUtilities;

/**
 * Created by bt on 09/30/2016.
 */
public class ModuleReport implements DownloadUtility, ParsingUtilities {
    Context context;
    public ArrayList<ReportProductStockBean> reportDataList=new ArrayList<>();
    public ModuleReport(Context context)
    {
        this.context = context;
    }
    AsyncStreamParsingUtilities asyncStreamParsingUtilities;



    //-------------------------------------------------web service call-------------------------------------


    public void getProductStockReport(int clientId, int categoryId)
    {
        // request code =1 is to call product stock report by category

        AsyncUtilities asyncUtilities=new AsyncUtilities(context,false, CommonUtilities.URL+"StockService.svc/ProdctStockReport?ClientID="+clientId+"&CategoryId="+categoryId,null,1,this);
        asyncUtilities.execute();

    }

    public void getProductStockReportWithNotify(int clientId, int categoryId)
    {


        asyncStreamParsingUtilities =new AsyncStreamParsingUtilities(context,false, CommonUtilities.URL+"StockService.svc/ProdctStockReport?ClientID="+clientId+"&CategoryId="+categoryId,null,1,this, this);
        asyncStreamParsingUtilities.setHidePdAfterConnection(true);
        asyncStreamParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncStreamParsingUtilities.execute();
    }


    public void getProductPhysicalStockReportWithNotify(int clientId, int catId)
    {
        asyncStreamParsingUtilities =new AsyncStreamParsingUtilities(context,false, CommonUtilities.URL+"StockService.svc/ProdctPhysicalStockReport?ClientID="+clientId+"&CategoryId="+catId,null,2,this, this);
        asyncStreamParsingUtilities.setHidePdAfterConnection(true);
        asyncStreamParsingUtilities.setProgressDialoaugeVisibility(true);
        asyncStreamParsingUtilities.execute();
    }



    //-------------------------------------------- web service response handling ---------------------------------
    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
        // request code =1 is to call product stock report by category


        if(requestCode==1 || requestCode == 2) {
           DownloadUtility downloadUtility = (DownloadUtility) context;
           downloadUtility.onComplete("Success", requestCode, responseCode);
        }
    }

    public void stopAsyncStreaming()
    {
        if(asyncStreamParsingUtilities!=null)
        {
            asyncStreamParsingUtilities.closeConnection();
        }
    }

    @Override
    public boolean parseStreamingData(InputStream stream)
    {

        reportDataList.clear();

        NotifyCallback notifyCallback=(NotifyCallback)context;
        Gson gson=new Gson();
        try {

            JsonReader jsonReader=new JsonReader(new InputStreamReader(stream, "UTF-8"));
            jsonReader.beginArray();
            int count =1;
            while(jsonReader.hasNext())
            {
                count++;
                ReportProductStockBean bean=gson.fromJson(jsonReader,ReportProductStockBean.class);
                reportDataList.add(bean);
                //if(count==1)
                 //  asyncStreamParsingUtilities.hideProgress();
                if(count%10==0)
                {
                   // if(notifyCallback!=null)
                    //    notifyCallback.notifyToActivity();
                }
            }
           //notifyCallback.notifyToActivity();
            jsonReader.endArray();
            jsonReader.close();


        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }



}