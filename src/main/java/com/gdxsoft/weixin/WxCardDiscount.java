package com.gdxsoft.weixin;

import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 折扣券
 * 
 * @author jackylian
 */
public class WxCardDiscount extends WxCard {
	public static WxCardDiscount parse(JSONObject obj) {
		WxCardDiscount g = new WxCardDiscount();
		JSONObject card = obj.getJSONObject("card");
		JSONObject groupon = card.getJSONObject("discount");
		g.m_baseInfo.parse(groupon.getJSONObject("base_info"));

		g.setDiscount(groupon.optInt("discount"));
		return g;
	}
	/**
	 * 折扣券
	 */
	public WxCardDiscount() {
		init("DISCOUNT");
	}

	/**
	 * 折扣券专用，表示打折额度（百分比）。填30 就是七折。(1-99)
	 * 
	 * @param discount
	 */
	public void setDiscount(int discount) {
		m_data.put("discount", discount);
	}

}
