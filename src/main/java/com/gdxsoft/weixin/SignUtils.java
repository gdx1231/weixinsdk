package com.gdxsoft.weixin;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gdxsoft.easyweb.utils.Utils;
import com.gdxsoft.easyweb.utils.msnet.MStr;

public class SignUtils {

	private static char[] CHARS = "01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * JSON对象 签名Md5
	 * 
	 * @param json
	 * @param keyName
	 * @param keyVal
	 * @return
	 */
	public static String signMd5(JSONObject json, String keyName, String keyVal) {
		Iterator<?> keys = json.keys();

		ArrayList<String> names = new ArrayList<String>();

		while (keys.hasNext()) {
			String key = keys.next().toString();
			names.add(key);
		}

		String[] names1 = new String[names.size()];
		names.toArray(names1);
		Arrays.sort(names1);
		StringBuilder sb = new StringBuilder();
		try {
			for (int i = 0; i < names1.length; i++) {
				Object val = json.get(names1[i]);
				if (val == null || val.toString().trim().length() == 0) {
					continue;
				}
				String v1 = val.toString().trim();
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(names1[i]);
				sb.append("=");
				sb.append(v1);
			}

			sb.append("&");
			sb.append(keyName);
			sb.append("=");
			sb.append(keyVal);

			return signMd5(sb.toString());
		} catch (JSONException e) {
			return e.getMessage();
		}
	}

	public static String signMd5(String source) {
		String sign;
		try {
			sign = Utils.md5(source.getBytes("UTF-8"));
			return sign;
		} catch (UnsupportedEncodingException e) {
			return e.getMessage();
		}

	}

	/**
	 * 创建xml节点
	 * @param doc
	 * @param nodeName
	 * @param text
	 */
	public static void addXmlNode(Document doc, String nodeName, String text) {
		Element ele = doc.createElement(nodeName);
		// CDATASection eleCdata = doc.createCDATASection(text);
		// ele.appendChild(eleCdata);
		if (text != null) {
			ele.setTextContent(text);
			doc.getFirstChild().appendChild(ele);
		}
	}
	
	/**
	 * XmlDoc对象 签名Md5
	 * 
	 * @param doc
	 * @param keyName
	 * @param keyVal
	 * @return
	 */
	public static String signMd5(Document doc, String keyName, String keyVal) {
		NodeList nl = doc.getFirstChild().getChildNodes();
		ArrayList<String> names = new ArrayList<String>();

		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Element.ELEMENT_NODE && n.getNodeName() != null) {
				names.add(n.getNodeName());
			}
		}
		String[] list = new String[names.size()];
		names.toArray(list);

		Arrays.sort(list);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			String name = list[i];
			if (name.equals("sign")) { //sign不作为签名字符串
				continue;
			}
			String val = doc.getElementsByTagName(name).item(0).getTextContent();
			if (val != null && val.trim().length() > 0) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(name);
				sb.append("=");
				sb.append(val);
			}
		}
		sb.append("&");
		sb.append(keyName);
		sb.append("=");
		sb.append(keyVal);

		return signMd5(sb.toString());

	}

	/**
	 * 利用随机数生成字符串（32位长度）
	 * 
	 * @return
	 */
	public static String nonceStr() {
		StringBuilder sb = new StringBuilder();
		int max = CHARS.length;
		for (int i = 0; i < 32; i++) {
			String a = Math.random() * max + "";
			int b = Integer.parseInt(a.split("\\.")[0]);

			char c = CHARS[b];
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 将数字前导为0，例如 123生成00000123
	 * 
	 * @param number
	 *            数字
	 * @param maxLen
	 *            合成的字符长度
	 * @return
	 */
	public static String fixNumberWithZero(int number, int maxLen) {
		MStr sb = new MStr("0000000000000000000000000000000000000000");
		while (maxLen > sb.length()) {
			sb.append(sb.toString());
		}
		sb.append(number);
		int len = sb.length();
		return sb.toString().substring(len - maxLen, len);
	}
}
