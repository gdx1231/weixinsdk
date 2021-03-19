package com.gdxsoft.weixin;

import java.util.Iterator;

import org.json.JSONObject;

/**
 * 微信企业 部门
 * 
 * @author admin
 *
 */

public class QyDepartment {

	/**
	 * 从json中返回对象
	 * 
	 * @param obj
	 *            JSONObject
	 * @return QyDepartment
	 */
	public static QyDepartment parse(JSONObject obj) {
		QyDepartment d = new QyDepartment();
		Iterator<?> keys = obj.keys();
		while (keys.hasNext()) {
			String key = keys.next().toString();
			if (key.equals("id")) {
				d.id_ = obj.getInt(key);
			} else if (key.equals("parentid")) {
				d.parentid_ = obj.getInt(key);
			} else if (key.equals("order")) {
				d.order_ = obj.getInt(key);
			} else if (key.equals("name")) {
				d.name_ = obj.getString(key);
			}
		}

		d.json_ = obj;
		return d;
	}

	private int id_;
	private String name_;
	private int parentid_;
	private int order_;

	private JSONObject json_;

	/**
	 * 部门id
	 * 
	 * @return
	 */
	public int getId() {
		return id_;
	}

	/**
	 * 部门id
	 * 
	 * @param id_
	 */
	public void setId(int id_) {
		this.id_ = id_;
	}

	/**
	 * 部门名称
	 * 
	 * @return
	 */
	public String getName() {
		return name_;
	}

	/**
	 * 部门名称
	 * 
	 * @param name_
	 */
	public void setName(String name_) {
		this.name_ = name_;
	}

	/**
	 * 父亲部门id。根部门为1
	 * 
	 * @return
	 */
	public int getParentid() {
		return parentid_;
	}

	/**
	 * 父亲部门id。根部门为1
	 * 
	 * @param parentid_
	 */
	public void setParentid(int parentid_) {
		this.parentid_ = parentid_;
	}

	/**
	 * 在父部门中的次序值。order值小的排序靠前。
	 * 
	 * @return
	 */
	public int getOrder() {
		return order_;
	}

	/**
	 * 在父部门中的次序值。order值小的排序靠前。
	 * 
	 * @param order_
	 */
	public void setOrder(int order_) {
		this.order_ = order_;
	}

	/**
	 * 返回JSON
	 * 
	 * @return
	 */
	public JSONObject toJson() {
		JSONObject body = new JSONObject();
		body.put("name", name_);
		body.put("parentid", parentid_);
		body.put("order", order_);
		body.put("id", id_);

		return body;
	}

	/**
	 * 获取parse 原始的json
	 * 
	 * @return
	 */
	public JSONObject getJson() {
		return json_;
	}

	/**
	 * parse 原始的json
	 * 
	 * @param json
	 */
	public void setJson_(JSONObject json) {
		this.json_ = json;
	}
}
