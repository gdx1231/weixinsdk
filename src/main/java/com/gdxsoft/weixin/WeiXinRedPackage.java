package com.gdxsoft.weixin;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.gdxsoft.easyweb.utils.UXml;
import com.gdxsoft.easyweb.utils.Utils;

public class WeiXinRedPackage {

	private WeiXinRedPackageResult OrderResult_;
	
	/**
	 * 红包发送返回的结果
	 * @return
	 */
	public WeiXinRedPackageResult getOrderResult() {
		return OrderResult_;
	}

	/**
	 * 红包发送返回的结果
	 * @param orderResult_
	 */
	public void setOrderResult(WeiXinRedPackageResult orderResult_) {
		OrderResult_ = orderResult_;
	}

	private String nonce_str_; // 随机字符串

	/**
	 * 随机字符串 nonce_str 是 5K8264ILTKCH16CQ2502SI8ZNMTM67VS String(32)
	 * 随机字符串，不长于32位
	 **/
	public String getNonceStr() {
		return this.nonce_str_;
	}

	public void setNonceStr(String nonce_str) {
		this.nonce_str_ = nonce_str;
	}

	private String sign_; // 签名

	/**
	 * 签名 sign 是 C380BEC2BFD727A4B6845133519F3AD6 String(32) 详见签名生成算法
	 **/
	public String getSign() {
		return this.sign_;
	}

	private String mch_billno_; // 商户订单号

	/**
	 * 商户订单号 mch_billno 是 10000098201411111234567890 String(28) 商户订单号（每个订单号必须唯一）
	 * 组成： mch_id+yyyymmdd+10位一天内不能重复的数字。 接口根据商户订单号支持重入， 如出现超时可再调用。
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

	private String sub_mch_id_; // 子商户号

	/**
	 * 子商户号 sub_mch_id 否 10000090 String(32) 微信支付分配的子商户号，受理模式下必填
	 **/
	public String getSubMchId() {
		return this.sub_mch_id_;
	}

	public void setSubMchId(String sub_mch_id) {
		this.sub_mch_id_ = sub_mch_id;
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

	private String nick_name_; // 提供方名称

	/**
	 * 提供方名称 nick_name 是 天虹百货 String(32) 提供方名称
	 **/
	public String getNickName() {
		return this.nick_name_;
	}

	public void setNickName(String nick_name) {
		this.nick_name_ = nick_name;
	}

	private String send_name_; // 商户名称

	/**
	 * 商户名称 send_name 是 天虹百货 String(32) 红包发送者名称
	 **/
	public String getSendName() {
		return this.send_name_;
	}

	public void setSendName(String send_name) {
		this.send_name_ = send_name;
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

	private int total_amount_; // 付款金额

	/**
	 * 付款金额 total_amount 是 1000 int 付款金额，单位分
	 **/
	public int getTotalAmount() {
		return this.total_amount_;
	}

	public void setTotalAmount(int total_amount) {
		this.total_amount_ = total_amount;
	}

	private int min_value_; // 最小红包金额

	/**
	 * 最小红包金额 min_value 是 1000 int 最小红包金额，单位分
	 **/
	public int getMinValue() {
		return this.min_value_;
	}

	public void setMinValue(int min_value) {
		this.min_value_ = min_value;
	}

	private int max_value_; // 最大红包金额

	/**
	 * 最大红包金额 max_value 是 1000 int 最大红包金额，单位分
	 * （ 最小金额等于最大金额： min_value=max_value =total_amount）
	 **/
	public int getMaxValue() {
		return this.max_value_;
	}

	public void setMaxValue(int max_value) {
		this.max_value_ = max_value;
	}

	private int total_num_; // 红包发放总人数

	/**
	 * 红包发放总人数 total_num 是 1 int 红包发放总人数 total_num=1
	 **/
	public int getTotalNum() {
		return this.total_num_;
	}

	public void setTotalNum(int total_num) {
		this.total_num_ = total_num;
	}

	private String wishing_; // 红包祝福语

	/**
	 * 红包祝福语 wishing 是 感谢您参加猜灯谜活动，祝您元宵节快乐！ String(128) 红包祝福语
	 **/
	public String getWishing() {
		return this.wishing_;
	}

	public void setWishing(String wishing) {
		this.wishing_ = wishing;
	}

	private String client_ip_; // Ip地址

	/**
	 * Ip地址 client_ip 是 192.168.0.1 String(15) 调用接口的机器Ip地址
	 **/
	public String getClientIp() {
		return this.client_ip_;
	}

	public void setClientIp(String client_ip) {
		this.client_ip_ = client_ip;
	}

	private String act_name_; // 活动名称

	/**
	 * 活动名称 act_name 是 猜灯谜抢红包活动 String(32) 活动名称
	 **/
	public String getActName() {
		return this.act_name_;
	}

	public void setActName(String act_name) {
		this.act_name_ = act_name;
	}

	private String remark_; // 备注

	/**
	 * 备注 remark 是 猜越多得越多，快来抢！ String(256) 备注信息
	 **/
	public String getRemark() {
		return this.remark_;
	}

	public void setRemark(String remark) {
		this.remark_ = remark;
	}

	private String logo_imgurl_; // 商户logo的url

	/**
	 * 商户logo的url logo_imgurl 否 https://wx.gtimg.com/mch/img/ico-logo.png
	 * String(128) 商户logo的url
	 **/
	public String getLogoImgurl() {
		return this.logo_imgurl_;
	}

	public void setLogoImgurl(String logo_imgurl) {
		this.logo_imgurl_ = logo_imgurl;
	}

	private String share_content_; // 分享文案

	/**
	 * 分享文案 share_content 否 快来参加猜灯谜活动 String(256) 分享文案
	 **/
	public String getShareContent() {
		return this.share_content_;
	}

	public void setShareContent(String share_content) {
		this.share_content_ = share_content;
	}

	private String share_url_; // 分享链接

	/**
	 * 分享链接 share_url 否 http://www.qq.com String(128) 分享链接
	 **/
	public String getShareUrl() {
		return this.share_url_;
	}

	public void setShareUrl(String share_url) {
		this.share_url_ = share_url;
	}

	private String share_imgurl_; // 分享的图片

	/**
	 * 分享的图片 share_imgurl 否 https://wx.gtimg.com/mch/img/ico-logo.png
	 * String(128) 分享的图片url
	 **/
	public String getShareImgurl() {
		return this.share_imgurl_;
	}

	public void setShareImgurl(String share_imgurl) {
		this.share_imgurl_ = share_imgurl;
	}

	private Document doc_;

	/**
	 * 生成提交的XML
	 * 
	 * @param shopKey
	 *            http://pay.weixin.qq.com/上的api key
	 * @return
	 */
	public String toPostXml(String shopKey) {
		doc_ = UXml.asDocument("<xml>\n</xml>");

		this.addXmlParam("mch_billno", this.mch_billno_);
		this.addXmlParam("mch_id", this.mch_id_);
		this.addXmlParam("wxappid", this.wxappid_);
		this.addXmlParam("nick_name", this.nick_name_);
		this.addXmlParam("send_name", this.send_name_);
		this.addXmlParam("re_openid", this.re_openid_);
		// （ 最小金额等于最大金额： min_value=max_value =total_amount）
		this.addXmlParam("total_amount", this.total_amount_ + "");
		this.addXmlParam("min_value", this.min_value_ + "");
		this.addXmlParam("max_value", this.max_value_ + "");
		this.addXmlParam("total_num", this.total_num_ + "");

		this.addXmlParam("wishing", this.wishing_);
		this.addXmlParam("client_ip", this.client_ip_);

		this.addXmlParam("act_name", this.act_name_);
		//this.addXmlParam("act_id", "29271");
		this.addXmlParam("remark", this.remark_);

//		this.addXmlParam("logo_imgurl", this.logo_imgurl_);
//		this.addXmlParam("share_content", this.share_content_);
//		this.addXmlParam("share_url", this.share_url_);
//		this.addXmlParam("share_imgurl", this.share_imgurl_);

		if (this.nonce_str_ == null || this.nonce_str_.trim().length() == 0) {
			this.nonce_str_ = Utils.getGuid().replace("-", "");
		}
		if (this.nonce_str_.length() > 32) {
			this.nonce_str_ = this.nonce_str_.substring(0, 32);
		}
		this.addXmlParam("nonce_str", this.nonce_str_);

		this.addXmlParam("sub_mch_id", this.sub_mch_id_);

		// sign
		this.sign_ = SignUtils.signMd5(doc_, "key", shopKey);
		//doc_.getElementsByTagName("sign").item(0).setTextContent(this.sign_);
		
		this.addXmlParam("sign", this.sign_);
		String xmlcontent = UXml.asXml(doc_);
		return xmlcontent;
	}

	private void addXmlParam(String nodeName, String nodeVal) {

		if (nodeVal == null || nodeVal.trim().length() == 0
				&& !nodeName.equals("sign")) {
			return;
		}
		Element ele = doc_.createElement(nodeName);
		CDATASection cdata = doc_.createCDATASection(nodeVal);
		ele.appendChild(cdata);

		// if (nodeVal != null) {
		// ele.setTextContent(nodeVal.trim());
		// }
		doc_.getFirstChild().appendChild(ele);

		Text t = doc_.createTextNode("\n");
		doc_.getFirstChild().appendChild(t);
	}
}
