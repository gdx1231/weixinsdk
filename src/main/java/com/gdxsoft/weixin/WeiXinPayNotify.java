package com.gdxsoft.weixin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gdxsoft.easyweb.utils.UXml;

/**
 * 支付结果通用通知
 * 
 * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。<br>
 * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，
 * 但微信不保证通知最终能成功。 （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）<br>
 * 
 * 注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。<br>
 * 
 * 推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。
 * 在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。<br>
 * 
 * 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。<br>
 * 
 * 技术人员可登进微信商户后台扫描加入接口报警群。<br>
 * 
 * 
 * @author admin
 *
 */
public class WeiXinPayNotify {

	/**
	 * 来自微信推送的支付结果信息
	 * 
	 * @param request
	 * @return
	 */
	public static String handleNotifyFromPost(HttpServletRequest request) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (java.io.IOException e) {
					System.err.println(e.getMessage());
					return null;
				}

			}
		}
		return buffer.toString();
	}

	/**
	 * 从json返回对象
	 * 
	 * @return
	 **/
	public static WeiXinPayNotify parse(String xml) {
		Document doc1 = UXml.asDocument(xml);

		WeiXinPayNotify o = new WeiXinPayNotify();

		o.doc_ = doc1;
		NodeList nl1 = doc1.getFirstChild().getChildNodes();
		for (int i = 0; i < nl1.getLength(); i++) {
			Node node = nl1.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String key = node.getNodeName();
			String val = node.getTextContent();

			if (key.equals("")) {
			} else if (key.equals("return_code")) {
				// 返回状态码 return_code 是 String(16) SUCCESS SUCCESS/FAIL
				// 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
				o.setReturnCode(val);
			} else if (key.equals("return_msg")) {
				// 返回信息 return_msg 否 String(128) 签名失败 返回信息，如非空，为错误原因 签名失败
				// 参数格式校验错误
				o.setReturnMsg(val);
			} else if (key.equals("appid")) {
				// 公众账号ID appid 是 String(32) wx8888888888888888
				// 微信分配的公众账号ID（企业号corpid即为此appId）
				o.setAppid(val);
			} else if (key.equals("mch_id")) {
				// 商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号
				o.setMchId(val);
			} else if (key.equals("device_info")) {
				// 设备号 device_info 否 String(32) 013467007045764 微信支付分配的终端设备号，
				o.setDeviceInfo(val);
			} else if (key.equals("nonce_str")) {
				// 随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
				// 随机字符串，不长于32位
				o.setNonceStr(val);
			} else if (key.equals("sign")) {
				// 签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6
				// 签名，详见签名算法
				o.setSign(val);
			} else if (key.equals("result_code")) {
				// 业务结果 result_code 是 String(16) SUCCESS SUCCESS/FAIL
				o.setResultCode(val);
			} else if (key.equals("err_code")) {
				// 错误代码 err_code 否 String(32) SYSTEMERROR 错误返回的信息描述
				o.setErrCode(val);
			} else if (key.equals("err_code_des")) {
				// 错误代码描述 err_code_des 否 String(128) 系统错误 错误返回的信息描述
				o.setErrCodeDes(val);
			} else if (key.equals("openid")) {
				// 用户标识 openid 是 String(128) wxd930ea5d5a258f4f 用户在商户appid下的唯一标识
				o.setOpenid(val);
			} else if (key.equals("is_subscribe")) {
				// 是否关注公众账号 is_subscribe 否 String(1) Y
				// 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
				o.setIsSubscribe(val);
			} else if (key.equals("trade_type")) {
				// 交易类型 trade_type 是 String(16) JSAPI JSAPI、NATIVE、APP
				o.setTradeType(val);
			} else if (key.equals("bank_type")) {
				// 付款银行 bank_type 是 String(16) CMC 银行类型，采用字符串类型的银行标识，银行类型见银行列表
				o.setBankType(val);
			} else if (key.equals("total_fee")) {
				// 总金额 total_fee 是 Int 100 订单总金额，单位为分
				o.setTotalFee(val);
			} else if (key.equals("fee_type")) {
				// 货币种类 fee_type 否 String(8) CNY
				// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
				o.setFeeType(val);
			} else if (key.equals("cash_fee")) {
				// 现金支付金额 cash_fee 是 Int 100 现金支付金额订单现金支付金额，详见支付金额
				o.setCashFee(val);
			} else if (key.equals("cash_fee_type")) {
				// 现金支付货币类型 cash_fee_type 否 String(16) CNY
				// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
				o.setCashFeeType(val);
			} else if (key.equals("coupon_fee")) {
				// 代金券或立减优惠金额 coupon_fee 否 Int 10
				// 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额，详见支付金额
				o.setCouponFee(val);
			} else if (key.equals("coupon_count")) {
				// 代金券或立减优惠使用数量 coupon_count 否 Int 1 代金券或立减优惠使用数量
				o.setCouponCount(val);
			} else if (key.equals("coupon_id_$n")) {
				// 代金券或立减优惠ID coupon_id_$n 否 String(20) 10000
				// 代金券或立减优惠ID,$n为下标，从0开始编号
				o.setCouponId$n(val);
			} else if (key.equals("coupon_fee_$n")) {
				// 单个代金券或立减优惠支付金额 coupon_fee_$n 否 Int 100
				// 单个代金券或立减优惠支付金额,$n为下标，从0开始编号
				o.setCouponFee$n(val);
			} else if (key.equals("transaction_id")) {
				// 微信支付订单号 transaction_id 是 String(32)
				// 1217752501201407033233368018 微信支付订单号
				o.setTransactionId(val);
			} else if (key.equals("out_trade_no")) {
				// 商户订单号 out_trade_no 是 String(32) 1212321211201407033568112322
				// 商户系统的订单号，与请求一致。
				o.setOutTradeNo(val);
			} else if (key.equals("attach")) {
				// 商家数据包 attach 否 String(128) 123456 商家数据包，原样返回
				o.setAttach(val);
			} else if (key.equals("time_end")) {
				// 支付完成时间 time_end 是 String(14) 20141030133525
				// 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
				o.setTimeEnd(val);
			}
		}
		return o;
	}

	
	private Document doc_;

	/**
	 * 返回的XML对象
	 * @return
	 */
	public Document getDoc() {
		return doc_;
	}

	public void setDoc(Document doc_) {
		this.doc_ = doc_;
	}

	/**
	 * 返回json对象
	 * 
	 * @return
	 **/
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();

		// 返回状态码 return_code 是 String(16) SUCCESS SUCCESS/FAIL
		// 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
		obj.put("return_code", this.return_code_);

		// 返回信息 return_msg 否 String(128) 签名失败 返回信息，如非空，为错误原因 签名失败 参数格式校验错误
		obj.put("return_msg", this.return_msg_);

		// 公众账号ID appid 是 String(32) wx8888888888888888
		// 微信分配的公众账号ID（企业号corpid即为此appId）
		obj.put("appid", this.appid_);

		// 商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号
		obj.put("mch_id", this.mch_id_);

		// 设备号 device_info 否 String(32) 013467007045764 微信支付分配的终端设备号，
		obj.put("device_info", this.device_info_);

		// 随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
		// 随机字符串，不长于32位
		obj.put("nonce_str", this.nonce_str_);

		// 签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名算法
		obj.put("sign", this.sign_);

		// 业务结果 result_code 是 String(16) SUCCESS SUCCESS/FAIL
		obj.put("result_code", this.result_code_);

		// 错误代码 err_code 否 String(32) SYSTEMERROR 错误返回的信息描述
		obj.put("err_code", this.err_code_);

		// 错误代码描述 err_code_des 否 String(128) 系统错误 错误返回的信息描述
		obj.put("err_code_des", this.err_code_des_);

		// 用户标识 openid 是 String(128) wxd930ea5d5a258f4f 用户在商户appid下的唯一标识
		obj.put("openid", this.openid_);

		// 是否关注公众账号 is_subscribe 否 String(1) Y
		// 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
		obj.put("is_subscribe", this.is_subscribe_);

		// 交易类型 trade_type 是 String(16) JSAPI JSAPI、NATIVE、APP
		obj.put("trade_type", this.trade_type_);

		// 付款银行 bank_type 是 String(16) CMC 银行类型，采用字符串类型的银行标识，银行类型见银行列表
		obj.put("bank_type", this.bank_type_);

		// 总金额 total_fee 是 Int 100 订单总金额，单位为分
		obj.put("total_fee", this.total_fee_);

		// 货币种类 fee_type 否 String(8) CNY
		// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
		obj.put("fee_type", this.fee_type_);

		// 现金支付金额 cash_fee 是 Int 100 现金支付金额订单现金支付金额，详见支付金额
		obj.put("cash_fee", this.cash_fee_);

		// 现金支付货币类型 cash_fee_type 否 String(16) CNY
		// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
		obj.put("cash_fee_type", this.cash_fee_type_);

		// 代金券或立减优惠金额 coupon_fee 否 Int 10
		// 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额，详见支付金额
		obj.put("coupon_fee", this.coupon_fee_);

		// 代金券或立减优惠使用数量 coupon_count 否 Int 1 代金券或立减优惠使用数量
		obj.put("coupon_count", this.coupon_count_);

		// 代金券或立减优惠ID coupon_id_$n 否 String(20) 10000 代金券或立减优惠ID,$n为下标，从0开始编号
		obj.put("coupon_id_$n", this.coupon_id_$n_);

		// 单个代金券或立减优惠支付金额 coupon_fee_$n 否 Int 100 单个代金券或立减优惠支付金额,$n为下标，从0开始编号
		obj.put("coupon_fee_$n", this.coupon_fee_$n_);

		// 微信支付订单号 transaction_id 是 String(32) 1217752501201407033233368018
		// 微信支付订单号
		obj.put("transaction_id", this.transaction_id_);

		// 商户订单号 out_trade_no 是 String(32) 1212321211201407033568112322
		// 商户系统的订单号，与请求一致。
		obj.put("out_trade_no", this.out_trade_no_);

		// 商家数据包 attach 否 String(128) 123456 商家数据包，原样返回
		obj.put("attach", this.attach_);

		// 支付完成时间 time_end 是 String(14) 20141030133525
		// 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
		obj.put("time_end", this.time_end_);
		return obj;
	}

	private String return_code_; // 返回状态码 return_code 是 String(16) SUCCESS
									// SUCCESS/FAIL
									// 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断

	/**
	 * 返回返回状态码 return_code 是 String(16) SUCCESS SUCCESS/FAIL
	 * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 * 
	 * @return
	 **/
	public String getReturnCode() {
		return this.return_code_;
	}

	/**
	 * 设置返回状态码 return_code 是 String(16) SUCCESS SUCCESS/FAIL
	 * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 * 
	 * @param return_code
	 **/
	public void setReturnCode(String return_code) {
		this.return_code_ = return_code;
	}

	private String return_msg_; // 返回信息 return_msg 否 String(128) 签名失败
								// 返回信息，如非空，为错误原因 签名失败 参数格式校验错误

	/**
	 * 返回返回信息 return_msg 否 String(128) 签名失败 返回信息，如非空，为错误原因 签名失败 参数格式校验错误
	 * 
	 * @return
	 **/
	public String getReturnMsg() {
		return this.return_msg_;
	}

	/**
	 * 设置返回信息 return_msg 否 String(128) 签名失败 返回信息，如非空，为错误原因 签名失败 参数格式校验错误
	 * 
	 * @param return_msg
	 **/
	public void setReturnMsg(String return_msg) {
		this.return_msg_ = return_msg;
	}

	private String appid_; // 公众账号ID appid 是 String(32) wx8888888888888888
							// 微信分配的公众账号ID（企业号corpid即为此appId）

	/**
	 * 返回公众账号ID appid 是 String(32) wx8888888888888888
	 * 微信分配的公众账号ID（企业号corpid即为此appId）
	 * 
	 * @return
	 **/
	public String getAppid() {
		return this.appid_;
	}

	/**
	 * 设置公众账号ID appid 是 String(32) wx8888888888888888
	 * 微信分配的公众账号ID（企业号corpid即为此appId）
	 * 
	 * @param appid
	 **/
	public void setAppid(String appid) {
		this.appid_ = appid;
	}

	private String mch_id_; // 商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号

	/**
	 * 返回商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号
	 * 
	 * @return
	 **/
	public String getMchId() {
		return this.mch_id_;
	}

	/**
	 * 设置商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号
	 * 
	 * @param mch_id
	 **/
	public void setMchId(String mch_id) {
		this.mch_id_ = mch_id;
	}

	private String device_info_; // 设备号 device_info 否 String(32) 013467007045764
									// 微信支付分配的终端设备号，

	/**
	 * 返回设备号 device_info 否 String(32) 013467007045764 微信支付分配的终端设备号，
	 * 
	 * @return
	 **/
	public String getDeviceInfo() {
		return this.device_info_;
	}

	/**
	 * 设置设备号 device_info 否 String(32) 013467007045764 微信支付分配的终端设备号，
	 * 
	 * @param device_info
	 **/
	public void setDeviceInfo(String device_info) {
		this.device_info_ = device_info;
	}

	private String nonce_str_; // 随机字符串 nonce_str 是 String(32)
								// 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 随机字符串，不长于32位

	/**
	 * 返回随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 * 随机字符串，不长于32位
	 * 
	 * @return
	 **/
	public String getNonceStr() {
		return this.nonce_str_;
	}

	/**
	 * 设置随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 * 随机字符串，不长于32位
	 * 
	 * @param nonce_str
	 **/
	public void setNonceStr(String nonce_str) {
		this.nonce_str_ = nonce_str;
	}

	private String sign_; // 签名 sign 是 String(32)
							// C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名算法

	/**
	 * 返回签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名算法
	 * 
	 * @return
	 **/
	public String getSign() {
		return this.sign_;
	}

	/**
	 * 设置签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名算法
	 * 
	 * @param sign
	 **/
	public void setSign(String sign) {
		this.sign_ = sign;
	}

	private String result_code_; // 业务结果 result_code 是 String(16) SUCCESS
									// SUCCESS/FAIL

	/**
	 * 返回业务结果 result_code 是 String(16) SUCCESS SUCCESS/FAIL
	 * 
	 * @return
	 **/
	public String getResultCode() {
		return this.result_code_;
	}

	/**
	 * 设置业务结果 result_code 是 String(16) SUCCESS SUCCESS/FAIL
	 * 
	 * @param result_code
	 **/
	public void setResultCode(String result_code) {
		this.result_code_ = result_code;
	}

	private String err_code_; // 错误代码 err_code 否 String(32) SYSTEMERROR
								// 错误返回的信息描述

	/**
	 * 返回错误代码 err_code 否 String(32) SYSTEMERROR 错误返回的信息描述
	 * 
	 * @return
	 **/
	public String getErrCode() {
		return this.err_code_;
	}

	/**
	 * 设置错误代码 err_code 否 String(32) SYSTEMERROR 错误返回的信息描述
	 * 
	 * @param err_code
	 **/
	public void setErrCode(String err_code) {
		this.err_code_ = err_code;
	}

	private String err_code_des_; // 错误代码描述 err_code_des 否 String(128) 系统错误
									// 错误返回的信息描述

	/**
	 * 返回错误代码描述 err_code_des 否 String(128) 系统错误 错误返回的信息描述
	 * 
	 * @return
	 **/
	public String getErrCodeDes() {
		return this.err_code_des_;
	}

	/**
	 * 设置错误代码描述 err_code_des 否 String(128) 系统错误 错误返回的信息描述
	 * 
	 * @param err_code_des
	 **/
	public void setErrCodeDes(String err_code_des) {
		this.err_code_des_ = err_code_des;
	}

	private String openid_; // 用户标识 openid 是 String(128) wxd930ea5d5a258f4f
							// 用户在商户appid下的唯一标识

	/**
	 * 返回用户标识 openid 是 String(128) wxd930ea5d5a258f4f 用户在商户appid下的唯一标识
	 * 
	 * @return
	 **/
	public String getOpenid() {
		return this.openid_;
	}

	/**
	 * 设置用户标识 openid 是 String(128) wxd930ea5d5a258f4f 用户在商户appid下的唯一标识
	 * 
	 * @param openid
	 **/
	public void setOpenid(String openid) {
		this.openid_ = openid;
	}

	private String is_subscribe_; // 是否关注公众账号 is_subscribe 否 String(1) Y
									// 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效

	/**
	 * 返回是否关注公众账号 is_subscribe 否 String(1) Y 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
	 * 
	 * @return
	 **/
	public String getIsSubscribe() {
		return this.is_subscribe_;
	}

	/**
	 * 设置是否关注公众账号 is_subscribe 否 String(1) Y 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
	 * 
	 * @param is_subscribe
	 **/
	public void setIsSubscribe(String is_subscribe) {
		this.is_subscribe_ = is_subscribe;
	}

	private String trade_type_; // 交易类型 trade_type 是 String(16) JSAPI
								// JSAPI、NATIVE、APP

	/**
	 * 返回交易类型 trade_type 是 String(16) JSAPI JSAPI、NATIVE、APP
	 * 
	 * @return
	 **/
	public String getTradeType() {
		return this.trade_type_;
	}

	/**
	 * 设置交易类型 trade_type 是 String(16) JSAPI JSAPI、NATIVE、APP
	 * 
	 * @param trade_type
	 **/
	public void setTradeType(String trade_type) {
		this.trade_type_ = trade_type;
	}

	private String bank_type_; // 付款银行 bank_type 是 String(16) CMC
								// 银行类型，采用字符串类型的银行标识，银行类型见银行列表

	/**
	 * 返回付款银行 bank_type 是 String(16) CMC 银行类型，采用字符串类型的银行标识，银行类型见银行列表
	 * 
	 * @return
	 **/
	public String getBankType() {
		return this.bank_type_;
	}

	/**
	 * 设置付款银行 bank_type 是 String(16) CMC 银行类型，采用字符串类型的银行标识，银行类型见银行列表
	 * 
	 * @param bank_type
	 **/
	public void setBankType(String bank_type) {
		this.bank_type_ = bank_type;
	}

	private String total_fee_; // 总金额 total_fee 是 Int 100 订单总金额，单位为分

	/**
	 * 返回总金额 total_fee 是 Int 100 订单总金额，单位为分
	 * 
	 * @return
	 **/
	public String getTotalFee() {
		return this.total_fee_;
	}

	/**
	 * 设置总金额 total_fee 是 Int 100 订单总金额，单位为分
	 * 
	 * @param total_fee
	 **/
	public void setTotalFee(String total_fee) {
		this.total_fee_ = total_fee;
	}

	private String fee_type_; // 货币种类 fee_type 否 String(8) CNY
								// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型

	/**
	 * 返回货币种类 fee_type 否 String(8) CNY
	 * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	 * 
	 * @return
	 **/
	public String getFeeType() {
		return this.fee_type_;
	}

	/**
	 * 设置货币种类 fee_type 否 String(8) CNY
	 * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	 * 
	 * @param fee_type
	 **/
	public void setFeeType(String fee_type) {
		this.fee_type_ = fee_type;
	}

	private String cash_fee_; // 现金支付金额 cash_fee 是 Int 100 现金支付金额订单现金支付金额，详见支付金额

	/**
	 * 返回现金支付金额 cash_fee 是 Int 100 现金支付金额订单现金支付金额，详见支付金额
	 * 
	 * @return
	 **/
	public String getCashFee() {
		return this.cash_fee_;
	}

	/**
	 * 设置现金支付金额 cash_fee 是 Int 100 现金支付金额订单现金支付金额，详见支付金额
	 * 
	 * @param cash_fee
	 **/
	public void setCashFee(String cash_fee) {
		this.cash_fee_ = cash_fee;
	}

	private String cash_fee_type_; // 现金支付货币类型 cash_fee_type 否 String(16) CNY
									// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型

	/**
	 * 返回现金支付货币类型 cash_fee_type 否 String(16) CNY
	 * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	 * 
	 * @return
	 **/
	public String getCashFeeType() {
		return this.cash_fee_type_;
	}

	/**
	 * 设置现金支付货币类型 cash_fee_type 否 String(16) CNY
	 * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	 * 
	 * @param cash_fee_type
	 **/
	public void setCashFeeType(String cash_fee_type) {
		this.cash_fee_type_ = cash_fee_type;
	}

	private String coupon_fee_; // 代金券或立减优惠金额 coupon_fee 否 Int 10
								// 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额，详见支付金额

	/**
	 * 返回代金券或立减优惠金额 coupon_fee 否 Int 10
	 * 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额，详见支付金额
	 * 
	 * @return
	 **/
	public String getCouponFee() {
		return this.coupon_fee_;
	}

	/**
	 * 设置代金券或立减优惠金额 coupon_fee 否 Int 10
	 * 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额，详见支付金额
	 * 
	 * @param coupon_fee
	 **/
	public void setCouponFee(String coupon_fee) {
		this.coupon_fee_ = coupon_fee;
	}

	private String coupon_count_; // 代金券或立减优惠使用数量 coupon_count 否 Int 1
									// 代金券或立减优惠使用数量

	/**
	 * 返回代金券或立减优惠使用数量 coupon_count 否 Int 1 代金券或立减优惠使用数量
	 * 
	 * @return
	 **/
	public String getCouponCount() {
		return this.coupon_count_;
	}

	/**
	 * 设置代金券或立减优惠使用数量 coupon_count 否 Int 1 代金券或立减优惠使用数量
	 * 
	 * @param coupon_count
	 **/
	public void setCouponCount(String coupon_count) {
		this.coupon_count_ = coupon_count;
	}

	private String coupon_id_$n_; // 代金券或立减优惠ID coupon_id_$n 否 String(20) 10000
									// 代金券或立减优惠ID,$n为下标，从0开始编号

	/**
	 * 返回代金券或立减优惠ID coupon_id_$n 否 String(20) 10000 代金券或立减优惠ID,$n为下标，从0开始编号
	 * 
	 * @return
	 **/
	public String getCouponId$n() {
		return this.coupon_id_$n_;
	}

	/**
	 * 设置代金券或立减优惠ID coupon_id_$n 否 String(20) 10000 代金券或立减优惠ID,$n为下标，从0开始编号
	 * 
	 * @param coupon_id_$n
	 **/
	public void setCouponId$n(String coupon_id_$n) {
		this.coupon_id_$n_ = coupon_id_$n;
	}

	private String coupon_fee_$n_; // 单个代金券或立减优惠支付金额 coupon_fee_$n 否 Int 100
									// 单个代金券或立减优惠支付金额,$n为下标，从0开始编号

	/**
	 * 返回单个代金券或立减优惠支付金额 coupon_fee_$n 否 Int 100 单个代金券或立减优惠支付金额,$n为下标，从0开始编号
	 * 
	 * @return
	 **/
	public String getCouponFee$n() {
		return this.coupon_fee_$n_;
	}

	/**
	 * 设置单个代金券或立减优惠支付金额 coupon_fee_$n 否 Int 100 单个代金券或立减优惠支付金额,$n为下标，从0开始编号
	 * 
	 * @param coupon_fee_$n
	 **/
	public void setCouponFee$n(String coupon_fee_$n) {
		this.coupon_fee_$n_ = coupon_fee_$n;
	}

	private String transaction_id_; // 微信支付订单号 transaction_id 是 String(32)
									// 1217752501201407033233368018 微信支付订单号

	/**
	 * 返回微信支付订单号 transaction_id 是 String(32) 1217752501201407033233368018
	 * 微信支付订单号
	 * 
	 * @return
	 **/
	public String getTransactionId() {
		return this.transaction_id_;
	}

	/**
	 * 设置微信支付订单号 transaction_id 是 String(32) 1217752501201407033233368018
	 * 微信支付订单号
	 * 
	 * @param transaction_id
	 **/
	public void setTransactionId(String transaction_id) {
		this.transaction_id_ = transaction_id;
	}

	private String out_trade_no_; // 商户订单号 out_trade_no 是 String(32)
									// 1212321211201407033568112322
									// 商户系统的订单号，与请求一致。

	/**
	 * 返回商户订单号 out_trade_no 是 String(32) 1212321211201407033568112322
	 * 商户系统的订单号，与请求一致。
	 * 
	 * @return
	 **/
	public String getOutTradeNo() {
		return this.out_trade_no_;
	}

	/**
	 * 设置商户订单号 out_trade_no 是 String(32) 1212321211201407033568112322
	 * 商户系统的订单号，与请求一致。
	 * 
	 * @param out_trade_no
	 **/
	public void setOutTradeNo(String out_trade_no) {
		this.out_trade_no_ = out_trade_no;
	}

	private String attach_; // 商家数据包 attach 否 String(128) 123456 商家数据包，原样返回

	/**
	 * 返回商家数据包 attach 否 String(128) 123456 商家数据包，原样返回
	 * 
	 * @return
	 **/
	public String getAttach() {
		return this.attach_;
	}

	/**
	 * 设置商家数据包 attach 否 String(128) 123456 商家数据包，原样返回
	 * 
	 * @param attach
	 **/
	public void setAttach(String attach) {
		this.attach_ = attach;
	}

	private String time_end_; // 支付完成时间 time_end 是 String(14) 20141030133525
								// 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则

	/**
	 * 返回支付完成时间 time_end 是 String(14) 20141030133525
	 * 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	 * 
	 * @return
	 **/
	public String getTimeEnd() {
		return this.time_end_;
	}

	/**
	 * 设置支付完成时间 time_end 是 String(14) 20141030133525
	 * 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	 * 
	 * @param time_end
	 **/
	public void setTimeEnd(String time_end) {
		this.time_end_ = time_end;
	}

}
