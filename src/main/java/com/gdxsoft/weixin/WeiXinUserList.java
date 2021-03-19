package com.gdxsoft.weixin;

import java.util.HashMap;

public class WeiXinUserList {
	private int total_ = 0;
	private int count_ = 0;
	private HashMap<String, String> Users_ =new  HashMap<String, String>();
	private String next_openid_;
	public int getCount() {
		return count_;
	}

	public void setCount(int count_) {
		this.count_ = count_;
	}

	

	public WeiXinUserList() {

	}

	public int getTotal() {
		return total_;
	}

	public void setTotal(int total_) {
		this.total_ = total_;
	}

	public HashMap<String, String> getUsers() {
		return Users_;
	}

	public void setUsers(HashMap<String, String> users_) {
		Users_ = users_;
	}

	public String getNextOpenid() {
		return next_openid_;
	}

	public void setNextOpenid(String next_openid_) {
		this.next_openid_ = next_openid_;
	}

}
