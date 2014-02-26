package com.org.studopoly.activities;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.org.studopoly.R;
import com.org.studopoly.R.id;
import com.org.studopoly.R.layout;
import com.org.studopoly.validations.Validation;

public class Register extends Activity {

	EditText first_name,last_name, password, email, username,rePassword,phonenumber;
	Button register_btn;
	ProgressDialog prog;
	Context context;
	Spinner college_name;
	String first_name_text,last_name_text, password_text, email_text, username_text,
			college_name_text,phonenumber_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		initialize();

		// On Register Click
		register_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Check if Forms Are Validated before Performing other Operations
				if (save()) {
					first_name_text = first_name.getText().toString();
					last_name_text=last_name.getText().toString();
					password_text = password.getText().toString();
					email_text = email.getText().toString();
					username_text = username.getText().toString();
					//Remove - between Numbers
					phonenumber_text=phonenumber.getText().toString().replaceAll("[^0-9]", "");
					// Remove All spaces between Strings
					college_name_text = college_name.getSelectedItem()
							.toString().replaceAll("\\s+", "");
					// Execute the AsyncTask to Register User
					new ValidateRegister().execute();
				}
			}
		});
	}

	// Method to check if validated returns true
	protected boolean save() {
		boolean saved=true;
		if(validated()){
			
		}else{
			saved=false;
		}
		return saved;
	}

	private boolean validated() {
		boolean validated=true;
		if(!Validation.hasText(first_name)){
			first_name.setError("Name Required");
			validated=false;
		}
		if(!Validation.hasText(username)){
			username.setError("Username Required");
			validated=false;
		}
		if(!Validation.isEmailAddress(email, true)){
			email.setError("Email Format xxx@xxx.edu");
			validated=false;
		}
		if(!Validation.isPassword(password, true)){
			password.setError("Minimum 6 characters No special Characters Allowed");
			validated=false;
		}
		if(!Validation.isPhoneNumber(phonenumber, true)){
			phonenumber.setError("XXX-XXX-XXXX");
			validated=false;
		}
		// Check if both passwords are same
		if(!password.getText().toString().equals(rePassword.getText().toString())){
			rePassword.setError("Password Don't Match");
			validated=false;
		}
		return validated;
	}

	// Method to initialize all variables
	private void initialize() {

		username = (EditText) findViewById(R.id.user_register);
		first_name = (EditText) findViewById(R.id.first_name_register);
		last_name=(EditText) findViewById(R.id.last_name_register);
		password = (EditText) findViewById(R.id.password_register);
		email = (EditText) findViewById(R.id.email_register);
		register_btn = (Button) findViewById(R.id.btnRegister);
		college_name = (Spinner) findViewById(R.id.school_select);
		rePassword=(EditText) findViewById(R.id.repassword_register);
		phonenumber=(EditText) findViewById(R.id.phonenumber_register);
		context = this;
		prog = new ProgressDialog(context);

	}

	/*
	 * 
	 * @return status Codes 111- Exception occured 200- user Created 222-
	 * Duplicate User 221- Exception other thatn Duplicate User
	 */
	private class ValidateRegister extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				StringBuilder url = new StringBuilder(
						"http://192.168.1.76:8080/StudoPolyServices/user/registration/");
				url.append(username_text);
				url.append("/");
				url.append(password_text);
				url.append("/");
				url.append(email_text);
				url.append("/");
				url.append(college_name_text);
				url.append("/");
				url.append(first_name_text);
				url.append("/");
				url.append(last_name_text);
				url.append("/");
				url.append(phonenumber_text);
				Log.d("Final URL",url.toString());
				HttpPost postRequest = new HttpPost(url.toString());
				HttpResponse response = httpClient.execute(postRequest);
				return response.getStatusLine().getStatusCode();
			} catch (Exception e) {
				e.printStackTrace();
				return 111;
			}
		}

		@Override
		protected void onPreExecute() {
			prog.setTitle("Please Wait");
			prog.show();
		}

		@Override
		protected void onPostExecute(Integer result) {
			prog.dismiss();
			if (result == 200) {
				Toast.makeText(context, "Registed SuccessFully",
						Toast.LENGTH_LONG).show();
				Intent login_screen = new Intent(getApplicationContext(),
						Login.class);
				startActivity(login_screen);
			} else if (result == 222) {
				Toast.makeText(context, "Choose Different Username",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "Unable to Register", Toast.LENGTH_LONG)
						.show();
			}

		}

	}
}
