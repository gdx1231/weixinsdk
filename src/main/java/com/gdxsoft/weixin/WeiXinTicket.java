package com.gdxsoft.weixin;

public class WeiXinTicket {
	// {
	// "errcode":0,
	// "errmsg":"ok",
	// "ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA",
	// "expires_in":7200
	// }
	private int errcode_;
	private String errmsg_;
	private int expires_in_;
	private String ticket_;
	private String jsonStr_;
	private long expiresTime_;

	/**
	 * 失效时间
	 * @return
	 */
	public long getExpiresTime() {
		return expiresTime_;
	}

	public int getErrcode() {
		return errcode_;
	}

	public void setErrcode(int errcode_) {
		this.errcode_ = errcode_;
	}

	public String getErrmsg() {
		return errmsg_;
	}

	public void setErrmsg(String errmsg_) {
		this.errmsg_ = errmsg_;
	}

	public int getExpires_in() {
		return expires_in_;
	}

	public void setExpires_in(int expires_in_) {
		this.expires_in_ = expires_in_;
		this.expiresTime_ = System.currentTimeMillis() + expires_in_ * 1000;
	}

	public String getTicket() {
		return ticket_;
	}

	public void setTicket(String ticket_) {
		this.ticket_ = ticket_;
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
