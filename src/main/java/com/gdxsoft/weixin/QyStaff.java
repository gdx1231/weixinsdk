package com.gdxsoft.weixin;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class QyStaff {

	private String userid_;// 是 成员UserID。对应管理端的帐号，企业内必须唯一。长度为1~64个字节
	private String name_;// 是 成员名称。长度为1~64个字节
	private int[] department_;// 否 成员所属部门id列表。注意，每个部门的直属成员上限为1000个
	private String position_;// 否 职位信息。长度为0~64个字节
	private String mobile_;// 否 手机号码。企业内必须唯一，mobile/weixinid/email三者不能同时为空
	private String email_;// 否 邮箱。长度为0~64个字节。企业内必须唯一
	private String weixinid_;// 否 微信号。企业内必须唯一。（注意：是微信号，不是微信的名字）
	private HashMap<String, String> extattr_;// 否
												// 扩展属性。扩展属性需要在WEB管理端创建后才生效，否则忽略未知属性的赋值

	private String avatar_; // 头像url。注：如果要获取小图将url最后的"/0"改成"/64"即可
	private int status_;// 激活状态: 1=已激活，2=已禁用，4=未激活
						// 已激活代表已激活企业微信或已关注微信插件。未激活代表既未激活企业微信又未关注微信插件。
	private int isleader_; // 标示是否为上级。第三方暂不支持
	private String telephone_;// 座机。第三方暂不支持
	private String english_name_; // 英文名。第三方暂不支持
	private int enable_;// 启用/禁用成员，第三方不可获取。1表示启用成员，0表示禁用成员
	private int wxplugin_status_;// 关注微信插件的状态: 1=已关注，0=未关注
	private JSONObject json_;
	private int gender_;
	private int hide_mobile_;

	public static QyStaff parse(JSONObject obj) {
		QyStaff d = new QyStaff();
		Iterator<?> keys = obj.keys();
		while (keys.hasNext()) {
			String key = keys.next().toString();
			if (key.equals("userid")) {
				d.userid_ = obj.getString(key);
			} else if (key.equals("name")) {
				d.name_ = obj.getString(key);
			} else if (key.equals("position")) {
				d.position_ = obj.getString(key);
			} else if (key.equals("mobile")) {
				d.mobile_ = obj.getString(key);
			} else if (key.equals("weixinid")) {
				d.weixinid_ = obj.getString(key);
			} else if (key.equals("avatar")) {
				d.avatar_ = obj.getString(key);
			} else if (key.equals("status")) {
				d.status_ = obj.getInt(key);
			} else if (key.equals("isleader")) {
				d.isleader_ = obj.getInt(key);
			} else if (key.equals("enable")) {
				d.enable_ = obj.getInt(key);
			} else if (key.equals("wxplugin_status")) {
				d.wxplugin_status_ = obj.getInt(key);
			} else if (key.equals("telephone")) {
				d.telephone_ = obj.getString(key);
			} else if (key.equals("english_name")) {
				d.english_name_ = obj.getString(key);
			} else if (key.equals("email")) {
				d.email_ = obj.getString(key);
			} else if (key.equals("gender")) {
				d.gender_ = obj.getInt(key);
			} else if (key.equals("hide_mobile")) {
				d.hide_mobile_ = obj.getInt(key);
			} else if (key.equals("department")) {
				try {
					JSONArray arr = obj.getJSONArray(key);
					d.department_ = new int[arr.length()];
					for (int i = 0; i < arr.length(); i++) {
						d.department_[i] = arr.getInt(i);
					}
				} catch (Exception err) {

				}
			} else if (key.equals("extattr")) {
				JSONObject exts = obj.getJSONObject(key);
				if (exts.has("attrs")) {
					try {
						JSONArray arr = exts.getJSONArray("atts");
						d.extattr_ = new HashMap<String, String>();
						for (int i = 0; i < arr.length(); i++) {
							JSONObject att = arr.getJSONObject(i);
							String name = att.getString("name");
							String value = att.getString("value");
							d.extattr_.put(name, value);
						}
					} catch (Exception err) {

					}
				}
			} else {
				System.out.println(key);
			}
		}

		d.json_ = obj;
		return d;
	}

	public JSONObject toJson() {
		JSONObject body = new JSONObject();
		body.put("userid", userid_);
		body.put("name", name_);
		body.put("position", position_);

		// mobile/weixinid/email三者不能同时为空
		body.put("mobile", mobile_);
		body.put("email", email_);
		body.put("weixinid", weixinid_);
		body.put("english_name", english_name_);
		body.put("telephone", telephone_);
		body.put("wxplugin_status", wxplugin_status_);
		body.put("enable", enable_);
		body.put("isleader", isleader_);
		body.put("avatar", avatar_);
		body.put("status", status_);
		body.put("weixinid", weixinid_);
		body.put("gender", this.gender_);
		body.put("hide_mobile", this.hide_mobile_);
		
		if (department_ != null && department_.length > 0) {
			JSONArray arr = new JSONArray();
			for (int i = 0; i < department_.length; i++) {
				arr.put(department_[i]);
			}
			body.put("department", arr);

		}

		if (extattr_ != null) {
			// "extattr":
			// {"attrs":[{"name":"爱好","value":"旅游"},{"name":"卡号","value":"1234567234"}]}
			JSONObject exts = new JSONObject();
			JSONArray atts = new JSONArray();
			exts.put("atts", atts);
			boolean isHave = false;
			for (String key : extattr_.keySet()) {
				String v = extattr_.get(key);
				if (v != null && v.trim().length() > 0) {
					JSONObject att = new JSONObject();
					att.put("name", key);
					att.put("value", v);
					atts.put(att);
					isHave = true;
				}
			}
			if (isHave) {
				body.put("extattr", exts);
			}
		}
		return body;

	}

	/**
	 * 成员UserID。对应管理端的帐号，企业内必须唯一。长度为1~64个字节
	 * 
	 * @return
	 */
	public String getUserid() {
		return userid_;
	}

	/**
	 * 成员UserID。对应管理端的帐号，企业内必须唯一。长度为1~64个字节
	 * 
	 * @param userid
	 */
	public void setUserid(String userid) {
		this.userid_ = userid;
	}

	/**
	 * 成员名称。长度为1~64个字节
	 * 
	 * @return
	 */
	public String getName() {
		return name_;
	}

	/**
	 * 成员名称。长度为1~64个字节
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name_ = name;
	}

	/**
	 * 否 成员所属部门id列表
	 * 
	 * @return
	 */
	public int[] getDepartment() {
		return department_;
	}

	/**
	 * 否 成员所属部门id列表
	 * 
	 * @param department
	 */
	public void setDepartment(int[] department) {
		this.department_ = department;
	}

	/**
	 * 职位信息 长度为0~64个字节
	 * 
	 * @return
	 */
	public String getPosition() {
		return position_;
	}

	/**
	 * 职位信息 长度为0~64个字节
	 * 
	 * @param position
	 */
	public void setPosition(String position) {
		this.position_ = position;
	}

	/**
	 * 手机号码。企业内必须唯一，mobile/weixinid/email三者不能同时为空
	 * 
	 * @return
	 */
	public String getMobile() {
		return mobile_;
	}

	/**
	 * 手机号码。企业内必须唯一，mobile/weixinid/email三者不能同时为空
	 * 
	 * @param mobile
	 */
	public void setMobile(String mobile) {
		this.mobile_ = mobile;
	}

	/**
	 * 手机号码。企业内必须唯一，mobile/weixinid/email三者不能同时为空
	 * 
	 * @return
	 */
	public String getEmail() {
		return email_;
	}

	/**
	 * 手机号码。企业内必须唯一，mobile/weixinid/email三者不能同时为空
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email_ = email;
	}

	/**
	 * 手机号码。企业内必须唯一，mobile/weixinid/email三者不能同时为空
	 * 
	 * @return
	 */
	public String getWeixinid() {
		return weixinid_;
	}

	/**
	 * 手机号码。企业内必须唯一，mobile/weixinid/email三者不能同时为空
	 * 
	 * @param weixinid
	 */
	public void setWeixinid(String weixinid) {
		this.weixinid_ = weixinid;
	}

	/**
	 * 扩展属性。扩展属性需要在WEB管理端创建后才生效，否则忽略未知属性的赋值
	 * 
	 * @return
	 */
	public HashMap<String, String> getExtattr() {
		return extattr_;
	}

	/**
	 * 扩展属性。扩展属性需要在WEB管理端创建后才生效，否则忽略未知属性的赋值
	 * 
	 * @param extattr
	 */
	public void setExtattr(HashMap<String, String> extattr) {
		this.extattr_ = extattr;
	}

	/**
	 * 头像url。注：如果要获取小图将url最后的"/0"改成"/64"即可
	 * 
	 * @return
	 */
	public String getAvatar() {
		return avatar_;
	}

	/**
	 * 头像url。注：如果要获取小图将url最后的"/0"改成"/64"即可
	 * 
	 * @param avatar
	 */
	public void setAvatar(String avatar) {
		this.avatar_ = avatar;
	}

	/**
	 * 激活状态: 1=已激活，2=已禁用，4=未激活 已激活代表已激活企业微信或已关注微信插件。未激活代表既未激活企业微信又未关注微信插件。
	 * 
	 * @return
	 */
	public int getStatus() {
		return status_;
	}

	/**
	 * 激活状态: 1=已激活，2=已禁用，4=未激活 已激活代表已激活企业微信或已关注微信插件。未激活代表既未激活企业微信又未关注微信插件。
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status_ = status;
	}

	public JSONObject getJson() {
		return json_;
	}

	/**
	 * 标示是否为上级。第三方暂不支持
	 * 
	 * @return the isleader_
	 */
	public int getIsleader_() {
		return isleader_;
	}

	/**
	 * 标示是否为上级。第三方暂不支持
	 * 
	 * @param isleader_
	 *            the isleader_ to set
	 */
	public void setIsleader(int isleader) {
		this.isleader_ = isleader;
	}

	/**
	 * 座机。第三方暂不支持
	 * 
	 * @return the telephone_
	 */
	public String getTelephone() {
		return telephone_;
	}

	/**
	 * 座机。第三方暂不支持
	 * 
	 * @param telephone_
	 *            the telephone_ to set
	 */
	public void setTelephone(String telephone) {
		this.telephone_ = telephone;
	}

	/**
	 * 英文名。第三方暂不支持
	 * 
	 * @return the english_name_
	 */
	public String getEnglishName() {
		return english_name_;
	}

	/**
	 * 英文名。第三方暂不支持
	 * 
	 * @param english_name_
	 *            the english_name_ to set
	 */
	public void setEnglishName(String english_name) {
		this.english_name_ = english_name;
	}

	/**
	 * 激活状态: 1=已激活，2=已禁用，4=未激活 已激活代表已激活企业微信或已关注微信插件。未激活代表既未激活企业微信又未关注微信插件。
	 * 
	 * @return the enable_
	 */
	public int getEnable() {
		return enable_;
	}

	/**
	 * 激活状态: 1=已激活，2=已禁用，4=未激活 已激活代表已激活企业微信或已关注微信插件。未激活代表既未激活企业微信又未关注微信插件。
	 * 
	 * @param enable_
	 *            the enable_ to set
	 */
	public void setEnable(int enable) {
		this.enable_ = enable;
	}

	/**
	 * 启用/禁用成员，第三方不可获取。1表示启用成员，0表示禁用成员
	 * 
	 * @return the wxplugin_status_
	 */
	public int getWxpluginStatus() {
		return wxplugin_status_;
	}

	/**
	 * 启用/禁用成员，第三方不可获取。1表示启用成员，0表示禁用成员
	 * 
	 * @param wxplugin_status_
	 *            the wxplugin_status_ to set
	 */
	public void setWxpluginStatus(int wxplugin_status) {
		this.wxplugin_status_ = wxplugin_status;
	}

	/**
	 * 性别。0表示未定义，1表示男性，2表示女性
	 * 
	 * @return the gender_
	 */
	public int getGender() {
		return gender_;
	}

	/**
	 * 性别。0表示未定义，1表示男性，2表示女性
	 * 
	 * @param gender_
	 *            the gender_ to set
	 */
	public void setGender(int gender) {
		this.gender_ = gender;
	}

	/**
	 * @return the hide_mobile_
	 */
	public int getHideMobile() {
		return hide_mobile_;
	}

	/**
	 * @param hide_mobile_
	 *            the hide_mobile_ to set
	 */
	public void setHideMobile(int hide_mobile) {
		this.hide_mobile_ = hide_mobile;
	}

}
