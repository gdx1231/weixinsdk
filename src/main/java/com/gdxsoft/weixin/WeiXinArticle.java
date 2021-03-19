package com.gdxsoft.weixin;

import org.json.JSONException;
import org.json.JSONObject;

public class WeiXinArticle {
	private String title_;// ":"Happy Day",
	private String description_;// ":"Is Really A Happy Day",
	private String url_;// ":"URL",
	private String picurl_;// ":"PIC_URL"

	public WeiXinArticle() {

	}

	/**
	 * 微信新闻
	 * @param title 标题
	 * @param description 描述
	 * @param url 文章地址
	 * @param picurl 图片
	 */
	public WeiXinArticle(String title, String description, String url,
			String picurl) {
		this.title_ = title;
		this.description_ = description;
		this.picurl_ = picurl;
		this.url_ = url;
	}

	public JSONObject toJSON() throws JSONException{
		JSONObject o=new JSONObject();
		o.put("title", title_);
		o.put("description", description_);
		o.put("picurl", picurl_);
		o.put("url", url_);
		
		return o;
	}
	
	public String getTitle() {
		return title_;
	}

	public void setTitle(String title_) {
		this.title_ = title_;
	}

	public String getDescription() {
		return description_;
	}

	public void setDescription(String description_) {
		this.description_ = description_;
	}

	public String getUrl() {
		return url_;
	}

	public void setUrl(String url_) {
		this.url_ = url_;
	}

	public String getPicurl() {
		return picurl_;
	}

	public void setPicurl(String picurl_) {
		this.picurl_ = picurl_;
	}

}
