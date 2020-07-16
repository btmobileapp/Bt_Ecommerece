package biyaniparker.com.parker.utilities.serverutilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.io.File;

import biyaniparker.com.parker.utilities.CommonUtilities;
import biyaniparker.com.parker.utilities.DownloadUtility;

/**
 * Created by bt18 on 08/08/2016.
 */
public class AsyncFileUploadUtilities extends AsyncTask<Void,Void,String>
{
    DownloadUtility objDownloadUtility;
    ConnectServer connectServer;
    Context context;
    boolean isPost;
    int requestCode;
    String url,paylod;
    ProgressDialog pd;
    ResponseBody responseBody;
    File bitmapFile;
    public AsyncFileUploadUtilities(Context context, int requestCode,String url,
                                    DownloadUtility objDownloadUtility,File bitmapFile)
    {
        this.context=context;
        this.url=url;
        this.bitmapFile=bitmapFile;
        connectServer=new ConnectServer();
        this.objDownloadUtility=objDownloadUtility;
        this.requestCode=requestCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AsyncFileUploadUtilities.this.cancel(true);
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
              responseBody= upload.UploadFile(bitmapFile.getAbsolutePath(), url,1);

             //  responseBody= connectServer.performPostCallJson(url, paylod);
             // if(responseBody!=null)
                // str=responseBody.getResponseString();
           }
            catch (Exception e){}


        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        pd.dismiss();
        if(responseBody!=null) {
            objDownloadUtility.onComplete(responseBody.getResponseString(), requestCode, responseBody.responseCode);
        }
    }


}
