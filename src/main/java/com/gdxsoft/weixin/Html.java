package com.gdxsoft.weixin;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ref http://mp.weixin.qq.com/wiki/17/c0f37d5704f0b64713d5d2c37b468d75.html
 * 
 * @author admin
 *
 */
public class Html implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5809479181981798540L;
	private String appID_; // 公众号的唯一标识
	private String appsecret_; // 公众号的appsecret
	private String lastErr_;
	private String access_token_;
	private int expires_in_;
	private String refresh_token_;
	private String openid_;
	private String scope_;
	private String unionid_;
	private Long end_time_;
	private boolean isOk_;
	private String code_;

	/**
	 * 
	 */
	public Html(String appID, String appsecret) {
		this.appID_ = appID;
		this.appsecret_ = appsecret;

	}

	/**
	 * OAuth的授权登录方式
	 * 
	 * @param redirect_uri
	 *            授权后重定向的回调链接地址，请使用urlencode对链接进行处理
	 * @param state
	 *            重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
	 * @return
	 */
	public String getSnsapiUserinfo(String redirect_uri, String state) {
		try {
			redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
		} catch (UnsupportedEncodingException e) {

		}
		try {
			state = URLEncoder.encode(state, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		String oath = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + this.appID_ + "&redirect_uri="
				+ redirect_uri + "&response_type=code&scope=snsapi_userinfo&state=" + state + "#wechat_redirect";
		return oath;
	}

	// // debug 调试用
	// protected Html(String appID, String appsecret, String token,
	// String access_token, long end_time) {
	// this.appID_ = appID;
	// this.appsecret_ = appsecret;
	// this.access_token_ = token;
	// this.isOk_ = true;
	// this.access_token_ = access_token;
	// this.end_time_ = end_time;
	// }

	/**
	 * 通过code换取网页授权 access_token 首先请注意，这里通过 code 换取的是一个特殊的网页授权 access_token,
	 * <br>
	 * 与基础支持中的access_token （该access_token用于调用其他接口 ） 不同。公众号可通过下述接口来获取网页授权
	 * access_token。如果网页授权的作用域为 snsapi_base
	 * ，则本步骤中获取到网页授权access_token的同时，也获取到了openid，snsapi_base式的网页授权流程即到此为止。
	 * 
	 * 返回说明<br>
	 * access_token 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同<br>
	 * expires_in access_token接口调用凭证超时时间，单位（秒）<br>
	 * refresh_token 用户刷新access_token<br>
	 * openid 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID<br>
	 * scope 用户授权的作用域，使用逗号（,）分隔<br>
	 * unionid 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）<br>
	 * 
	 * @param code
	 *            填写第一步获取的code参数
	 * @return
	 */
	public boolean getAccessToken(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + this.appID_ + "&secret="
				+ this.appsecret_ + "&code=" + code + "&grant_type=authorization_code";

		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode")) {
			this.setError(obj.toString());
			return false;
		}
		this.code_ = code;
		this.initTokenParams(obj);
		return true;
	}

	/**
	 * 由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，
	 * refresh_token拥有较长的有效期（7天、30天、60天、90天），当refresh_token失效的后，需要用户重新授权。
	 * 
	 * @return
	 */
	public boolean refreshToken() {
		String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + this.appID_
				+ "&grant_type=refresh_token&refresh_token=" + this.refresh_token_;
		JSONObject obj = this.getResult(url);
		if (obj == null) {
			this.isOk_ = false;
			return false;
		}
		if (obj.has("errcode")) {
			this.setError(obj.toString());
			this.isOk_ = false;
			return false;
		}

		this.initTokenParams(obj);
		return true;
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
	public WeiXinUser getWeiXinUserInfo() {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + this.access_token_ + "&openid="
				+ this.openid_ + "&lang=zh_CN";
		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return null;
		}
		if (obj.has("errcode")) {
			this.setError(obj.toString());
			return null;
		}

		Iterator<?> it = obj.keys();
		WeiXinUser u = new WeiXinUser();
		while (it.hasNext()) {
			String key = it.next().toString();
			Object ov;
			try {
				ov = obj.get(key);
			} catch (JSONException e) {
				ov = null;
			}
			if (ov == null) {
				continue;
			}
			String v = ov.toString();

			if (key.equals("subscribe")) {
				int subscribe = Integer.parseInt(v);
				u.setSubscribe(subscribe);
			} else if (key.equals("openid")) {
				u.setOpenid(v);
			} else if (key.equals("nickname")) {
				u.setNickname(v);
			} else if (key.equals("city")) {
				u.setCity(v);
			} else if (key.equals("country")) {
				u.setCountry(v);
			} else if (key.equals("province")) {
				u.setProvince(v);
			} else if (key.equals("language")) {
				u.setLanguage(v);
			} else if (key.equals("headimgurl")) {
				u.setHeadimgurl(v);
			} else if (key.equals("sex")) {
				int sex = Integer.parseInt(v);
				u.setSex(sex);
			} else if (key.equals("subscribe_time")) {
				long subscribe_time = Long.parseLong(v);
				u.setSubscribeTime(subscribe_time);
			}
		}
		u.setJsonStr(obj.toString());
		return u;
	}

	private void initTokenParams(JSONObject obj) {
		try {
			this.access_token_ = obj.getString("access_token");
			this.expires_in_ = obj.getInt("expires_in");
			// 失效时间
			this.end_time_ = System.currentTimeMillis() + this.expires_in_ * 1000;

			this.refresh_token_ = obj.getString("refresh_token");
			this.openid_ = obj.getString("openid");
			this.scope_ = obj.getString("scope");
			if (obj.has("unionid")) {
				this.unionid_ = obj.getString("unionid");
			}
			this.isOk_ = true;
		} catch (JSONException e) {
			this.setError(e.getMessage());
		}
	}

	private JSONObject getResult(String url) {
		String rst = "";
		try {
			rst = Ssl.get(url);
		} catch (Exception e) {
			this.setError(e.getMessage());
			return null;
		}

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

	public String getRefreshToken() {
		return refresh_token_;
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
