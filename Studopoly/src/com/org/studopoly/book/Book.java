
package com.org.studopoly.book;

import java.util.List;

public class Book{
   	private List items;
   	private String kind;
   	private Number totalItems;

 	public List getItems(){
		return this.items;
	}
	public void setItems(List items){
		this.items = items;
	}
 	public String getKind(){
		return this.kind;
	}
	public void setKind(String kind){
		this.kind = kind;
	}
 	public Number getTotalItems(){
		return this.totalItems;
	}
	public void setTotalItems(Number totalItems){
		this.totalItems = totalItems;
	}
}
