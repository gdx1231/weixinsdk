package com.gdxsoft.weixin;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 接收事件推送 <br>
 * 
 * 1 关注/取消关注事件<br>
 * 2 扫描带参数二维码事件 <br>
 * 3 上报地理位置事件 <br>
 * 4 自定义菜单事件 <br>
 * 5 点击菜单拉取消息时的事件推送<br>
 * 6 点击菜单跳转链接时的事件推送
 * 
 * @author admin
 *
 */
public class WeiXinMsgEvent extends WeiXinMsgBase {
	// event
	private String Event_; // 事件
	private String EventKey_; // 事件KEY值，qrscene_为前缀，后面为二维码的参数值
	private String Ticket_;// 二维码的ticket，可用来换取二维码图片

	private double Latitude_;// 地理位置纬度
	private double Longitude_;// 地理位置经度
	private double Precision_;// 地理位置精度
	private String CardId_; // 用户获取卡编号

	/**
	 * 用户获取卡券Id，当event = user_get_card
	 * @return
	 */
	public String getCardId() {
		return CardId_;
	}

	/**
	 * 根据Xml初始化对象
	 * 
	 * @param xml
	 */
	public void init(String xml) {
		NodeList nl = super.initBase(xml);
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			this.setValue(n);
		}
	}

	private void setValue(Node n) {
		if (n.getNodeType() != Node.ELEMENT_NODE) {
			return;
		}
		Element ele = (Element) n;
		String val = ele.getTextContent();
		String name = ele.getNodeName();
		if (name.equals("Event")) {
			this.Event_ = val;
		} else if (name.equals("EventKey")) {
			this.EventKey_ = val;
		} else if (name.equals("Ticket")) {
			this.Ticket_ = val;
		} else if (name.equals("Latitude")) {
			this.Latitude_ = Double.parseDouble(val);
		} else if (name.equals("Longitude")) {
			this.Longitude_ = Double.parseDouble(val);
		} else if (name.equals("Precision")) {
			this.Precision_ = Double.parseDouble(val);
		} else if (name.equals("CardId")) {
			this.CardId_ = val;
		}
	}

	/**
	 * 事件 WeiXinMsgEvents 定义
	 * 
	 * @return
	 */
	public String getEvent() {
		return Event_;
	}

	/**
	 * 事件 WeiXinMsgEvents 定义
	 * 
	 * @param event_
	 */
	public void setEvent(String event_) {
		Event_ = event_;
	}

	/**
	 * 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
	 * 
	 * @return
	 */
	public String getEventKey() {
		return EventKey_;
	}

	/**
	 * 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
	 * 
	 * @param eventKey_
	 */
	public void setEventKey(String eventKey_) {
		EventKey_ = eventKey_;
	}

	/**
	 * 二维码的ticket，可用来换取二维码图片
	 * 
	 * @return
	 */
	public String getTicket() {
		return Ticket_;
	}

	/**
	 * 二维码的ticket，可用来换取二维码图片
	 * 
	 * @param ticket_
	 */
	public void setTicket(String ticket_) {
		Ticket_ = ticket_;
	}

	/**
	 * 地理位置纬度
	 * 
	 * @return
	 */
	public double getLatitude() {
		return Latitude_;
	}

	/**
	 * 地理位置纬度
	 * 
	 * @param latitude_
	 */
	public void setLatitude(double latitude_) {
		Latitude_ = latitude_;
	}

	/**
	 * 地理位置经度
	 * 
	 * @return
	 */
	public double getLongitude() {
		return Longitude_;
	}

	/**
	 * 地理位置经度
	 * 
	 * @param longitude_
	 */
	public void setLongitude(double longitude_) {
		Longitude_ = longitude_;
	}

	/**
	 * 地理位置精度
	 * 
	 * @return
	 */
	public double getPrecision() {
		return Precision_;
	}

	/**
	 * 地理位置精度
	 * 
	 * @param precision_
	 */
	public void setPrecision(double precision_) {
		Precision_ = precision_;
	}
}
