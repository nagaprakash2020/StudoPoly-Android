package com.org.studopoly.activities;

import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.gson.Gson;
import com.org.studopoly.NetworkUtil;
import com.org.studopoly.R;
import com.org.studopoly.Resources;
import com.org.studopoly.book.ImageLinks;
import com.org.studopoly.book.VolumeInfo;
import com.org.studopoly.dao.BookAndroid;

public class SellBookDetails extends Activity {

	String		url;
	ImageView 	bookImage;
	EditText	bookTitle;
	EditText	bookAuthors;
	EditText	bookPublisher;
	EditText	bookPublishedDate;
	EditText	price;
	EditText    Comments;
	CheckBox    torn_pages;
	CheckBox    highlighiting;
	SeekBar		quality;
	Button		list_book;
	
	BookAndroid    book;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sell_book_details);
		init();
		
				//url				=	Resources.GOOGLE_BOOK_URL + getIntent().getStringExtra(Resources.ISBN);
				// url = "https://www.googleapis.com/books/v1/volumes?q=isbn:9780596806286";
				url = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+Resources.ISBN;
				Log.d("URL",url);
				Log.d("LOG","Inside SellBookDetails");
				new BookDetails().execute();
				if(NetworkUtil.getConnectivityStatus(this)!=0)
				{
					
				}
				else
				{
					Log.d("Internet","Not Connected to Internet!!!!");
				}
				
				list_book.setOnClickListener(new View.OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						Log.d("ListBook","List Book Button Clicked");
						book.setSellingPrice(Float.parseFloat(price.getText().toString()));
						book.setComments(Comments.getText().toString());
						book.setTornPages(torn_pages.isChecked());
						book.setHighlighting(highlighiting.isChecked());
						// Set the quality from seek bar.
						book.setQuality(2);
						
						Gson gson =new Gson();
						String bookMainJson=gson.toJson(book);
						Log.d("Book Main",bookMainJson);
						new sendBookToServer().execute();
						
					}
				});
				
	}

	private void init() {

		bookImage			=	(ImageView) findViewById(R.id.BookThumbNail);
		bookTitle			=	(EditText) findViewById(R.id.book_title);
		bookAuthors			=	(EditText) findViewById(R.id.book_authors);
		bookPublisher		=	(EditText) findViewById(R.id.book_publisher);
		bookPublishedDate	=	(EditText) findViewById(R.id.book_published_date);
		price				=	(EditText) findViewById(R.id.price);
		Comments			=	(EditText) findViewById(R.id.comments);
		torn_pages			=	(CheckBox) findViewById(R.id.torn_pages);
		highlighiting		=	(CheckBox) findViewById(R.id.highlighting);
		quality				=	(SeekBar)  findViewById(R.id.quality);
		list_book			=	(Button)   findViewById(R.id.list_book_btn);
		
		book				=	new BookAndroid();
		
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

		@Override
		protected String doInBackground(Void... params) 
		{
			HttpClient 	client 		 =	new DefaultHttpClient();
			HttpGet 	request 	 =	new HttpGet(url);
			String		jsonString   =  null;
			try
			{
			HttpResponse  response 	 = 	client.execute(request);
			HttpEntity 	  httpEntity = 	response.getEntity();
						  jsonString = 	EntityUtils.toString(httpEntity);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return jsonString;
		}

		@Override
		protected void onPostExecute(String result) 
		{
			String author="";
			Bitmap mIcon_val = null;
			if(result!=null)
			{
				Gson gson = new Gson();
				try
				{
					/*List<VolumeInfo> Volumes		=	new ArrayList<VolumeInfo>();
					for(int i=0;i<jArray.length();i++)
					{
						JSONObject 		volumeInfo 		= 	jArray.getJSONObject(i).getJSONObject("volumeInfo");
						VolumeInfo 		vInfo			=	gson.fromJson(volumeInfo.toString(), VolumeInfo.class);
						Volumes.add(vInfo);
					}
					 Though we get many books with same volume we generally use the first volume 
					Volumes.get(0).getTitle(); */
					
					JSONObject 		jsonObject		= 	new JSONObject(result);
					JSONArray 		jArray 			= 	jsonObject.getJSONArray("items");
					JSONObject 		volumeInfo 		= 	jArray.getJSONObject(0).getJSONObject("volumeInfo");
					VolumeInfo 		vInfo			=	gson.fromJson(volumeInfo.toString(), VolumeInfo.class);
					
					// Displaying Book Image in BookImage ImageView.
					ImageLinks		imageLinks		=	gson.fromJson(volumeInfo.getJSONObject("imageLinks").toString(), ImageLinks.class);
					URL 			newurl 			= new URL(imageLinks.getThumbnail()); 
									//mIcon_val 		= BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
					
					//Getting Author from vInfo
					for(int i=0;i<vInfo.getAuthors().size();i++)
					{
						author+=vInfo.getAuthors().get(i)+",";
					}
					//Getting ISBN 
					for(int i=0;i<vInfo.getIndustryIdentifiers().size();i++)
					{
						String isbn_10=vInfo.getIndustryIdentifiers().get(i).toString();
						Log.d("ISBN",isbn_10);
					}
					
				    	author = author.substring(0, author.length() - 1);
					// Setting the book object partially
					// Rest will be set when user enters details
				    	
					book.setTitle(vInfo.getTitle());
					book.setSubTitle(vInfo.getSubtitle());
					book.setIsbn(Resources.ISBN);
					book.setAuthor(author);
					book.setPublisher(vInfo.getPublisher());
					book.setPublishedDate(vInfo.getPublishedDate());
					book.setDescription(vInfo.getDescription());
					book.setSelfLink(jArray.getJSONObject(0).getString("selfLink"));
					book.setPageCount(Integer.parseInt(vInfo.getPageCount().toString()));
					book.setImageLink1(imageLinks.getSmallThumbnail());
					book.setImageLink2(imageLinks.getThumbnail());
					book.setPreviewLink(vInfo.getPreviewLink());
					
					
				    				
				    bookImage.setImageBitmap(mIcon_val);
				    bookTitle.setText(book.getTitle());
				    bookAuthors.setText(book.getAuthor());
				    bookPublisher.setText(book.getPublisher());
				    bookPublishedDate.setText(book.getPublishedDate().toString());
				    
				    Log.d("Author",author);
				    Log.d("Book Value","Book details"+gson.toJson(volumeInfo));
				    
				    
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
	
private	class sendBookToServer extends AsyncTask<Void, Void, Integer>
	{

		@Override
		protected Integer doInBackground(Void... params) {
			String 		postUrl		= "http://10.0.2.2:8080/StudoPolyServices/user/listbook";	
			DefaultHttpClient httpClient =	new DefaultHttpClient();
			try
			{
				Gson gson= new Gson();
				HttpPost	  post		 =	new HttpPost(postUrl);
				StringEntity  postingString =new StringEntity(gson.toJson(book));
				post.setEntity(postingString);
				//post.addHeader("content-type", "application/x-www-form-urlencoded");
				post.setHeader("Content-type", "application/json");
				HttpResponse  response 	 = 	httpClient.execute(post);
				Log.d("Error","Success"+response.getStatusLine().getStatusCode());
				return response.getStatusLine().getStatusCode();
			}
			catch(ConnectTimeoutException e)
			{
				e.printStackTrace();
				Log.d("Error","Error 401");
				return 401;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Log.d("Error","Error 402");
				return 402;
			}
		}
		
	}	
}