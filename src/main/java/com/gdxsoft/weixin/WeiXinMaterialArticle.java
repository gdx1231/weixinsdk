package com.gdxsoft.weixin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeiXinMaterialArticle {
	/**
	 * 从json返回对象
	 * 
	 * @return
	 **/
	public static WeiXinMaterialArticle parse(JSONObject item) {

		WeiXinMaterialArticle article = new WeiXinMaterialArticle();
		article.json_ = item;
		JSONObject content = item.getJSONObject("content");
		if (item.has("update_time")) {
			article.setUpdateTime(item.getInt("update_time"));
		}
		if (item.has("media_id")) {
			article.setMediaId(item.getString("media_id"));
		}
		JSONArray arr = content.getJSONArray("news_item");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			Iterator<?> keys = obj.keys();
			WeiXinMaterialArticle o;
			if (i == 0) {
				o = article;
			} else {
				// 相关文档
				o = new WeiXinMaterialArticle();
//				if (article.getRelated() == null) {
//					article.related_ = new ArrayList<WeiXinMaterialArticle>();
//				}
//				article.getRelated().add(o);
				article.addSub(o);
			}
			while (keys.hasNext()) {
				String key = keys.next().toString();
				if (key.equals("")) {
				} else if (key.equals("title")) {
					// 标题
					o.title_ = obj.getString(key);
				} else if (key.equals("thumb_media_id")) {
					// 图文消息的封面图片素材id（必须是永久mediaID）
					o.thumb_media_id_ = obj.getString(key);
				} else if (key.equals("author")) {
					// 作者
					o.author_ = obj.getString(key);
				} else if (key.equals("digest")) {
					// 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
					o.digest_ = obj.getString(key);
				} else if (key.equals("show_cover_pic")) {
					// 是否显示封面，0为false，即不显示，1为true，即显示
					o.show_cover_pic_ = obj.getInt(key);
				} else if (key.equals("content")) {
					// 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
					o.content_ = obj.getString(key);
				} else if (key.equals("content_source_url")) {
					// 图文消息的原文地址，即点击“阅读原文”后的URL
					o.content_source_url_ = obj.getString(key);
				}
			}
		}
		return article;
	}

	/**
	 * 添加相关子文档
	 * @param sub
	 */
	public void addSub(WeiXinMaterialArticle sub){
		if (this.getRelated() == null) {
			this.related_ = new ArrayList<WeiXinMaterialArticle>();
		}
		this.getRelated().add(sub);
	}
	
	/**
	 * 返回json对象
	 * 
	 * @return
	 **/
	public JSONObject toJson() {
		JSONObject body = new JSONObject();
		JSONArray arr = new JSONArray();
		body.put("articles", arr);

		JSONObject obj = this.toJsonChd();

		arr.put(obj);

		if (this.related_ != null) {
			for (int i = 0; i < this.related_.size(); i++) {
				JSONObject r = this.related_.get(i).toJsonChd();
				arr.put(r);
			}
		}
		if (arr.length() == 1) {
			// 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
			obj.put("digest", this.digest_);
		}
		return body;
	}
	/**
	 * 生成相关联子文档
	 * @return
	 */
	public JSONObject toJsonChd() {

		JSONObject obj = new JSONObject();

		// 标题
		obj.put("title", this.title_);

		// 图文消息的封面图片素材id（必须是永久mediaID）
		obj.put("thumb_media_id", this.thumb_media_id_);

		// 作者
		obj.put("author", this.author_);

		// 是否显示封面，0为false，即不显示，1为true，即显示
		obj.put("show_cover_pic", this.show_cover_pic_);

		// 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
		obj.put("content", this.content_);

		// 图文消息的原文地址，即点击“阅读原文”后的URL
		obj.put("content_source_url", this.content_source_url_);

 
		return obj;
	}

	/**
	 * 返回更新用的json对象
	 * 
	 * @param index
	 *            要更新的文章在图文消息中的位置（多图文消息时，此字段才有意义），第一篇为0
	 * @return
	 **/
	public JSONObject toUpdateJson(int index) {
		JSONObject body = new JSONObject();
		body.put("media_id", this.media_id_);
		body.put("index", index);
		
		JSONObject obj = new JSONObject();
		//JSONArray arr = new JSONArray();
		//body.put("articles", arr);
		//arr.put(obj);
		
		body.put("articles", obj);

		// 标题
		obj.put("title", this.title_);

		// 图文消息的封面图片素材id（必须是永久mediaID）
		obj.put("thumb_media_id", this.thumb_media_id_);

		// 作者
		obj.put("author", this.author_);

		// 是否显示封面，0为false，即不显示，1为true，即显示
		obj.put("show_cover_pic", this.show_cover_pic_);

		// 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
		obj.put("content", this.content_);

		// 图文消息的原文地址，即点击“阅读原文”后的URL
		obj.put("content_source_url", this.content_source_url_);

		// 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
		obj.put("digest", this.digest_);
		return body;
	}

	private JSONObject json_;

	public JSONObject getJson() {
		return this.json_;
	}

	private String title_; // 标题

	/**
	 * 返回标题
	 * 
	 * @return
	 **/
	public String getTitle() {
		return this.title_;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 **/
	public void setTitle(String title) {
		this.title_ = title;
	}

	private String thumb_media_id_; // 图文消息的封面图片素材id（必须是永久mediaID）

	/**
	 * 返回图文消息的封面图片素材id（必须是永久mediaID）
	 * 
	 * @return
	 **/
	public String getThumbMediaId() {
		return this.thumb_media_id_;
	}

	/**
	 * 设置图文消息的封面图片素材id（必须是永久mediaID）
	 * 
	 * @param thumb_media_id
	 **/
	public void setThumbMediaId(String thumb_media_id) {
		this.thumb_media_id_ = thumb_media_id;
	}

	private String author_; // 作者

	/**
	 * 返回作者
	 * 
	 * @return
	 **/
	public String getAuthor() {
		return this.author_;
	}

	/**
	 * 设置作者
	 * 
	 * @param author
	 **/
	public void setAuthor(String author) {
		this.author_ = author;
	}

	private String digest_; // 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空

	/**
	 * 返回图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
	 * 
	 * @return
	 **/
	public String getDigest() {
		return this.digest_;
	}

	/**
	 * 设置图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
	 * 
	 * @param digest
	 **/
	public void setDigest(String digest) {
		this.digest_ = digest;
	}

	private int show_cover_pic_; // 是否显示封面，0为false，即不显示，1为true，即显示

	/**
	 * 返回是否显示封面，0为false，即不显示，1为true，即显示
	 * 
	 * @return
	 **/
	public int getShowCoverPic() {
		return this.show_cover_pic_;
	}

	/**
	 * 设置是否显示封面，0为false，即不显示，1为true，即显示
	 * 
	 * @param show_cover_pic
	 **/
	public void setShowCoverPic(int show_cover_pic) {
		this.show_cover_pic_ = show_cover_pic;
	}

	private String content_; // 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS

	/**
	 * 返回图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
	 * 
	 * @return
	 **/
	public String getContent() {
		return this.content_;
	}

	/**
	 * 设置图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS
	 * 
	 * @param content
	 **/
	public void setContent(String content) {
		this.content_ = content;
	}

	private String content_source_url_; // 图文消息的原文地址，即点击“阅读原文”后的URL

	/**
	 * 返回图文消息的原文地址，即点击“阅读原文”后的URL
	 * 
	 * @return
	 **/
	public String getContentSourceUrl() {
		return this.content_source_url_;
	}

	/**
	 * 设置图文消息的原文地址，即点击“阅读原文”后的URL
	 * 
	 * @param content_source_url
	 **/
	public void setContentSourceUrl(String content_source_url) {
		this.content_source_url_ = content_source_url;
	}

	private List<WeiXinMaterialArticle> related_;// 多图文素材

	/**
	 * 多图文素材的下级
	 * 
	 * @return
	 */
	public List<WeiXinMaterialArticle> getRelated() {
		return related_;
	}

	/**
	 * 多图文素材
	 * 
	 * @param related
	 */
	public void setRelated_(List<WeiXinMaterialArticle> related) {
		this.related_ = related;
	}

	/**
	 * 是否为多图文素材
	 * 
	 * @return
	 */
	public boolean isMulti() {
		return this.related_ != null && this.related_.size() > 0;
	}

	private int update_time_;
	private String media_id_;

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
}
