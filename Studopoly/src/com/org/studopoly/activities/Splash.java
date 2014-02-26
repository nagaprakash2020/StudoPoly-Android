package com.org.studopoly.activities;

import com.org.studopoly.R;
import com.org.studopoly.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread t=new Thread()
		{
			public void run()
			{
				try
				{
				sleep(2500);
				Intent login_screen=new Intent(getApplicationContext(),Login.class);
				startActivity(login_screen);
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				finally
				{
					finish();
				}
			}
			
		};
		t.start();
	}

}
