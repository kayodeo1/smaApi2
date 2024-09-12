package smaApi;

import java.util.ArrayList;

import auth.kayodeo1.com.NotificationModel;

public class notification{
	private String type;
	private ArrayList<NotificationModel> data;
	public String getType() {
		return type;
	}
	public ArrayList<NotificationModel> getData() {
		return data;
	}
	public void setData(ArrayList<NotificationModel> data) {
		this.data = data;
	}
	
	public void setType(String type) {
		this.type = type;
	}



}