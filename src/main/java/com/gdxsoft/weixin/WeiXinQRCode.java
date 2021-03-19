package com.gdxsoft.weixin;

public class WeiXinQRCode {
	private int expire_seconds_;// 二维码的有效时间，以秒为单位。最大不超过1800。
	private String url_;// 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
	private String ticket_;// 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
	private String jsonStr_;

	/**
	 * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码
	 * 
	 * @return
	 */
	public String getTicket() {
		return ticket_;
	}

	/**
	 * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码
	 * 
	 * @param ticket_
	 */
	public void setTicket(String ticket_) {
		this.ticket_ = ticket_;
	}

	public int getExpireSeconds() {
		return expire_seconds_;
	}

	public void setExpireSeconds(int expire_seconds_) {
		this.expire_seconds_ = expire_seconds_;
	}

	public String getUrl() {
		return url_;
	}

	public void setUrl(String url_) {
		this.url_ = url_;
	}

	public String getJsonStr() {
		return jsonStr_;
	}

	public void setJsonStr(String jsonStr_) {
		this.jsonStr_ = jsonStr_;
	}

	public String toString() {
		return this.jsonStr_;
	}
}
