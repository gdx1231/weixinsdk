package com.gdxsoft.weixin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jackylian
 */
public abstract class WxCard {

	protected WxCardBaseInfo m_baseInfo;
	protected org.json.JSONObject m_requestData;
	protected org.json.JSONObject m_data;
	public org.json.JSONObject getData() {
		return m_data;
	}

	/**
	 * 卡券类型。
	 * 
	 * 通用券：GENERAL_COUPON;<br>
	 * 团购券：GROUPON;<br>
	 * 折扣券：DISCOUNT;<br>
	 * 礼品券：GIFT;<br>
	 * 代金券：CASH;<br>
	 * 会员卡：MEMBER_CARD;<br>
	 * 景点门票：SCENIC_TICKET；<br>
	 * 电影票：MOVIE_TICKET；<br>
	 * 飞机票：BOARDING_PASS；<br>
	 * 红包: LUCKY_MONEY；<br>
	 * 会议门票：MEETING_TICKET；<br>
	 */
	protected String m_cardType;

	public String getCardType() {
		return m_cardType;
	}
 
	public WxCard() {
		m_baseInfo = new WxCardBaseInfo();
		m_requestData = new org.json.JSONObject();
	}

	void init(String cardType) {
		m_cardType = cardType;
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("card_type", m_cardType.toUpperCase());
		m_data = new org.json.JSONObject();
		m_data.put("base_info", m_baseInfo.m_data);
		obj.put(m_cardType.toLowerCase(), m_data);
		m_requestData.put("card", obj);
	}

	public org.json.JSONObject getJSONObject() {
		return m_requestData;
	}

	public String toJsonString() {
		return m_requestData.toString();
	}

	public String toString() {
		return toJsonString();
	}

	public WxCardBaseInfo getBaseInfo() {
		return m_baseInfo;
	}

//	public void setBaseInfo(WxCardBaseInfo base_info) {
//		m_baseInfo = base_info;
//	}
}
