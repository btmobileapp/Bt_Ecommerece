package biyaniparker.com.parker.utilities.serverutilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import biyaniparker.com.parker.bal.ModuleSync;
import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.ParsingUtilities;

/**
 * Created by bt18 on 08/08/2016.
 */
public class AsyncStreamParsingUtilities extends AsyncTask<Void,Void,String>
{

    public static final int IDLE=0,PRE_EXECUTE=1,ON_EXECUTE=2,AFTER_EXCECUTE=3;
    public int progressStatus=-1;

    DownloadUtility objDownloadUtility;
    ConnectServer connectServer;
    Context context;
    boolean isPost;
    int requestCode;
    String url,paylod;
    ProgressDialog pd;
    ResponseBody responseBody;
    boolean progressVisibility=true;
    boolean hidePdAfterConnection=false;
    boolean autoCancelable=true;





    ParsingUtilities parsingUtilities;

    public AsyncStreamParsingUtilities(Context context, boolean isPost, String url, String paylod,
                                       int requestCode, DownloadUtility objDownloadUtility,ParsingUtilities parsingUtilities)
    {

        this.context=context;
        this.url=url;
        this.isPost=isPost;
        this.paylod=paylod;
        connectServer=new ConnectServer();
        this.objDownloadUtility=objDownloadUtility;
        this.requestCode=requestCode;
        this.parsingUtilities=parsingUtilities;
        progressStatus=IDLE;
    }



    public void setHidePdAfterConnection(boolean hidePdAfterConnection) {
        this.hidePdAfterConnection = hidePdAfterConnection;
    }



    public void setProgressDialoaugeVisibility(boolean visibility)
    {
             progressVisibility=visibility;
    }

    public  void hideProgress()
    {

        if(pd!=null)
            pd.hide();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progressVisibility)
        {
            pd = new ProgressDialog(context);

            pd.setMessage("Loading...");

            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AsyncStreamParsingUtilities.this.cancel(true);
                    connectServer.closeConnection();
                }
            });
            pd.setCanceledOnTouchOutside(autoCancelable);
            pd.show();
        }
        progressStatus=PRE_EXECUTE;
    }

    @Override
    protected String doInBackground(Void... params)
    {

        progressStatus=ON_EXECUTE;
        String str="";
        if(isPost)
        {
           try
           {
                 responseBody= connectServer.performPostCallJsonForStream(url, paylod);

             // if(responseBody!=null)
                // str=responseBody.getResponseString();
           }
            catch (Exception e){}
        }
        else
        {
            try
            {
                responseBody= connectServer.getDataSteram(url);

                /*if(hidePdAfterConnection)
                    {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.hide();
                            }
                        });

                    }*/
                parsingUtilities.parseStreamingData(responseBody.stream);


            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {

        super.onPostExecute(s);
        try
        {
            pd.dismiss();
        }catch (Exception e)
        {}
        objDownloadUtility.onComplete(responseBody.getResponseString(),requestCode,responseBody.responseCode);
        progressStatus=AFTER_EXCECUTE;
    }

    public boolean closeConnection()
    {
        try
        {
           connectServer.closeConnection();
        }
        catch (Exception e)
        {}
        return  true;
    }

    public void setAutoCancelable(boolean b) {
        this.autoCancelable=b;
    }
}
