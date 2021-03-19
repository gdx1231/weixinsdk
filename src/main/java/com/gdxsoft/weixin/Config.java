/**
 * 
 */
package com.gdxsoft.weixin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.ssl.SSLContexts;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gdxsoft.easyweb.utils.UConvert;
import com.gdxsoft.easyweb.utils.UFile;
import com.gdxsoft.easyweb.utils.UNet;
import com.gdxsoft.easyweb.utils.UPath;
import com.gdxsoft.easyweb.utils.UXml;
import com.gdxsoft.easyweb.utils.Utils;

/**
 * @author admin
 *
 */
public class Config implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5824365775309372277L;

	private static String API_ROOT = "https://api.weixin.qq.com/cgi-bin/";
	private static HashMap<Integer, Config> MAP = new HashMap<Integer, Config>();

	/**
	 * 实例化对象，如果缓存中存在，直接返回，如果超时或不存在，重新生成
	 * 
	 * @param appID
	 * @param appsecret
	 * @param token
	 * @return
	 */
	public synchronized static Config instance(String appID, String appsecret, String token) {
		String key = appID + "?" + appsecret + "?" + token;
		int code = key.hashCode();
		if (MAP.containsKey(code)) {
			Config cfg = MAP.get(code);
			if (!cfg.checkIsExpired()) {
				// 过期时间超过50秒；
				return cfg;
			} else {
				// 过期重新获取
				MAP.remove(code);
			}
		}
		Config cfg = new Config(appID, appsecret, token);
		if (cfg.isOk_) {
			MAP.put(code, cfg);
			return cfg;
		} else {
			return null;
		}
	}

	/**
	 * 重新创建实例,用于实例未过期但qq不认的情况
	 * 
	 * @param appID
	 * @param appsecret
	 * @param token
	 * @return
	 */
	public synchronized static Config instanceNew(String appID, String appsecret, String token) {
		String key = appID + "?" + appsecret + "?" + token;
		int code = key.hashCode();
		if (MAP.containsKey(code)) {

			MAP.remove(code);

		}

		return instance(appID, appsecret, token);
	}

	/**
	 * 从JSON中创建
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Config fromJson(JSONObject obj) throws Exception {
		String appID = obj.getString("appID");
		String appsecret = obj.getString("appsecret");
		String token = obj.getString("token");
		String access_token = obj.getString("access_token");
		long end_time = obj.optLong("end_time");

		Config cfg = new Config();
		cfg.appID_ = appID;
		cfg.appsecret_ = appsecret;
		cfg.token_ = token;
		cfg.access_token_ = access_token;
		cfg.end_time_ = end_time;
		cfg.expires_in_ = obj.optInt("expires_in");

		ConfigCard card = ConfigCard.fromJson(obj.getJSONObject("card"));

		cfg.card_ = card;
		cfg.isOk_ = true;

		if (obj.has("shop_key")) {
			String shop_key = obj.getString("shop_key");
			String shop_id = obj.getString("shop_id");
			String p12bytesBase64 = obj.getString("p12bytesBase64");
			byte[] buf = UConvert.FromBase64String(p12bytesBase64);

			cfg.setShopKey(shop_key);
			cfg.setShopId(shop_id);

			cfg.initSslContext(buf);
		}
		return cfg;
	}

	/**
	 * 映射为JSON
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONObject toJson() throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("appID", this.appID_);
		obj.put("appsecret", this.appsecret_);
		obj.put("token", this.token_);
		obj.put("end_time", this.end_time_);
		obj.put("access_token", this.access_token_);
		obj.put("expires_in", this.expires_in_);

		obj.put("shop_key", this.shop_key_);
		obj.put("shop_id", this.shop_id_);
		obj.put("p12bytesBase64", this.p12bytesBase64_); // 支付数字证书base64

		obj.put("card", this.card_.toJson());
		return obj;
	}

	private String appID_; // 公众号appID

	/**
	 * 公众号appID
	 * 
	 * @return
	 */
	public String getAppId() {
		return appID_;
	}

	private String appsecret_;
	private String token_;

	private String shop_key_; // abcdefg1234567ABCDEFG1234567love

	/**
	 * 商户MH_ID 在 https://pay.weixin.qq.com
	 * 
	 * @return
	 */
	public String getShopId() {
		return shop_id_;
	}

	/**
	 * 商户MH_ID 在 https://pay.weixin.qq.com
	 * 
	 * @param shop_id_
	 */
	public void setShopId(String shop_id_) {
		this.shop_id_ = shop_id_;
	}

	private ConfigCard card_;

	private String p12bytesBase64_;

	/**
	 * 微信卡券 操作类
	 * 
	 * @return
	 */
	public ConfigCard getCard() {
		return this.card_;
	}

	/**
	 * 检查是否超时
	 * 
	 * @return
	 */
	public boolean checkIsExpired() {
		long span = this.end_time_ - System.currentTimeMillis();
		if (span <= 50 * 1000) {
			System.out.println("AccessToken 超时：" + span);
			return true;

		} else {
			return false;
		}
	}

	/**
	 * 初始化商户SSL加密对象, 数字证书为p12
	 * 
	 * @param p12bytes
	 * @return
	 */
	public boolean initSslContext(byte[] p12bytes) {
		this.p12bytesBase64_ = UConvert.ToBase64String(p12bytes);

		ByteArrayInputStream is = new ByteArrayInputStream(p12bytes);
		try {
			KeyStore keyStore_ = KeyStore.getInstance("PKCS12");
			keyStore_.load(is, this.shop_id_.toCharArray());
			this.sslcontext_ = SSLContexts.custom().loadKeyMaterial(keyStore_, this.shop_id_.toCharArray()).build();

			return true;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("initSslContext" + e.getMessage());
			return false;
		} catch (CertificateException e) {
			System.out.println("initSslContext" + e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("initSslContext" + e.getMessage());
			return false;
		} catch (KeyManagementException e) {
			System.out.println("initSslContext" + e.getMessage());
			return false;
		} catch (UnrecoverableKeyException e) {
			System.out.println("initSslContext" + e.getMessage());
			return false;
		} catch (KeyStoreException e) {
			System.out.println("initSslContext" + e.getMessage());
			return false;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				System.out.println("initSslContext" + e.getMessage());
				return false;
			}
		}

	}

	private String shop_id_;

	private SSLContext sslcontext_;

	/**
	 * 商户SSL加密对象
	 * 
	 * @return
	 */
	public SSLContext getSslContext() {
		return sslcontext_;
	}

	/**
	 * 商户的app token
	 * 
	 * @return
	 */
	public String getToken() {
		return token_;
	}

	/**
	 * https://pay.weixin.qq.com/ 商户API KEY
	 * 
	 * @return
	 */
	public String getShopKey() {
		return shop_key_;
	}

	/**
	 * https://pay.weixin.qq.com/ 商户API KEY
	 * 
	 * @param shop_key_
	 */
	public void setShopKey(String shop_key_) {
		this.shop_key_ = shop_key_;
	}

	private int expires_in_ = 7200;// 凭证有效时间，单位：秒
	private String access_token_; // 获取到的凭证

	/**
	 * 获取到的凭证
	 * 
	 * @return
	 */
	public String getAccessToken() {
		return access_token_;
	}

	private Long end_time_;
	private String lastErr;

	private boolean isOk_;

	private String[] weixinVaildIps_;
	private WeiXinUserList weiXinUserList_;

	private HashMap<String, WeiXinTicket> mapTickets_ = new HashMap<String, WeiXinTicket>();

	/**
	 * 
	 */
	protected Config(String appID, String appsecret, String token) {
		this.appID_ = appID;
		this.appsecret_ = appsecret;
		this.token_ = token;
		this.isOk_ = initGetAccessToken();

		initCard();
	}

	/**
	 * 初始化卡券操作类
	 */
	private void initCard() {
		if (isOk_) {
			this.card_ = new ConfigCard(this);
		}

	}

	// denug 调试用
	protected Config(String appID, String appsecret, String token, String access_token, long end_time) {
		this.appID_ = appID;
		this.appsecret_ = appsecret;
		this.token_ = token;
		this.isOk_ = true;
		this.access_token_ = access_token;
		this.end_time_ = end_time;

		initCard();
	}

	private Config() {

	}

	/**
	 * access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。开发者需要进行妥善保存。
	 * access_token的存储至少要保留512个字符空间
	 * 。access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。<br>
	 * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential &appid
	 * =APPID&secret=APPSECRET <br>
	 * 参数说明<br>
	 * grant_type 获取access_token填写client_credential <br>
	 * appid 是 第三方用户唯一凭证 <br>
	 * secret 是 第三方用户唯一凭证密钥，即appsecret
	 * 
	 * 返回说明<br>
	 * 正常情况下，微信会返回下述JSON数据包给公众号：<br>
	 * {"access_token":"ACCESS_TOKEN","expires_in":7200}<br>
	 * access_token 获取到的凭证<br>
	 * expires_in 凭证有效时间，单位：秒<br>
	 * 
	 * @throws HttpException
	 * @throws IOException
	 * @throws JSONException
	 */
	private boolean initGetAccessToken() {
		String u1 = API_ROOT + "token?grant_type=client_credential&appid=" + appID_ + "&secret=" + appsecret_;

		JSONObject obj = this.getResult(u1);
		if (obj == null) {
			return false;
		}
		try {
			String access_token = obj.getString("access_token");
			int expires_in = obj.getInt("expires_in"); // 过期时间，单位秒
			this.access_token_ = access_token;
			this.expires_in_ = expires_in;

			// 过期时间
			this.end_time_ = System.currentTimeMillis() + this.expires_in_ * 1000;
			return true;
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return false;
		}

	}

	/**
	 * 新增其他类型永久素材
	 * 
	 * 1、新增的永久素材也可以在公众平台官网素材管理模块中看到
	 * 2、永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000 3、素材的格式大小等要求与公众平台官网一致。具体是
	 * ，图片大小不超过2M，支持bmp/png/jpeg/jpg/gif格式，语音大小不超过5M，长度不超过60秒
	 * ，支持mp3/wma/wav/amr格式<br>
	 * 4、调用该接口需https协议
	 * 
	 * @param media_file
	 *            文件
	 * @param type
	 *            媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * @return media_id
	 */
	public String createWeiXinMaterial(String media_file, String type) {
		String url = API_ROOT + "material/add_material?access_token=" + this.access_token_;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		String s1;
		try {
			s1 = Ssl.uploadFile(url, "media", media_file, params);
			System.out.println(s1);
		} catch (Exception e) {
			this.setError(e.getMessage());
			return null;
		}

		JSONObject obj;
		try {
			obj = new JSONObject(s1);
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}

		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
		}

		return obj.getString("media_id");

	}

	/**
	 * 增加视频素材
	 * 
	 * @param media_file
	 *            视频文件 大小: 不超过20M, 格式: rm, rmvb, wmv, avi, mpg, mpeg, mp4
	 * @param title
	 *            标题
	 * @param introduction
	 *            简介（选填）
	 * @return media_id
	 */
	public String createWeiXinMaterialVideo(String media_file, String title, String introduction) {
		String url = API_ROOT + "material/add_material?access_token=" + this.access_token_;
		JSONObject body = new JSONObject();
		body.put("title", title);
		body.put("introduction", introduction);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("description", body.toString());
		String s1;
		try {
			s1 = Ssl.uploadFile(url, "media", media_file, params);
			System.out.println(s1);
		} catch (Exception e) {
			this.setError(e.getMessage());
			return null;
		}

		JSONObject obj;
		try {
			obj = new JSONObject(s1);
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}

		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
			return null;
		}

		return obj.getString("media_id");

	}

	/**
	 * 获取全部新闻素材（永久） 图文（news）
	 * 
	 * @return
	 */
	public List<WeiXinMaterialArticle> getWeiXinMaterialArticles() {
		JSONObject total = this.getWeiXinMaterialCount();
		int news_count = total.getInt("news_count");

		List<WeiXinMaterialArticle> al = new ArrayList<WeiXinMaterialArticle>();

		if (news_count == 0) {
			return al;
		}
		int offset = 0;
		int count = 20;

		int inc = 0;
		while (inc < news_count) {
			List<WeiXinMaterialArticle> subs = this.getWeiXinMaterialArticles(offset, count);
			inc += subs.size();
			offset = inc;
			al.addAll(subs);
		}

		return al;
	}

	/**
	 * 获取新闻素材（永久） 图文（news）
	 * 
	 * @param offset
	 *            从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count
	 *            返回素材的数量，取值在1到20之间
	 * @return
	 */
	public List<WeiXinMaterialArticle> getWeiXinMaterialArticles(int offset, int count) {

		List<WeiXinMaterialArticle> al = new ArrayList<WeiXinMaterialArticle>();

		JSONObject obj = this.getWeiXinMaterialsQuery("news", offset, count);

		if (obj == null) {
			return null;
		}
		if (!obj.has("item")) {
			this.setError(obj.toString());
			return null;
		}

		JSONArray arr = obj.getJSONArray("item");

		for (int i = 0; i < arr.length(); i++) {
			JSONObject item = arr.getJSONObject(i);
			WeiXinMaterialArticle m = WeiXinMaterialArticle.parse(item);
			al.add(m);
		}
		return al;
	}

	/**
	 * 更新文章
	 * 
	 * @param article
	 *            文章对象
	 * @param index
	 *            要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0
	 * @return
	 */
	public boolean updateWeiXinMaterialArticle(WeiXinMaterialArticle article, int index) {
		String url = API_ROOT + "material/update_news?access_token=" + this.access_token_;
		JSONObject obj = this.postResult(url, article.toUpdateJson(index));

		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
			return false;
		}

		return true;
	}

	/**
	 * 获取全部新闻素材图片（image）、视频（video）、语音 （voice）
	 * 
	 * @return
	 */
	public List<WeiXinMaterial> getWeiXinMaterials(String type) {
		JSONObject total = this.getWeiXinMaterialCount();
		int news_count = total.getInt(type + "_count");

		List<WeiXinMaterial> al = new ArrayList<WeiXinMaterial>();

		if (news_count == 0) {
			return al;
		}
		int offset = 0;
		int count = 20;

		int inc = 0;
		while (inc < news_count) {

			List<WeiXinMaterial> subs = this.getWeiXinMaterials(type, offset, count);
			inc += subs.size();
			offset = inc;
			al.addAll(subs);
		}

		return al;
	}

	/**
	 * 获取素材列表 图片（image）、视频（video）、语音 （voice）
	 * 
	 * @param type
	 *            素材的类型，图片（image）、视频（video）、语音 （voice）
	 * @param offset
	 *            从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count
	 *            返回素材的数量，取值在1到20之间
	 * @return
	 */
	public List<WeiXinMaterial> getWeiXinMaterials(String type, int offset, int count) {

		JSONObject obj = this.getWeiXinMaterialsQuery(type, offset, count);

		if (obj == null) {
			return null;
		}
		if (!obj.has("item")) {
			this.setError(obj.toString());
			return null;
		}

		JSONArray arr = obj.getJSONArray("item");
		List<WeiXinMaterial> al = new ArrayList<WeiXinMaterial>();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject item = arr.getJSONObject(i);
			WeiXinMaterial m = WeiXinMaterial.parse(item, type);
			al.add(m);
		}
		return al;
	}

	/**
	 * 获取素材列表
	 * 
	 * @param type
	 *            素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
	 * @param offset
	 *            从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count
	 *            返回素材的数量，取值在1到20之间
	 * @return
	 */
	private JSONObject getWeiXinMaterialsQuery(String type, int offset, int count) {
		String url = API_ROOT + "material/batchget_material?access_token=" + this.access_token_;

		// { "type":TYPE, "offset":OFFSET,"count":COUNT}
		JSONObject body = new JSONObject();
		body.put("type", type);
		body.put("offset", offset);
		body.put("count", count);
		JSONObject obj = this.postResult(url, body);
		return obj;
	}

	/**
	 * 获取素材总数
	 * 
	 * voice_count 语音总数量 video_count 视频总数量 image_count 图片总数量 news_count 图文总数量
	 * 
	 * 
	 * @return
	 */
	public JSONObject getWeiXinMaterialCount() {
		String url = API_ROOT + "material/get_materialcount?access_token=" + this.access_token_;
		JSONObject obj = this.getResult(url);
		return obj;
	}

	/**
	 * 获取新闻文章
	 * 
	 * @param media_id
	 * @return
	 */
	public WeiXinMaterialArticle getWeiXinMaterialArticle(String media_id) {
		String url = API_ROOT + "material/get_material?access_token=" + this.access_token_;
		// { "media_id":MEDIA_ID }
		JSONObject body = new JSONObject();
		body.put("media_id", media_id);
		JSONObject obj = this.postResult(url, body);
		if (obj == null) {
			return null;
		}
		if (obj.has("errorcode") && obj.getInt("errorcode") != 0) {
			this.setError(obj.toString());

			return null;
		}
		WeiXinMaterialArticle a = WeiXinMaterialArticle.parse(obj);
		return a;
	}

	/**
	 * 新增永久图文素材
	 * 
	 * @param article
	 *            图文类
	 * @return media_id
	 */
	public String createWeiXinMaterialArticle(WeiXinMaterialArticle article) {
		String url = API_ROOT + "material/add_news?access_token=" + this.access_token_;

		JSONObject obj = this.postResult(url, article.toJson());
		if (obj == null) {
			return null;
		}
		if (obj.has("errorcode") && obj.getInt("errorcode") != 0) {
			this.setError(obj.toString());
			return null;
		}
		return obj.getString("media_id");
	}

	/**
	 * 获取视频文件地址
	 * 
	 * { "title":TITLE, "description":DESCRIPTION, "down_url":DOWN_URL, }
	 * 
	 * @param media_id
	 * @return
	 */
	public String getWeiXinMaterialVideo(String media_id) {
		String url = API_ROOT + "material/get_material?access_token=" + this.access_token_;
		JSONObject body = new JSONObject();
		body.put("media_id", media_id);
		JSONObject obj = this.postResult(url, body);
		if (obj == null) {
			return null;
		}
		if (obj.has("errorcode") && obj.getInt("errorcode") != 0) {
			this.setError(obj.toString());
			return null;
		}
		return obj.getString("down_url");
	}

	/**
	 * 下载媒体文件
	 * 
	 * @param media_id
	 * @return
	 */
	public byte[] getWeiXinMaterial(String media_id) {
		// https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN
		String url = API_ROOT + "material/get_material?access_token=" + this.access_token_;
		// { "media_id":MEDIA_ID }
		JSONObject body = new JSONObject();
		body.put("media_id", media_id);

		byte[] buf;
		try {
			buf = Ssl.download(url, body.toString());
			return buf;
		} catch (Exception e) {
			this.setError(e.getMessage());
			return null;
		}
	}

	/**
	 * 删除永久素材
	 * 
	 * 
	 * 1、请谨慎操作本接口，因为它可以删除公众号在公众平台官网素材管理模块中新建的图文消息、语音、视频等素材（
	 * 但需要先通过获取素材列表来获知素材的media_id）
	 * 
	 * 2、临时素材无法通过本接口删除
	 * 
	 * 3、调用该接口需https协议
	 * 
	 * @param media_id
	 *            要获取的素材的media_id
	 * @return
	 */
	public boolean deleteWeiXinMaterial(String media_id) {
		// https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=ACCESS_TOKEN
		String url = API_ROOT + "material/del_material?access_token=" + this.access_token_;
		// { "media_id":MEDIA_ID }
		JSONObject body = new JSONObject();
		body.put("media_id", media_id);
		JSONObject obj = this.postResult(url, body);
		if (obj == null) {
			return false;
		}
		if (obj.has("errorcode") && obj.getInt("errorcode") != 0) {
			this.setError(obj.toString());
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param weixin_shop_id
	 *            mch_id
	 * @param open_id
	 *            用户微信编号
	 * @param money
	 *            金额
	 * @param orderIndex
	 *            订单顺序号码
	 * @param wishing
	 *            红包祝福语
	 * @param client_ip
	 *            工作机IP
	 * @param act_name
	 *            活动名称
	 * @param remark
	 *            活动备注
	 * @param nick_name
	 *            提供方名称
	 * @param send_name
	 *            商户名称
	 * @return
	 */
	public WeiXinRedPackageResult sendRegPackage(String weixin_shop_id, String open_id, int money, int orderIndex,
			String wishing, String client_ip, String act_name, String remark, String nick_name, String send_name) {
		WeiXinRedPackage p = new WeiXinRedPackage();
		p.setWxappid(appID_);
		p.setMchId(weixin_shop_id);
		p.setReOpenid(open_id);

		// 红包值
		p.setMinValue(money);
		p.setMaxValue(money);
		p.setTotalAmount(money);

		// 红包数量
		p.setTotalNum(1);

		// 商户订单号 mch_billno 是 10000098201411111234567890 String(28)
		// 商户订单号（每个订单号必须唯一）
		// 组成： mch_id+yyyymmdd+10位一天内不能重复的数字。 接口根据商户订单号支持重入， 如出现超时可再调用。
		String dt = Utils.getDateString(new Date(), "yyyyMMdd");
		String mch_billno = weixin_shop_id + dt + SignUtils.fixNumberWithZero(orderIndex, 10);

		p.setMchBillno(mch_billno);
		// 红包祝福语
		p.setWishing(wishing);
		// 调用接口的机器Ip地址
		p.setClientIp(client_ip);

		p.setActName(act_name);
		p.setRemark(remark);

		// 提供方名称
		p.setNickName(nick_name);
		// 商户名称
		p.setSendName(send_name);

		String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
		try {
			String xmlBody = p.toPostXml(this.shop_key_);
			String rst = Ssl.pcks12(this.sslcontext_, url, xmlBody);
			Document doc1 = UXml.asDocument(rst);

			WeiXinRedPackageResult order = new WeiXinRedPackageResult();
			order.setXml(rst);

			NodeList nl1 = doc1.getFirstChild().getChildNodes();
			for (int i = 0; i < nl1.getLength(); i++) {
				Node node = nl1.item(i);
				if (node.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				String name = node.getNodeName();
				String val = node.getTextContent();

				order.setParameter(name, val);
			}
			order.setSendPackage(p);
			p.setOrderResult(order);
			return order;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	/**
	 * * 公众账号ID appid 是 String(32) wx8888888888888888 微信分配的公众账号ID<br>
	 * 商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号<br>
	 * 设备号 device_info 否 String(32) 013467007045764 终端设备号(游戏wap支付此字段必传)<br>
	 * 随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 * 随机字符串，不长于32位。推荐随机数生成算法<br>
	 * 签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名生成算法<br>
	 * 商品描述 body 是 String(32) Ipad mini 16G 白色 商品或支付单简要描述<br>
	 * 商品详情 detail 否 String(8192) Ipad mini 16G 白色 商品名称明细列表<br>
	 * 附加数据 attach 否 String(127) 说明 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据 <br>
	 * 商户订单号 out_trade_no 是 String(32) 1217752501201407033233368018 ---
	 * 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号<br>
	 * 货币类型 fee_type 否 String(16) CNY 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型 <br>
	 * 总金额 total_fee 是 Int 888 订单总金额，只能为整数，详见支付金额<br>
	 * 终端IP spbill_create_ip 是 String(16) 8.8.8.8
	 * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。<br>
	 * 交易起始时间 time_start 否 String(14) 20091225091010
	 * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则 <br>
	 * 交易结束时间 time_expire 否 String(14) 20091227091010
	 * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则 <br>
	 * 商品标记 goods_tag 否 String(32) WXG 商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠<br>
	 * 通知地址 notify_url 是 String(256) http://www.baidu.com/ 接收微信支付异步通知回调地址<br>
	 * 交易类型 trade_type 是 String(16) JSAPI 取值如下：JSAPI，NATIVE，APP，WAP,详细说明见参数规定 <br>
	 * 商品ID product_id 否 String(32) 12235413214070356458058
	 * trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。<br>
	 * 用户标识 openid 否 String(128)oUpF8uMuAJO_M2pxb1Q9zNjWeS6o trade_type=JSAPI，此参数必传
	 * ，用户在商户appid下的唯一标识。下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。<br>
	 * 
	 * @param weixin_shop_id
	 *            商户编号
	 * @param ip
	 *            客户端ip地址
	 * @param openid
	 *            用户openid
	 * @param out_trade_no
	 *            商户订单号 String(32)
	 * @param body
	 *            商品描述 String(32)
	 * @param detail
	 *            商品详情 detail 否 String(8192)
	 * @param attach
	 *            附加数据 attach 否 String(127)
	 * @param total_fee
	 *            交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。
	 * @param notify_url
	 *            接受财付通的url String(256)
	 * @throws Exception
	 */
	public WeiXinOrder createWeiXinOrder(String weixin_shop_id, String ip, String openid, String out_trade_no,
			String body, String detail, String attach, int total_fee, String notify_url) throws Exception {

		if (this.shop_key_ == null || this.shop_key_.trim().length() == 0) {
			throw new Exception("ShopKey not defined");
		}
		// https://api.mch.weixin.qq.com/pay/unifiedorder
		// 公司名称 北京诺美莱国际商务咨询有限公司
		// 微信支付商户号 10094047
		// 商户名称 CCA夏令营协会
		// 网站域名
		// 客服电话 4008909151
		/**
		 * <xml> <appid>wx2421b1c4370ec43b</appid> <attach>支付测试</attach>
		 * <body>JSAPI支付测试</body> <mch_id>10000100</mch_id>
		 * <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str> <notify_url>http
		 * ://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php</notify_url>
		 * <openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
		 * <out_trade_no>1415659990</out_trade_no>
		 * <spbill_create_ip>14.23.150.211</spbill_create_ip> <total_fee>1</total_fee>
		 * <trade_type>JSAPI</trade_type> <sign>0CB01533B8C1EF103065174F50BCA001</sign>
		 * </xml> 注：参数值用XML转义即可，CDATA标签用于说明数据不被XML解析器解析。
		 */

		Document doc = UXml.asDocument("<xml />");
		this.addXmlNode(doc, "mch_id", weixin_shop_id);
		// this.addXmlNode(doc, "mch_id", weixin_shop_id);

		this.addXmlNode(doc, "appid", this.appID_);
		this.addXmlNode(doc, "trade_type", "JSAPI");

		this.addXmlNode(doc, "spbill_create_ip", ip);
		this.addXmlNode(doc, "fee_type", "CNY");
		this.addXmlNode(doc, "openid", openid);

		// 商户订单号 String(32) 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
		this.addXmlNode(doc, "out_trade_no", out_trade_no);

		// 随机字符串，不长于32位。推荐随机数生成算法
		String nonce_str = SignUtils.nonceStr();
		this.addXmlNode(doc, "nonce_str", nonce_str);

		// 交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。
		this.addXmlNode(doc, "total_fee", total_fee + "");

		this.addXmlNode(doc, "body", body);
		this.addXmlNode(doc, "detail", detail);
		this.addXmlNode(doc, "attach", attach);

		this.addXmlNode(doc, "notify_url", notify_url);

		NodeList nl = doc.getFirstChild().getChildNodes();
		String[] list = new String[nl.getLength()];
		for (int i = 0; i < nl.getLength(); i++) {
			list[i] = nl.item(i).getNodeName();
		}
		Arrays.sort(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			String name = list[i];
			String val = doc.getElementsByTagName(name).item(0).getTextContent();
			if (val != null && val.trim().length() > 0) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(name);
				sb.append("=");
				sb.append(val);
			}
		}
		sb.append("&key=" + this.shop_key_);
		String sign_sorce = sb.toString();

		// System.out.println(sign_sorce);
		String sign = SignUtils.signMd5(sign_sorce);

		this.addXmlNode(doc, "sign", sign.toUpperCase());

		String xml = UXml.asXml(doc);
		System.out.println(xml.replace("><", ">\n<"));

		String u1 = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String rst = this.postResult(u1, xml);

		if (rst == null) {
			return null;
		}

		WeiXinOrder order = this.parseWeiXinOrder(rst);

		return order;
	}

	/**
	 * 统一下单 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
	 * 
	 * @param weixin_shop_id
	 *            商户编号
	 * @param ip
	 *            客户端ip地址
	 * @param out_trade_no
	 *            商户订单号 String(32)
	 * @param body
	 *            商品描述 String(32)
	 * @param detail
	 *            商品详情 detail 否 String(8192)
	 * @param attach
	 *            附加数据 attach 否 String(127)
	 * @param total_fee
	 *            交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。
	 * @param notify_url
	 *            接受财付通的url String(256)
	 * @throws Exception
	 */
	public WeiXinOrder createWeiXinOrderApp(String weixin_shop_id, String ip, String out_trade_no, String body,
			String detail, String attach, int total_fee, String notify_url) throws Exception {

		if (this.shop_key_ == null || this.shop_key_.trim().length() == 0) {
			throw new Exception("ShopKey not defined");
		}

		String nonce_str = SignUtils.nonceStr();

		Document doc = UXml.asDocument("<xml />");
		// 微信开放平台审核通过的应用APPID
		this.addXmlNode(doc, "appid", this.appID_);
		// 微信支付分配的商户号
		this.addXmlNode(doc, "mch_id", weixin_shop_id);
		// 终端设备号(门店号或收银设备ID)，默认请传"WEB"
		this.addXmlNode(doc, "device_info", "WEB");
		// 随机字符串，不长于32位。推荐随机数生成算法
		this.addXmlNode(doc, "nonce_str", nonce_str);
		// 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
		this.addXmlNode(doc, "sign_type", "MD5");
		// 商品描述交易字段格式根据不同的应用场景按照以下格式：APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
		this.addXmlNode(doc, "body", body);
		// 商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”String(8192)
		this.addXmlNode(doc, "detail", detail);
		// 商户订单号 String(32) 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
		this.addXmlNode(doc, "out_trade_no", out_trade_no);
		// 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
		this.addXmlNode(doc, "fee_type", "CNY");
		// 交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。
		this.addXmlNode(doc, "total_fee", total_fee + "");
		// 用户端实际ip
		this.addXmlNode(doc, "spbill_create_ip", ip);
		// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。String(256)
		this.addXmlNode(doc, "notify_url", notify_url);
		// 支付类型
		this.addXmlNode(doc, "trade_type", "APP");
		// 附加数据 attach 否 String(127) 深圳分店
		this.addXmlNode(doc, "attach", attach);

		/*
		 * 场景信息 scene_info 否 String(256) 该字段用于统一下单时上报场景信息，目前支持上报实际门店信息。{"store_id": "",
		 * //门店唯一标识，选填，String(32)"store_name":"”//门店名称，选填，String(64) } 指定支付方式 limit_pay
		 * 否 String(32) no_credit no_credit--指定不能使用信用卡支付 订单优惠标记 goods_tag 否 String(32)
		 * WXG 订单优惠标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠 交易起始时间 time_start 否 String(14)
		 * 20091225091010
		 * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。 其他详见时间规则
		 * 交易结束时间 time_expire 否 String(14) 20091227091010
		 * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。 其他详见时间规则
		 * 注意：最短失效时间间隔必须大于5分钟
		 */

		NodeList nl = doc.getFirstChild().getChildNodes();
		String[] list = new String[nl.getLength()];
		for (int i = 0; i < nl.getLength(); i++) {
			list[i] = nl.item(i).getNodeName();
		}
		Arrays.sort(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			String name = list[i];
			String val = doc.getElementsByTagName(name).item(0).getTextContent();
			if (val != null && val.trim().length() > 0) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(name);
				sb.append("=");
				sb.append(val);
			}
		}
		sb.append("&key=" + this.shop_key_);
		String sign_sorce = sb.toString();

		// System.out.println(sign_sorce);
		String sign = SignUtils.signMd5(sign_sorce);

		this.addXmlNode(doc, "sign", sign.toUpperCase());

		String xml = UXml.asXml(doc);
		// System.out.println(xml.replace("><", ">\n<"));

		String u1 = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String rst = this.postResult(u1, xml);

		if (rst == null) {
			return null;
		}

		WeiXinOrder order = this.parseWeiXinOrder(rst);
		order.setRequestSource(xml);
		order.setResultSource(rst);
		return order;
	}

	/**
	 * 创建订单（Native）模式，为了获取扫码模式2
	 * 
	 * @param mch_id
	 *            mch_id
	 * @param ip
	 * @param openid
	 *            trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【
	 *            获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【
	 *            企业号userid转openid接口】进行转换
	 * @param out_trade_no
	 * @param body
	 * @param detail
	 * @param attach
	 * @param total_fee
	 * @param notify_url
	 * @return
	 * @throws Exception
	 */
	public WeiXinOrder createWeiXinOrderNative(String mch_id, String ip, String openid, String out_trade_no,
			String body, String detail, String attach, int total_fee, String notify_url) throws Exception {

		if (this.shop_key_ == null || this.shop_key_.trim().length() == 0) {
			throw new Exception("ShopKey not defined");
		}

		/**
		 * <xml> <appid>wx2421b1c4370ec43b</appid> <attach>支付测试</attach>
		 * <body>JSAPI支付测试</body> <mch_id>10000100</mch_id>
		 * <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str> <notify_url>http
		 * ://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php</notify_url>
		 * <openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
		 * <out_trade_no>1415659990</out_trade_no>
		 * <spbill_create_ip>14.23.150.211</spbill_create_ip> <total_fee>1</total_fee>
		 * <trade_type>JSAPI</trade_type> <sign>0CB01533B8C1EF103065174F50BCA001</sign>
		 * </xml> 注：参数值用XML转义即可，CDATA标签用于说明数据不被XML解析器解析。
		 */

		Document doc = UXml.asDocument("<xml />");
		this.addXmlNode(doc, "mch_id", mch_id);

		this.addXmlNode(doc, "appid", this.appID_);
		this.addXmlNode(doc, "trade_type", "NATIVE");

		this.addXmlNode(doc, "spbill_create_ip", ip);

		// 货币类型 fee_type 否 String(16) CNY 符合ISO
		// 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
		this.addXmlNode(doc, "fee_type", "CNY");
		this.addXmlNode(doc, "openid", openid);

		// 商户订单号 String(32) 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
		this.addXmlNode(doc, "out_trade_no", out_trade_no);

		// 随机字符串，不长于32位。推荐随机数生成算法
		String nonce_str = SignUtils.nonceStr();
		this.addXmlNode(doc, "nonce_str", nonce_str);

		// 交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。
		this.addXmlNode(doc, "total_fee", total_fee + "");

		// 商品描述 body 是 String(128) Ipad mini 16G 白色 商品或支付单简要描述
		this.addXmlNode(doc, "body", body);
		// 商品详情 detail 否 String(8192) Ipad mini 16G 白色 商品名称明细列表
		this.addXmlNode(doc, "detail", detail);
		// 附加数据 attach 否 String(127) 深圳分店
		// 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
		this.addXmlNode(doc, "attach", attach);

		this.addXmlNode(doc, "notify_url", notify_url);

		NodeList nl = doc.getFirstChild().getChildNodes();
		String[] list = new String[nl.getLength()];
		for (int i = 0; i < nl.getLength(); i++) {
			list[i] = nl.item(i).getNodeName();
		}
		Arrays.sort(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			String name = list[i];
			String val = doc.getElementsByTagName(name).item(0).getTextContent();
			if (val != null && val.trim().length() > 0) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(name);
				sb.append("=");
				sb.append(val);
			}
		}
		sb.append("&key=" + this.shop_key_);
		String sign_sorce = sb.toString();

		// System.out.println(sign_sorce);
		String sign = SignUtils.signMd5(sign_sorce);

		this.addXmlNode(doc, "sign", sign.toUpperCase());

		String xml = UXml.asXml(doc);
		System.out.println(xml.replace("><", ">\n<"));

		String u1 = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String rst = this.postResult(u1, xml);

		if (rst == null) {
			return null;
		}

		WeiXinOrder order = this.parseWeiXinOrder(rst);
		return order;
	}

	/**
	 * 将xml字符串转换为 WeiXinOrder
	 * 
	 * @param xmlStr
	 * @return
	 */
	public WeiXinOrder parseWeiXinOrder(String xmlStr) {
		WeiXinOrder order = new WeiXinOrder();
		Document doc1 = UXml.asDocument(xmlStr);

		order.setXml(xmlStr);

		NodeList nl1 = doc1.getFirstChild().getChildNodes();
		for (int i = 0; i < nl1.getLength(); i++) {
			Node node = nl1.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String name = node.getNodeName();
			String val = node.getTextContent();

			if (name.equals("return_code")) {
				order.setReturnCode(val);
			} else if (name.equals("return_msg")) {
				order.setReturnMsg(val);
			} else if (name.equals("appid")) {
				order.setAppId(val);
			} else if (name.equals("mch_id")) {
				order.setMchId(val);
			} else if (name.equals("nonce_str")) {
				order.setNonceStr(val);
			} else if (name.equals("sign")) {
				order.setSign(val);
			} else if (name.equals("result_code")) {
				order.setResultCode(val);
			} else if (name.equals("prepay_id")) {
				order.setPrepayId(val);
			} else if (name.equals("trade_type")) {
				order.setTradeType(val);
			} else if (name.equals("err_code")) {
				order.setErrCode(val);
			} else if (name.equals("err_code_des")) {
				order.setErrCodeDes(val);
			} else if (name.equals("device_info")) {
				order.setDeviceInfo(val);
			} else if (name.equals("code_url")) {
				order.setCodeUrl(val);
			}
		}
		return order;
	}

	/**
	 * 获取订单信息
	 * 
	 * 
	 * 公众账号ID appid 是 String(32) wx8888888888888888 微信分配的公众账号ID<br>
	 * 商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号<br>
	 * 微信订单号 transaction_id 否 String(32) 013467007045764 微信的订单号，优先使用<br>
	 * 商户订单号 out_trade_no 否 String(32) 商户系统内部的订单号，当没提供transaction_id时需要传这个。<br>
	 * 随机字符串 nonce_str 是 String(32) C380BEC2BFD727A4B6845133519F3AD6
	 * 随机字符串，不长于32位。推荐随机数生成算法<br>
	 * 签名 sign 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 签名，详见签名生成算法<br>
	 * 
	 * @param weixin_shop_id
	 *            商户编号
	 * @param orderNo
	 *            微信订单号/商户订单号
	 * @param isWeiXinOrder
	 *            是否按照微信的订单号查询
	 * @return
	 */
	public WeiXinOrderResult getWeiXinOrderResult(String weixin_shop_id, String orderNo, boolean isWeiXinOrder) {

		String nonce_str = Utils.getGuid().replace("-", "");
		if (nonce_str.length() > 32) {
			nonce_str = nonce_str.substring(0, 32);
		}
		Document doc = UXml.asDocument("<xml />");
		this.addXmlNode(doc, "mch_id", weixin_shop_id);

		this.addXmlNode(doc, "appid", this.appID_);
		if (isWeiXinOrder) {
			this.addXmlNode(doc, "transaction_id", orderNo);
		} else {
			this.addXmlNode(doc, "out_trade_no", orderNo);
		}
		this.addXmlNode(doc, "nonce_str", nonce_str);

		String sign = SignUtils.signMd5(doc, "key", this.shop_key_);

		this.addXmlNode(doc, "sign", sign.toUpperCase());

		String xml = UXml.asXml(doc);
		// System.out.println(xml.replace("><", ">\n<"));

		String u1 = "https://api.mch.weixin.qq.com/pay/orderquery";
		String rst = this.postResult(u1, xml);

		if (rst == null) {
			return null;
		}
		WeiXinOrderResult rst1 = new WeiXinOrderResult();
		Document doc1 = UXml.asDocument(rst);
		NodeList nl1 = doc1.getFirstChild().getChildNodes();
		rst1.setXml(rst);
		for (int i = 0; i < nl1.getLength(); i++) {
			Node node = nl1.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String name = node.getNodeName();
			String val = node.getTextContent();
			rst1.setParameter(name, val);
		}

		return rst1;
	}

	public String downloadWeiXinBill(String bill_date) {
		String url = "https://api.mch.weixin.qq.com/pay/downloadbill";

		// <xml>
		// <appid>wx2421b1c4370ec43b</appid>
		// <bill_date>20141110</bill_date>
		// <bill_type>ALL</bill_type>
		// <mch_id>10000100</mch_id>
		// <nonce_str>21df7dc9cd8616b56919f20d9f679233</nonce_str>
		// <sign>332F17B766FC787203EBE9D6E40457A1</sign>
		// </xml>

		Document doc = UXml.asDocument("<xml />");
		this.addXmlNode(doc, "mch_id", this.shop_id_);
		this.addXmlNode(doc, "appid", this.appID_);
		this.addXmlNode(doc, "nonce_str", SignUtils.nonceStr());
		this.addXmlNode(doc, "bill_type", "ALL");
		this.addXmlNode(doc, "bill_date", bill_date);
		String sign = SignUtils.signMd5(doc, "key", this.shop_key_);

		this.addXmlNode(doc, "sign", sign.toUpperCase());

		String xml = UXml.asXml(doc);

		String rst = this.postResult(url, xml);
		return rst;
	}

	private void addXmlNode(Document doc, String nodeName, String text) {
		// Element ele = doc.createElement(nodeName);
		// // CDATASection eleCdata = doc.createCDATASection(text);
		// // ele.appendChild(eleCdata);
		// if (text != null) {
		// ele.setTextContent(text);
		// doc.getFirstChild().appendChild(ele);
		// }

		SignUtils.addXmlNode(doc, nodeName, text);
	}

	/**
	 * 用第一步拿到的access_token 采用http GET方式请求获得jsapi_ticket
	 * 
	 * @return
	 */
	public WeiXinTicket getWeiXinTicketJsapi() {
		return this.getWeiXinTicket("jsapi");
	}

	/**
	 * 用第一步拿到的access_token 采用http GET方式请求获得 ticket
	 * 
	 * @param type
	 * @return
	 */
	public WeiXinTicket getWeiXinTicket(String type) {
		// jsapi
		if (this.mapTickets_.containsKey(type)) {
			WeiXinTicket t = this.mapTickets_.get(type);
			if (t.getExpiresTime() - System.currentTimeMillis() > 50 * 1000) {
				return t;
			} else {
				this.mapTickets_.remove(type);
			}
		}

		String u1 = API_ROOT + "ticket/getticket?access_token=" + this.access_token_ + "&type=" + type;
		JSONObject obj = this.getResult(u1);
		if (obj == null) {
			return null;
		}
		WeiXinTicket ticket = new WeiXinTicket();

		Iterator<?> it = obj.keys();
		try {
			while (it.hasNext()) {
				String key = it.next().toString();
				Object ov = obj.get(key);
				if (ov == null) {
					continue;
				}
				String v = ov.toString();

				if (key.equals("errcode")) {
					int errcode = Integer.parseInt(v);
					ticket.setErrcode(errcode);
				} else if (key.equals("errmsg")) {
					ticket.setErrmsg(v);
				} else if (key.equals("ticket")) {
					ticket.setTicket(v);

				} else if (key.equals("expires_in")) {
					int expires_in = Integer.parseInt(v);
					ticket.setExpires_in(expires_in);
				}
			}
			ticket.setJsonStr(obj.toString());

			this.mapTickets_.put(type, ticket);
			return ticket;
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}
	}

	/**
	 * 检查是否是合法IP地址
	 * 
	 * @param ip
	 * @return
	 */
	public boolean checkVaildIp(String ip) {
		if (ip == null || ip.trim().length() == 0) {
			return false;
		}
		this.getWeiXinIps();
		ip = ip.trim();
		for (int i = 0; i < this.weixinVaildIps_.length; i++) {
			if (this.weixinVaildIps_[i].equals(ip)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取微信服务器IP地址
	 * 
	 * @return
	 */
	public String[] getWeiXinIps() {
		if (this.weixinVaildIps_ != null && this.weixinVaildIps_.length > 0) {
			return this.weixinVaildIps_;
		}
		String url = API_ROOT + "getcallbackip?access_token=" + this.access_token_;

		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return null;
		}
		try {
			JSONArray arr = obj.getJSONArray("ip_list");
			String[] ips = new String[arr.length()];
			for (int i = 0; i < arr.length(); i++) {
				ips[i] = arr.getString(i);
			}
			this.weixinVaildIps_ = ips;
			return ips;
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}
	}

	/**
	 * 重置用户列表数据
	 */
	public void resetUserList() {
		this.weiXinUserList_ = new WeiXinUserList();
	}

	/**
	 * 获取关注用户数量
	 * 
	 * @return
	 */
	public int getWeiXinUserCount() {
		String url = API_ROOT + "user/get?access_token=" + this.access_token_ + "&next_openid=";
		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return -1;
		}
		try {
			int total = obj.getInt("total");
			return total;
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return -1;
		}
	}

	/**
	 * 
	 * 获取一次(只有open_id),一次拉取调用最多拉取10000个关注者的OpenID
	 * 
	 * 公众号可通过本接口来获取帐号的关注者列表，关注者列表由一串OpenID（加密后的微信号，每个用户对每个公众号的OpenID是唯一的）组成。
	 * 一次拉取调用最多拉取10000个关注者的OpenID，可以通过多次拉取的方式来满足需求。
	 * 
	 * @param NEXT_OPENID
	 * @return
	 */
	public WeiXinUserList getWeiXinUserListOnce(String NEXT_OPENID) {
		NEXT_OPENID = NEXT_OPENID == null ? "" : NEXT_OPENID.trim();
		WeiXinUserList lst = new WeiXinUserList();
		String url = API_ROOT + "user/get?access_token=" + this.access_token_ + "&next_openid=" + NEXT_OPENID;
		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return null;
		}
		try {
			int total = obj.getInt("total");
			int count = obj.getInt("count");
			String NEXT_OPENID1 = obj.getString("next_openid");
			lst.setCount(count);

			lst.setTotal(total);
			lst.setNextOpenid(NEXT_OPENID1);

			if (obj.has("data")) {
				JSONArray arr = obj.getJSONObject("data").getJSONArray("openid");
				for (int i = 0; i < arr.length(); i++) {
					String open_id = arr.getString(i);
					lst.getUsers().put(open_id, open_id);
				}
			}
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}
		return lst;
	}

	/**
	 * 公众号可通过本接口来获取帐号的关注者列表， 关注者列表由一串OpenID（加密后的微信号，每个用户对每个公众号的OpenID是唯一的）组成。
	 * 一次拉取调用最多拉取10000个关注者的OpenID，可以通过多次拉取的方式来满足需求。 获取用户列表(全部)
	 * 
	 * @param NEXT_OPENID
	 * @return
	 */
	public WeiXinUserList getWeiXinUserList(String NEXT_OPENID) {
		NEXT_OPENID = NEXT_OPENID == null ? "" : NEXT_OPENID.trim();
		if (NEXT_OPENID.length() == 0) {
			this.weiXinUserList_ = new WeiXinUserList();
		}
		String url = API_ROOT + "user/get?access_token=" + this.access_token_ + "&next_openid=" + NEXT_OPENID;
		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return null;
		}
		try {
			int total = obj.getInt("total");
			int count = obj.getInt("count");
			String NEXT_OPENID1 = obj.getString("next_openid");
			this.weiXinUserList_.setCount(this.weiXinUserList_.getCount() + count);

			this.weiXinUserList_.setTotal(total);
			this.weiXinUserList_.setNextOpenid(NEXT_OPENID1);

			if (obj.has("data")) {
				JSONArray arr = obj.getJSONObject("data").getJSONArray("openid");
				for (int i = 0; i < arr.length(); i++) {
					String open_id = arr.getString(i);
					this.weiXinUserList_.getUsers().put(open_id, open_id);
				}
			}
			if (this.weiXinUserList_.getCount() < total && NEXT_OPENID1.length() > 0) {
				this.getWeiXinUserList(NEXT_OPENID1);
			}

		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}
		return this.weiXinUserList_;
	}

	/**
	 * 获取用户基本信息（包括UnionID机制）
	 * 
	 * @param openId
	 * @return
	 */
	public WeiXinUser getWeiXinUserInfo(String openId) {
		// access_token 是 调用接口凭证
		// openid 是 普通用户的标识，对当前公众号唯一
		// lang 否 返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
		String url = API_ROOT + "user/info?access_token=" + this.access_token_ + "&openid=" + openId + "&lang=zh_CN";
		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return null;
		}
		Iterator<?> it = obj.keys();
		WeiXinUser u = new WeiXinUser();
		try {
			while (it.hasNext()) {
				String key = it.next().toString();
				Object ov = obj.get(key);
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
				} else if (key.equals("groupid")) {
					int groupid = Integer.parseInt(v);
					u.setGroupId(groupid);
				}
			}
			u.setJsonStr(obj.toString());
			return u;
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}
	}

	/**
	 * 本接口将会提供公众号当前使用的自定义菜单的配置，如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，
	 * 而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置。
	 * 
	 * @return
	 */
	public WeiXinButton getWeiXinButtonsSelf() {
		String url = API_ROOT + "get_current_selfmenu_info?access_token=" + this.access_token_;
		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return null;
		}
		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
			return null;
		}
		return WeiXinButton.parseAll(obj);
	}

	/**
	 * 自定义菜单查询接口
	 * 
	 * @return
	 */
	public WeiXinButton getWeixinButtons() {
		String u = API_ROOT + "menu/get?access_token=" + this.access_token_;
		JSONObject obj = this.getResult(u);
		if (obj == null) {
			return null;
		}

		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
			return null;
		}
		return WeiXinButton.parseAll(obj);
	}

	/**
	 * 发微信消息（文字）
	 * 
	 * @param touser
	 *            用户的open_id
	 * @param content
	 *            内容
	 * @return
	 */
	public boolean sendWeiXinServiceMsgText(String touser, String content) {
		// 客服接口-发消息
		// https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
		// {
		// "touser":"OPENID",
		// "msgtype":"text",
		// "text":
		// {
		// "content":"Hello World"
		// }
		// }

		JSONObject msg = new JSONObject();
		JSONObject msgContent = new JSONObject();
		try {
			msg.put("touser", touser);
			msg.put("msgtype", "text");
			msgContent.put("content", content);
			msg.put("text", msgContent);

			return this.sendWeiXinServiceMsg(msg);

		} catch (JSONException e) {
			this.setError(e.getMessage());
			return false;
		}
	}

	/**
	 * 发微信消息（文字）
	 * 
	 * @param touser
	 *            用户的open_id
	 * @param content
	 *            内容
	 * @return
	 */
	public int sendWeiXinServiceMsgText1(String touser, String content) {
		// 客服接口-发消息
		// https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
		// {
		// "touser":"OPENID",
		// "msgtype":"text",
		// "text":
		// {
		// "content":"Hello World"
		// }
		// }

		JSONObject msg = new JSONObject();
		JSONObject msgContent = new JSONObject();
		try {
			msg.put("touser", touser);
			msg.put("msgtype", "text");
			msgContent.put("content", content);
			msg.put("text", msgContent);

			return this.sendWeiXinServiceMsg1(msg);

		} catch (JSONException e) {
			this.setError(e.getMessage());
			return -1;
		}
	}

	/**
	 * 发微信消息（文字）
	 * 
	 * @param touser
	 *            用户的open_id
	 * @param media_id
	 *            media_id
	 * @return
	 */
	public boolean sendWeiXinServiceMsgImage(String touser, String media_id) {
		// {
		// "touser":"OPENID",
		// "msgtype":"image",
		// "image":
		// {
		// "media_id":"MEDIA_ID"
		// }
		// }

		JSONObject msg = new JSONObject();
		JSONObject msgContent = new JSONObject();
		try {
			msg.put("touser", touser);
			msg.put("msgtype", "image");
			msgContent.put("media_id", media_id);
			msg.put("image", msgContent);

			return this.sendWeiXinServiceMsg(msg);

		} catch (JSONException e) {
			this.setError(e.getMessage());
			return false;
		}
	}

	/**
	 * 发送图文消息 图文消息条数限制在10条以内，注意，如果图文数超过10，则将会无响应
	 * 
	 * @param touser
	 *            用户的open_id
	 * @param articles
	 *            新闻对象，注意，如果图文数超过10，则将会无响应
	 * @return
	 */
	public boolean sendWeiXinServiceMsgNews(String touser, ArrayList<WeiXinArticle> articles) {
		// {
		// "touser":"OPENID",
		// "msgtype":"news",
		// "news":{
		// "articles": [
		// {
		// "title":"Happy Day",
		// "description":"Is Really A Happy Day",
		// "url":"URL",
		// "picurl":"PIC_URL"
		// },
		// {
		// "title":"Happy Day",
		// "description":"Is Really A Happy Day",
		// "url":"URL",
		// "picurl":"PIC_URL"
		// }
		// ]
		// }
		// }

		JSONObject msg = new JSONObject();
		JSONObject msgContent = new JSONObject();
		JSONArray arts = new JSONArray();
		try {
			msg.put("touser", touser);
			msg.put("msgtype", "news");
			msg.put("news", msgContent);

			for (int i = 0; i < articles.size(); i++) {
				if (i == 10) {
					break;
				}
				JSONObject artcle = articles.get(i).toJSON();
				arts.put(artcle);

			}
			msgContent.put("articles", arts);

			return this.sendWeiXinServiceMsg(msg);

		} catch (JSONException e) {
			this.setError(e.getMessage());
			return false;
		}
	}

	/**
	 * 根据分组进行群发【订阅号与服务号认证后均可用】<br>
	 * 
	 * 1、对于认证订阅号，群发接口每天可成功调用1次，此次群发可选择发送给全部用户或某个分组；
	 * 2、对于认证服务号虽然开发者使用高级群发接口的每日调用限制为100次
	 * ，但是用户每月只能接收4条，无论在公众平台网站上，还是使用接口群发，用户每月只能接收4条群发消息，多于4条的群发将对该用户发送失败；
	 * 3、具备微信支付权限的公众号，在使用群发接口上传、群发图文消息类型时，可使用<a>标签加入外链；
	 * 4、开发者可以使用预览接口校对消息样式和排版，通过预览接口可发送编辑好的消息给指定用户校验效果。
	 * 
	 * filter 是 用于设定图文消息的接收者 <br>
	 * is_to_all 否 用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，
	 * 选择false可根据group_id发送给指定群组的用户 <br>
	 * group_id 否 群发到的分组的group_id，参加用户管理中用户分组接口，若is_to_all值为true，可不填写group_id mpnews
	 * 是 用于设定即将发送的图文消息<br>
	 * media_id 是 用于群发的消息的media_id<br>
	 * msgtype 是 群发的消息类型，图文消息为mpnews， 文本消息为text， 语音为voice， 音乐为music， 图片为image，
	 * 视频为video<br>
	 * title 否 消息的标题<br>
	 * description 否 消息的描述<br>
	 * thumb_media_id 是 视频缩略图的媒体ID
	 * 
	 * { "filter":{ "is_to_all":false "group_id":"2" }, "mpnews":{
	 * "media_id":"123dsdajkasd231jhksad" }, "msgtype":"mpnews" }
	 * 
	 * @param group_id
	 *            分组编号
	 * @param media_id
	 *            媒体编号
	 * @return
	 */
	public boolean sendWeiXinArticleByGroup(int group_id, String media_id) {

		JSONObject body = new JSONObject();
		body.put("msgtype", "mpnews");

		JSONObject mpnews = new JSONObject();
		body.put("mpnews", mpnews);
		mpnews.put("media_id", media_id);

		JSONObject filter = new JSONObject();
		body.put("filter", filter);
		filter.put("is_to_all", false);
		filter.put("group_id", group_id);

		return this.sendWeiXinMsgs(body);
	}

	/**
	 * 群发消息到全体
	 * 
	 * @param media_id
	 * @return
	 */
	public boolean sendWeiXinArticleAll(String media_id) {
		JSONObject body = new JSONObject();
		body.put("msgtype", "mpnews");

		JSONObject mpnews = new JSONObject();
		body.put("mpnews", mpnews);
		mpnews.put("media_id", media_id);

		JSONObject filter = new JSONObject();
		body.put("filter", filter);
		filter.put("is_to_all", true);
		return this.sendWeiXinMsgs(body);
	}

	/**
	 * 群发消息
	 * 
	 * @param body
	 * @return
	 */
	private boolean sendWeiXinMsgs(JSONObject body) {
		String url = API_ROOT + "message/mass/sendall?access_token=" + this.access_token_;
		JSONObject obj = this.postResult(url, body);
		if (obj == null) {
			return false;
		}

		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(obj.toString());
			return false;
		}
	}

	/**
	 * 预览接口News【订阅号与服务号认证后均可用】<br>
	 * 开发者可通过该接口发送消息给指定用户，在手机端查看消息的样式和排版。
	 * 
	 * { "touser":"OPENID", "mpnews":{ "media_id":"xxx" }, "msgtype":"mpnews" }
	 * 
	 * @param media_id
	 * @param open_id
	 * @return
	 */
	public boolean sendWeiXinMsgPreviewNews(String media_id, String open_id) {
		String url = API_ROOT + "message/mass/preview?access_token=" + this.access_token_;
		JSONObject body = new JSONObject();
		body.put("touser", open_id);
		body.put("msgtype", "mpnews");

		JSONObject mpnews = new JSONObject();
		body.put("mpnews", mpnews);
		mpnews.put("media_id", media_id);

		JSONObject obj = this.postResult(url, body);
		if (obj == null) {
			return false;
		}

		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(obj.toString());
			return false;
		}
	}

	/**
	 * 发客服消息公共方法
	 * 
	 * @param msg
	 * @return
	 */
	public boolean sendWeiXinServiceMsg(JSONObject msg) {
		// 客服接口-发消息
		// https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
		String url = API_ROOT + "message/custom/send?access_token=" + this.access_token_ + "&method=POST&body=0";
		JSONObject obj = this.postResult(url, msg);
		if (obj == null) {
			return false;
		}
		// 正确时的JSON返回结果
		// {
		// "errcode": 0,
		// "errmsg": "ok"
		// }

		try {
			int errcode = obj.getInt("errcode");
			if (errcode == 0) {
				return true;
			} else {
				this.setError(obj.toString());
				return false;
			}
		} catch (JSONException e) {
			this.setError(e.getMessage());

			return false;
		}
	}

	/**
	 * 发客服消息公共方法返回错误或正确代码
	 * 
	 * @param msg
	 * @return int
	 */
	public int sendWeiXinServiceMsg1(JSONObject msg) {
		// 客服接口-发消息
		// https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
		String url = API_ROOT + "message/custom/send?access_token=" + this.access_token_ + "&method=POST&body=0";
		JSONObject obj = this.postResult(url, msg);
		if (obj == null) {
			return -1;
		}
		// 正确时的JSON返回结果
		// {
		// "errcode": 0,
		// "errmsg": "ok"
		// }

		try {
			int errcode = obj.getInt("errcode");
			if (errcode == 0) {
				return errcode;
			} else {
				this.setError(obj.toString());
				return errcode;
			}
		} catch (JSONException e) {
			this.setError(e.getMessage());

			return -1;
		}
	}

	/**
	 * 添加客服帐号
	 * 
	 * @param kf_account
	 * @param nickname
	 * @param password
	 * @return
	 */
	public boolean addWeiXinServiceStaff(String kf_account, String nickname, String password) {
		// {
		// "kf_account" : "test1@test",
		// "nickname" : "客服1",
		// "password" : "pswmd5",
		// }
		String url = API_ROOT + "customservice/kfaccount/add?access_token=" + this.access_token_;
		JSONObject body = new JSONObject();
		try {
			body.put("kf_account", kf_account);
			body.put("nickname", nickname);
			body.put("password", password);
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return false;
		}

		JSONObject obj = this.postResult(url, body);
		if (obj == null) {
			return false;
		}
		try {
			int errcode = obj.getInt("errcode");
			if (errcode == 0) {
				return true;
			} else {
				this.setError(obj.toString());
				return false;
			}
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return false;
		}
	}

	/**
	 * 获取客服列表
	 * 
	 * @return
	 */
	public boolean getWeiXinServiceStaffs() {
		String url = API_ROOT + "customservice/getkflist?access_token=" + this.access_token_;
		JSONObject obj = this.getResult(url);

		return true;
	}

	/**
	 * 创建微信分组
	 * 
	 * 一个公众账号，最多支持创建100个分组。 接口调用请求说明
	 * 
	 * @param groupName
	 *            分组名字（30个字符以内）
	 * @return
	 */
	public WeiXinGroup createWeiXinGroup(String groupName) {
		String u = API_ROOT + "groups/create?access_token=" + this.access_token_;
		JSONObject data = new JSONObject();// {"group":{"name":"test"}}
		JSONObject group = new JSONObject();

		data.put("group", group);
		group.put("name", groupName);
		JSONObject obj = this.postResult(u, data);
		if (obj == null) {
			return null;
		}
		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
			return null;
		}
		WeiXinGroup g = WeiXinGroup.parse(obj.getJSONObject("group"));
		return g;
	}

	/**
	 * 删除分组
	 * 
	 * 注意本接口是删除一个用户分组，删除分组后，所有该分组内的用户自动进入默认分组。 接口调用请求说明
	 * 
	 * http请求方式: POST（请使用https协议）
	 * https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=ACCESS_TOKEN
	 * POST数据格式：json POST数据例子：{"group":{"id":108}}
	 * 
	 * @param grp_id
	 * @return
	 */
	public boolean deleteWeiXinGroup(int grp_id) {
		String url = API_ROOT + "groups/delete?access_token=" + this.access_token_;
		JSONObject data = new JSONObject();// {"group":{"name":"test"}}
		JSONObject group = new JSONObject();

		data.put("group", group);
		group.put("id", grp_id);

		JSONObject obj = this.postResult(url, data);
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
			return false;
		}
		return true;
	}

	/**
	 * 修改微信组名称
	 * 
	 * @param id
	 *            分组id，由微信分配
	 * @param groupName
	 *            分组名字（30个字符以内）
	 * @return
	 */
	public boolean changeWeiXinGroup(int id, String groupName) {
		String u = API_ROOT + "groups/update?access_token=" + this.access_token_;
		WeiXinGroup g = new WeiXinGroup();
		g.setId(id);
		g.setName(groupName);

		JSONObject obj = this.postResult(u, g.toJson());
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
			return false;
		}

		return true;
	}

	/**
	 * 查询用户所在分组
	 * 
	 * @param openId
	 *            用户的openid
	 * @return 微信分组编号
	 */
	public int getWeiXinUserGroup(String openId) {
		String u = API_ROOT + "groups/getid?access_token=" + this.access_token_;
		JSONObject data = new JSONObject();// {"openid":"od8XIjsmk6QdVTETa9jLtGWA6KBc"}
		data.put("openid", openId);
		JSONObject obj = this.postResult(u, data);
		if (obj == null) {
			return -1;
		}
		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
			return -1;
		}

		return obj.getInt("groupid");
	}

	/**
	 * 移动用户分组
	 * 
	 * @param groupId
	 *            分组id
	 * @param openId
	 *            用户唯一标识符
	 * @return
	 */
	public boolean changeWeiXinUserGroup(int groupId, String openId) {
		String u = API_ROOT + "groups/members/update?access_token=" + this.access_token_;
		JSONObject data = new JSONObject();// POST数据例子：{"openid":"oDF3iYx0ro3_7jD4HFRDfrjdCM58","to_groupid":108}
		data.put("openid", openId);
		data.put("to_groupid", groupId);
		JSONObject obj = this.postResult(u, data);
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") > 0) {
			this.setError(obj.toString());
			return false;
		}

		return true;

	}

	/**
	 * 查询所有分组
	 * 
	 * @return
	 */
	public List<WeiXinGroup> getWeiXinGroups() {
		String u = API_ROOT + "groups/get?access_token=" + this.access_token_;
		JSONObject obj = this.getResult(u);
		if (obj == null) {
			return null;
		}
		if (obj.has("errcode") && obj.getInt("errcode") != 0) {
			this.setError(obj.toString());
			return null;
		}
		List<WeiXinGroup> al = new ArrayList<WeiXinGroup>();
		JSONArray arr = obj.getJSONArray("groups");
		for (int i = 0; i < arr.length(); i++) {
			WeiXinGroup g = WeiXinGroup.parse(arr.getJSONObject(i));
			al.add(g);
		}

		return al;
	}

	/**
	 * 下载媒体文件 http
	 * 
	 * @param MEDIA_ID
	 * @return
	 * @throws Exception
	 */
	public String[] downWeiXinMedia(String MEDIA_ID) throws Exception {
		String u = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + this.access_token_ + "&media_id="
				+ MEDIA_ID;
		String hash = u.hashCode() + "";
		hash = hash.replace("-", "gdx");
		String file = UPath.getPATH_IMG_CACHE() + "/weixin_cache/" + hash;
		String url = UPath.getPATH_IMG_CACHE_URL() + "/weixin_cache/" + hash;
		File f1 = new File(file);
		if (!f1.exists()) {
			UNet net = new UNet();

			byte[] buf = net.downloadData(u);
			UFile.createBinaryFile(file, buf, true);
		}
		String[] rets = new String[2];
		rets[0] = file;
		rets[1] = url;
		return rets;
	}

	/**
	 * 创建生成带参数的二维码 为了满足用户渠道推广分析的需要，公众平台提供了生成带参数二维码的接口。使用该接口可以获得多个带不同场景值的二维码，用户扫描后
	 * ，公众号可以接收到事件推送。
	 * 
	 * 目前有2种类型的二维码，分别是临时二维码和永久二维码，前者有过期时间，最大为1800秒，但能够生成较多数量，后者无过期时间，数量较少（
	 * 目前参数只支持1--100000）。两种二维码分别适用于帐号绑定、用户来源统计等场景。
	 * 
	 * 用户扫描带场景值二维码时，可能推送以下两种事件：
	 * 
	 * 如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
	 * 如果用户已经关注公众号，在用户扫描后会自动进入会话，微信也会将带场景值扫描事件推送给开发者。
	 * 获取带参数的二维码的过程包括两步，首先创建二维码ticket，然后凭借ticket到指定URL换取二维码。
	 * 
	 * 每次创建二维码ticket需要提供一个开发者自行设定的参数（scene_id），分别介绍临时二维码和永久二维码的创建二维码ticket过程。
	 * 
	 * @return
	 */
	public WeiXinQRCode createWeiXinQRCode() {
		String url = API_ROOT + "qrcode/create?access_token=" + this.access_token_;

		// POST数据例子：{"expire_seconds": 1800, "action_name": "QR_SCENE",
		// "action_info": {"scene": {"scene_id": 123}}}
		JSONObject body = new JSONObject();
		JSONObject action_info = new JSONObject();
		JSONObject scene = new JSONObject();
		try {
			body.put("expire_seconds", 1800);
			body.put("action_name", "QR_SCENE");
			body.put("action_info", action_info);

			action_info.put("scene", scene);
			scene.put("scene_id", 123);

		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}

		JSONObject obj = this.postResult(url, body);
		if (obj == null) {
			return null;
		}
		try {
			if (!obj.has("errcode")) {
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
				this.setError(obj.toString());
				return null;
			}
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}
	}

	/**
	 * 创建微信自定义菜单
	 * 
	 * @param butRoot
	 * @return
	 */
	public boolean createWeiXinButtons(WeiXinButton butRoot) {
		String url = API_ROOT + "menu/create?access_token=" + this.access_token_;
		JSONObject rst = this.postResult(url, butRoot.toJson());
		if (rst == null) {
			return false;
		}
		if (rst.has("errcode") && rst.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(rst.toString());
			return false;
		}
	}

	/**
	 * 将一条长链接转成短链接。
	 * 
	 * 主要使用场景： 开发者用于生成二维码的原链接（商品、支付二维码等）太长导致扫码速度和成功率下降，
	 * 将原长链接通过此接口转成短链接再生成二维码将大大提升扫码速度和成功率。
	 * 
	 * @param longUrl
	 * @return
	 */
	public String createWeiXinShortUrl(String longUrl) {
		// https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN
		String url = API_ROOT + "shorturl?access_token=" + this.access_token_;
		JSONObject body = new JSONObject();
		body.put("action", "long2short");
		body.put("long_url", longUrl);

		JSONObject rst = this.postResult(url, body);
		if (rst == null) {
			return null;
		}
		if (rst.has("errcode") && rst.getInt("errcode") == 0) {
			return rst.getString("short_url");

		} else {
			this.setError(rst.toString());
			return null;
		}
	}

	public void getWeiXinQRCode(String ticket) {
		String t = ticket;

		try {
			t = URLEncoder.encode(ticket, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + t;

	}

	public void setError(String errmsg) {
		if (errmsg != null && errmsg.indexOf("\"errcode\":40001") > 0) {
			// access_token超时
			this.initGetAccessToken();
		}
		this.lastErr = errmsg;
		Ssl.log("错误" + errmsg);

	}

	JSONObject postResult(String url, JSONObject body) {
		String rst = "";
		// HashMap<String, String> params = new HashMap<String, String>();
		// params.put("body", body.toString());
		Ssl.log(body.toString());
		try {
			rst = Ssl.postMsg(url, body.toString());
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

	private String postResult(String url, String body) {
		String rst = "";
		// HashMap<String, String> params = new HashMap<String, String>();
		// params.put("body", body.toString());
		Ssl.log(body.toString());
		try {
			rst = Ssl.postMsg(url, body.toString());

			return rst;
		} catch (Exception e) {
			this.setError(e.getMessage());
			return null;
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("appID=" + this.appID_);
		sb.append("\nappsecret_=" + this.appsecret_);
		sb.append("\ntoken_=" + this.token_);
		sb.append("\naccess_token_=" + this.access_token_);
		sb.append("\nend_time_=" + this.end_time_);
		return sb.toString();
	}

	/**
	 * 获取最后错误
	 * 
	 * @return
	 */
	public String getLastErr() {
		return lastErr;
	}

	/**
	 * Token有效期开始时间
	 * 
	 * @return
	 */
	public long getEndTime() {
		return this.end_time_;
	}

	/**
	 * 设置Token有效期开始时间
	 * 
	 * @param endTime
	 *            有效期开始时间
	 */
	public void setEndTime(long endTime) {
		this.end_time_ = endTime;
	}
}
