package com.gdxsoft.weixin;

import org.json.JSONException;
import org.json.JSONObject;

public class WeiXinOrder {

	private String requestSource_;
	private String resultSource_;
	
	/**
	 * <xml><return_code><![CDATA[SUCCESS]]></return_code>
	 * <return_msg><![CDATA[OK]]></return_msg>
	 * <appid><![CDATA[wxd6d954c618642b7c]]></appid>
	 * <mch_id><![CDATA[1226722702]]></mch_id>
	 * <nonce_str><![CDATA[LPEvcrDhTGcNspl2]]></nonce_str>
	 * <sign><![CDATA[F363601A37C0796B6117DE10DBBD6E16]]></sign>
	 * <result_code><![CDATA[SUCCESS]]></result_code>
	 * <prepay_id><![CDATA[wx201504211007591175d8b28a0455881178]]></prepay_id>
	 * <trade_type><![CDATA[JSAPI]]></trade_type> </xml>
	 */

	private String return_code_; // 返回状态码 return_code 是 String(16) SUCCESS
									// SUCCESS/FAIL此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	private String return_msg_; // 返回信息 return_msg 否 String(128) 签名失败
								// 返回信息，如非空，为错误原因签名失败参数格式校验错误

	private String appid_; // 公众账号ID appid 是 String(32)
							// wx8888888888888888调用接口提交的公众账号ID
	private String mch_id_; // 商户号 mch_id 是 String(32) 1900000109 调用接口提交的商户号
	private String device_info_; // 设备号 device_info 否 String(32) 013467007045764
									// 调用接口提交的终端设备号，
	private String nonce_str_; // 随机字符串 nonce_str 是 String(32)
								// 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 微信返回的随机字符串
	private String sign_; // 签名 sign 是 String(32)
							// C380BEC2BFD727A4B6845133519F3AD6 微信返回的签名，详见签名算法
	private String result_code_; // 业务结果 result_code 是 String(16) SUCCESS
									// SUCCESS/FAIL
	private String err_code_; // 错误代码 err_code 否 String(32) SYSTEMERROR
								// 详细参见第6节错误列表
	private String err_code_des_; // 错误代码描述 err_code_des 否 String(128) 系统错误
									// 错误返回的信息描述
	private String prepay_id_;// 预支付交易会话标识 prepay_id 是 String(64)
								// wx201410272009395522657a690389285100
								// 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	private String trade_type_; // 交易类型 trade_type 是 String(16) JSAPI
								// 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
	private String code_url_;

	/**
	 * 设备号 device_info 否 String(32) 013467007045764 调用接口提交的终端设备号，
	 * 
	 * @return
	 */
	public String getDeviceInfo() {
		return device_info_;
	}

	/**
	 * 设备号 device_info 否 String(32) 013467007045764 调用接口提交的终端设备号，
	 * 
	 * @param device_info_
	 */
	public void setDeviceInfo(String device_info_) {
		this.device_info_ = device_info_;
	}

	/**
	 * 错误代码 err_code 否 String(32) SYSTEMERROR // 详细参见第6节错误列表
	 * 
	 * @return
	 */
	public String getErrCode() {
		return err_code_;
	}

	/**
	 * 错误代码 err_code 否 String(32) SYSTEMERROR // 详细参见第6节错误列表
	 * 
	 * @param err_code_
	 */
	public void setErrCode(String err_code_) {
		this.err_code_ = err_code_;
	}

	/**
	 * // 错误代码描述 err_code_des 否 String(128) 系统错误 // 错误返回的信息描述
	 * 
	 * @return
	 */
	public String getErrCodeDes() {
		return err_code_des_;
	}

	/**
	 * // 错误代码描述 err_code_des 否 String(128) 系统错误 // 错误返回的信息描述
	 * 
	 * @param err_code_des_
	 */
	public void setErrCodeDes(String err_code_des_) {
		this.err_code_des_ = err_code_des_;
	}

	/**
	 * 二维码链接 code_url 否 String(64) URl：weixin：//wxpay/s/An4baqw
	 * trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
	 * 
	 * @return
	 */
	public String getCodeUrl() {
		return code_url_;
	}

	/**
	 * 二维码链接 code_url 否 String(64) URl：weixin：//wxpay/s/An4baqw
	 * trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
	 * 
	 * @param code_url_
	 */
	public void setCodeUrl(String code_url_) {
		this.code_url_ = code_url_;
	}

	private String xml_;

	/**
	 * // 返回状态码 return_code 是 String(16) SUCCESS //
	 * SUCCESS/FAIL此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 * 
	 * @return
	 */
	public String getReturnCode() {
		return return_code_;
	}

	/**
	 * // 返回状态码 return_code 是 String(16) SUCCESS //
	 * SUCCESS/FAIL此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 * 
	 * @param return_code_
	 */
	public void setReturnCode(String return_code_) {
		this.return_code_ = return_code_;
	}

	/**
	 * // 返回信息 return_msg 否 String(128) 签名失败 // 返回信息，如非空，为错误原因签名失败参数格式校验错误
	 * 
	 * @return
	 */
	public String getReturnMsg() {
		return return_msg_;
	}

	/**
	 * // 返回信息 return_msg 否 String(128) 签名失败 // 返回信息，如非空，为错误原因签名失败参数格式校验错误
	 * 
	 * @param return_msg_
	 */
	public void setReturnMsg(String return_msg_) {
		this.return_msg_ = return_msg_;
	}

	/**
	 * 公众账号ID appid 是 String(32) // wx8888888888888888调用接口提交的公众账号ID
	 * 
	 * @return
	 */
	public String getAppId() {
		return appid_;
	}

	/**
	 * 公众账号ID appid 是 String(32) // wx8888888888888888调用接口提交的公众账号ID
	 * 
	 * @param appid_
	 */
	public void setAppId(String appid_) {
		this.appid_ = appid_;
	}

	/**
	 * 商户号 mch_id 是 String(32) 1900000109 调用接口提交的商户号
	 * 
	 * @return
	 */
	public String getMchId() {
		return mch_id_;
	}

	/**
	 * 商户号 mch_id 是 String(32) 1900000109 调用接口提交的商户号
	 * 
	 * @param mch_id_
	 */
	public void setMchId(String mch_id_) {
		this.mch_id_ = mch_id_;
	}

	/**
	 * 随机字符串 nonce_str 是 String(32) // 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 * 微信返回的随机字符串
	 * 
	 * @return
	 */
	public String getNonceStr() {
		return nonce_str_;
	}

	/**
	 * 随机字符串 nonce_str 是 String(32) // 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 * 微信返回的随机字符串
	 * 
	 * @param nonce_str_
	 */
	public void setNonceStr(String nonce_str_) {
		this.nonce_str_ = nonce_str_;
	}

	/**
	 * 签名 sign 是 String(32) // C380BEC2BFD727A4B6845133519F3AD6 微信返回的签名，详见签名算法
	 * 
	 * @return
	 */
	public String getSign() {
		return sign_;
	}

	/**
	 * 签名 sign 是 String(32) // C380BEC2BFD727A4B6845133519F3AD6 微信返回的签名，详见签名算法
	 * 
	 * @param sign_
	 */
	public void setSign(String sign_) {
		this.sign_ = sign_;
	}

	/**
	 * 业务结果 result_code 是 String(16) SUCCESS // SUCCESS/FAIL
	 * 
	 * @return
	 */
	public String getResultCode() {
		return result_code_;
	}

	/**
	 * 业务结果 result_code 是 String(16) SUCCESS // SUCCESS/FAIL
	 * 
	 * @param result_code_
	 */
	public void setResultCode(String result_code_) {
		this.result_code_ = result_code_;
	}

	/**
	 * 预支付交易会话标识 prepay_id 是 String(64) // wx201410272009395522657a690389285100
	 * // 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	 * 
	 * @return
	 */
	public String getPrepayId() {
		return prepay_id_;
	}

	/**
	 * 预支付交易会话标识 prepay_id 是 String(64) // wx201410272009395522657a690389285100
	 * // 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	 * 
	 * @param prepay_id_
	 */
	public void setPrepayId(String prepay_id_) {
		this.prepay_id_ = prepay_id_;
	}

	/**
	 * 交易类型 trade_type 是 String(16) JSAPI //
	 * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
	 * 
	 * @return
	 */
	public String getTradeType() {
		return trade_type_;
	}

	/**
	 * 交易类型 trade_type 是 String(16) JSAPI //
	 * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
	 * 
	 * @param trade_type_
	 */
	public void setTradeType(String trade_type_) {
		this.trade_type_ = trade_type_;
	}

	public String getXml() {
		return xml_;
	}

	public void setXml(String xml_) {
		this.xml_ = xml_;
	}

	/**
	 * 扫码交易模式2
	 * 
	 * @return
	 */
	public String toQr2() {
		return this.code_url_;
	}

	/**
	 * 扫码交易模式1 二维码中的内容为链接，形式为：
	 * weixin：//wxpay/bizpayurl?sign=XXXXX&appid=XXXXX&mch_id=XXXXX&product_id=
	 * XXXXXX&time_stamp=XXXXXX&nonce_str=XXXXX
	 * 其中XXXXX为商户需要填写的内容，商户将该链接生成二维码，如需要打印发布二维码，需要采用此格式。商户可调用第三方库生成二维码图片。参数说明如下：
	 * 表6.1 生成二维码所需参数列表
	 * 
	 * 名称 变量名 类型 必填 示例值 描述 <br>
	 * 公众账号ID appid String(32) 是 wx8888888888888888 微信分配的公众账号ID <br>
	 * 商户号 mch_id String(32) 是 1900000109 微信支付分配的商户号 <br>
	 * 时间戳 time_stamp String(10) 是 1414488825 系统当前时间，定义规则详见时间戳 <br>
	 * 随机字符串 nonce_str String(32) 是 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 * 随机字符串，不长于32位。推荐随机数生成算法 <br>
	 * 商品ID product_id String(32) 是 88888 商户定义的商品id 或者订单号 <br>
	 * 签名 sign String(32) 是 C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名生成算法
	 * 
	 * @return
	 */
	public String toQr(String shopKey, String product_id) {
		JSONObject obj = new JSONObject();
		String paySign;
		try {
			obj.put("appid", this.appid_);
			obj.put("time_stamp", System.currentTimeMillis() / 1000);
			obj.put("mch_id", mch_id_);
			obj.put("nonce_str", SignUtils.nonceStr());
			obj.put("product_id", product_id);

			paySign = SignUtils.signMd5(obj, "key", shopKey);
			obj.put("paySign", paySign);
		} catch (JSONException e) {
			return e.getMessage();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("weixin://wxpay/bizpayurl?sign=");
		sb.append(paySign);

		sb.append("&appid=");
		sb.append(this.appid_);

		sb.append("&mch_id=");
		sb.append(mch_id_);

		sb.append("&product_id=");
		sb.append(product_id);

		sb.append("&time_stamp=");
		sb.append(obj.get("time_stamp"));

		sb.append("&nonce_str=");
		sb.append(obj.get("nonce_str"));
		return sb.toString();
	}

	/**
	 * timestamp: 0, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。
	 * 但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符<br>
	 * nonceStr: '', // 支付签名随机串，不长于 32 位<br>
	 * package: '', // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）<br>
	 * signType: '', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5' <br>
	 * paySign: '', // 支付签名
	 * 
	 * @return
	 */
	public String toJs(String shopKey) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("appid", this.appid_);
			obj.put("timestamp", System.currentTimeMillis() / 1000);
			obj.put("nonceStr", SignUtils.nonceStr());
			obj.put("package", "prepay_id=" + this.prepay_id_);
			obj.put("signType", "MD5");
			String paySign = SignUtils.signMd5(obj, "key", shopKey);
			obj.put("paySign", paySign);
			return obj.toString();
		} catch (JSONException e) {
			return e.getMessage();
		}
	}

	/**
	 * 创建App调用的参数对象
	 * @param shopKey
	 * @return
	 */
	public JSONObject toApp(String shopKey) {
		JSONObject obj = new JSONObject();
		// 微信开放平台审核通过的应用APPID
		obj.put("appid", this.appid_); 
		
		// 商户号 partnerid String(32) 是 1900000109 微信支付分配的商户号
		obj.put("partnerid", this.mch_id_);
		
		// 预支付交易会话ID prepayid String(32) 是 WX1217752501201407033233368018
		// 微信返回的支付交易会话ID
		obj.put("prepayid", this.prepay_id_);
		
		// 扩展字段 package String(128) 是 Sign=WXPay 暂填写固定值Sign=WXPay
		obj.put("package", "Sign=WXPay");
		
		// 随机字符串 noncestr String(32)
		obj.put("noncestr", SignUtils.nonceStr());
		
		// 时间戳 timestamp String(10)
		obj.put("timestamp", System.currentTimeMillis() / 1000);

		String paySign = SignUtils.signMd5(obj, "key", shopKey);
		
		obj.put("sign", paySign);
		return obj;

	}

	public String toJsH5(String shopKey) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("appId", this.appid_);
			obj.put("timeStamp", System.currentTimeMillis() / 1000 + "");
			obj.put("nonceStr", SignUtils.nonceStr());
			obj.put("package", "prepay_id=" + this.prepay_id_);
			obj.put("signType", "MD5");
			String paySign = SignUtils.signMd5(obj, "key", shopKey);
			obj.put("paySign", paySign);
			return obj.toString();
		} catch (JSONException e) {
			return e.getMessage();
		}
	}

	/**
	 * 请求数据
	 * @return the requestSource_
	 */
	public String getRequestSource() {
		return requestSource_;
	}

	/**
	 * 请求数据
	 * @param requestSource_ the requestSource_ to set
	 */
	public void setRequestSource(String requestSource) {
		this.requestSource_ = requestSource;
	}

	/**
	 * 返回数据
	 * @return the resultSource_
	 */
	public String getResultSource() {
		return resultSource_;
	}

	/**
	 * 返回数据
	 * @param resultSource_ the resultSource_ to set
	 */
	public void setResultSource(String resultSource) {
		this.resultSource_ = resultSource;
	}
}
