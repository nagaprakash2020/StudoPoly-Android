package com.org.studopoly.activities;

import com.org.studopoly.MyBooks;
import com.org.studopoly.R;
import com.org.studopoly.R.id;
import com.org.studopoly.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreen extends Activity implements OnClickListener
{

	Button 		sell_book_btn;
	Button      my_books_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen_activity);
		init();
		
		sell_book_btn.setOnClickListener(this);
		my_books_btn.setOnClickListener(this);
		
		
		
	}
	
	
	private void init() 
	{
		sell_book_btn	=	(Button) findViewById(R.id.scan_bar_code_btn);
		my_books_btn	=	(Button) findViewById(R.id.MyBooks);
	}


	@Override
	public void onClick(View v) 
	{

		switch (v.getId()) 
		{
			case R.id.scan_bar_code_btn:
				// Change this intent to sellBook from sellBookDetails
						Intent 	sell_book_intent  =	new Intent(this,SellBookDetails.class);
						startActivity(sell_book_intent);
						break;
			case R.id.MyBooks:
						Intent  my_book_intent	  = new Intent(this,BooksListed.class);
						startActivity(my_book_intent);
						break;
			default:
					break;
		}
	}


}
