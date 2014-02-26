package com.org.studopoly.activities;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;

import com.org.studopoly.R;
import com.org.studopoly.Resources;
import com.org.studopoly.R.id;
import com.org.studopoly.R.layout;
import com.org.studopoly.validations.Validation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity 
{

	EditText 			username;
	EditText			password;
	TextView			notRegisered;
	TextView			login_result;
	Button 				login_btn;
	String 				username_entered;
	String				password_entered;
	ProgressDialog 		prog;
	Context 			context;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initialize();

		// Redirect to Register Screen when clicked on "Not Registered Text"
		notRegisered.setOnClickListener(new View.OnClickListener() 
		{
			@Override	
			public void onClick(View v) 
			{
				Intent register_screen = new Intent(getApplicationContext(),Register.class);
				startActivity(register_screen);
			}
		});

		// Method To validate Login
		login_btn.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(save())
				{
				username_entered = username.getText().toString();
				password_entered = password.getText().toString();
				
				//Remove this block once you are able to hit the servers from real device
				Intent home_screen = new Intent(getApplicationContext(),HomeScreen.class);
				startActivity(home_screen);
				
				// Execute the AsyncTask to validate Login.
				//new ValidateLogin().execute();
				}

			}
		});

	}

	protected boolean save() 
	{
		boolean saved=false;
		if(validated())
		{
			saved=true;
		}
		return saved;
	}

	private boolean validated() 
	{
		boolean validated=true;
		if(!Validation.hasText(username))
		{
			username.setError(Resources.TEXT_REQUIRED);
			validated=false;
		}
		if(!Validation.hasText(password))
		{
			password.setError(Resources.TEXT_REQUIRED);
			validated=false;
		}
		return validated;
	}

	/*
	 * Use this method to initialize all the Variables
	 */
	private void initialize() 
	{

		// Get UserName from Edit box of activity_login layout
		username = (EditText) findViewById(R.id.userid);
		// Get Password from Edit box of activity_login layout
		password = (EditText) findViewById(R.id.repassword_register);
		// Get Reference to "Not Registered Yet" text from activity_login layout
		notRegisered = (TextView) findViewById(R.id.link_to_register);
		// Get Reference to Login Button from activity_login layout
		login_btn = (Button) findViewById(R.id.btnLogin);
		context=this;
		prog=new ProgressDialog(context);
		login_result=(TextView) findViewById(R.id.loginError);
		// Hide Login Result Text
		login_result.setVisibility(View.INVISIBLE);
	}

	private class ValidateLogin extends AsyncTask<Void, Void, Integer> 
	{

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 * @return Return Values
		 * status 333- password Don't match 
		 * 200-validated
		 * 250- UserName Doesn't Exist 
		 * 222- Another SQL Exception
		 * 111- (Returned in this method) when Exception Occurs 
		 *  
		 */
		
		@Override
		protected Integer doInBackground(Void... params) 
		{
			try
			{
				DefaultHttpClient httpClient 	=	new DefaultHttpClient();
				StringBuilder     url		 	=	new StringBuilder("http://"+Resources.HOST+":"+Resources.Server_Port+"/StudoPolyServices/user/validateLogin/");
				url.append(username_entered);
				url.append("/");
				url.append(password_entered);
				Log.d("Url",url.toString());
				HttpPost 		  postRequest 	=	new HttpPost(url.toString());
				HttpResponse 	  response 		=	httpClient.execute(postRequest);
				return response.getStatusLine().getStatusCode();
			}
			catch(ConnectTimeoutException e)
			{
				e.printStackTrace();
				return 401;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return 402;
			}
		}
		
		@Override
		protected void onPreExecute() 
		{
			prog.setTitle("Please Wait");
			prog.show();
		}

		@Override
		protected void onPostExecute(Integer result) 
		{
			prog.dismiss();
			if(result==200)
			{
				Intent home_screen = new Intent(getApplicationContext(),HomeScreen.class);
				startActivity(home_screen);
			}
			else if(result==333 || result==250)
			{
				login_result.setVisibility(View.VISIBLE);
				login_result.setText(Resources.INVALID_ACCESS);
			}
			else if(result==401)
			{
				login_result.setVisibility(View.VISIBLE);
				login_result.setText(Resources.NETWORK_ERROR);
			}
			else
			{
				login_result.setVisibility(View.VISIBLE);
				login_result.setText(Resources.UNKNOWN_ERROR);
			}
		}
	}
	
	
}
