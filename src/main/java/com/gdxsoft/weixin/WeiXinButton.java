package com.gdxsoft.weixin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 自定义菜单创建接口
 * 
 * 1、自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。<br>
 * 2、一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
 * 3、创建自定义菜单后，由于微信客户端缓存，需要24小时微信客户端才会展现出来。测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。
 * 
 * @author admin
 *
 */
public class WeiXinButton {

	public static WeiXinButton parseAll(JSONObject obj) {
		WeiXinButton butRoot = new WeiXinButton();
		JSONArray buts =  obj.getJSONObject("selfmenu_info").getJSONArray("button");
		for (int i = 0; i < buts.length(); i++) {
			JSONObject butItem = buts.getJSONObject(i);
			WeiXinButton but = WeiXinButton.parse(butItem);
			butRoot.addSub(but);
			if (butItem.has("sub_button")) {
				JSONObject sub_button = butItem.getJSONObject("sub_button");
				if (sub_button.has("list")) {
					JSONArray subs = sub_button.getJSONArray("list");
					for (int m = 0; m < subs.length(); m++) {
						JSONObject subItem = subs.getJSONObject(m);
						WeiXinButton subbut = WeiXinButton.parse(subItem);
						but.addSub(subbut);
					}
				}
			}
		}

		return butRoot;
	}

	public static WeiXinButton parse(JSONObject item) {
		WeiXinButton but = new WeiXinButton();
		but.json_ = item;
		if (item.has("type")) {
			but.setType(item.getString("type"));
		}
		if (item.has("name")) {
			but.setName(item.getString("name"));
		}
		if (item.has("url")) {
			but.setUrl(item.getString("url"));
		}
		if (item.has("key")) {
			but.setKey(item.getString("key"));
		}
		if (item.has("value")) {
			but.setValue(item.getString("value"));
		}
		// value
		return but;
	}

	private JSONObject json_;

	public JSONObject getJson() {
		return this.json_;
	}

	// private String button_; // 是 一级菜单数组，个数应为1~3个
	private List<WeiXinButton> sub_button_; // 否 二级菜单数组，个数应为1~5个
	private String type_; // 是 菜单的响应动作类型
	private String name_; // 是 菜单标题，不超过16个字节，子菜单不超过40个字节
	private String key_; // click等点击类型必须 菜单KEY值，用于消息接口推送，不超过128字节
	private String url_; // view类型必须 网页链接，用户点击菜单可打开链接，不超过256字节
	private String media_id_; // media_id类型和view_limited类型必须
								// 调用新增永久素材接口返回的合法media_id
	private WeiXinButton parent_;

	private String value_;

	public String getValue() {
		return value_;
	}

	public void setValue(String value_) {
		this.value_ = value_;
	}

	public WeiXinButton getParent() {
		return parent_;
	}

	public void setParent(WeiXinButton parent_) {
		this.parent_ = parent_;
	}

	/**
	 * 生成JSON对象，提交数据用
	 * 
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		if (this.parent_ == null) { // 根节点
			JSONArray buts = new JSONArray();
			obj.put("button", buts);
			if (this.sub_button_ != null) {
				for (int i = 0; i < this.sub_button_.size(); i++) {
					WeiXinButton sub = this.sub_button_.get(i);
					buts.put(sub.toJson());
				}
			}
		} else {
			if (this.type_ != null && this.type_.trim().length() > 0) {
				obj.put("type", this.type_);
			}
			if (this.name_ != null && this.name_.trim().length() > 0) {
				obj.put("name", this.name_);
			}
			if (this.key_ != null && this.key_.trim().length() > 0) {
				obj.put("key", this.key_);
			}
			if (this.media_id_ != null && this.media_id_.trim().length() > 0) {
				obj.put("media_id", this.media_id_);
			}
			if (this.url_ != null && this.url_.trim().length() > 0) {
				obj.put("url", this.url_);
			}
			JSONArray subs = new JSONArray();
			obj.put("sub_button", subs);
			if (this.sub_button_ != null) {
				for (int i = 0; i < this.sub_button_.size(); i++) {
					WeiXinButton sub = this.sub_button_.get(i);
					subs.put(sub.toJson());
				}
			}
		}
		return obj;
	}

	/**
	 * 添加子菜单
	 * 
	 * @param sub
	 *            子菜单
	 */
	public void addSub(WeiXinButton sub) {
		if (this.sub_button_ == null) {
			this.sub_button_ = new ArrayList<WeiXinButton>();
		}
		this.sub_button_.add(sub);
		sub.setParent(this);
	}

	public List<WeiXinButton> getSubButtons() {
		return sub_button_;
	}

	public void setSubButtons(List<WeiXinButton> sub_buttons) {
		this.sub_button_ = sub_buttons;
	}

	/**
	 * 菜单的响应动作类型, 参见WeiXinButtonClickEvents
	 * 
	 * @return
	 */
	public String getType() {
		return type_;
	}

	/**
	 * 菜单的响应动作类型, 参见WeiXinButtonClickEvents
	 * 
	 * @param type_
	 */
	public void setType(String type_) {
		this.type_ = type_;
	}

	/**
	 * 菜单标题，不超过16个字节，子菜单不超过40个字节
	 * 
	 * @return
	 */
	public String getName() {
		return name_;
	}

	/**
	 * 菜单标题，不超过16个字节，子菜单不超过40个字节
	 * 
	 * @param name_
	 */
	public void setName(String name_) {
		this.name_ = name_;
	}

	/**
	 * click等点击类型必须 菜单KEY值，用于消息接口推送，不超过128字节
	 * 
	 * @return
	 */
	public String getKey() {
		return key_;
	}

	/**
	 * click等点击类型必须 菜单KEY值，用于消息接口推送，不超过128字节
	 * 
	 * @param key_
	 */
	public void setKey(String key_) {
		this.key_ = key_;
	}

	/**
	 * view类型必须 网页链接，用户点击菜单可打开链接，不超过256字节
	 * 
	 * @return
	 */
	public String getUrl() {
		return url_;
	}

	/**
	 * view类型必须 网页链接，用户点击菜单可打开链接，不超过256字节
	 * 
	 * @param url_
	 */
	public void setUrl(String url_) {
		this.url_ = url_;
	}

	/**
	 * media_id类型和view_limited类型必须,调用新增永久素材接口返回的合法media_id
	 * 
	 * @return
	 */
	public String getMediaId() {
		return media_id_;
	}

	/**
	 * media_id类型和view_limited类型必须,调用新增永久素材接口返回的合法media_id
	 * 
	 * @param media_id_
	 */
	public void setMediaId(String media_id_) {
		this.media_id_ = media_id_;
	}

}
