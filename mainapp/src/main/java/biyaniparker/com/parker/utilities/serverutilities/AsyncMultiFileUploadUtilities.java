package biyaniparker.com.parker.utilities.serverutilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.io.File;

import biyaniparker.com.parker.utilities.DownloadUtility;
import biyaniparker.com.parker.utilities.MultifileUploadUtility;

/**
 * Created by bt18 on 08/08/2016.
 */
public class AsyncMultiFileUploadUtilities extends AsyncTask<Void,Void,String>
{
    MultifileUploadUtility objDownloadUtility;
    ConnectServer connectServer;
    Context context;
    boolean isPost;
    int requestCode;
    String url,paylod;
    ProgressDialog pd;
    ResponseMultiFileUpload responseBodies=new ResponseMultiFileUpload();
    File bitmapFile,bitmapFile1;
    public AsyncMultiFileUploadUtilities(Context context, int requestCode, String url,
                                         MultifileUploadUtility objDownloadUtility, File bitmapFile,File bitmapFile1)
    {
        this.context=context;
        this.url=url;
        this.bitmapFile=bitmapFile;
        this.bitmapFile1=bitmapFile1;
        connectServer=new ConnectServer();
        this.objDownloadUtility=objDownloadUtility;
        this.requestCode=requestCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AsyncMultiFileUploadUtilities.this.cancel(true);
                connectServer.closeConnection();
            }
        });
    }

    @Override
    protected String doInBackground(Void... params)
    {

        String str="";

           try
           {

               FileUpload upload=new FileUpload();
               ResponseBody responseBody1= upload.UploadFile(bitmapFile.getAbsolutePath(), url,1);
               FileUpload upload1=new FileUpload();
               ResponseBody responseBody2= upload1.UploadFile(bitmapFile1.getAbsolutePath(), url,1);
             //  responseBody= connectServer.performPostCallJson(url, paylod);
             // if(responseBody!=null)
                // str=responseBody.getResponseString();
               responseBodies.res1=responseBody1;
               responseBodies.res2=responseBody2;

           }
            catch (Exception e){}


        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        pd.dismiss();
        if(responseBodies!=null)
        {
            if(responseBodies.res1.getResponseCode()==200 && responseBodies.res2.getResponseCode()==200) {
                objDownloadUtility.onUploadComplete(responseBodies.res1.getResponseString(), responseBodies.res2.getResponseString(), requestCode, 200);
            }
            else
            {
                objDownloadUtility.onUploadComplete(responseBodies.res1.getResponseString(), responseBodies.res2.getResponseString(), requestCode,0);
            }
        }
        else
        {
            objDownloadUtility.onUploadComplete(responseBodies.res1.getResponseString(), responseBodies.res2.getResponseString(), requestCode,0);
        }
    }


}
