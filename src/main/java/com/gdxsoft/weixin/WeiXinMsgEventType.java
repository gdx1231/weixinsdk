package com.gdxsoft.weixin;

/**
 * 事件类型
 * 
 * 1 关注/取消关注事件<br>
 * 2 扫描带参数二维码事件<br>
 * 3 上报地理位置事件<br>
 * 4 自定义菜单事件<br>
 * 5 点击菜单拉取消息时的事件推送<br>
 * 6 点击菜单跳转链接时的事件推送<br>
 * 
 * @author admin
 *
 */
public class WeiXinMsgEventType {

	/**
	 * subscribe(订阅)、
	 */
	public final static String subscribe = "subscribe";

	/**
	 * unsubscribe(取消订阅)
	 */
	public final static String unsubscribe = "unsubscribe";

	/**
	 * 扫描带参数二维码事件
	 */
	public final static String SCAN = "SCAN";

	public final static String scancode_push="scancode_push";
	/**
	 * 上报地理位置事件
	 */
	public final static String LOCATION = "LOCATION";

	/**
	 * 自定义菜单事件
	 */
	public final static String CLICK = "CLICK";

	/**
	 * 点击菜单跳转链接时的事件推送
	 */
	public final static String VIEW = "VIEW";

	/**
	 * 用户获取卡券
	 */
	public final static String user_get_card="user_get_card";
}
