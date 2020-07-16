package biyaniparker.com.parker.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;

import biyaniparker.com.parker.utilities.serverutilities.FileUpload;
import biyaniparker.com.parker.utilities.serverutilities.ResponseBody;



/**
 * Created by bt on 01/28/2017.
 */
public class AsyncFileUpload extends AsyncTask
{

    Context context;
    File file;
    ProgressDialog pd;
    ResponseBody responseBody;
    DownloadUtility downloadUtility;
    int requestCode;
    String url;
    public AsyncFileUpload(Context context, File file,String url, int requestCode, DownloadUtility downloadUtility)
    {
        this.context=context;
        this.file=file;
        this.downloadUtility=downloadUtility;
        this.requestCode=requestCode;
        this.url=url;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(context);
        pd.setMessage("Uploading ...");

        pd.show();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        FileUpload fileUpload=new FileUpload();
        try
        {
            long l=0l;
            responseBody=fileUpload.UploadFile(file.getAbsolutePath(),url,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        pd.dismiss();
        downloadUtility.onComplete(responseBody.responseString,requestCode,responseBody.responseCode);
    }
}
