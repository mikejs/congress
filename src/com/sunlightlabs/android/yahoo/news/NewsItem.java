package com.sunlightlabs.android.yahoo.news;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Time;

public class NewsItem {
	public String title, source, displayURL, clickURL, summary;
	public Time timestamp;
	
	public NewsItem(String title, String summary, String source, String displayURL, String clickURL, Time timestamp) {
		this.title = title;
		this.displayURL = displayURL;
		this.clickURL = clickURL;
		this.source = source;
		this.timestamp = timestamp;
		this.summary = summary;
	}
	
	public NewsItem(JSONObject json) {
		try {
			this.title = json.getString("Title");
			this.displayURL = json.getString("Url");
			this.clickURL = json.getString("ClickUrl");
			this.source = json.getString("NewsSource");
			this.summary = json.getString("Summary");
			this.timestamp = new Time();
			long publishDate = json.getLong("PublishDate") * 1000;
			this.timestamp.set(publishDate);
			
		} catch (JSONException e) {
			setDefaults();
		}
	}
	
	private void setDefaults() {
		this.title = "[No article loaded]";
		this.displayURL = "[no URL]";
		this.clickURL = null;
		this.source = "[no source]";
		this.summary = "[no summary]";
		this.timestamp = new Time();
	}
}