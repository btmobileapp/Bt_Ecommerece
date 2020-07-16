package biyaniparker.com.parker.utilities.serverutilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import biyaniparker.com.parker.utilities.CommonUtilities;

public class ConnectServer
{

	/*
	    public String  performPostCallJson(String requestURL, String postDataParams)
	    public String getData(String myurl)
      	public void closeConnection()
	 */

	private final String USER_AGENT = "Mozilla/5.0";

	// HTTP GET request
	public String sendGet(String url) throws Exception
	{

		//String url = "http://www.google.com/search?q=mkyong";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		con.setReadTimeout(20000);
		con.setConnectTimeout(20000);

		//add request header
		con.setRequestProperty("UserBean-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();

	}

	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for(Map.Entry<String, String> entry : params.entrySet())
		{
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}


	public void closeConnection()
	{
		try
		{
			conn.disconnect();
		}
		catch (Exception e){}
	}






	HttpURLConnection conn;
	public ResponseBody performPostCallJson(String requestURL, String postDataParams)
	{

		ResponseBody responseBody=new ResponseBody(0,"");
		URL url;
		String response = "";
		// postDataParams.put("", "");
		try {
			url = new URL(requestURL);

			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(30000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/json");
			conn.setDoInput(true);
			conn.setDoOutput(true);


			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			//writer.
			writer.write(postDataParams);

			writer.flush();
			writer.close();
			os.close();
			int responseCode=conn.getResponseCode();
            responseBody.setResponseCode(responseCode);
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

		responseBody.setResponseString(response);
		return responseBody;
	}




	public ResponseBody performPostCallJsonForStream(String requestURL, String postDataParams)
	{

		ResponseBody responseBody=new ResponseBody(0,"");
		URL url;
		String response = "";
		// postDataParams.put("", "");
		try {
			url = new URL(requestURL);

			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(20000);
			conn.setConnectTimeout(20000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/json");
			conn.setDoInput(true);
			conn.setDoOutput(true);


			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			//writer.
			writer.write(postDataParams);

			writer.flush();
			writer.close();
			os.close();
			int responseCode=conn.getResponseCode();
			responseBody.setResponseCode(responseCode);
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				String line;

				responseBody.stream=    conn.getInputStream();
						//new InputStreamReader(conn.getInputStream());
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

		responseBody.setResponseString(response);
		return responseBody;
	}




	public Bitmap getBitmap(String Url) throws Exception {
		URL url1 = null;
		url1 = new URL(Url);
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.outWidth = 620;
		bfo.outHeight = 350;
		return BitmapFactory.decodeStream(url1.openConnection()
				.getInputStream(), null, bfo);
	}












	//******************************************************************************************************//
	//                         Get Method


	private String contentAsString;
	public ResponseBody getData(String myurl) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 500;
ResponseBody responseBody=new ResponseBody(0,"");
		try
		{
			URL url = new URL(myurl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(20000 /* milliseconds */);
			conn.setConnectTimeout(20000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			responseBody.setResponseCode(response);
			Log.d("TAG", "The response is: " + response);
			is = conn.getInputStream();

			// Convert the InputStream into a string
			contentAsString = getStramSting(is);
			responseBody.setResponseString(contentAsString);
			return responseBody;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		}

		finally
		{

			if (is != null)
			{
				is.close();

			}

			responseBody.setResponseString(contentAsString);

			if(contentAsString==null || contentAsString.equalsIgnoreCase(""))
				return responseBody;
			else
				return responseBody;
		}
	}


	//   get Input Stream method return inputstream

public  int contentLength;
	public InputStream getConnectionInputStream (String myurl) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 500;
		ResponseBody responseBody=new ResponseBody(0,"");
		try
		{
			URL url = new URL(myurl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(20000 /* milliseconds */);
			conn.setConnectTimeout(20000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			responseBody.setResponseCode(response);
			Log.d("TAG", "The response is: " + response);
			is = conn.getInputStream();

			contentLength=conn.getContentLength();
			// Convert the InputStream into a string
			//contentAsString = getStramSting(is);
			//responseBody.setResponseString(contentAsString);
			return is;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		}
		catch ( Exception e)
		{
			e.printStackTrace();
		}
		return null;


	}




	public ResponseBody getDataSteram(String myurl) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 500;
		ResponseBody responseBody=new ResponseBody(0,"");
		try
		{
			URL url = new URL(myurl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(20000 /* milliseconds */);
			conn.setConnectTimeout(20000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			responseBody.setResponseCode(response);
			Log.d("TAG", "The response is: " + response);
			is = conn.getInputStream();
             responseBody.stream=is;
			// Convert the InputStream into a string
		//	contentAsString = getStramSting(is);
			responseBody.setResponseString(contentAsString);
			return responseBody;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{

			if (is != null)
			{
				//is.close();

			}

			responseBody.setResponseString(contentAsString);

			if(contentAsString==null || contentAsString.equalsIgnoreCase(""))
				return responseBody;
			else
				return responseBody;
		}
	}




	public String readIt(InputStream stream, int len) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[len];
		reader.read(buffer);
		return new String(buffer);
	}

	String getStramSting(InputStream stream) throws IllegalStateException,
			IOException
	{


		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		sb.append(reader.readLine() + "\n");
		String line = "0";

		while ((line = reader.readLine()) != null) {

			sb.append(line + "\n");
			// Log.e("log_loginerror1",line.toString());
			// test=line.toString();

		}

		String res = sb.toString();
		stream.close();
		return res;
	}





	private static final char PARAMETER_DELIMITER = '&';
	private static final char PARAMETER_EQUALS_CHAR = '=';
	public static String createQueryStringForParameters(Map<String, String> parameters) {
		StringBuilder parametersAsQueryString = new StringBuilder();
		if (parameters != null) {
			boolean firstParameter = true;

			for (String parameterName : parameters.keySet()) {
				if (!firstParameter) {
					parametersAsQueryString.append(PARAMETER_DELIMITER);
				}

				parametersAsQueryString.append(parameterName)
						.append(PARAMETER_EQUALS_CHAR)
						.append(URLEncoder.encode(
								parameters.get(parameterName)));

				firstParameter = false;
			}
		}
		return parametersAsQueryString.toString();
	}
}