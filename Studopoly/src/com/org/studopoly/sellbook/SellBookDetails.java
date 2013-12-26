package com.org.studopoly.sellbook;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.org.studopoly.NetworkUtil;
import com.org.studopoly.R;
import com.org.studopoly.Resources;
import com.org.studopoly.book.Book;
import com.org.studopoly.book.ImageLinks;
import com.org.studopoly.book.VolumeInfo;

public class SellBookDetails extends Activity {

	String	url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sell_book_details);
		
				url				=	Resources.GOOGLE_BOOK_URL + getIntent().getStringExtra(Resources.ISBN);
				Log.d("final url",url);
				if(NetworkUtil.getConnectivityStatus(this)!=0)
				{
					Log.d("Internet Connection",NetworkUtil.getConnectivityStatusString(this));
					//TODO get book details using Async Task.
					new BookDetails().execute();
				}
				else
				{
					Log.d("Internet","Not Connected to Internet!!!!");
				}
	}

	
	
	
	public boolean isConnectedToInternet() {
    	ConnectivityManager connectManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

    	NetworkInfo mobile = connectManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    	NetworkInfo wifi   = connectManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI  );

    	// Return true if connected, either in 3G or wi-fi
    	return ((mobile != null && mobile.getState() == NetworkInfo.State.CONNECTED) || 
    	        (wifi   != null && wifi.getState()   == NetworkInfo.State.CONNECTED)   );
	}
	
	
	private class BookDetails extends AsyncTask<Void, Void, String>
	{

		//String url="http://www.google.com";
		@Override
		protected String doInBackground(Void... params) 
		{
			HttpClient 			client 		=	new DefaultHttpClient();
			HttpGet 			request 	=	new HttpGet(url);
			Log.d("final url",url);
			try
			{
			HttpResponse 		response 	= 	client.execute(request);
			HttpEntity 			httpEntity 	= 	response.getEntity();
			String 				jsonString 	= 	EntityUtils.toString(httpEntity);
			return jsonString;
			}
			catch(ClientProtocolException e)
			{
				e.printStackTrace();
				return null;
			}
			catch(HttpHostConnectException e)
			{
				e.printStackTrace();
				return Resources.NETWORK_ERROR;
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return null;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) 
		{
			String author="";
			if(result!=null)
			{
				Gson gson = new Gson();
				try
				{
					JSONObject 		jsonObject		= 	new JSONObject(result);
					JSONArray 		jArray 			= 	jsonObject.getJSONArray("items");
					JSONObject 		volumeInfo 		= 	jArray.getJSONObject(0).getJSONObject("volumeInfo");
					VolumeInfo 		vInfo			=	gson.fromJson(volumeInfo.toString(), VolumeInfo.class);
					ImageLinks		imageLinks		=	gson.fromJson(volumeInfo.getJSONObject("imageLinks").toString(), ImageLinks.class);
					Log.d("ImageLinks Thumbnail",imageLinks.getThumbnail());
					for(int i=0;i<vInfo.getAuthors().size();i++)
					{
						author+=vInfo.getAuthors().get(i)+",";
					}
				    				author 			= 	author.substring(0, author.length() - 1);
				    Log.d("Author",author);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			if(result==Resources.NETWORK_ERROR)
			{
				Log.d("Netowrk Error","Unable to Connect to Internet");
			}
			
		}
		
	}
	
}
