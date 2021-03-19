package com.gdxsoft.weixin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jackylian
 */
public class WxCardBaseInfo {

	private JSONObject json_;

	public JSONObject getJson() {
		return json_;
	}

	public void setJson(JSONObject json_) {
		this.json_ = json_;
	}

	public void parse(JSONObject obj) {
		WxCardBaseInfo o = this;
		o.json_ = obj;
		Iterator<?> keys = obj.keys();
		while (keys.hasNext()) {
			String key = keys.next().toString();
			if (key.equals("")) {
			} else if (key.equals("logo_url")) {
				// 卡券的商户logo，建议像素为300*300。
				o.setLogoUrl(obj.getString(key));
			} else if (key.equals("code_type")) {
				// "CODE_TYPE_TEXT"，文本；"CODE_TYPE_BARCODE"，一维码
				// ；"CODE_TYPE_QRCODE"，二维码；"CODE_TYPE_ONLY_QRCODE",二维码无code显示；"CODE_TYPE_ONLY_BARCODE",一维码无code显示；
				String code_type=obj.getString(key);
				int code=2;
				if(code_type.equals("CODE_TYPE_TEXT")){
					code=0;
				}else if(code_type.equals("CODE_TYPE_BARCODE")){
					code=1;
				}
				o.setCodeType(code);
			} else if (key.equals("brand_name")) {
				// 商户名字（填写直接提供服务的商户名，第三方商户名填写在source字段）。
				o.setBrandName(obj.getString(key));
			} else if (key.equals("title")) {
				// 卡券名。
				o.setTitle(obj.getString(key));
			} else if (key.equals("sub_title")) {
				// 卡券名的副标题。
				o.setSubTitle(obj.getString(key));
			} else if (key.equals("color")) {
				// 卡券的背景颜色。
				o.setColor(obj.getString(key));
			} else if (key.equals("notice")) {
				// 使用提醒，字数上限为16个汉字。
				o.setNotice(obj.getString(key));
			} else if (key.equals("description")) {
				// 使用说明。长文本描述。
				o.setDescription(obj.getString(key));
			} else if (key.equals("date_info")) {
				// 使用日期，有效期的信息。
				JSONObject date_info = obj.getJSONObject("date_info");
				int type = date_info.getInt("type");
				if (type == 1) {
					o.setDateInfoTimeRange(
							date_info.getLong("begin_timestamp"),
							date_info.getLong("end_timestamp"));

				} else {
					o.setDateInfoFixTerm(date_info.getInt("fixed_term"));
				}
			} else if (key.equals("sku")) {
				// 卡券库存的数量，不支持填写0。（上限为100000000）
				o.setQuantity(obj.getJSONObject(key).getInt("quantity"));
				o.setTotalQuantity(obj.getJSONObject(key).getInt("total_quantity"));
			} else if (key.equals("location_id_list")) {
				// 门店位置ID。
				JSONArray arr = obj.getJSONArray(key);
				ArrayList<Integer> al = new ArrayList<Integer>();
				for (int i = 0; i < arr.length(); i++) {
					al.add(arr.getInt(i));
				}
				o.setLocationIdList(al);

			} else if (key.equals("use_custom_code")) {
				// 是否自定义Code码。填写true或false，默认为false。
				o.setUseCustomCode(obj.getBoolean(key));
			} else if (key.equals("bind_openid")) {
				// 是否指定用户领取，填写true或false。默认为否。
				o.setBindOpenid(obj.getBoolean(key));
			} else if (key.equals("can_share")) {
				// 卡券是否可转赠，填写true或false,true代表可转赠。默认为true。
				o.setCanShare(obj.getBoolean(key));
			} else if (key.equals("service_phone")) {
				// 客服电话。
				o.setServicePhone(obj.getString(key));
			} else if (key.equals("source")) {
				// 第三方来源名，例如同程旅游、大众点评。
				o.setSource(obj.getString(key));
			} else if (key.equals("custom_url_name")) {
				// 商户自定义入口名称。
				o.setCustomUrlName(obj.getString(key));
			} else if (key.equals("custom_url")) {
				// 商户自定义入口跳转外链的地址链接,跳转页面内容需与自定义cell名称保持匹配。
				o.setCustomUrl(obj.getString(key));
			} else if (key.equals("custom_url_sub_title")) {
				// 显示在入口右侧的tips，长度限制在6个汉字内。
				o.setCustomUrlSubTitle(obj.getString(key));
			} else if (key.equals("promotion_url_name")) {
				// 营销场景的自定义入口。
				o.setPromotionUrlName(obj.getString(key));
			} else if (key.equals("promotion_url")) {
				// 入口跳转外链的地址链接。
				o.setPromotionUrl(obj.getString(key));
			} else if (key.equals("promotion_url_sub_title")) {
				// 显示在营销入口右侧的提示语。
				o.setPromotionUrlSubTitle(obj.getString(key));
			} else if (key.equals("custom_url_name")) {
				// 商户自定义入口名称。
				o.setCustomUrlName(obj.getString(key));
			} else if (key.equals("status")) {
				// “CARD_STATUS_NOT_VERIFY”,待审核；“CARD_STATUS_VERIFY_FALL”,审核失败；“CARD_STATUS_VERIFY_OK”，通过审核；“CARD_STATUS_USER_DELETE”，卡券被用户删除；“CARD_STATUS_USER_DISPATCH”，在公众平台投放过的卡券
				o.setStatus(obj.getString(key));
			} else if (key.equals("get_limit")) {
				//领取限制
				o.setGetLimit(obj.getInt(key)); 
			}
		}
	}

	
	/**
	 * 第三方来源名，例如同程旅游、大众点评。
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		m_data.put("source", source);
	}

	/**
	 * 第三方来源名，例如同程旅游、大众点评。
	 * 
	 * @return
	 */
	public String getSource() {
		return m_data.optString("source");
	}

	/**
	 * 商户自定义入口跳转外链的地址链接,跳转页面内容需与自定义cell名称保持匹配。
	 * 
	 * @param custom_url
	 */
	public void setCustomUrl(String custom_url) {
		m_data.put("custom_url", custom_url);
	}

	/**
	 * 商户自定义入口跳转外链的地址链接,跳转页面内容需与自定义cell名称保持匹配。
	 * 
	 * @return
	 */
	public String getCustomUrl() {
		return m_data.optString("custom_url");
	}

	/**
	 * 显示在入口右侧的tips，长度限制在6个汉字内
	 * 
	 * @param custom_url_sub_title
	 */
	public void setCustomUrlSubTitle(String custom_url_sub_title) {
		m_data.put("custom_url_sub_title", custom_url_sub_title);
	}

	/**
	 * 显示在入口右侧的tips，长度限制在6个汉字内
	 * 
	 * @return
	 */
	public String getCustomUrlSubTitle() {
		return m_data.optString("custom_url_sub_title");
	}

	public void setPromotionUrlName(String promotion_url_name) {
		m_data.put("promotion_url_name", promotion_url_name);
	}

	/**
	 * 商户自定义入口名称
	 * 
	 * @return
	 */
	public String getPromotionUrlName() {
		return m_data.optString("promotion_url_name");
	}

	public void setPromotionUrl(String promotion_url) {
		m_data.put("promotion_url", promotion_url);
	}

	/**
	 * 入口跳转外链的地址链接
	 * 
	 * @return
	 */
	public String getPromotionUrl() {
		return m_data.optString("promotion_url");
	}

	public void setPromotionUrlSubTitle(String promotion_url_sub_title) {
		m_data.put("promotion_url_sub_title", promotion_url_sub_title);
	}

	/**
	 * 显示在营销入口右侧的提示语
	 * 
	 * @return
	 */
	public String getPromotionUrlSubTitle() {
		return m_data.optString("promotion_url_sub_title");
	}

	public void setCustomUrlName(String custom_url_name) {
		m_data.put("custom_url_name", custom_url_name);

	}

	/**
	 * 商户自定义入口名称
	 * 
	 * @return
	 */
	public String getCustomUrlName() {
		return m_data.optString("custom_url_name");
	}

	public void setStatus(String status) {
		m_data.put("status", status);

	}

	/**
	 * “CARD_STATUS_NOT_VERIFY”,待审核；“CARD_STATUS_VERIFY_FALL”,审核失败；“
	 * CARD_STATUS_VERIFY_OK
	 * ”，通过审核；“CARD_STATUS_USER_DELETE”，卡券被用户删除；“CARD_STATUS_USER_DISPATCH
	 * ”，在公众平台投放过的卡券
	 * 
	 * @return
	 */
	public String getStatus() {
		return m_data.optString("status");
	}

	/**
	 * "CODE_TYPE_TEXT"，文本；<br>
	 */
	public final static int CODE_TYPE_TEXT = 0;
	/**
	 * "CODE_TYPE_BARCODE"，一维 码；<br>
	 */
	public final static int CODE_TYPE_BARCODE = 1;
	/**
	 * "CODE_TYPE_QRCODE"，二维 码；<br>
	 */
	public final static int CODE_TYPE_QRCODE = 2;
	org.json.JSONObject m_data;

	public WxCardBaseInfo() {
		m_data = new org.json.JSONObject();
		m_data.put("date_info", new org.json.JSONObject());
		m_data.put("location_id_list", new org.json.JSONArray());
		m_data.put("sku", new org.json.JSONObject());
	}

	public String toJsonString() {
		return m_data.toString();
	}

	public String toString() {
		return toJsonString();
	}

	/**
	 * 卡券的商户logo，尺寸为 300*300。
	 * 
	 * @param logoUrl
	 */
	public void setLogoUrl(String logoUrl) {
		m_data.put("logo_url", logoUrl);
	}

	/**
	 * 卡券的商户logo，尺寸为 300*300。
	 * 
	 * @return
	 */
	public String getLogoUrl() {
		return m_data.optString("logo_url");
	}

	public void setCodeType(int code) {
		m_data.put("code_type", code);
	}

	public int getCodeType() {
		return m_data.optInt("code_type");
	}

	/**
	 * 商户名字,字数上限为12 个汉字。 （填写直接提供服务的商户名， 第三方商户名填写在source 字 段）
	 * 
	 * @param name
	 */
	public void setBrandName(String name) {
		m_data.put("brand_name", name);
	}

	/**
	 * 商户名字,字数上限为12 个汉字。 （填写直接提供服务的商户名， 第三方商户名填写在source 字 段）
	 * 
	 * @return
	 */
	public String getBrandName() {
		return m_data.optString("brand_name");
	}

	/**
	 * 券名，字数上限为9 个汉字。(建 议涵盖卡券属性、服务及金额)
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		m_data.put("title", title);
	}

	/**
	 * 券名，字数上限为9 个汉字。(建 议涵盖卡券属性、服务及金额)
	 * 
	 * @return
	 */
	public String getTitle() {
		return m_data.optString("title");
	}

	/**
	 * 券名的副标题，字数上限为18 个 汉字。
	 * 
	 * @param subTitle
	 */
	public void setSubTitle(String subTitle) {
		m_data.put("sub_title", subTitle);
	}

	/**
	 * 券名的副标题，字数上限为18 个 汉字。
	 * 
	 * @return
	 */
	public String getSubTitle() {
		return m_data.optString("sub_title");
	}

	/**
	 * type= 1 设置卡有效期（开始时间和结束时间）
	 * 
	 * @param beginTime
	 *            type 为1 时专用，表示起用时间。
	 * @param endTime
	 *            type 为1 时专用，表示结束时间
	 */
	public void setDateInfoTimeRange(Date beginTime, Date endTime) {
		setDateInfoTimeRange(beginTime.getTime() / 1000,
				endTime.getTime() / 1000);
	}

	/**
	 * type= 1 设置卡有效期（开始时间和结束时间）
	 * 
	 * @param beginTimestamp
	 *            type 为1 时专用，表示起用时间。 从1970 年1 月1 日00:00:00 至 起用时间的秒数，最终需转换为
	 *            字符串形态传入，下同。（单位 为秒）
	 * @param endTimestamp
	 *            type 为1 时专用，表示结束时间。 （单位为秒）
	 */
	public void setDateInfoTimeRange(long beginTimestamp, long endTimestamp) {
		getDateInfo().put("type", 1);
		getDateInfo().put("begin_timestamp", beginTimestamp);
		getDateInfo().put("end_timestamp", endTimestamp);
	}

	/**
	 * type= 2：固定时长 （自领取后按天算）
	 * 
	 * @param fixedTerm
	 */
	public void setDateInfoFixTerm(int fixedTerm) {
		setDateInfoFixTerm(fixedTerm, 0);
	}

	/**
	 * type= 2：固定时长 （自领取后按天算）
	 * 
	 * @param fixedTerm
	 * @param fixedBeginTerm
	 *            是领取后多少天开始生效
	 */
	public void setDateInfoFixTerm(int fixedTerm, int fixedBeginTerm) {
		getDateInfo().put("type", 2);
		getDateInfo().put("fixed_term", fixedTerm);
		getDateInfo().put("fixed_begin_term", fixedBeginTerm);
	}

	/**
	 * 使用日期，有效期的信息
	 * 
	 * @return
	 */
	public org.json.JSONObject getDateInfo() {
		return m_data.optJSONObject("date_info");
	}

	/**
	 * 券颜色。按色彩规范标注填写 Color010-Color100
	 * 
	 * @param color
	 */
	public void setColor(String color) {
		m_data.put("color", color);
	}

	/**
	 * 券颜色。按色彩规范标注填写 Color010-Color100
	 * 
	 * @return
	 */
	public String getColor() {
		return m_data.optString("color");
	}

	/**
	 * 使用提醒，字数上限为12 个汉 字。（一句话描述，展示在首页， 示例：请出示二维码核销卡券）
	 * 
	 * @param notice
	 */
	public void setNotice(String notice) {
		m_data.put("notice", notice);
	}

	/**
	 * 使用提醒，字数上限为12 个汉 字。（一句话描述，展示在首页， 示例：请出示二维码核销卡券）
	 * 
	 * @return
	 */
	public String getNotice() {
		return m_data.optString("notice");
	}

	/**
	 * 客服电话
	 * 
	 * @param phone
	 */
	public void setServicePhone(String phone) {
		m_data.put("service_phone", phone);
	}

	/**
	 * 客服电话
	 * 
	 * @return
	 */
	public String getServicePhone() {
		return m_data.optString("service_phone");
	}

	/**
	 * 使用说明。长文本描述，可以分 行，上限为1000 个汉字。
	 * 
	 * @param desc
	 */
	public void setDescription(String desc) {
		m_data.put("description", desc);
	}

	/**
	 * 使用说明。长文本描述，可以分 行，上限为1000 个汉字。
	 * 
	 * @return
	 */
	public String getDescription() {
		return m_data.optString("description");
	}

	/**
	 * 门店位置ID。商户需在mp 平台 上录入门店信息或调用批量导入 门店信息接口获取门店位置ID。
	 * 
	 * @param value
	 *            门店位置IDs
	 */
	public void setLocationIdList(Collection<Integer> value) {
		org.json.JSONArray array = new org.json.JSONArray();
		for (int integer : value) {
			array.put(integer);
		}
		m_data.put("location_id_list", array);
	}

	/**
	 * 添加门店
	 * 
	 * @param locationId
	 */
	public void addLocationIdList(int locationId) {
		getLocationIdList().put(locationId);
	}

	/**
	 * 获取门店的 IDS
	 * 
	 * @return 门店的 IDS（JSONArray）
	 */
	public org.json.JSONArray getLocationIdList() {
		return m_data.getJSONArray("location_id_list");
	}

	public void setUseLimit(int limit) {
		m_data.put("use_limit", limit);
	}

	public int getUseLimit() {
		return m_data.optInt("useLimit");
	}

	/**
	 * 每人最大领取次数，不填写默认 等于quantity。
	 * 
	 * @param limit
	 */
	public void setGetLimit(int limit) {
		m_data.put("get_limit", limit);
	}

	/**
	 * 每人最大领取次数，不填写默认 等于quantity。
	 * 
	 * @return
	 */
	public int getGetLimit() {
		return m_data.optInt("get_limit");
	}

	/**
	 * 领取卡券原生页面是否可分享， 填写true 或false，true 代表可 分享。默认为true。
	 * 
	 * @param canShare
	 */
	public void setCanShare(boolean canShare) {
		m_data.put("can_share", canShare);
	}

	/**
	 * 领取卡券原生页面是否可分享， 填写true 或false，true 代表可 分享。默认为true。
	 * 
	 * @return
	 */
	public boolean getCanShare() {
		return m_data.optBoolean("can_share");
	}

	/**
	 * 卡券是否可转赠，填写true 或 false,true 代表可转赠。默认为 true。
	 * 
	 * @param canGive
	 */
	public void setCanGiveFriend(boolean canGive) {
		m_data.put("can_give_friend", canGive);
	}

	/**
	 * 卡券是否可转赠，填写true 或 false,true 代表可转赠。默认为 true。
	 * 
	 * @return
	 */
	public boolean getCanGiveFriend() {
		return m_data.optBoolean("can_give_friend");
	}

	/**
	 * 是否自定义code 码。填写true 或false，不填代表默认为false。 该字段需单独申请权限，详情见 三、开发者必读。
	 * 
	 * @param isUse
	 */
	public void setUseCustomCode(boolean isUse) {
		m_data.put("use_custom_code", isUse);
	}

	/**
	 * 是否自定义code 码。填写true 或false，不填代表默认为false。 该字段需单独申请权限，详情见 三、开发者必读。
	 * 
	 * @return
	 */
	public boolean getUseCustomCode() {
		return m_data.optBoolean("use_custom_code");
	}

	/**
	 * 是否指定用户领取，填写true 或 false。不填代表默认为否。
	 * 
	 * @param isBind
	 */
	public void setBindOpenid(boolean isBind) {
		m_data.put("bind_openid", isBind);
	}

	/**
	 * 是否指定用户领取，填写true 或 false。不填代表默认为否。
	 * 
	 * @return
	 */
	public boolean getBindOpenid() {
		return m_data.optBoolean("bind_openid");
	}

	/**
	 * 卡券库存的数量。（不支持填写0 或无限大）
	 * 
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		m_data.optJSONObject("sku").put("quantity", quantity);
	}

	/**
	 * 卡券库存的数量。（不支持填写0 或无限大）
	 * 
	 * @return
	 */
	public int getQuantity() {
		return m_data.optJSONObject("sku").optInt("quantity");
	}
	
	/**
	 * 获取时候 总共数量
	 * @param total_quantity
	 */
	public void setTotalQuantity(int total_quantity) {
		m_data.optJSONObject("sku").put("total_quantity", total_quantity);
		
	}
	/**
	 * 获取时候 总共数量
	 * @param total_quantity
	 */
	public int getTotalQuantity(){
		return m_data.optJSONObject("sku").optInt("total_quantity");
	}

}
