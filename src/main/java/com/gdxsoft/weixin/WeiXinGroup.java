package com.gdxsoft.weixin;

import java.util.Iterator;

import org.json.JSONObject;

/**
 * 微信分组信息
 * 
 * @author admin
 *
 */
public class WeiXinGroup {

	public static WeiXinGroup parse(JSONObject json) {
		/**
		 * { "id": 0, "name": "未分组", "count": 72596 }
		 */
		WeiXinGroup w = new WeiXinGroup();
		Iterator<?> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next().toString();
			if (key.equals("id")) {
				w.id_ = json.optInt(key);
			} else if (key.equals("count")) {
				w.count_ = json.optInt(key);
			} else if (key.equals("name")) {
				w.name_ = json.optString(key);
			}
		}

		return w;
	}

	private int id_;
	private String name_;
	private int count_;

	
	public JSONObject toJson(){
		JSONObject json=new JSONObject();
		JSONObject group=new JSONObject();
		json.put("group", group);
		group.put("id", this.id_);
		group.put("name", this.name_);
		
		return json;
	}
	
	/**
	 * 分组id，由微信分配
	 * 
	 * @return
	 */
	public int getId() {
		return id_;
	}

	/**
	 * 分组id，由微信分配
	 * 
	 * @param id_
	 */
	public void setId(int id_) {
		this.id_ = id_;
	}

	/**
	 * 分组名字，UTF8编码
	 * 
	 * @return
	 */
	public String getName() {
		return name_;
	}

	/**
	 * 分组名字，UTF8编码
	 * 
	 * @param name_
	 */
	public void setName(String name_) {
		this.name_ = name_;
	}

	/**
	 * 分组内用户数量
	 * 
	 * @return
	 */
	public int getCount() {
		return count_;
	}

	/**
	 * 分组内用户数量
	 * 
	 * @param count
	 */
	public void setCount(int count) {
		this.count_ = count;
	}

}
