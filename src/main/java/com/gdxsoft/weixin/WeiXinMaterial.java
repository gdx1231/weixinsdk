package com.gdxsoft.weixin;

import org.json.JSONObject;

/**
 * 素材 ，图片（image）、视频（video）、语音 （voice）
 * 
 * @author admin
 *
 */
public class WeiXinMaterial {

	private JSONObject json_;

	public JSONObject getJson() {
		return this.json_;
	}

	/**
	 * 从json返回对象
	 * 
	 * @return
	 **/
	public static WeiXinMaterial parse(JSONObject item, String mtype) {

		WeiXinMaterial article = new WeiXinMaterial();
		article.json_ = item;
		if (item.has("update_time")) {
			article.setUpdateTime(item.getInt("update_time"));
		}
		if (item.has("media_id")) {
			article.setMediaId(item.getString("media_id"));
		}
		if (item.has("name")) {
			article.setName(item.getString("name"));
		}
		article.material_type_ = mtype;
		return article;
	}

	private int update_time_;
	private String media_id_;
	private String name_;
	private String material_type_;

	/**
	 * 更新时间(秒)
	 * 
	 * @return
	 */
	public int getUpdateTime() {
		return update_time_;
	}

	/**
	 * 更新时间(秒)
	 * 
	 * @param update_time_
	 */
	public void setUpdateTime(int update_time_) {
		this.update_time_ = update_time_;
	}

	/**
	 * 微信媒体编号
	 * 
	 * @return
	 */
	public String getMediaId() {
		return media_id_;
	}

	/**
	 * 微信媒体编号
	 * 
	 * @param media_id_
	 */
	public void setMediaId(String media_id_) {
		this.media_id_ = media_id_;
	}

	public String getName() {
		return name_;
	}

	public void setName(String name_) {
		this.name_ = name_;
	}

	/**
	 * 素材的类型，图片（image）、视频（video）、语音 （voice）
	 * 
	 * @return
	 */
	public String getMaterialType() {
		return material_type_;
	}

	/**
	 * 素材的类型，图片（image）、视频（video）、语音 （voice）
	 * 
	 * @param material_type_
	 */
	public void setMaterialType(String material_type_) {
		this.material_type_ = material_type_;
	}

	/**
	 * 返回json对象
	 * 
	 * @return
	 **/
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		obj.put("name", this.name_);
		// 图文消息的封面图片素材id（必须是永久mediaID）
		obj.put("media_id", this.media_id_);
		// 作者
		obj.put("update_time", this.update_time_);

		return obj;
	}
}
