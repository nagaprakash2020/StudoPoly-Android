package com.org.studopoly.sellbook;

import com.org.studopoly.R;
import com.org.studopoly.Resources;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SellBook extends Activity {

	Button		scan_barCode_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_book);
		
		init();
		
		// Scan the Bar code after cliking Scan Button.
		scan_barCode_btn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				try 
				{
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					startActivityForResult(intent, 0);
				}
				catch (Exception e) 
					{
					Toast.makeText(getApplicationContext(), "ERROR:" + e, Toast.LENGTH_SHORT).show();
					}
			}
		});
	}
		
	public void onActivityResult(int requestCode, int resultCode, Intent intent) 
	{
    	if (requestCode == 0) 
    	{
    		if (resultCode == RESULT_OK) 
    		{
    			String 	ISBN_Format		=	intent.getStringExtra("SCAN_RESULT_FORMAT");
    			String 	ISBN			=	intent.getStringExtra("SCAN_RESULT");
    			Log.d("ISBN Number",ISBN);
    			Log.d("ISBN Format",ISBN_Format);
    			Intent 	sellBookDetails =	new Intent(this,SellBookDetails.class);
    			sellBookDetails.putExtra(Resources.ISBN, intent.getStringExtra("SCAN_RESULT"));
    			startActivity(sellBookDetails);
    		}
    		
    		else if (resultCode == RESULT_CANCELED) 
    		{
    		Log.d("Response","Result Canceled");
    		}
    		else
    		{
    			Log.d("Unknown Error","Unknown Error");
    		}
    	}
    }

	private void init() 
	{
		scan_barCode_btn			=	(Button) findViewById(R.id.scan_bar_code_btn);
		
	}

}
