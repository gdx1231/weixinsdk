package com.gdxsoft.weixin;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ref https://work.weixin.qq.com/api/doc#10028
 * 
 * @author admin
 *
 */
public class QyHtml implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -580947911981798540L;
	private String appID_; // 公众号的唯一标识
	private String appsecret_; // 公众号的appsecret
	private String lastErr_;
	private String access_token_;
	private int expires_in_;
	private String openid_;
	private String scope_;
	private String unionid_;
	private Long end_time_;
	private boolean isOk_;
	private String code_;
	private int agentid_;

	/**
	 * 
	 */
	public QyHtml(String appID, String accessToken, int agentid) {
		this.appID_ = appID;
		this.access_token_ = accessToken;
		this.agentid_ = agentid;
	}

	/**
	 * OAuth的授权登录方式 获取code
	 * 
	 * 如果企业需要在打开的网页里面携带用户的身份信息，第一步需要构造如下的链接来获取code参数：
	 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&
	 * redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&agentid=AGENTID&
	 * state=STATE#wechat_redirect
	 * 
	 * @param redirect_uri 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
	 * @param state        重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
	 * @return
	 */
	public String getWorkSnsapiUserinfo(String redirect_uri, String state) {
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
		} catch (UnsupportedEncodingException e) {

		}
		try {
			state = URLEncoder.encode(state, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		StringBuilder sb = new StringBuilder();
		sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=");
		sb.append(this.appID_);
		sb.append("&redirect_uri=");
		sb.append(redirect_uri);
		sb.append("&response_type=code&scope=snsapi_userinfo&agentid=");
		sb.append(this.agentid_);
		sb.append("&state=");
		sb.append(state);
		sb.append("#wechat_redirect");
		String oath = sb.toString();
		return oath;
	}

	/**
	 * 拉取用户信息(需scope为 snsapi_userinfo)<br>
	 * 
	 * 返回说明<br>
	 * openid 用户的唯一标识<br>
	 * nickname 用户昵称<br>
	 * sex 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知<br>
	 * province 用户个人资料填写的省份<br>
	 * city 普通用户个人资料填写的城市<br>
	 * country 国家，如中国为CN<br>
	 * headimgurl
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	 * 。若用户更换头像，原有头像URL将失效。<br>
	 * privilege 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）<br>
	 * unionid 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）<br>
	 * 
	 * @return
	 */
	public String getWeiXinWorkUserInfo(String code) {
		StringBuilder sb = new StringBuilder();
		sb.append("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=");
		sb.append(this.access_token_);
		sb.append("&code=");
		sb.append(code);
		String url = sb.toString();
		JSONObject obj = this.getResult(url);

		if (obj == null) {
			return null;
		}
		if (obj.has("errcode") && obj.optInt("errcode") != 0) {
			this.setError(obj.toString());
			return null;
		}

		/*
		 * a) 当用户为企业成员时返回示例如下：
		 * 
		 * { "errcode": 0, "errmsg": "ok", "UserId":"USERID", "DeviceId":"DEVICEID",
		 * "user_ticket": "USER_TICKET"， "expires_in":7200 }
		 */

		/*
		 * b) 非企业成员授权时返回示例如下：
		 * 
		 * { "errcode": 0, "errmsg": "ok", "OpenId":"OPENID", "DeviceId":"DEVICEID" }
		 */

		if (obj.has("UserId")) {
			return obj.optString("UserId"); // 微信用户编号
		} else {
			return "非企业成员授权";
		}
	}

	private JSONObject getResult(String url) {
		String rst = "";
		rst = Ssl.get(url);

		JSONObject obj;
		try {
			obj = new JSONObject(rst);

			return obj;
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}
	}

	private void setError(String errmsg) {
		this.lastErr_ = errmsg;
		Ssl.log("错误" + errmsg);

	}

	/**
	 * 获取最后错误
	 * 
	 * @return
	 */
	public String getLastErr() {
		return this.lastErr_;
	}

	public String getAppID() {
		return appID_;
	}

	public String getAppsecret() {
		return appsecret_;
	}

	public String getAccessToken() {
		return access_token_;
	}

	public int getExpiresin() {
		return expires_in_;
	}

	public String getOpenid() {
		return openid_;
	}

	public String getScope() {
		return scope_;
	}

	public String getUnionid() {
		return unionid_;
	}

	public Long getEndTime() {
		return end_time_;
	}

	public boolean isOk() {
		return isOk_;
	}

	/**
	 * 第一步：用户同意授权，获取的 code
	 * 
	 * @return
	 */
	public String getCode() {
		return code_;
	}

}
