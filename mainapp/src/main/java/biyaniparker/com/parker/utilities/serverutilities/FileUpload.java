package biyaniparker.com.parker.utilities.serverutilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FileUpload
{
	private String lineEnd="\n";
	private String twoHyphens="--";
	private String boundary="==================================";
    HttpURLConnection conn = null;


    public ResponseBody UploadFile(String filename,String wcfurl ,long UserId) throws IOException
    {  String response="";
        int responseCode=0;
               try
               {
                URL url=null;
                DataOutputStream dos = null;

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = Integer.MAX_VALUE;
                String responseFromServer = "";
                
                String nm="1";
                try
                {
                	nm=filename.split("/")[filename.split("/").length-1];
                	
                	
                }
                catch(Exception e)
                {
                	
                }
                
                url = new URL(wcfurl);
                		//("http://10.66.51.241/mywcf/Service.svc/Service/uploadMyDoc");               

                try
                {
                 //------------------ CLIENT REQUEST
                FileInputStream fileInputStream = new FileInputStream(new File(filename) );

                 // Open a HTTP connection to the URL
                 conn = (HttpURLConnection) url.openConnection();
                 // Allow Inputs
                 conn.setDoInput(true);
                 // Allow Outputs
                 conn.setDoOutput(true);
                 // Don't use a cached copy.
                 conn.setUseCaches(false);
                 // Use a post method.
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("Content-Type", "application/stream");
                 conn.setRequestProperty("filename", nm);
                 conn.setRequestProperty("UserId", UserId+"");


                 dos = new DataOutputStream( conn.getOutputStream() );
              //   dos.writeBytes(twoHyphens + boundary + lineEnd);
                 //dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
               //  dos.writeBytes(lineEnd);
                 // create a buffer of maximum size
                 bytesAvailable = fileInputStream.available();
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
                 // read file and write it into form...
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                 while (bytesRead > 0)
                 {
                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                 }
                 // send multipart form data necesssary after file data...
                 dos.writeBytes(lineEnd);

                 // close streams
                 Log.e("Debug",twoHyphens + boundary + twoHyphens + lineEnd);
                 fileInputStream.close();
                 dos.flush();
                 dos.close();
                }
                catch (MalformedURLException ex)
                {
                     Log.e("Debug", "error: " + ex.getMessage(), ex);
                }
                catch (IOException ioe)
                {
                     Log.e("Debug", "error: " + ioe.getMessage(), ioe);
                }
                //------------------ read the SERVER RESPONSE
         responseCode=conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        }
        else
        {
            response="";

                   throw new Exception(responseCode+"");
        }
    } catch (Exception e)
    {
        e.printStackTrace();
    }

        ResponseBody body=new ResponseBody(responseCode,response);
    return body;
}
    public void cancleUpload()
    {
        conn.disconnect();
    }
/*
    public String uploadFile(String rid, String filename, String wcfurl, String UserId, String TaskId, String desc, long remindartiming, int sendmsg) throws IOException
    {  String response="";

        try
        {
            URL url=null;

            DataOutputStream dos = null;
            DataInputStream inStream = null;
            String existingFileName= null;

            existingFileName=    "/mnt/sdcard/"+rid+".doc";
            int bytesRead, bytesAvailable=1024, bufferSize;
            byte[] buffer;
            int maxBufferSize = Integer.MAX_VALUE;
            String responseFromServer = "";

            String nm="1";
            try
            {
                nm=filename.split("/")[filename.split("/").length-1];
            }
            catch(Exception e)
            {
                String str=e.toString();
                Log.d("CMR",str);

            }

            url = new URL(wcfurl);
            //("http://10.66.51.241/mywcf/Service.svc/Service/uploadMyDoc");

            try
            {
                //------------------ CLIENT REQUEST
                FileInputStream fileInputStream = null;
                try
                {
                    fileInputStream= new FileInputStream(new File(filename));
                }
                catch (Exception e){
                    String str=e.toString();
                    Log.d("CMR",str);
                }

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);
                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "application/stream");
                conn.setRequestProperty("UserId", UserId);
                conn.setRequestProperty("TaskId", TaskId);
                conn.setRequestProperty("desc", desc);
                conn.setRequestProperty("remtime", remindartiming+"");
                conn.setRequestProperty("sendmsg", sendmsg+"");
                try
                {
                    conn.setRequestProperty("filename", filename.split("/")[ filename.split("/").length-1]);
                }
                catch (Exception e)
                {
                    String str=e.toString();
                    Log.d("CMR",str);
                }

                dos = new DataOutputStream( conn.getOutputStream() );

                // create a buffer of maximum size
                try {
                    bytesAvailable = fileInputStream.available();
                }
                catch (Exception e){
                    String str=e.toString();
                    Log.d("CMR",str);
                }
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                try {
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    // send multipart form data necesssary after file data...
                }catch (Exception e){
                    String str=e.toString();
                    Log.d("CMR",str);
                }
                dos.writeBytes(lineEnd);

                // close streams
                Log.e("Debug",twoHyphens + boundary + twoHyphens + lineEnd);
                try {
                    fileInputStream.close();
                }
                catch (Exception e){
                    String str=e.toString();
                    Log.d("CMR",str);
                }
                dos.flush();
                dos.close();
            }
            catch (MalformedURLException ex)
            {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
            catch (IOException ioe) {
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
            }
            //------------------ read the SERVER RESPONSE
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else
            {
                response="";

                throw new Exception(responseCode+"");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }*/
}
