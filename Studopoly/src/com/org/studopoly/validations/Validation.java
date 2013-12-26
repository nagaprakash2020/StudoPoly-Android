package com.org.studopoly.validations;

import java.util.regex.Pattern;

import android.graphics.Color;
import android.widget.EditText;

public class Validation {

	public static final int VALID_TEXT_COLOR=Color.BLACK;
	public static final int INVALID_TEXT_COLOR=Color.RED;
	
	public static boolean isEmailAddress(EditText editText,boolean required)
	{
		
		String regex="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return isValid(editText,regex,required);
	}
	
	public static boolean isPhoneNumber(EditText editText,boolean required){
		String regex="[0-9]{10}";
		return isValid(editText,regex,required);
	}

	public static boolean ispostalCode(EditText editText,boolean required){
		String regex="^\\d{5}(-\\d{4})?$";
		return isValid(editText,regex,required);
	}
	
	public static boolean isPassword(EditText editText,boolean required){
		String regex="[A-Za-z0-9]{6,10}";
		return isValid(editText,regex,required);
	}
	
	private static boolean isValid(EditText editText, String regex,boolean required) {
		boolean validated=true;
		String text=editText.getText().toString().trim();
		boolean hasText=hasText(editText);
		if (required && !hasText) validated = false;
		 if (validated && hasText) 
		 {
		   if (!Pattern.matches(regex, text)) 
		   {
		    editText.setTextColor(INVALID_TEXT_COLOR);
		    validated = false;
		   }
		   else
		   {
			   editText.setTextColor(VALID_TEXT_COLOR);
		   }
		  }
		 return validated;
	}

	public static boolean hasText(EditText editText) {
		boolean validated = true;
		   
		  String text = editText.getText().toString().trim();
		   
		  if (text.length() == 0) {
		   editText.setText(text);
		   validated = false;
		  }
		 
		  return validated;
	}
}
