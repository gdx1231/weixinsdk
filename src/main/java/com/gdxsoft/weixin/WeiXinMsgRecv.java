package com.gdxsoft.weixin;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WeiXinMsgRecv extends WeiXinMsgBase {

	private String Content_;// 文本消息内容

	// image
	private String PicUrl_;// 图片链接
	private String MediaId_;// 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。

	private String Format_;// 语音格式，如amr，speex等

	private String ThumbMediaId_;// 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。

	// location
	private double Location_X_; // 地理位置维度
	private double Location_Y_; // 地理位置经度
	private int Scale_; // 地图缩放大小
	private String Label_; // 地理位置信息

	// link
	private String Title_; // 消息标题
	private String Description_; // 消息描述
	private String Url_; // 消息链接

 

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
		if (name.equals("Content")) {
			this.Content_ = val;
		} else if (name.equals("PicUrl")) {
			this.PicUrl_ = val;
		} else if (name.equals("MediaId")) {
			this.MediaId_ = val;
		} else if (name.equals("Format")) {
			this.Format_ = val;
		} else if (name.equals("Location_X")) {
			this.Location_X_ = Double.parseDouble(val);
		} else if (name.equals("Location_Y")) {
			this.Location_Y_ = Double.parseDouble(val);
		} else if (name.equals("Scale")) {
			this.Scale_ = Integer.parseInt(val);
		} else if (name.equals("Label")) {
			this.Label_ = val;
		} else if (name.equals("Title")) {
			this.Title_ = val;
		} else if (name.equals("Description")) {
			this.Description_ = val;
		} else if (name.equals("Url")) {
			this.Url_ = val;
		}
	}

	/**
	 * 文本消息内容(type=text)
	 * 
	 * @return
	 */
	public String getContent() {
		return Content_;
	}

	/**
	 * 文本消息内容(type=text)
	 * 
	 * @param content_
	 */
	public void setContent(String content_) {
		Content_ = content_;
	}

	/**
	 * 图片链接 (type=image)
	 * 
	 * @return
	 */
	public String getPicUrl() {
		return PicUrl_;
	}

	/**
	 * 图片链接 (type=image)
	 * 
	 * @param picUrl_
	 */
	public void setPicUrl(String picUrl_) {
		PicUrl_ = picUrl_;
	}

	/**
	 * 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。(type=image/voice/video/shortvideo)
	 * 
	 * @return
	 */
	public String getMediaId() {
		return MediaId_;
	}

	/**
	 * 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。(type=image/voice/video/shortvideo)
	 * 
	 * @param mediaId_
	 */
	public void setMediaId(String mediaId_) {
		MediaId_ = mediaId_;
	}

	/**
	 * 语音格式，如amr，speex等 (type=voice)
	 * 
	 * @return
	 */
	public String getFormat() {
		return Format_;
	}

	/**
	 * 语音格式，如amr，speex等 (type=voice)
	 * 
	 * @param format_
	 */
	public void setFormat(String format_) {
		Format_ = format_;
	}

	/**
	 * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。 (type=video/shortvideo)
	 * 
	 * @return
	 */
	public String getThumbMediaId() {
		return ThumbMediaId_;
	}

	/**
	 * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。 (type=video/shortvideo)
	 * 
	 * @param thumbMediaId_
	 */
	public void setThumbMediaId(String thumbMediaId_) {
		ThumbMediaId_ = thumbMediaId_;
	}

	/**
	 * 地理位置维度(type=location)
	 * 
	 * @return
	 */
	public double getLocationX() {
		return Location_X_;
	}

	/**
	 * 地理位置维度(type=location)
	 * 
	 * @param location_X_
	 */
	public void setLocationX(double location_X_) {
		Location_X_ = location_X_;
	}

	/**
	 * 地理位置经度(type=location)
	 * 
	 * @return
	 */
	public double getLocationY() {
		return Location_Y_;
	}

	/**
	 * 地理位置经度(type=location)
	 * 
	 * @param location_Y_
	 */
	public void setLocationY(double location_Y_) {
		Location_Y_ = location_Y_;
	}

	/**
	 * 地图缩放大小(type=location)
	 * 
	 * @return
	 */
	public int getScale() {
		return Scale_;
	}

	/**
	 * 地图缩放大小(type=location)
	 * 
	 * @param scale_
	 */
	public void setScale(int scale_) {
		Scale_ = scale_;
	}

	/**
	 * 地理位置信息 (type=location)
	 * 
	 * @return
	 */
	public String getLabel() {
		return Label_;
	}

	/**
	 * 地理位置信息 (type=location)
	 * 
	 * @param label_
	 */
	public void setLabel(String label_) {
		Label_ = label_;
	}

	/**
	 * 消息标题 type=link)
	 * 
	 * @return
	 */
	public String getTitle() {
		return Title_;
	}

	/**
	 * 消息标题 (type=link)
	 * 
	 * @param title_
	 */
	public void setTitle(String title_) {
		Title_ = title_;
	}

	/**
	 * 消息描述 (type=link)
	 * 
	 * @return
	 */
	public String getDescription() {
		return Description_;
	}

	/**
	 * 消息描述 (type=link)
	 * 
	 * @param description_
	 */
	public void setDescription(String description_) {
		Description_ = description_;
	}

	/**
	 * 消息链接 (type=link)
	 * 
	 * @return
	 */
	public String getUrl() {
		return Url_;
	}

	/**
	 * 消息链接 (type=link)
	 * 
	 * @param url_
	 */
	public void setUrl(String url_) {
		Url_ = url_;
	}

}
