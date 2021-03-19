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
public class WxCardCash extends WxCard {
	public static WxCardCash parse(JSONObject obj) {
		WxCardCash g = new WxCardCash();
		JSONObject card = obj.getJSONObject("card");
		JSONObject groupon = card.getJSONObject("cash");
		g.m_baseInfo.parse(groupon.getJSONObject("base_info"));

		g.setLeastCost(groupon.optInt("least_cost"));
		g.setReduceCost(groupon.optInt("reduce_cost"));
		return g;
	}

	/**
	 * 代金券
	 */
	public WxCardCash() {
		init("CASH");
	}

	/**
	 * 代金券专用，表示起用金额（单位为分）,可不填
	 * 
	 * @param least_cost
	 */
	public void setLeastCost(int least_cost) {
		m_data.put("least_cost", least_cost);
	}

	/**
	 * 代金券专用，表示减免金额（单位为分）,必填
	 * 
	 * @param detail
	 */
	public void setReduceCost(int reduce_cost) {
		m_data.put("reduce_cost", reduce_cost);
	}
}
