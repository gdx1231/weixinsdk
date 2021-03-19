package com.gdxsoft.weixin;

import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 礼品券
 * 
 * @author jackylian
 */
public class WxCardGift extends WxCard {
	
	public static WxCardGift parse(JSONObject obj) {
		WxCardGift g = new WxCardGift();
		JSONObject card = obj.getJSONObject("card");
		JSONObject groupon = card.getJSONObject("gift");
		g.m_baseInfo.parse(groupon.getJSONObject("base_info"));

		g.setGift(groupon.optString("gift"));

		return g;
	}
	/**
	 * 礼品券
	 */
	public WxCardGift() {
		init("GIFT");
	}

	/**
	 * 礼品券专用，表示礼品名字
	 * @param gift
	 */
	public void setGift(String gift) {
		m_data.put("gift", gift);
	}

	/**
	 * 礼品券专用，表示礼品名字
	 * @return
	 */
	public String getGift() {
		return m_data.optString("gift");
	}
}
