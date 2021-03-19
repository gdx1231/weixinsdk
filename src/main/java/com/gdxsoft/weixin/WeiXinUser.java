package com.gdxsoft.weixin;

public class WeiXinUser {
	private Integer subscribe_;// 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
	private String openid_;// 用户的标识，对当前公众号唯一
	private String nickname_;// 用户的昵称
	private int sex_;// 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private String city_;// 用户所在城市
	private String country_;// 用户所在国家
	private String province_;// 用户所在省份
	private String language_;// 用户的语言，简体中文为zh_CN
	private String headimgurl_;// 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	private long subscribe_time_;// 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
	private String unionid_;// 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
	private String jsonStr_;
	private int groupid_; //分组编号

	public WeiXinUser() {

	}

	/**
	 * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
	 * 
	 * @return
	 */
	public Integer getSubscribe() { 
		//用Integer 因为当auth2.0时，不返回是否关注微信号参数
		return subscribe_;
	}

	/**
	 * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
	 * 
	 * @param subscribe_
	 */
	public void setSubscribe(int subscribe_) {
		this.subscribe_ = subscribe_;
	}

	/**
	 * 用户的标识，对当前公众号唯一
	 * 
	 * @return
	 */
	public String getOpenid() {
		return openid_;
	}

	/**
	 * 用户的标识，对当前公众号唯一
	 * 
	 * @param openid_
	 */
	public void setOpenid(String openid_) {
		this.openid_ = openid_;
	}

	public String getNickname() {
		return nickname_;
	}

	public void setNickname(String nickname_) {
		this.nickname_ = nickname_;
	}

	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 * 
	 * @return
	 */
	public int getSex() {
		return sex_;
	}

	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 * 
	 * @param sex_
	 */
	public void setSex(int sex_) {
		this.sex_ = sex_;
	}

	public String getCity() {
		return city_;
	}

	public void setCity(String city_) {
		this.city_ = city_;
	}

	public String getCountry() {
		return country_;
	}

	public void setCountry(String country_) {
		this.country_ = country_;
	}

	public String getProvince() {
		return province_;
	}

	public void setProvince(String province_) {
		this.province_ = province_;
	}

	/**
	 * 用户的语言，简体中文为zh_CN
	 * 
	 * @return
	 */
	public String getLanguage() {
		return language_;
	}

	/**
	 * 用户的语言，简体中文为zh_CN
	 * 
	 * @param language_
	 */
	public void setLanguage(String language_) {
		this.language_ = language_;
	}

	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。
	 * 若用户更换头像，原有头像URL将失效。
	 * 
	 * @return
	 */
	public String getHeadimgurl() {
		return headimgurl_;
	}

	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。
	 * 若用户更换头像，原有头像URL将失效。
	 * 
	 * @param headimgurl_
	 */
	public void setHeadimgurl(String headimgurl_) {
		this.headimgurl_ = headimgurl_;
	}

	public long getSubscribeTime() {
		return subscribe_time_;
	}

	public void setSubscribeTime(long subscribe_time_) {
		this.subscribe_time_ = subscribe_time_;
	}

	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
	 * 
	 * @return
	 */
	public String getUnionid() {
		return unionid_;
	}

	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
	 * 
	 * @param unionid_
	 */
	public void setUnionid(String unionid_) {
		this.unionid_ = unionid_;
	}
	
	public String getJsonStr() {
		return jsonStr_;
	}

	public void setJsonStr(String jsonStr_) {
		this.jsonStr_ = jsonStr_;
	}
	
	public String toString(){
		return this.jsonStr_;
	}

	/**
	 * 用户在微信公众号下的分组编号
	 * @return
	 */
	public int getGroupId() {
		return groupid_;
	}

	/**
	 * 用户在微信公众号下的分组编号
	 * @param groupid_
	 */
	public void setGroupId(int groupid_) {
		this.groupid_ = groupid_;
	}

}
