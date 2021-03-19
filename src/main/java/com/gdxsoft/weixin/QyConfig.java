package com.gdxsoft.weixin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信企业号
 * 
 * @author admin
 *
 */
public class QyConfig {
	private static String API_ROOT = "https://qyapi.weixin.qq.com/cgi-bin/";
	private static HashMap<Integer, QyConfig> MAP = new HashMap<Integer, QyConfig>();
	private static Logger LOGGER = LoggerFactory.getLogger(QyConfig.class);

	/**
	 * 实例化对象，如果缓存中存在，直接返回，如果超时或不存在，重新生成
	 * 
	 * @param appID
	 * @param appsecret
	 * @param token
	 * @return
	 */
	public static QyConfig instance(String corpid, String corpsecret) {
		String key = corpid + "?" + corpsecret;
		int code = key.hashCode();
		if (MAP.containsKey(code)) {
			QyConfig cfg = MAP.get(code);
			if (!cfg.checkIsExpired()) {
				// 过期时间超过50秒；
				return cfg;
			} else {
				// 过期重新获取
				MAP.remove(code);
			}
		}
		QyConfig cfg = new QyConfig(corpid, corpsecret);
		if (cfg.isOk_) {
			MAP.put(code, cfg);
			return cfg;
		} else {
			return null;
		}
	}

	public static QyConfig instance(String corpid, String corpsecret, int agentid) {
		String key = corpid + "?" + corpsecret + "?" + agentid;
		int code = key.hashCode();
		if (MAP.containsKey(code)) {
			QyConfig cfg = MAP.get(code);
			if (!cfg.checkIsExpired()) {
				// 过期时间超过50秒；
				return cfg;
			} else {
				// 过期重新获取
				MAP.remove(code);
			}
		}
		QyConfig cfg = new QyConfig(corpid, corpsecret, agentid);
		if (cfg.isOk_) {
			MAP.put(code, cfg);
			return cfg;
		} else {
			return null;
		}
	}

	private String corpid_; // 微信企业号
	private String corpsecret_;// 管理组的凭证密钥,每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret。
	private int agentid_; // 企业应用编号

	private int expires_in_ = 7200;// 凭证有效时间，单位：秒
	private String access_token_; // 获取到的凭证

	private String token_;
	private Long end_time_;
	private String lastErr;
	private boolean isOk_;
	private String[] weixinVaildIps_;

	private JSONObject lastPostResult_;

	/**
	 * 初始化接口
	 * 
	 * @param corpid     企业Id
	 * @param corpsecret 管理组的凭证密钥,每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret
	 */
	public QyConfig(String corpid, String corpsecret) {
		this.corpid_ = corpid;
		this.corpsecret_ = corpsecret;
		this.isOk_ = initGetAccessToken();
	}

	/**
	 * 初始化接口
	 * 
	 * @param corpid     企业Id
	 * @param corpsecret 管理组的凭证密钥,每个secret代表了对应用、通讯录、接口的不同权限；不同的管理组拥有不同的secret
	 * @param agentid    企业应用编号
	 */
	public QyConfig(String corpid, String corpsecret, int agentid) {
		this.corpid_ = corpid;
		this.corpsecret_ = corpsecret;
		this.setAgentid(agentid);
		this.isOk_ = initGetAccessToken();
	}

	/**
	 * 发送文字消息
	 * 
	 * @param toUsers 用户列表
	 * @param content 消息内容，最长不超过2048个字节
	 * @param isSafe  表示是否是保密消息，0表示否，1表示是，默认0
	 * @return
	 */
	public boolean sendTextMsg(List<String> toUsers, String content, boolean isSafe) {
		// https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN
		String url = API_ROOT + "message/send?access_token=" + this.access_token_;
		/**
		 * { "touser" : "UserID1|UserID2|UserID3", "toparty" : " PartyID1|PartyID2 ", "totag" : " TagID1 | TagID2 ",
		 * "msgtype" : "text", "agentid" : 1, "text" : { "content" : "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a
		 * href=\"http://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。" }, "safe":0 }
		 */

		JSONObject msg = new JSONObject();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < toUsers.size(); i++) {
			String user_id = toUsers.get(i).trim();
			if (i > 0) {
				sb.append("|");
			}
			sb.append(user_id);
		}
		msg.put("touser", sb.toString());

		JSONObject text = new JSONObject();
		text.put("content", content);
		msg.put("text", text);

		msg.put("msgtype", "text");
		msg.put("agentid", this.agentid_);
		msg.put("safe", isSafe ? 1 : 0); // 表示是否是保密消息，0表示否，1表示是，默认0

		JSONObject obj = this.postResult(url, msg);

		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(obj.toString());
			return false;
		}
	}

	public boolean sendTextMsg(String[] toUsers, String content, boolean isSafe) {
		List<String> al = new ArrayList<String>();
		for (int i = 0; i < toUsers.length; i++) {
			al.add(toUsers[i]);
		}
		return this.sendTextMsg(al, content, isSafe);
	}

	public boolean sendTextMsgToSingle(String toUser, String content, boolean isSafe) {
		List<String> al = new ArrayList<String>();
		al.add(toUser);
		return this.sendTextMsg(al, content, isSafe);
	}

	public boolean sendTextMsgToAll(String content, boolean isSafe) {
		return this.sendTextMsgToSingle("@all", content, isSafe);
	}

	/**
	 * 创建成员
	 * 
	 * @param staff
	 * @return
	 */
	public boolean createWxStaff(QyStaff staff) {
		// https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN
		String url = API_ROOT + "user/create?access_token=" + this.access_token_;

		JSONObject obj = this.postResult(url, staff.toJson());
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(obj.toString());
			return false;
		}
	}

	/**
	 * 创建成员
	 * 
	 * @param staff
	 * @return
	 */
	public boolean updateWxStaff(QyStaff staff) {
		// https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=ACCESS_TOKEN
		String url = API_ROOT + "user/update?access_token=" + this.access_token_;

		JSONObject obj = this.postResult(url, staff.toJson());
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(obj.toString());
			return false;
		}
	}

	/**
	 * 删除成员
	 * 
	 * @param userid
	 * @return
	 */
	public boolean deleteWxStaff(String userid) {
		// https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=ACCESS_TOKEN&userid=lisi
		String url = API_ROOT + "user/delete?access_token=" + this.access_token_ + "&userid=" + userid;

		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(obj.toString());

			return false;
		}
	}

	/**
	 * 删除成员
	 * 
	 * @param userids 成员UserID列表。对应管理端的帐号
	 * @return
	 */
	public boolean deleteWxStaffs(List<String> userids) {
		// https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete?access_token=ACCESS_TOKEN
		String url = API_ROOT + "user/batchdelete?access_token=" + this.access_token_;

		JSONObject body = new JSONObject();
		JSONArray arr = new JSONArray();

		for (int i = 0; i < userids.size(); i++) {
			arr.put(arr.getString(i));
		}
		body.put("useridlist", arr);
		JSONObject obj = this.postResult(url, body);
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(obj.toString());
			return false;
		}
	}

	/**
	 * 获取成员
	 * 
	 * @param userid 成员UserID。对应管理端的帐号
	 * @return
	 */
	public QyStaff getWxStaff(String userid) {
		// https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=lisi
		String url = API_ROOT + "user/get?access_token=" + this.access_token_ + "&userid=" + userid;

		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return null;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			QyStaff q = QyStaff.parse(obj);
			return q;
		} else {
			this.setError(obj.toString());

			return null;
		}
	}

	/**
	 * 获取部门成员信息
	 * 
	 * @param department_id  获取的部门id
	 * @param is_fetch_child 1/0：是否递归获取子部门下面的成员
	 * @param status         0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加
	 * @param is_sample_info 是否获取简单信息
	 */
	public List<QyStaff> getDepartmentStaffs(int department_id, boolean is_fetch_child, int status,
			boolean is_sample_info) {
		// 简单信息（simplelist）
		// https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=ACCESS_TOKEN&department_id=1
		// &fetch_child=0&status=0

		// 全部信息（list）
		// https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=ACCESS_TOKEN&department_id=1&fetch_child=0&status=0
		String url = API_ROOT + "user/" + (is_sample_info ? "simplelist" : "list") + "?access_token="
				+ this.access_token_ + "&department_id=" + department_id + "&fetch_child="
				+ (is_fetch_child ? "1" : "0") + "&stats=" + status;

		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return null;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			JSONArray arr = obj.getJSONArray("userlist");
			List<QyStaff> al = new ArrayList<QyStaff>();
			for (int i = 0; i < arr.length(); i++) {
				QyStaff q = QyStaff.parse(arr.getJSONObject(i));
				al.add(q);
			}
			return al;
		} else {
			this.setError(obj.toString());

			return null;
		}
	}

	/**
	 * 邀请成员关注
	 * 
	 * 认证号优先使用微信推送邀请关注，如果没有weixinid字段则依次对手机号，邮箱绑定的微信进行推送，全部没有匹配则通过邮件邀请关注。 邮箱字段无效则邀请失败。 非认证号只通过邮件邀请关注。邮箱字段无效则邀请失败。
	 * 已关注以及被禁用成员不允许发起邀请关注请求。
	 * 
	 * 为避免骚扰成员，企业应遵守以下邀请规则：
	 * 
	 * 每月邀请的总人次不超过成员上限的2倍；每7天对同一个成员只能邀请一次。
	 * 
	 * @param userid 成员UserID。对应管理端的帐号
	 * @param msg    推送到微信上的提示语（只有认证号可以使用）。当使用微信推送时，该字段默认为“请关注XXX企业号”，邮件邀请时，该字段无效。
	 * @return 1:微信邀请 2.邮件邀请,-1错误
	 */
	public int invateStaffJoin(String userid, String msg) {
		// https://qyapi.weixin.qq.com/cgi-bin/invite/send?access_token=ACCESS_TOKEN
		String url = API_ROOT + "invite/send?access_token=" + this.access_token_;
		JSONObject body = new JSONObject();
		// { "userid":"xxxxx","invite_tips":"xxx"}

		body.put("userid", userid);
		body.put("invite_tips", msg);

		JSONObject obj = this.postResult(url, body);
		if (obj == null) {
			return -1;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return obj.getInt("type");
		} else {
			this.setError(obj.toString());
			return -1;
		}
	}

	/**
	 * 创建部门
	 * 
	 * @param name     部门名称。长度限制为1~64个字节
	 * @param parentid 父亲部门id。根部门id为1
	 * @param order    在父部门中的次序值。order值小的排序靠前。
	 * @param id       部门id，整型。指定时必须大于1，不指定时则自动生成
	 * @return 生成的部门编号
	 */
	public int createWxDepartment(String name, int parentid, int order, int id) {
		QyDepartment d = new QyDepartment();
		d.setId(id);
		d.setName(name);
		d.setParentid(parentid);
		d.setOrder(order);

		return this.createWxDepartment(d);
	}

	/**
	 * 创建部门
	 * 
	 * @param d
	 * @return
	 */
	public int createWxDepartment(QyDepartment d) {
		String url = API_ROOT + "department/create?access_token=" + this.access_token_;

		JSONObject obj = this.postResult(url, d.toJson());
		if (obj == null) {
			return -1;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return obj.getInt("id");
		} else {
			this.setError(obj.toString());
			return -1;
		}
	}

	/**
	 * 更新部门
	 * 
	 * @param d
	 * @return
	 */
	public boolean updateWxDepartment(QyDepartment d) {
		// https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=ACCESS_TOKEN
		String url = API_ROOT + "department/update?access_token=" + this.access_token_;
		JSONObject obj = this.postResult(url, d.toJson());
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(obj.toString());
			return false;
		}
	}

	/**
	 * 删除部门
	 * 
	 * @param id 部门编号(微信的)
	 * @return
	 */
	public boolean deleteWxDepartment(int id) {
		// https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=ACCESS_TOKEN&id=2
		String url = API_ROOT + "department/delete?access_token=" + this.access_token_ + "&id=" + id;
		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return false;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			return true;
		} else {
			this.setError(obj.toString());

			return false;
		}
	}

	/**
	 * 获取微信所有部门
	 * 
	 * @return
	 */
	public List<QyDepartment> getWxDepartments() {
		// https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID
		String url = API_ROOT + "department/list?access_token=" + this.access_token_;
		JSONObject obj = this.getResult(url);
		if (obj == null) {
			return null;
		}
		if (obj.has("errcode") && obj.getInt("errcode") == 0) {
			JSONArray lst = obj.getJSONArray("department");
			List<QyDepartment> al = new ArrayList<QyDepartment>();
			for (int i = 0; i < lst.length(); i++) {
				QyDepartment d = QyDepartment.parse(lst.getJSONObject(i));
				al.add(d);
			}

			return al;
		} else {
			this.setError(obj.toString());

			return null;
		}
	}

	JSONObject postResult(String url, JSONObject body) {
		String rst = "";
		// HashMap<String, String> params = new HashMap<String, String>();
		// params.put("body", body.toString());
		Ssl.log(body.toString());
		try {
			rst = Ssl.postMsg(url, body.toString());
		} catch (Exception e) {
			this.setError(e.getMessage());
			return null;
		}

		JSONObject obj;
		try {
			obj = new JSONObject(rst);
			this.lastPostResult_ = obj;
			return obj;
		} catch (JSONException e) {
			this.setError(e.getMessage());
			LOGGER.error(url);
			LOGGER.error(body.toString());
			LOGGER.error(e.getMessage());

			return null;
		}
	}

	private boolean initGetAccessToken() {
		// https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=id&corpsecret=secrect
		String u1 = API_ROOT + "gettoken?corpid=" + this.corpid_ + "&corpsecret=" + this.corpsecret_;

		JSONObject obj = this.getResult(u1);
		if (obj == null) {
			return false;
		}
		try {
			String access_token = obj.getString("access_token");
			this.access_token_ = access_token;
			// 过期时间
			this.end_time_ = new Date().getTime() + this.expires_in_ * 1000;
			return true;
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return false;
		}
	}

	/**
	 * 检查是否超时
	 * 
	 * @return
	 */
	public boolean checkIsExpired() {
		long span = this.end_time_ - System.currentTimeMillis();
		if (span >= 50 * 1000) {
			return false;
		} else {
			System.out.println("AccessToken 超时：" + span);
			return true;
		}
	}

	/**
	 * 映射为JSON
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONObject toJson() throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("corpid", this.corpid_);
		obj.put("corpsecret", this.corpsecret_);

		obj.put("token", this.token_);
		obj.put("end_time", this.end_time_);
		obj.put("access_token", this.access_token_);
		obj.put("expires_in", this.expires_in_);
		obj.put("agentid", this.agentid_);

		return obj;
	}

	private JSONObject getResult(String url) {
		String rst = "";
		try {
			rst = Ssl.get(url);
		} catch (Exception e) {
			this.setError(e.getMessage());
			return null;
		}

		JSONObject obj;
		try {
			obj = new JSONObject(rst);

			return obj;
		} catch (JSONException e) {
			this.setError(e.getMessage());
			return null;
		}
	}

	public void setError(String errmsg) {
		this.lastErr = errmsg;
		LOGGER.error("错误" + errmsg);

	}

	/**
	 * 获取最后错误
	 * 
	 * @return
	 */
	public String getLastErr() {
		return lastErr;
	}

	/**
	 * @return the agentid_
	 */
	public int getAgentid() {
		return agentid_;
	}

	/**
	 * @param agentid_ the agentid_ to set
	 */
	public void setAgentid(int agentid) {
		this.agentid_ = agentid;
	}

	/**
	 * @return the weixinVaildIps_
	 */
	public String[] getWeixinVaildIps() {
		return weixinVaildIps_;
	}

	/**
	 * 微信企业号 corpid
	 * 
	 * @return
	 */
	public String getCorpId() {
		return corpid_;
	}

	/**
	 * 获取到的凭证
	 * 
	 * @return
	 */
	public String getAccessToken() {
		return access_token_;
	}

	/**
	 * 最后一次提交返回的结果
	 * 
	 * @return the lastPostResult_
	 */
	public JSONObject getLastPostResult() {
		return lastPostResult_;
	}
}
