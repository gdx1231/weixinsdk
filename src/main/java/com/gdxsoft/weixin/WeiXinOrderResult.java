package com.gdxsoft.weixin;

public class WeiXinOrderResult {
	private String xml_;

	public String getXml() {
		return xml_;
	}

	public void setXml(String xml_) {
		this.xml_ = xml_;
	}
	
	public void setParameter(String name, String val) {
		if (name.equals("return_code")) {
			this.return_code_ = val;
		} else if (name.equals("return_msg")) {
			this.return_msg_ = val;
		} else if (name.equals("appid")) {
			this.appid_ = val;
		} else if (name.equals("mch_id")) {
			this.mch_id_ = val;
		} else if (name.equals("nonce_str")) {
			this.nonce_str_ = val;
		} else if (name.equals("sign")) {
			this.sign_ = val;
		} else if (name.equals("result_code")) {
			this.result_code_ = val;
		} else if (name.equals("err_code")) {
			this.err_code_ = val;
		} else if (name.equals("err_code_des")) {
			this.err_code_des_ = val;
		} else if (name.equals("device_info")) {
			this.device_info_ = val;
		} else if (name.equals("openid")) {
			this.openid_ = val;
		} else if (name.equals("is_subscribe")) {
			this.is_subscribe_ = val;
		} else if (name.equals("trade_type")) {
			this.trade_type_ = val;
		} else if (name.equals("trade_state")) {
			this.trade_state_ = val;
		} else if (name.equals("bank_type")) {
			this.bank_type_ = val;
		} else if (name.equals("total_fee")) {
			this.total_fee_ = val;
		} else if (name.equals("fee_type")) {
			this.fee_type_ = val;
		} else if (name.equals("cash_fee")) {
			this.cash_fee_ = val;
		} else if (name.equals("cash_fee_type")) {
			this.cash_fee_type_ = val;
		} else if (name.equals("coupon_fee")) {
			this.coupon_fee_ = val;
		} else if (name.equals("coupon_count")) {
			this.coupon_count_ = val;
		} else if (name.equals("coupon_batch_id_$n")) {
			this.coupon_batch_id_$n_ = val;
		} else if (name.equals("coupon_id_$n")) {
			this.coupon_id_$n_ = val;
		} else if (name.equals("coupon_fee_$n")) {
			this.coupon_fee_$n_ = val;
		} else if (name.equals("transaction_id")) {
			this.transaction_id_ = val;
		} else if (name.equals("out_trade_no")) {
			this.out_trade_no_ = val;
		} else if (name.equals("attach")) {
			this.attach_ = val;
		} else if (name.equals("time_end")) {
			this.time_end_ = val;
		} else if (name.equals("trade_state_desc")) {
			this.trade_state_desc_ = val;
		} else{
			System.out.println(name+"不存在");
		}
	}

	private String return_code_; // 返回状态码

	/**
	 * 返回状态码 return_code 是 String(16) SUCCESS SUCCESS/FAIL
	 * 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
	 **/
	public String getreturn_code() {
		return this.return_code_;
	}

	public void setreturn_code(String return_code) {
		this.return_code_ = return_code;
	}

	private String return_msg_; // 返回信息

	/**
	 * 返回信息 return_msg 否 String(128) 签名失败   返回信息，如非空，为错误原因 签名失败 参数格式校验错误
	 **/
	public String getreturn_msg() {
		return this.return_msg_;
	}

	public void setreturn_msg(String return_msg) {
		this.return_msg_ = return_msg;
	}

	private String appid_; // 公众账号ID

	/**
	 * 公众账号ID appid 是 String(32) wx8888888888888888 微信分配的公众账号ID
	 **/
	public String getappid() {
		return this.appid_;
	}

	public void setappid(String appid) {
		this.appid_ = appid;
	}

	private String mch_id_; // 商户号

	/**
	 * 商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号
	 **/
	public String getmch_id() {
		return this.mch_id_;
	}

	public void setmch_id(String mch_id) {
		this.mch_id_ = mch_id;
	}

	private String nonce_str_; // 随机字符串

	/**
	 * 随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	 * 随机字符串，不长于32位。推荐随机数生成算法
	 **/
	public String getnonce_str() {
		return this.nonce_str_;
	}

	public void setnonce_str(String nonce_str) {
		this.nonce_str_ = nonce_str;
	}

	private String sign_; // 签名

	/**
	 * 签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名生成算法
	 **/
	public String getsign() {
		return this.sign_;
	}

	public void setsign(String sign) {
		this.sign_ = sign;
	}

	private String result_code_; // 业务结果

	/**
	 * 业务结果 result_code 是 String(16) SUCCESS SUCCESS/FAIL
	 **/
	public String getresult_code() {
		return this.result_code_;
	}

	public void setresult_code(String result_code) {
		this.result_code_ = result_code;
	}

	private String err_code_; // 错误代码

	/**
	 * 错误代码 err_code 否 String(32) SYSTEMERROR 详细参见第6节错误列表
	 **/
	public String geterr_code() {
		return this.err_code_;
	}

	public void seterr_code(String err_code) {
		this.err_code_ = err_code;
	}

	private String err_code_des_; // 错误代码描述

	/**
	 * 错误代码描述 err_code_des 否 String(128) 系统错误 结果信息描述
	 **/
	public String geterr_code_des() {
		return this.err_code_des_;
	}

	public void seterr_code_des(String err_code_des) {
		this.err_code_des_ = err_code_des;
	}

	private String device_info_; // 设备号

	/**
	 * 设备号 device_info 否 String(32) 013467007045764 微信支付分配的终端设备号，
	 **/
	public String getdevice_info() {
		return this.device_info_;
	}

	public void setdevice_info(String device_info) {
		this.device_info_ = device_info;
	}

	private String openid_; // 用户标识

	/**
	 * 用户标识 openid 是 String(128) wxd930ea5d5a258f4f 用户在商户appid下的唯一标识
	 **/
	public String getopenid() {
		return this.openid_;
	}

	public void setopenid(String openid) {
		this.openid_ = openid;
	}

	private String is_subscribe_; // 是否关注公众账号

	/**
	 * 是否关注公众账号 is_subscribe 是 String(1) Y 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
	 **/
	public String getis_subscribe() {
		return this.is_subscribe_;
	}

	public void setis_subscribe(String is_subscribe) {
		this.is_subscribe_ = is_subscribe;
	}

	private String trade_type_; // 交易类型

	/**
	 * 交易类型 trade_type 是 String(16) JSAPI
	 * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY，详细说明见参数规定
	 **/
	public String gettrade_type() {
		return this.trade_type_;
	}

	public void settrade_type(String trade_type) {
		this.trade_type_ = trade_type;
	}

	private String trade_state_; // 交易状态

	/**
	 * 交易状态 trade_state 是 String(32) SUCCESS SUCCESS—支付成功 REFUND—转入退款 NOTPAY—未支付
	 * CLOSED—已关闭 REVOKED—已撤销 USERPAYING--用户支付中 PAYERROR--支付失败(其他原因，如银行返回失败)
	 **/
	public String gettrade_state() {
		return this.trade_state_;
	}

	public void settrade_state(String trade_state) {
		this.trade_state_ = trade_state;
	}

	private String bank_type_; // 付款银行

	/**
	 * 付款银行 bank_type 是 String(16) CMC 银行类型，采用字符串类型的银行标识
	 **/
	public String getbank_type() {
		return this.bank_type_;
	}

	public void setbank_type(String bank_type) {
		this.bank_type_ = bank_type;
	}

	private String total_fee_; // 总金额

	/**
	 * 总金额 total_fee 是 Int 100 订单总金额，单位为分
	 **/
	public String gettotal_fee() {
		return this.total_fee_;
	}

	public void settotal_fee(String total_fee) {
		this.total_fee_ = total_fee;
	}

	private String fee_type_; // 货币种类

	/**
	 * 货币种类 fee_type 否 String(8) CNY
	 * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	 **/
	public String getfee_type() {
		return this.fee_type_;
	}

	public void setfee_type(String fee_type) {
		this.fee_type_ = fee_type;
	}

	private String cash_fee_; // 现金支付金额

	/**
	 * 现金支付金额 cash_fee 是 Int 100 现金支付金额订单现金支付金额，详见支付金额
	 **/
	public String getcash_fee() {
		return this.cash_fee_;
	}

	public void setcash_fee(String cash_fee) {
		this.cash_fee_ = cash_fee;
	}

	private String cash_fee_type_; // 现金支付货币类型

	/**
	 * 现金支付货币类型 cash_fee_type 否 String(16) CNY
	 * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	 **/
	public String getcash_fee_type() {
		return this.cash_fee_type_;
	}

	public void setcash_fee_type(String cash_fee_type) {
		this.cash_fee_type_ = cash_fee_type;
	}

	private String coupon_fee_; // 代金券或立减优惠金额

	/**
	 * 代金券或立减优惠金额 coupon_fee 否 Int 100
	 * “代金券或立减优惠”金额<=订单总金额，订单总金额-“代金券或立减优惠”金额=现金支付金额，详见支付金额
	 **/
	public String getcoupon_fee() {
		return this.coupon_fee_;
	}

	public void setcoupon_fee(String coupon_fee) {
		this.coupon_fee_ = coupon_fee;
	}

	private String coupon_count_; // 代金券或立减优惠使用数量

	/**
	 * 代金券或立减优惠使用数量 coupon_count 否 Int 1 代金券或立减优惠使用数量
	 **/
	public String getcoupon_count() {
		return this.coupon_count_;
	}

	public void setcoupon_count(String coupon_count) {
		this.coupon_count_ = coupon_count;
	}

	private String coupon_batch_id_$n_; // 代金券或立减优惠批次ID

	/**
	 * 代金券或立减优惠批次ID coupon_batch_id_$n 否 String(20) 100
	 * 代金券或立减优惠批次ID ,$n为下标，从1开始编号
	 **/
	public String getcoupon_batch_id_$n() {
		return this.coupon_batch_id_$n_;
	}

	public void setcoupon_batch_id_$n(String coupon_batch_id_$n) {
		this.coupon_batch_id_$n_ = coupon_batch_id_$n;
	}

	private String coupon_id_$n_; // 代金券或立减优惠ID

	/**
	 * 代金券或立减优惠ID coupon_id_$n 否 String(20) 10000  代金券或立减优惠ID, $n为下标，从1开始编号
	 **/
	public String getcoupon_id_$n() {
		return this.coupon_id_$n_;
	}

	public void setcoupon_id_$n(String coupon_id_$n) {
		this.coupon_id_$n_ = coupon_id_$n;
	}

	private String coupon_fee_$n_; // 单个代金券或立减优惠支付金额

	/**
	 * 单个代金券或立减优惠支付金额 coupon_fee_$n 否 Int 100 单个代金券或立减优惠支付金额, $n为下标，从1开始编号
	 **/
	public String getcoupon_fee_$n() {
		return this.coupon_fee_$n_;
	}

	public void setcoupon_fee_$n(String coupon_fee_$n) {
		this.coupon_fee_$n_ = coupon_fee_$n;
	}

	private String transaction_id_; // 微信支付订单号

	/**
	 * 微信支付订单号 transaction_id 是 String(32) 1217752501201407033233368018 微信支付订单号
	 **/
	public String gettransaction_id() {
		return this.transaction_id_;
	}

	public void settransaction_id(String transaction_id) {
		this.transaction_id_ = transaction_id;
	}

	private String out_trade_no_; // 商户订单号

	/**
	 * 商户订单号 out_trade_no 是 String(32) 1217752501201407033233368018
	 * 商户系统的订单号，与请求一致。
	 **/
	public String getout_trade_no() {
		return this.out_trade_no_;
	}

	public void setout_trade_no(String out_trade_no) {
		this.out_trade_no_ = out_trade_no;
	}

	private String attach_; // 商家数据包

	/**
	 * 商家数据包 attach 否 String(128) 123456 商家数据包，原样返回
	 **/
	public String getattach() {
		return this.attach_;
	}

	public void setattach(String attach) {
		this.attach_ = attach;
	}

	private String time_end_; // 支付完成时间

	/**
	 * 支付完成时间 time_end 是 String(14) 20141030133525
	 * 订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	 **/
	public String gettime_end() {
		return this.time_end_;
	}

	public void settime_end(String time_end) {
		this.time_end_ = time_end;
	}

	private String trade_state_desc_; // 交易状态描述

	/**
	 * 交易状态描述 trade_state_desc 是 String(256) 支付失败，请重新下单支付 对当前查询订单状态的描述和下一步操作的指引
	 **/
	public String gettrade_state_desc() {
		return this.trade_state_desc_;
	}

	public void settrade_state_desc(String trade_state_desc) {
		this.trade_state_desc_ = trade_state_desc;
	}

}
