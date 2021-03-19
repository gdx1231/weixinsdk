package com.gdxsoft.weixin;

import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 团购券
 * 
 * @author jackylian
 */
public class WxCardGroupon extends WxCard {

	public static WxCardGroupon parse(JSONObject obj) {
		WxCardGroupon g = new WxCardGroupon();
		JSONObject card = obj.getJSONObject("card");
		JSONObject groupon = card.getJSONObject("groupon");
		g.m_baseInfo.parse(groupon.getJSONObject("base_info"));

		g.setDealDetail(groupon.optString("deal_detail"));

		return g;
	}

	/**
	 * 团购券
	 */
	public WxCardGroupon() {
		init("GROUPON");
	}

	public void setDealDetail(String detail) {
		m_data.put("deal_detail", detail);
	}

}
