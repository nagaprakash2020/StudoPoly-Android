
package com.org.studopoly.book;

import java.util.List;

public class SaleInfo{
   	private String country;
   	private boolean isEbook;
   	private String saleability;

 	public String getCountry(){
		return this.country;
	}
	public void setCountry(String country){
		this.country = country;
	}
 	public boolean getIsEbook(){
		return this.isEbook;
	}
	public void setIsEbook(boolean isEbook){
		this.isEbook = isEbook;
	}
 	public String getSaleability(){
		return this.saleability;
	}
	public void setSaleability(String saleability){
		this.saleability = saleability;
	}
}
