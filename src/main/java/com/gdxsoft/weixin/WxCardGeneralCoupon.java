package com.gdxsoft.weixin;

import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 通用券
 * 
 * @author jackylian
 */
public class WxCardGeneralCoupon extends WxCard {
	
	public static WxCardGeneralCoupon parse(JSONObject obj) {
		WxCardGeneralCoupon g = new WxCardGeneralCoupon();
		JSONObject card = obj.getJSONObject("card");
		JSONObject groupon = card.getJSONObject("GENERAL_COUPON".toLowerCase());
		g.m_baseInfo.parse(groupon.getJSONObject("base_info"));

		g.setDefaultDetail(groupon.optString("default_detail"));

		return g;
	}
	
	/**
	 * 通用券
	 */
	public WxCardGeneralCoupon() {
		init("GENERAL_COUPON");
	}

	public void setDefaultDetail(String detail) {
		m_data.put("default_detail", detail);
	}

	public String getDefaultDetail() {
		return m_data.optString("default_detail");
	}
}
