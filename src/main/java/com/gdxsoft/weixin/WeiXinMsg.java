package com.gdxsoft.weixin;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gdxsoft.easyweb.utils.UXml;

public class WeiXinMsg {

	private String xml_;

	public WeiXinMsg(String xml) {
		this.xml_ = xml;
	}

	
	 
	
	/**
	 * 检查是否为Event消息
	 * 
	 * @param xml
	 * @return
	 */
	public boolean isEvent() {
		Document doc = UXml.asDocument(xml_);
		NodeList nl = doc.getElementsByTagName("MsgType");

		if (nl.getLength() > 0) {
			Node n1 = nl.item(0);

			String text = n1.getTextContent();
			if (text != null && text.equals("event")) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取事件消息
	 * @return
	 */
	public WeiXinMsgEvent getEvent() {
		WeiXinMsgEvent e = new WeiXinMsgEvent();
		e.init(this.xml_);

		return e;
	}

	/**
	 * 获取普通消息
	 * @return
	 */
	public WeiXinMsgRecv getRecv() {
		WeiXinMsgRecv r = new WeiXinMsgRecv();
		r.init(xml_);
		return r;
	}




	public String getXml() {
		return xml_;
	}


 
}
