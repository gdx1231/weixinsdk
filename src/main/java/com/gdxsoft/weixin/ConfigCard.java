package com.gdxsoft.weixin;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 微信卡券
 * 
 * @author admin
 *
 */
public class ConfigCard implements Serializable {

	public static ConfigCard fromJson(JSONObject obj) throws Exception {
		ConfigCard c = new ConfigCard();
		c.api_ticket_ = obj.getString("api_ticket");

		return c;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1073848374944242253L;
	private Config conifg_;
	private String api_ticket_;

	/**
	 * 微信卡券
	 * 
	 * @param cfg
	 */
	ConfigCard(Config cfg) {
		conifg_ = cfg;
		initCardTicket();
	}

	ConfigCard() {

	}

	public JSONObject toJson() throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("api_ticket", api_ticket_);

		return obj;
	}

	public Config getConifg() {
		return conifg_;
	}

	/**
	 * 是用于调用微信JS API 的临时票据
	 * 
	 * @return
	 */
	public String getApiTicket() {
		return api_ticket_;
	}

	/**
	 * api_ticket 是用于调用微信JS API 的临时票据，有效期为7200 秒，通过access_token 来获取。
	 */
	private void initCardTicket() {
		String u = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + this.conifg_.getAccessToken()
				+ "&type=wx_card";

		try {
			String rst = Ssl.get(u);
			if (rst == null) {
				conifg_.setError("初始化错误");
				return;
			}
			JSONObject obj;
			try {
				obj = new JSONObject(rst);

				if (obj.has("errcode") && obj.getInt("errcode") > 0) {
					conifg_.setError(rst);
				} else {
					api_ticket_ = obj.getString("ticket");
				}
			} catch (JSONException e) {
				conifg_.setError(e.getMessage());
			}
		} catch (Exception e) {
			conifg_.setError(e.getMessage());
		}
	}

	/**
	 * 上传LOGO 接口 <br>
	 *  接口说明 <br>
	 * 开发者需调用该接口上传商户图标至微信服务器，获取相应logo_url，用于卡券创建。 <br>
	 * 
	 * 注意事项<br>
	 * 1.上传的图片限制文件大小限制1MB，像素为300*300，支持JPG 格式。<br>
	 * 2.调用接口获取的logo_url 进支持在微信相关业务下使用，否则会做相应处理。
	 * 
	 * @param filePath
	 * @return 文件的url
	 * @throws Exception
	 */
	public String uploadLogo(String filePath) throws Exception {
		// access_token 调用接口凭证是
		// buffer 文件的数据流是
		String u = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=" + this.conifg_.getAccessToken();
		File f1 = new File(filePath);
		if (!f1.exists()) {
			throw new Exception(f1.getAbsolutePath() + "文件不存在");

		}

		String rst = Ssl.uploadFile(u, "buffer", f1.getAbsolutePath());
		if (rst == null) {
			return null;
		}

		JSONObject obj;
		try {
			obj = new JSONObject(rst);

			if (obj.has("errcode")) {
				conifg_.setError(rst);
				return null;
			} else {
				String url = obj.getString("url");
				return url;
			}
		} catch (JSONException e) {
			conifg_.setError(e.getMessage());
			return null;
		}

	}

	/**
	 * 获取颜色列表接口 <br>
	 * 获得卡券的最新颜色列表，用于卡券创建。
	 * 
	 * @return 颜色的json数组（{"name":"Color010","value":"#55bd47"},
	 *         {"name":"Color020","value":"#10ad61"},）
	 */
	public JSONArray getColors() {
		String u = "https://api.weixin.qq.com/card/getcolors?access_token=" + this.conifg_.getAccessToken();

		try {
			String rst = Ssl.get(u);
			JSONObject obj;
			try {
				obj = new JSONObject(rst);
				int errcode = obj.optInt("errcode");

				if (errcode != 0) {
					conifg_.setError(rst);
					return null;
				} else {
					return obj.getJSONArray("colors");
				}
			} catch (JSONException e) {
				conifg_.setError(e.getMessage());
				return null;
			}

		} catch (Exception e) {
			conifg_.setError(e.getMessage());
			return null;
		}
	}

	/**
	 * 获取所有卡
	 * 
	 * @param offset
	 * @param count
	 * @return
	 */
	public List<String> getCards(int offset, int count) {
		String u = "https://api.weixin.qq.com/card/batchget?access_token=" + this.conifg_.getAccessToken();
		JSONObject body = new JSONObject();
		// { "offset": 0, "count": 10}
		if (offset < 0) {
			offset = 0;
		}
		if (count <= 0) {
			count = 50;
		}
		body.put("offset", offset);
		body.put("count", count);

		JSONObject rst = this.conifg_.postResult(u, body);
		if (rst == null) {
			return null;
		}

		List<String> al = new ArrayList<String>();
		if (rst.has("errcode") && rst.getInt("errcode") == 0) {
			JSONArray arr = rst.getJSONArray("card_id_list");
			for (int i = 0; i < arr.length(); i++) {
				String card_id = arr.getString(i);
				al.add(card_id);
			}
		}
		return al;
	}

	public Object getCard(String card_id) {
		String u = "https://api.weixin.qq.com/card/get?access_token=" + this.conifg_.getAccessToken();
		JSONObject body = new JSONObject();
		body.put("card_id", card_id);
		JSONObject rst = this.conifg_.postResult(u, body);
		if (rst == null) {
			return null;
		}
		if (rst.has("errcode") && rst.getInt("errcode") == 0) {
			JSONObject card = rst.getJSONObject("card");
			String card_type = card.getString("card_type").toLowerCase();

			if (card_type.equals("groupon")) {
				return WxCardGroupon.parse(rst);
			} else if (card_type.equals("general_coupon")) {
				return WxCardGeneralCoupon.parse(rst);
			} else if (card_type.equals("gift")) {
				return WxCardGift.parse(rst);
			} else if (card_type.equals("cash")) {
				return WxCardCash.parse(rst);
			} else if (card_type.equals("discount")) {
				return WxCardDiscount.parse(rst);
			} else {
				System.out.println(rst);
				return null;
			}
		} else {
			this.conifg_.setError(rst.toString());

			return null;
		}
	}

	/**
	 * 
	 * 创建卡券接口是微信卡券的基础接口，用于创建一类新的卡券，获取card_id，创建成功
	 * 并通过审核后，商家可以通过文档提供的其他接口将卡券下发给用户，每次成功领取，库存数 量相应扣除。 需指定code
	 * 的商家必须在创建卡券时候，设定use_custom_code 为true，且在后续用 户领取卡券时，在二维码或添加到卡包JS API
	 * 内指定code。指定openid 同理。 can_share 字段指领取卡券原生页面是否可分享，建议指定code、指定openid 等强限
	 * 制条件的卡券填写false。
	 * 
	 * 为满足商户功能扩展的需求，新增可自定义卡券内cell 的名称类型，支持跳转到商户自定义 url 链接。
	 * 
	 * 注意：创建卡券仅代表成功创建一种卡券，创建成功不代表下发成功。商户需通过二维码或 JS API 的方式将卡券下发给用户
	 * 
	 * @param card 通用券
	 * @return
	 */
	public String createCard(WxCard card) {
		String u = "https://api.weixin.qq.com/card/create?access_token=" + this.conifg_.getAccessToken();
		try {
			String rst = Ssl.postMsg(u, card.toJsonString());
			JSONObject obj;
			try {
				obj = new JSONObject(rst);
				int errcode = obj.optInt("errcode");

				if (errcode != 0) {
					conifg_.setError(rst);
					return null;
				} else {
					return obj.getString("card_id");
				}
			} catch (JSONException e) {
				conifg_.setError(e.getMessage());
				return null;
			}

		} catch (Exception e) {
			conifg_.setError(e.getMessage());
			return null;
		}
	}

	/**
	 * 创建卡券后，商户可通过接口生成一张卡券二维码供用户扫码后添加卡券到卡包
	 * 
	 * @param card_id        卡券ID
	 * @param code           指定卡券code 码，只能被领一次。use_custom_code 字 段为true
	 *                       的卡券必须填写，非自定义code 不必填写。
	 * @param open_id        指定领取者的openid，只有该用户能领取。bind_openid 字段为true
	 *                       的卡券必须填写，非自定义openid 不必填写。
	 * @param is_unique_code 指定下发二维码，生成的二维码随机分配一个code，领取 后不可再次扫描。填写true
	 *                       或false。默认false。
	 * @param outer_id       领取场景值，用于领取渠道的数据统计，默认值为0，字 段类型为整型。用户领取卡券后触发的事件推送中会带上
	 *                       此自定义场景值。
	 * @return
	 */
	public WeiXinQRCode createCardQRCode(String card_id, String code, String openid, boolean is_unique_code,
			int outer_id) {
		String url = "https://api.weixin.qq.com/card/qrcode/create?access_token=" + this.conifg_.getAccessToken();

		JSONObject body = new JSONObject();
		JSONObject action_info = new JSONObject();
		JSONObject scene = new JSONObject();
		try {

			body.put("action_name", "QR_CARD");
			body.put("action_info", action_info);

			action_info.put("card", scene);
			scene.put("card_id", card_id);
			if (code != null && code.trim().length() > 0) {
				scene.put("code", code);
			}
			if (openid != null && openid.trim().length() > 0) {
				scene.put("openid", openid);
			}
			scene.put("is_unique_code", is_unique_code);
			scene.put("outer_id", outer_id);
			scene.put("expire_seconds", "1800");
		} catch (JSONException e) {
			conifg_.setError(e.getMessage());
			return null;
		}

		JSONObject obj = conifg_.postResult(url, body);
		if (obj == null) {
			return null;
		}
		try {
			if (!obj.has("errcode") || obj.getInt("errcode") == 0) {
				Iterator<?> it = obj.keys();
				WeiXinQRCode u = new WeiXinQRCode();

				while (it.hasNext()) {
					String key = it.next().toString();
					Object ov = obj.get(key);
					if (ov == null) {
						continue;
					}
					String v = ov.toString();

					if (key.equals("expire_seconds")) {
						int expire_seconds = Integer.parseInt(v);
						u.setExpireSeconds(expire_seconds);
					} else if (key.equals("ticket")) {
						u.setTicket(v);
					} else if (key.equals("url")) {
						u.setUrl(v);
					}
				}
				u.setJsonStr(obj.toString());
				return u;
			} else {
				conifg_.setError(obj.toString());
				return null;
			}
		} catch (JSONException e) {
			conifg_.setError(e.getMessage());
			return null;
		}
	}

	/**
	 * 拉起卡券列表（chooseCard）JSAPI
	 * 
	 * @param card_id     生成卡券时获得的card_id，可拉起指定id 的卡 券列表。当card_id 为空时默认拉起所有卡券的列表。
	 * @param card_type   卡券类型，可拉起指定类型的卡券列表。当 card_type 为空时，默认拉起所有卡券的列表。
	 * @param location_id 门店信息，为拉起卡券列表的筛选条件之一。 拉取无门店类型的卡券，不填写该字段。
	 * @return
	 */
	public String createJsChooseCard(String card_id, String card_type, String location_id) {
		JSONObject body = new JSONObject();

		WxCardSign sign = new WxCardSign();
		sign.AddData(this.api_ticket_);

		body.put("app_id", this.conifg_.getAppId());
		sign.AddData(this.conifg_.getAppId());

		body.put("sign_type", "sha1");

		String nonce_str = SignUtils.nonceStr();
		body.put("nonce_str", nonce_str);
		sign.AddData(nonce_str);

		String timestamp = System.currentTimeMillis() / 1000 + "";
		body.put("timestamp", timestamp);
		sign.AddData(timestamp);

		if (card_id != null && card_id.trim().length() > 0) {
			body.put("card_id", card_id);
			sign.AddData(card_id);
		}

		if (card_type != null && card_type.trim().length() > 0) {
			body.put("card_type", card_type);
			sign.AddData(card_type);
		}
		if (location_id != null && location_id.trim().length() > 0) {
			body.put("location_id", location_id);
			sign.AddData(location_id);
		}

		String signature = sign.GetSignature();
		body.put("card_sign", signature);

		return body.toString();
	}

	/**
	 * 创建卡券后，商户可通过接口生成一张卡券二维码供用户扫码后添加卡券到卡包
	 * 
	 * @param card_id 卡券ID
	 * @param code    指定卡券code 码，只能被领一次。use_custom_code 字 段为true 的卡券必须填写，非自定义code
	 *                不必填写。
	 * @param openid  指定领取者的openid，只有该用户能领取。bind_openid 字段为true 的卡券必须填写，非自定义openid
	 *                不必填写。
	 * @return
	 */
	public String createJsBathAddCard(String card_id, String code, String openid) {

		JSONObject body = new JSONObject();
		JSONArray card_list = new JSONArray();
		String timestamp = System.currentTimeMillis() / 1000 + "";

		/*
		 * 1．将api_ticket、timestamp、card_id、code、openid、balance 的value 值进行字符串 的字典序排序。
		 * 
		 * 2．将所有参数字符串拼接成一个字符串进行sha1 加密，得到signature。
		 * 
		 * 3．signature中的timestamp 和card_ext 中的timestamp 必须保持一致。
		 */
		WxCardSign sign = new WxCardSign();
		sign.AddData(this.api_ticket_);
		try {

			body.put("card_list", card_list);

			JSONObject card = new JSONObject();
			card_list.put(card);

			card.put("card_id", card_id);
			sign.AddData(card_id);

			JSONObject card_ext = new JSONObject();

			if (code == null)
				code = "";
			card_ext.put("code", code.trim());
			sign.AddData(code);

			if (openid == null)
				openid = "";
			card_ext.put("openid", openid.trim());
			sign.AddData(openid);

			card_ext.put("timestamp", timestamp);
			sign.AddData(timestamp);

			String signature = sign.GetSignature();

			card_ext.put("signature", signature);

			card.put("card_ext", card_ext.toString());
			System.out.println(body);

			return body.toString();
		} catch (JSONException e) {
			conifg_.setError(e.getMessage());
			return null;
		}

	}

	public boolean addWhite(String openid, String username) {
		String u = "https://api.weixin.qq.com/card/testwhitelist/set?access_token=" + this.conifg_.getAccessToken();
		JSONObject body = new JSONObject();
		JSONArray openids = new JSONArray();
		JSONArray usernames = new JSONArray();

		try {
			body.put("openid", openids);
			body.put("username", usernames);

			openids.put(openid);
			usernames.put(username);

			String rst = Ssl.postMsg(u, body.toString());

			JSONObject obj = new JSONObject(rst);

			if (obj.getInt("errcode") == 0) {
				return true;
			} else {
				conifg_.setError(rst);
				return false;
			}
		} catch (Exception e) {
			conifg_.setError(e.getMessage());
			return false;
		}
	}
}
