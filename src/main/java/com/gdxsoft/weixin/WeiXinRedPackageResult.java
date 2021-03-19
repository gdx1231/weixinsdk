package com.gdxsoft.weixin;

/**
 * 微信红包返回结果
 * 
 * @author admin
 *
 */
public class WeiXinRedPackageResult {

	private WeiXinRedPackage sendPackage_;
	
	/**
	 * 发送红包的对象
	 * @return
	 */
	public WeiXinRedPackage getSendPackage() {
		return sendPackage_;
	}

	/**
	 * 发送红包的对象
	 * @param sendPackage_
	 */
	public void setSendPackage(WeiXinRedPackage sendPackage_) {
		this.sendPackage_ = sendPackage_;
	}

	private String xml_;

	public String getXml() {
		return xml_;
	}

	public void setXml(String xml_) {
		this.xml_ = xml_;
	}

	private String return_code_; // 返回状态码

	/**
	 * 返回状态码 return_code 是 SUCCESS String(16) SUCCESS/FAIL
	 * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	 **/
	public String getReturnCode() {
		return this.return_code_;
	}

	public void setReturnCode(String return_code) {
		this.return_code_ = return_code;
	}

	private String return_msg_; // 返回信息

	/**
	 * 返回信息 return_msg 否 签名失败   String(128) 返回信息，如非空，为错误原因 签名失败 参数格式校验错误
	 **/
	public String getReturnMsg() {
		return this.return_msg_;
	}

	public void setReturnMsg(String return_msg) {
		this.return_msg_ = return_msg;
	}

	private String sign_; // 签名

	/**
	 * 签名 sign 是 C380BEC2BFD727A4B6845133519F3AD6 String(32) 生成签名方式详见签名生成算法
	 **/
	public String getSign() {
		return this.sign_;
	}

	public void setSign(String sign) {
		this.sign_ = sign;
	}

	private String result_code_; // 业务结果

	/**
	 * 业务结果 result_code 是 SUCCESS String(16) SUCCESS/FAIL
	 **/
	public String getResultCode() {
		return this.result_code_;
	}

	public void setResultCode(String result_code) {
		this.result_code_ = result_code;
	}

	private String err_code_; // 错误代码

	/**
	 * 错误代码 err_code 否 SYSTEMERROR String(32) 错误码信息
	 **/
	public String getErrCode() {
		return this.err_code_;
	}

	public void setErrCode(String err_code) {
		this.err_code_ = err_code;
	}

	private String err_code_des_; // 错误代码描述

	/**
	 * 错误代码描述 err_code_des 否 系统错误 String(128) 结果信息描述
	 **/
	public String getErrCodeDes() {
		return this.err_code_des_;
	}

	public void setErrCodeDes(String err_code_des) {
		this.err_code_des_ = err_code_des;
	}

	private String mch_billno_; // 商户订单号

	/**
	 * 商户订单号 mch_billno 是 10000098201411111234567890 String(28) 商户订单号（每个订单号必须唯一）
	 * 组成： mch_id+yyyymmdd+10位一天内不能重复的数字
	 **/
	public String getMchBillno() {
		return this.mch_billno_;
	}

	public void setMchBillno(String mch_billno) {
		this.mch_billno_ = mch_billno;
	}

	private String mch_id_; // 商户号

	/**
	 * 商户号 mch_id 是 10000098 String(32) 微信支付分配的商户号
	 **/
	public String getMchId() {
		return this.mch_id_;
	}

	public void setMchId(String mch_id) {
		this.mch_id_ = mch_id;
	}

	private String wxappid_; // 公众账号appid

	/**
	 * 公众账号appid wxappid 是 wx8888888888888888 String(32) 商户appid
	 **/
	public String getWxappid() {
		return this.wxappid_;
	}

	public void setWxappid(String wxappid) {
		this.wxappid_ = wxappid;
	}

	private String re_openid_; // 用户openid

	/**
	 * 用户openid re_openid 是 oxTWIuGaIt6gTKsQRLau2M0yL16E String(32) 接受收红包的用户
	 * 用户在wxappid下的openid
	 **/
	public String getReOpenid() {
		return this.re_openid_;
	}

	public void setReOpenid(String re_openid) {
		this.re_openid_ = re_openid;
	}

	private String total_amount_; // 付款金额

	/**
	 * 付款金额 total_amount 是 1000 int 付款金额，单位分
	 **/
	public String getTotalAmount() {
		return this.total_amount_;
	}

	public void setTotalAmount(String total_amount) {
		this.total_amount_ = total_amount;
	}

	// private String  _; //发放成功时间
	// /**
	// *发放成功时间          
	// **/
	// public String get (){return this. _;}
	// public void set (String  ){ this. _= ;}
	// private String  _; //微信单号
	// /**
	// *微信单号          
	// **/
	// public String get (){return this. _;}
	// public void set (String  ){ this. _= ;}
	public void setParameter(String name, String val) {
		if (name.equals("return_code")) {
			this.return_code_ = val;
		} else if (name.equals("return_msg")) {
			this.return_msg_ = val;
		} else if (name.equals("sign")) {
			this.sign_ = val;
		} else if (name.equals("result_code")) {
			this.result_code_ = val;
		} else if (name.equals("err_code")) {
			this.err_code_ = val;
		} else if (name.equals("err_code_des")) {
			this.err_code_des_ = val;
		} else if (name.equals("mch_billno")) {
			this.mch_billno_ = val;
		} else if (name.equals("mch_id")) {
			this.mch_id_ = val;
		} else if (name.equals("wxappid")) {
			this.wxappid_ = val;
		} else if (name.equals("re_openid")) {
			this.re_openid_ = val;
		} else if (name.equals("total_amount")) {
			this.total_amount_ = val;
		}
		// else if(name.equals(" ")){this. _=val;}
		// else if(name.equals(" ")){this. _=val;}
	}
}
