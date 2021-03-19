package com.gdxsoft.weixin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gdxsoft.easyweb.utils.UXml;

public class WeiXinMsgBase {

	private String ToUserName_;// 开发者微信号
	private String FromUserName_;// 发送方帐号（一个OpenID）
	private int CreateTime_;// 消息创建时间 （整型）
	private long MsgId_;// 消息id，64位整型
	private String MsgType_;// 1 文本消息 2 图片消息 3 语音消息 4 视频消息 5 小视频消息6 地理位置消息 7
							// 链接消息

	private String Xml_;// 原始数据

	/**
	 * 根据Xml初始化对象
	 * 
	 * @param xml
	 */
	public NodeList initBase(String xml) {
		this.Xml_ = xml;
		Document doc = UXml.asDocument(xml);
		NodeList nl = doc.getFirstChild().getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			this.setValueBase(n);
		}

		return nl;
	}

	protected void setValueBase(Node n) {
		if (n.getNodeType() != Node.ELEMENT_NODE) {
			return;
		}
		Element ele = (Element) n;
		String val = ele.getTextContent();
		String name = ele.getNodeName();
		if (name.equals("ToUserName")) {
			this.ToUserName_ = val;
		} else if (name.equals("FromUserName")) {
			this.FromUserName_ = val;
		} else if (name.equals("CreateTime")) {
			this.CreateTime_ = Integer.parseInt(val);
		} else if (name.equals("MsgId")) {
			this.MsgId_ = Long.parseLong(val);
		} else if (name.equals("MsgType")) {
			this.MsgType_ = val;
		}
	}

	/**
	 * 接收方微信号
	 * 
	 * @return
	 */
	public String getToUserName() {
		return ToUserName_;
	}

	/**
	 * 接收方微信号
	 * 
	 * @param toUserName_
	 */
	public void setToUserName(String toUserName_) {
		ToUserName_ = toUserName_;
	}

	/**
	 * 发送方微信号，若为普通用户，则是一个OpenID
	 * 
	 * @return
	 */
	public String getFromUserName() {
		return FromUserName_;
	}

	/**
	 * 发送方微信号，若为普通用户，则是一个OpenID
	 * 
	 * @param fromUserName_
	 */
	public void setFromUserName(String fromUserName_) {
		FromUserName_ = fromUserName_;
	}

	/**
	 * 消息创建时间(秒)
	 * 
	 * @return
	 */
	public int getCreateTime() {
		return CreateTime_;
	}

	/**
	 * 消息创建时间(秒)
	 * 
	 * @param createTime_
	 *            (秒)
	 */
	public void setCreateTime(int createTime_) {
		CreateTime_ = createTime_;
	}

	/**
	 * 消息id，64位整型
	 * 
	 * @return
	 */
	public long getMsgId() {
		return MsgId_;
	}

	/**
	 * 消息id，64位整型
	 * 
	 * @param msgId_
	 */
	public void setMsgId(long msgId_) {
		MsgId_ = msgId_;
	}

	/**
	 * 获取原始数据
	 * 
	 * @return
	 */
	public String getXml() {
		return Xml_;
	}

	/**
	 * 消息类型 WeiXinMsgType中定义
	 * 
	 * @return
	 */
	public String getMsgType() {
		return MsgType_;
	}

	/**
	 * 消息类型 WeiXinMsgType中定义
	 * 
	 * @param msgType_
	 */
	public void setMsgType(String msgType_) {
		MsgType_ = msgType_;
	}

}
