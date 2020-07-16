package biyaniparker.com.parker.utilities.serverutilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import biyaniparker.com.parker.bal.ModuleSync;
import biyaniparker.com.parker.utilities.DownloadUtility;

/**
 * Created by bt18 on 08/08/2016.
 */
public class AsyncParsingUtilities extends AsyncTask<Void,Void,String>
{
    DownloadUtility objDownloadUtility;
    ConnectServer connectServer;
    Context context;
    boolean isPost;
    int requestCode;
    String url,paylod;
    ProgressDialog pd;
    ResponseBody responseBody;
    boolean progressVisibility=true;

    public AsyncParsingUtilities(Context context, boolean isPost, String url, String paylod,
                                 int requestCode, DownloadUtility objDownloadUtility)
    {
        this.context=context;
        this.url=url;
        this.isPost=isPost;
        this.paylod=paylod;
        connectServer=new ConnectServer();
        this.objDownloadUtility=objDownloadUtility;
        this.requestCode=requestCode;
    }

    public void setProgressDialoaugeVisibility(boolean visibility)
    {
             progressVisibility=visibility;
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
                    AsyncParsingUtilities.this.cancel(true);
                    connectServer.closeConnection();
                }
            });


            pd.show();
        }
    }

    @Override
    protected String doInBackground(Void... params)
    {

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

                ModuleSync sync=new ModuleSync(context);
                        //(ModuleSync) objDownloadUtility;
                sync.parseStream(responseBody.stream);


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
    }


}
