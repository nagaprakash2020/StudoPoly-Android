package com.org.studopoly;

import com.org.studopoly.sellbook.SellBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreen extends Activity implements OnClickListener
{

	Button 		sell_book_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen_activity);
		init();
		
		sell_book_btn.setOnClickListener(this);
		
		
		
		
	}
	
	
	private void init() 
	{
		sell_book_btn		=		(Button) findViewById(R.id.scan_bar_code_btn);
		
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.scan_bar_code_btn:
			
						Intent 			sell_book_intent		=	new Intent(this,SellBook.class);
						startActivity(sell_book_intent);
						break;

		default:
			break;
		}
	}


}
